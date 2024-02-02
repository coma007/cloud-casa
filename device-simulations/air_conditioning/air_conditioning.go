package air_conditioning

import (
	"device-simulations/utils"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"math/rand"
	"net/http"
	"slices"
	"strconv"
	"strings"
	"time"

	mqtt "github.com/eclipse/paho.mqtt.golang"
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
	TEMPERATURE_COMMAND  = "temperature command"
	MODE_COMMAND         = "mode command"
	WORKING_COMMAND      = "working command"
	NEW_SCHEDULE_COMMAND = "new schedule command"
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
	Schedules          []utils.AirConditioningSchedule
	currentSchedule    utils.AirConditioningSchedule
}

func (conditioner *AirConditioning) handleScheduleCommand(client mqtt.Client, msg mqtt.Message) {

	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")
	var newSchedule utils.AirConditioningSchedule
	err := json.Unmarshal([]byte(contentTokens[1]), &newSchedule)

	result := "SUCCESS"
	if err != nil {
		result = "FAILURE"
		return
	}

	newSchedule.FixTimezone()

	now := time.Now()
	if newSchedule.EndTime.Time.Before(newSchedule.StartTime.Time) ||
		newSchedule.EndTime.Time.Before(now) ||
		newSchedule.StartTime.Time.Before(now) ||
		conditioner.existsOverlapping(newSchedule) {
		result = "FAILURE"
	}
	overlap := conditioner.existsOverlapping(newSchedule)
	fmt.Println(overlap)

	if result != "FAILURE" {
		conditioner.Schedules = append(conditioner.Schedules, newSchedule)
	}

	data := append(contentTokens, result)
	fmt.Println("HANDLING Scheduling COMMAND")
	utils.SendComplexMessage(client, "air_conditioning_new_schedule_ack", conditioner.Id, data)
}

func (conditioner *AirConditioning) getCurrentSchedule() *utils.AirConditioningSchedule {
	for _, schedule := range conditioner.Schedules {
		if schedule.Activated && !schedule.Override && utils.TimeIsBetween(time.Now(), schedule.StartTime.Time, schedule.EndTime.Time) {
			return &schedule
		}
	}
	return nil
}

func (conditioner *AirConditioning) overrideIfNeeded() {
	current := conditioner.getCurrentSchedule()
	if current != nil {
		current.Override = true
	}
}

func (conditioner *AirConditioning) checkSchedule() {
	//        reset repeating schedules
	for i, _ := range conditioner.Schedules {
		if conditioner.Schedules[i].EndTime.After(time.Now()) && conditioner.Schedules[i].Repeating {
			conditioner.Schedules[i].Override = false
			conditioner.Schedules[i].Activated = false
			conditioner.Schedules[i].EndTime.Time.AddDate(0, 0, int(conditioner.Schedules[i].RepeatingDaysIncrement))
			conditioner.Schedules[i].StartTime.Time.AddDate(0, 0, int(conditioner.Schedules[i].RepeatingDaysIncrement))

			if conditioner.Schedules[i].EndTime.After(time.Now()) {
				conditioner.Schedules[i].Activated = true
			}
		}
	}

	//        check for current schedules
	for i, schedule := range conditioner.Schedules {
		now := time.Now()
		isBefore := schedule.StartTime.Time.Before(now) || schedule.StartTime.Time.Equal(now)

		if isBefore && !schedule.Activated && !schedule.Override {
			conditioner.Working = schedule.Working
			if conditioner.Working {
				conditioner.currentSchedule = schedule
				if schedule.Mode != nil {
					conditioner.setMode(*schedule.Mode)
				}
				if schedule.Temperature != nil {
					conditioner.setTemperature(fmt.Sprintf("%f", *schedule.Temperature))
				}

			}

			conditioner.Schedules[i].Activated = true
		}
	}
}

func (conditioner *AirConditioning) handleWorkingCommand(client mqtt.Client, msg mqtt.Message) {
	conditioner.overrideIfNeeded()
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")

	rand.Seed(time.Now().UTC().UnixNano())

	//chooser, _ := wr.NewChooser(
	//	wr.Choice{Item: SUCCESS, Weight: 8},
	//	wr.Choice{Item: FAILURE, Weight: 2},
	//)
	//result := chooser.Pick().(string)
	result := SUCCESS

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
	conditioner.overrideIfNeeded()
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")

	result := conditioner.setTemperature(contentTokens[1])

	data := append(contentTokens, result)
	fmt.Println("HANDLING Temperature COMMAND")
	utils.SendComplexMessage(client, "air_conditioning_temperature_ack", conditioner.Id, data)
}

func (conditioner *AirConditioning) setTemperature(tempStr string) string {
	rand.Seed(time.Now().UTC().UnixNano())

	//chooser, _ := wr.NewChooser(
	//	wr.Choice{Item: "SUCCESS", Weight: 8},
	//	wr.Choice{Item: "FAILURE", Weight: 2},
	//)
	//result := chooser.Pick().(string)
	result := SUCCESS
	targetTemperature, err := strconv.ParseFloat(tempStr, 64)
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
	return result
}

func (conditioner *AirConditioning) handleModeCommand(client mqtt.Client, msg mqtt.Message) {
	conditioner.overrideIfNeeded()
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")
	result := conditioner.setMode(contentTokens[1])

	data := append(contentTokens, result)
	fmt.Println("HANDLING Mode COMMAND")
	utils.SendComplexMessage(client, "air_conditioning_mode_ack", conditioner.Id, data)
}

func (conditioner *AirConditioning) setMode(modeStr string) string {
	rand.Seed(time.Now().UTC().UnixNano())

	//chooser, _ := wr.NewChooser(
	//	wr.Choice{Item: SUCCESS, Weight: 8},
	//	wr.Choice{Item: FAILURE, Weight: 2},
	//)
	//result := chooser.Pick().(string)
	result := SUCCESS
	if modeStr != "COOLING" && modeStr != "HEATING" &&
		modeStr != "VENTILATION" && modeStr != "AUTO" {
		result = FAILURE
	}

	stringArray := make([]string, len(conditioner.SupportedModes))
	for i, mode := range conditioner.SupportedModes {
		stringArray[i] = ToString(mode)
	}
	if !slices.Contains(stringArray, modeStr) {
		result = FAILURE
	}

	if result == SUCCESS {
		switch modeStr {
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

	return result
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
	case NEW_SCHEDULE_COMMAND:
		{
			conditioner.handleScheduleCommand(client, msg)
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

func (conditioner *AirConditioning) existsOverlapping(newSchedule utils.AirConditioningSchedule) bool {
	for _, schedule := range conditioner.Schedules {
		if !schedule.Override &&
			(newSchedule.StartTime.Time.Before(schedule.EndTime.Time) || newSchedule.StartTime.Time.Equal(schedule.EndTime.Time)) &&
			(schedule.StartTime.Time.Before(newSchedule.EndTime.Time) || schedule.StartTime.Time.Equal(newSchedule.EndTime.Time)) {
			return true
		}
	}
	return false
}

func fetchDSchedules(id int64) []utils.AirConditioningSchedule {
	url := "http://" + utils.URL_DOMAIN + ":8080/api/airConditioning/public/simulation/schedules?deviceId=" + strconv.FormatInt(id, 10)
	var resp *http.Response
	var err error

	for {
		resp, err = http.Get(url)
		if err == nil {
			break
		}
		time.Sleep(5 * time.Second)
	}
	defer resp.Body.Close()
	responseBody, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		panic(err)
	}

	var schedules []utils.AirConditioningSchedule

	err = json.Unmarshal(responseBody, &schedules)
	if err != nil {
		panic(err)
	}

	return schedules
}

func StartSimulation(device AirConditioning) {
	client := utils.MqttSetup(device.Id, device.redirectCommand)
	defer client.Disconnect(250)
	rand.Seed(time.Now().UTC().UnixNano())
	minTemp := device.MinTemperature
	maxTemp := device.MaxTemperature
	device.CurrentTemperature = minTemp + rand.Float64()*(maxTemp-minTemp)
	device.Schedules = fetchDSchedules(device.Id)
	for i, _ := range device.Schedules {
		device.Schedules[i].FixTimezone()
	}

	for {
		device.checkSchedule()
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
