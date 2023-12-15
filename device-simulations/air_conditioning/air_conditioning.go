package air_conditioning

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"time"
)

type AirConditioningMode int64

const (
	COOLING AirConditioningMode = iota
	HEATING
	AUTO
	VENTILATION
)

type AirConditioning struct {
	Id             int64               `json:"id"`
	MinTemperature int64               `json:"minTemperature"`
	MaxTemperature int64               `json:"MaxTemperature"`
	SupportedModes AirConditioningMode `json:"supportedModes"`
}

func (airConditioning *AirConditioning) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	//tokens := strings.Split(message, "~")
	fmt.Println(message)
	// TODO: Message handler
}

func StartSimulation(device AirConditioning) {
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)

	for {

		// TODO: Simulation

		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
