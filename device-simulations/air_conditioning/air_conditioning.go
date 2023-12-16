package air_conditioning

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	wr "github.com/mroth/weightedrand"
	"math/rand"
	"strings"
	"time"
)

type AuxAirConditioningMode string

const (
	COOLING_STRING     AuxAirConditioningMode = "COOLING"
	HEATING_STRING     AuxAirConditioningMode = "HEATING"
	AUTO_STRING        AuxAirConditioningMode = "AUTO"
	VENTILATION_STRING AuxAirConditioningMode = "VENTILATION"
)

const (
	TEMPERATURE_COMMAND = "temperature command"
	MODE_COMMAND        = "mode command"
	WORKING_COMMAND     = "working command"
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

func (conditioner *AirConditioning) handleCommand(client mqtt.Client, msg mqtt.Message) []string {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")

	rand.Seed(time.Now().UTC().UnixNano())

	chooser, _ := wr.NewChooser(
		wr.Choice{Item: "SUCCESS", Weight: 8},
		wr.Choice{Item: "FAILURE", Weight: 2},
	)
	result := chooser.Pick().(string)

	data := append(contentTokens, result)
	fmt.Println("HANDLING COMMAND, CONTENT: " + content + ", RESULT: " + result)
	return data
}

func (conditioner *AirConditioning) handleWorkingCommand(client mqtt.Client, msg mqtt.Message) {
	data := conditioner.handleCommand(client, msg)
	fmt.Println("HANDLING Working COMMAND")
	utils.SendComplexMessage(client, "air_conditioning_working_ack", conditioner.Id, data)
}

func (conditioner *AirConditioning) handleTemperatureCommand(client mqtt.Client, msg mqtt.Message) {
	data := conditioner.handleCommand(client, msg)
	fmt.Println("HANDLING Temperature COMMAND")
	utils.SendComplexMessage(client, "air_conditioning_temperature_ack", conditioner.Id, data)
}

func (conditioner *AirConditioning) handleModeCommand(client mqtt.Client, msg mqtt.Message) {
	data := conditioner.handleCommand(client, msg)
	fmt.Println("HANDLING Mode COMMAND")
	utils.SendComplexMessage(client, "air_conditioning_mode_ack", conditioner.Id, data)
}

func (conditioner *AirConditioning) redirectCommand(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")
	switch contentTokens[0] {
	case TEMPERATURE_COMMAND:
		{
			conditioner.handleTemperatureCommand(client, msg)
			break
		}
	case MODE_COMMAND:
		{
			conditioner.handleModeCommand(client, msg)
		}
	case WORKING_COMMAND:
		{
			conditioner.handleWorkingCommand(client, msg)
		}
	}
}

func StartSimulation(device AirConditioning) {
	client := utils.MqttSetup(device.Id, device.redirectCommand)
	defer client.Disconnect(250)

	for {

		// TODO: Simulation

		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
