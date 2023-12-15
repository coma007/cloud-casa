package vehicle_gate

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"time"
)

type VehicleGateMode int64

const (
	PUBLIC VehicleGateMode = iota
	PRIVATE
)

type VehicleGate struct {
	Id              int64           `json:"id"`
	AllowedVehicles []string        `json:"allowedVehicles"`
	CurrentMode     VehicleGateMode `json:"currentMode"`
}

func (gate *VehicleGate) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	//tokens := strings.Split(message, "~")
	fmt.Println(message)
}

func StartSimulation(device VehicleGate) {
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)

	for {

		// Simulation

		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
