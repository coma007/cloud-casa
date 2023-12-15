package house_battery

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"math"
	"strconv"
	"strings"
	"time"
)

type HouseBattery struct {
	Id           int64   `json:"id"`
	Size         float64 `json:"size"`
	CurrentState float64 `json:"currentState"`
	Exported     float64
	Imported     float64
}

func (battery *HouseBattery) increasePower(power float64) {
	battery.CurrentState += power
	if battery.CurrentState > battery.Size {
		battery.Exported += battery.CurrentState - battery.Size
		battery.CurrentState = battery.Size
	}
}

func (battery *HouseBattery) reducePower(power float64) {
	battery.CurrentState -= power
	if battery.CurrentState < 0 {
		battery.Imported += math.Abs(battery.CurrentState)
		battery.CurrentState = 0
	}
}

// ID-(REDUCE/INCREASE)-VALUE
func (battery *HouseBattery) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	if tokens[1] == "REDUCE" {
		fmt.Printf("Device %s is reducing power by %s\n", battery.Id, tokens[2])
		power, err := strconv.ParseFloat(tokens[2], 64)
		if err != nil {
			panic(err)
		}
		battery.reducePower(power)
	} else {
		fmt.Printf("Device %s is increasing power by %s\n", battery.Id, tokens[2])
		power, err := strconv.ParseFloat(tokens[2], 64)
		if err != nil {
			panic(err)
		}
		battery.increasePower(power)
	}
	//fmt.Printf("Received message: %s from topic: %s\n", msg.Payload(), msg.Topic())
}

func (battery *HouseBattery) calculateImportExport() float64 {
	if battery.Exported == 0 {
		imported := 0 - battery.Imported
		battery.Imported = 0
		return imported
	} else {
		exported := battery.Exported
		battery.Exported = 0
		return exported
	}
}

func StartSimulation(device HouseBattery) {
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)
	counter := 1
	for {
		if counter%4 == 0 {
			value := fmt.Sprintf("%f", device.calculateImportExport())
			//fmt.Println(value)
			utils.SendMessage(client, "house_battery", device.Id, value)
			counter = 0
		}
		utils.Ping(device.Id, client)

		if counter < 4 {
			counter++
		}
		time.Sleep(15 * time.Second)
	}
}
