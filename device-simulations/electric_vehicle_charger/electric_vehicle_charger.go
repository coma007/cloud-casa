package electric_vehicle_charger

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"math"
	"math/rand"
	"strconv"
	"strings"
	"time"
)

type ElectricVehicleCharger struct {
	Id          int64   `json:"id"`
	ChargePower float64 `json:"chargePower"`
	NumOfSlots  int64   `json:"NumOfSlots"`
	PowerUsage  []float64
	Slots       []Vehicle
	TakenSlots  []bool
	Client      mqtt.Client
}

type Vehicle struct {
	batteryCapacity float64
	batteryState    float64
	maxPercentage   float64
}

// ID~(START/END/SET_MAX)~SLOT~USER~MAX
func (charger *ElectricVehicleCharger) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	if tokens[1] == "START" {
		slot, err := strconv.ParseInt(tokens[2], 10, 64)
		if err != nil {
			return
		}
		charger.startCharging(slot, tokens[3])
	} else if tokens[1] == "END" {
		slot, err := strconv.ParseInt(tokens[2], 10, 64)
		if err != nil {
			return
		}
		charger.endCharging(slot, tokens[3])
	} else if tokens[1] == "SET_MAX" {
		slot, err := strconv.ParseInt(tokens[2], 10, 64)
		if err != nil {
			return
		}
		percentage, err := strconv.ParseFloat(tokens[4], 64)
		if err != nil {
			return
		}
		charger.setMax(slot, percentage, tokens[3])
	}
	fmt.Printf("Received message: %s from topic: %s\n", msg.Payload(), msg.Topic())
}

func (charger *ElectricVehicleCharger) startCharging(slot int64, user string) {
	if slot != -1 && slot <= charger.NumOfSlots {
		if !charger.TakenSlots[slot] {
			charger.TakenSlots[slot] = true
			charger.Slots[slot] = charger.generateNewDevice()
			if user != "" {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging started on slot "+strconv.FormatInt(slot, 10)+"|"+user)
			} else {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging started on slot "+strconv.FormatInt(slot, 10)+"|Device")
			}
			return
		} else {
			if user != "" {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging failed to start on slot "+strconv.FormatInt(slot, 10)+". Slot already in use|"+user)
			} else {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging failed to start on slot "+strconv.FormatInt(slot, 10)+". Slot already in use|Device")
			}
		}
	} else {
		for i := int64(0); i < charger.NumOfSlots; i++ {
			if !charger.TakenSlots[i] {
				charger.TakenSlots[i] = true
				charger.Slots[i] = charger.generateNewDevice()
				if user != "" {
					utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging started on slot "+strconv.FormatInt(i, 10)+"|"+user)
				} else {
					utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging started on slot "+strconv.FormatInt(i, 10)+"|Device")
				}
				return
			}
		}
	}
}

func (charger *ElectricVehicleCharger) generateNewDevice() Vehicle {
	batteryCapacity := float64(rand.Intn(60)) + 40
	batteryState := math.Round(float64(rand.Intn(30)+10)*batteryCapacity) / 100
	maxPercentage := math.Round(float64(rand.Intn(20) + 80))
	return Vehicle{
		batteryCapacity: batteryCapacity,
		batteryState:    batteryState,
		maxPercentage:   maxPercentage,
	}
}

func (charger *ElectricVehicleCharger) charge() {
	for i := int64(0); i < charger.NumOfSlots; i++ {
		if charger.TakenSlots[i] {
			charger.Slots[i].batteryState += math.Round(100*charger.ChargePower/60) / 100
			charger.PowerUsage[i] += math.Round(100*charger.ChargePower/60) / 100
		}
	}
}

func (charger *ElectricVehicleCharger) endCharging(slot int64, user string) {
	if slot != -1 && slot <= charger.NumOfSlots {
		if charger.TakenSlots[slot] {
			// TODO: Send to mqtt
			if user != "" {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging ended on slot "+strconv.FormatInt(slot, 10)+"|"+user)
			} else {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging ended on slot "+strconv.FormatInt(slot, 10)+"|Device")
			}
			utils.SendMessage(charger.Client, "electric_vehicle_charger_power_usage", charger.Id, strconv.FormatInt(slot, 10)+"|"+strconv.FormatFloat(charger.PowerUsage[slot], 'f', -1, 64))
			charger.TakenSlots[slot] = false
			charger.PowerUsage[slot] = 0
		}
	} else {
		for i := int64(0); i < charger.NumOfSlots; i++ {
			vehicle := charger.Slots[i]
			if charger.TakenSlots[i] && vehicle.batteryState >= (vehicle.batteryCapacity*vehicle.maxPercentage/100) {
				// TODO: Send to mqtt
				if user != "" {
					utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging ended on slot "+strconv.FormatInt(i, 10)+"|"+user)
				} else {
					utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Charging ended on slot "+strconv.FormatInt(i, 10)+"|Device")
				}
				utils.SendMessage(charger.Client, "electric_vehicle_charger_power_usage", charger.Id, strconv.FormatInt(i, 10)+"|"+strconv.FormatFloat(charger.PowerUsage[i], 'f', -1, 64))
				charger.TakenSlots[i] = false
				charger.PowerUsage[i] = 0
			}
		}
	}
}

func (charger *ElectricVehicleCharger) setMax(slot int64, percentage float64, user string) {
	if slot != -1 && slot <= charger.NumOfSlots {
		if charger.TakenSlots[slot] {
			charger.Slots[slot].maxPercentage = percentage
			if user != "" {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Maximum battery percentage set to "+strconv.FormatFloat(percentage, 'f', -1, 64)+" on slot "+strconv.FormatInt(slot, 10)+"|"+user)
			} else {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Maximum battery percentage set to "+strconv.FormatFloat(percentage, 'f', -1, 64)+" on slot "+strconv.FormatInt(slot, 10)+"|Device")
			}
		} else {
			if user != "" {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Maximum battery percentage failed to set to "+strconv.FormatFloat(percentage, 'f', -1, 64)+" on slot "+strconv.FormatInt(slot, 10)+". Slot empty.|"+user)
			} else {
				utils.SendMessage(charger.Client, "electric_vehicle_charger_command", charger.Id, "Maximum battery percentage failed to set to "+strconv.FormatFloat(percentage, 'f', -1, 64)+" on slot "+strconv.FormatInt(slot, 10)+". Slot empty.|Device")
			}
		}
	}
}

func StartSimulation(device ElectricVehicleCharger) {
	device.TakenSlots = make([]bool, device.NumOfSlots)
	device.Slots = make([]Vehicle, device.NumOfSlots)
	device.PowerUsage = make([]float64, device.NumOfSlots)
	fmt.Println(device.TakenSlots)
	client := utils.MqttSetup(device.Id, device.messageHandler)
	device.Client = client
	device.startCharging(-1, "")
	defer client.Disconnect(250)
	counter := 1
	for {
		if counter%4 == 0 {
			chance := rand.Float64()
			if chance < 0.1 {
				device.startCharging(-1, "")
			}
			device.charge()
			device.endCharging(-1, "")
		}
		utils.Ping(device.Id, client)

		if counter < 4 {
			counter++
		}
		time.Sleep(15 * time.Second)
	}
}
