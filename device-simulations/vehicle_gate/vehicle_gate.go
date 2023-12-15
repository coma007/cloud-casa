package vehicle_gate

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"time"
)

type AuxVehicleGateMode string

const (
	PUBLIC_STRING  AuxVehicleGateMode = "PUBLIC"
	PRIVATE_STRING AuxVehicleGateMode = "PRIVATE"
)

type AuxVehicleGate struct {
	Id              int64              `json:"id"`
	AllowedVehicles []string           `json:"allowedVehicles"`
	CurrentMode     AuxVehicleGateMode `json:"currentMode"`
}

func (gate *AuxVehicleGate) ToModel() VehicleGate {
	var currentMode VehicleGateMode

	if gate.CurrentMode == PUBLIC_STRING {
		currentMode = PUBLIC
	} else {
		currentMode = PRIVATE
	}

	return VehicleGate{
		Id:              gate.Id,
		AllowedVehicles: gate.AllowedVehicles,
		CurrentMode:     currentMode,
	}
}

type VehicleGateMode int64

const (
	PUBLIC VehicleGateMode = iota
	PRIVATE
)

type VehicleGate struct {
	Id              int64
	AllowedVehicles []string
	CurrentMode     VehicleGateMode
}

func (gate *VehicleGate) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	//tokens := strings.Split(message, "~")
	fmt.Println(message)
	// TODO: Message handler
}

func StartSimulation(device VehicleGate) {
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)

	for {

		// TODO: Simulation

		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
