package electric_vehicle_charger

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"math"
	"math/rand"
)

type ElectricVehicleCharger struct {
	Id          int64   `json:"id"`
	ChargePower float64 `json:"chargePower"`
	NumOfSlots  int64   `json:"NumOfSlots"`
	PowerUsage  []float64
	Slots       []Vehicle
	TakenSlots  []bool
}

type Vehicle struct {
	batteryCapacity float64
	batteryState    float64
	maxPercentage   float64
}

// ID~(REDUCE/INCREASE)~VALUE
func (charger *ElectricVehicleCharger) messageHandler(client mqtt.Client, msg mqtt.Message) {
	//message := string(msg.Payload())
	//tokens := strings.Split(message, "~")
	//fmt.Printf("Received message: %s from topic: %s\n", msg.Payload(), msg.Topic())
}

func (charger *ElectricVehicleCharger) startCharging() {
	for i := int64(0); i < charger.NumOfSlots; i++ {
		if !charger.TakenSlots[i] {
			charger.TakenSlots[i] = true
			charger.Slots[i] = charger.generateNewDevice()
			return
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

func (charger *ElectricVehicleCharger) endCharging() {
	for i := int64(0); i < charger.NumOfSlots; i++ {
		vehicle := charger.Slots[i]
		if charger.TakenSlots[i] && vehicle.batteryState >= (vehicle.batteryCapacity*vehicle.maxPercentage/100) {
			// TODO: Send to mqtt
			charger.TakenSlots[i] = false
			charger.PowerUsage[i] = 0
		}
	}
}

func StartSimulation(device ElectricVehicleCharger) {
	device.TakenSlots = make([]bool, device.NumOfSlots)
	device.Slots = make([]Vehicle, device.NumOfSlots)
	fmt.Println(device.TakenSlots)
	client := utils.MqttSetup(device.Id, device.messageHandler)
	device.startCharging()
	defer client.Disconnect(250)
	counter := 1
	for {
		if counter%4 == 0 {
			chance := rand.Float64()
			if chance < 0.1 {
				device.startCharging()
			}
			device.charge()
			device.endCharging()
		}
		utils.Ping(device.Id, client)

		if counter < 4 {
			counter++
		}
		//time.Sleep(15 * time.Second)
	}
}
