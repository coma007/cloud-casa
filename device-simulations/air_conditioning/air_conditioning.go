package air_conditioning

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"time"
)

type AuxAirConditioningMode string

const (
	COOLING_STRING     AuxAirConditioningMode = "COOLING"
	HEATING_STRING     AuxAirConditioningMode = "HEATING"
	AUTO_STRING        AuxAirConditioningMode = "AUTO"
	VENTILATION_STRING AuxAirConditioningMode = "VENTILATION"
)

type AuxAirConditioning struct {
	Id             int64                    `json:"id"`
	MinTemperature int64                    `json:"minTemperature"`
	MaxTemperature int64                    `json:"maxTemperature"`
	SupportedModes []AuxAirConditioningMode `json:"supportedModes"`
}

func (conditioner *AuxAirConditioning) ToModel() AirConditioning {
	var supportedModes []AirConditioningMode
	for _, v := range conditioner.SupportedModes {
		switch v {
		case COOLING_STRING:
			supportedModes = append(supportedModes, COOLING)
			break
		case HEATING_STRING:
			supportedModes = append(supportedModes, HEATING)
			break
		case AUTO_STRING:
			supportedModes = append(supportedModes, AUTO)
			break
		default:
			supportedModes = append(supportedModes, VENTILATION)
			break
		}
	}
	return AirConditioning{
		Id:             conditioner.Id,
		MinTemperature: conditioner.MinTemperature,
		MaxTemperature: conditioner.MaxTemperature,
		SupportedModes: supportedModes,
	}
}

type AirConditioningMode int64

const (
	COOLING AirConditioningMode = iota
	HEATING
	AUTO
	VENTILATION
)

type AirConditioning struct {
	Id             int64
	MinTemperature int64
	MaxTemperature int64
	SupportedModes []AirConditioningMode
}

func (conditioner *AirConditioning) messageHandler(client mqtt.Client, msg mqtt.Message) {
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
