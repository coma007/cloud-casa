package air_conditioning

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	wr "github.com/mroth/weightedrand"
	"math/rand"
	//"slices"
	"strconv"
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

func ToStringAux(conditioner AuxAirConditioningMode) string {
	switch conditioner {
	case COOLING_STRING:
		return "COOLING"
	case HEATING_STRING:
		return "HEATING"
	case AUTO_STRING:
		return "AUTO"
	case VENTILATION_STRING:
		return "VENTILATION"
	default:
		return ""
	}
}

func ToString(conditioner AirConditioningMode) string {
	switch conditioner {
	case COOLING:
		return "COOLING"
	case HEATING:
		return "HEATING"
	case AUTO:
		return "AUTO"
	case VENTILATION:
		return "VENTILATION"
	default:
		return ""
	}
}

const (
	FAILURE = "FAILURE"
	SUCCESS = "SUCCESS"
)

const (
	TEMPERATURE_COMMAND = "temperature command"
	MODE_COMMAND        = "mode command"
	WORKING_COMMAND     = "working command"
)

type AuxAirConditioning struct {
	Id             int64                    `json:"id"`
	MinTemperature float64                  `json:"minTemperature"`
	MaxTemperature float64                  `json:"maxTemperature"`
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
		Working:        true,
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
	MinTemperature float64
	MaxTemperature float64
	SupportedModes []AirConditioningMode

	Working            bool
	CurrentTemperature float64
	TargetTemperature  float64
	CurrentMode        AuxAirConditioningMode
}

func (conditioner *AirConditioning) handleWorkingCommand(client mqtt.Client, msg mqtt.Message) {

	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")

	rand.Seed(time.Now().UTC().UnixNano())

	chooser, _ := wr.NewChooser(
		wr.Choice{Item: SUCCESS, Weight: 8},
		wr.Choice{Item: FAILURE, Weight: 2},
	)
	result := chooser.Pick().(string)

	if result == SUCCESS {
		if contentTokens[1] == "TURN ON" {
			conditioner.Working = true
		} else if contentTokens[1] == "TURN OFF" {
			conditioner.Working = false
		} else {
			result = FAILURE
		}
	} else {
		result = FAILURE
	}

	data := append(contentTokens, result)
	fmt.Println("HANDLING Working COMMAND")
	utils.SendComplexMessage(client, "air_conditioning_working_ack", conditioner.Id, data)
}

func (conditioner *AirConditioning) handleTemperatureCommand(client mqtt.Client, msg mqtt.Message) {
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
	targetTemperature, err := strconv.ParseFloat(contentTokens[1], 64)
	if err != nil {
		result = FAILURE
	}
	if targetTemperature < conditioner.MinTemperature || targetTemperature > conditioner.MaxTemperature {
		result = FAILURE
	}
	if result == SUCCESS {
		conditioner.TargetTemperature = targetTemperature
	} else {

	}

	data := append(contentTokens, result)
	fmt.Println("HANDLING Temperature COMMAND")
	utils.SendComplexMessage(client, "air_conditioning_temperature_ack", conditioner.Id, data)
}

func (conditioner *AirConditioning) handleModeCommand(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")

	rand.Seed(time.Now().UTC().UnixNano())

	chooser, _ := wr.NewChooser(
		wr.Choice{Item: SUCCESS, Weight: 8},
		wr.Choice{Item: FAILURE, Weight: 2},
	)
	result := chooser.Pick().(string)
	if contentTokens[1] != "COOLING" && contentTokens[1] != "HEATING" &&
		contentTokens[1] != "VENTILATION" && contentTokens[1] != "AUTO" {
		result = FAILURE
	}

	stringArray := make([]string, len(conditioner.SupportedModes))
	for i, mode := range conditioner.SupportedModes {
		stringArray[i] = ToString(mode)
	}
	//if !slices.Contains(stringArray, contentTokens[1]) {
	//	result = FAILURE
	//}

	if result == SUCCESS {
		switch contentTokens[1] {
		case "COOLING":
			{
				conditioner.CurrentMode = COOLING_STRING
			}
		case "HEATING":
			{
				conditioner.CurrentMode = HEATING_STRING
			}
		case "VENTILATION":
			{
				conditioner.CurrentMode = VENTILATION_STRING
			}
		default:
			{
				conditioner.CurrentMode = AUTO_STRING
			}
		}
	} else {

	}

	data := append(contentTokens, result)
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

func (conditioner *AirConditioning) findIncrement() float64 {
	increment := 0.0
	if conditioner.CurrentMode == COOLING_STRING && conditioner.CurrentTemperature > conditioner.TargetTemperature {
		increment = -0.1
	} else if conditioner.CurrentMode == HEATING_STRING && conditioner.CurrentTemperature < conditioner.TargetTemperature {
		increment = 0.1
	} else if conditioner.CurrentMode == AUTO_STRING {
		if conditioner.CurrentTemperature > conditioner.TargetTemperature {
			increment = -0.1
		} else if conditioner.CurrentTemperature < conditioner.TargetTemperature {
			increment = 0.1
		}
	}
	return increment
}

func StartSimulation(device AirConditioning) {
	client := utils.MqttSetup(device.Id, device.redirectCommand)
	defer client.Disconnect(250)
	rand.Seed(time.Now().UTC().UnixNano())
	minTemp := device.MinTemperature
	maxTemp := device.MaxTemperature
	device.CurrentTemperature = minTemp + rand.Float64()*(maxTemp-minTemp)

	for {
		fmt.Printf("CURENT TEMP: %f\n", device.CurrentTemperature)
		fmt.Printf("MODE: %s\n", device.CurrentMode)
		fmt.Printf("TARGET TEMP: %f\n", device.TargetTemperature)
		fmt.Printf("WORKING: %t\n", device.Working)
		increment := device.findIncrement()
		device.CurrentTemperature += increment

		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
