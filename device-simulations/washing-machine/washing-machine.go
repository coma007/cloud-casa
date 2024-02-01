package washing_machine

import (
	"device-simulations/utils"
	"encoding/json"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	wr "github.com/mroth/weightedrand"
	"io/ioutil"
	"math/rand"
	"net/http"
	"slices"
	"strconv"
	"strings"
	"time"
)

type AuxWashingMachineMode string

const (
	WHITE_STRING AuxWashingMachineMode = "WHITE"
	COLOR_STRING AuxWashingMachineMode = "COLOR"
)

func ToStringAux(mode AuxWashingMachineMode) string {
	switch mode {
	case WHITE_STRING:
		return "WHITE"
	case COLOR_STRING:
		return "COLOR"
	default:
		return ""
	}
}

func ToString(mode WashingMachineMode) string {
	switch mode {
	case WHITE:
		return "WHITE"
	case COLOR:
		return "COLOR"
	default:
		return ""
	}
}

const (
	FAILURE = "FAILURE"
	SUCCESS = "SUCCESS"
)

const (
	MODE_COMMAND         = "mode command"
	WORKING_COMMAND      = "working command"
	NEW_SCHEDULE_COMMAND = "new schedule command"
)

type AuxWashingMachine struct {
	Id             int64                   `json:"id"`
	SupportedModes []AuxWashingMachineMode `json:"supportedModes"`
}

func (machine *AuxWashingMachine) ToModel() WashingMachine {
	var supportedModes []WashingMachineMode
	for _, v := range machine.SupportedModes {
		switch v {
		case WHITE_STRING:
			supportedModes = append(supportedModes, WHITE)
			break
		//case COLOR_STRING:
		//	supportedModes = append(supportedModes, COLOR)
		//	break
		default:
			supportedModes = append(supportedModes, COLOR)
			break
		}
	}
	return WashingMachine{
		Id:             machine.Id,
		SupportedModes: supportedModes,
		Working:        true,
	}
}

type WashingMachineMode int64

const (
	WHITE WashingMachineMode = iota
	COLOR
)

type WashingMachine struct {
	Id             int64
	SupportedModes []WashingMachineMode

	Working         bool
	CurrentMode     AuxWashingMachineMode
	Schedules       []utils.WashingMachineSchedule
	currentSchedule utils.WashingMachineSchedule
}

func (machine *WashingMachine) handleScheduleCommand(client mqtt.Client, msg mqtt.Message) {

	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")
	var newSchedule utils.WashingMachineSchedule
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
		machine.existsOverlapping(newSchedule) {
		result = "FAILURE"
	}

	if *newSchedule.Mode != "WHITE" && *newSchedule.Mode != "COLOR" {
		result = FAILURE
	}

	if result != "FAILURE" {
		machine.Schedules = append(machine.Schedules, newSchedule)
	}

	data := append(contentTokens, result)
	fmt.Println("HANDLING Scheduling COMMAND")
	utils.SendComplexMessage(client, "washing_machine_new_schedule_ack", machine.Id, data)
}

func (machine *WashingMachine) getCurrentSchedule() *utils.WashingMachineSchedule {
	for _, schedule := range machine.Schedules {
		if schedule.Activated && utils.TimeIsBetween(time.Now(), schedule.StartTime.Time, schedule.EndTime.Time) {
			return &schedule
		}
	}
	return nil
}

func (machine *WashingMachine) checkSchedule() {

	//        check for current schedules
	for _, schedule := range machine.Schedules {
		now := time.Now()
		isBefore := schedule.StartTime.Time.Before(now) || schedule.StartTime.Time.Equal(now)

		if isBefore && !schedule.Activated {
			if machine.Working {
				machine.currentSchedule = schedule
				if schedule.Mode != nil {
					machine.setMode(*schedule.Mode)
				}

			}

			schedule.Activated = true
		}
	}
}

func (machine *WashingMachine) handleWorkingCommand(client mqtt.Client, msg mqtt.Message) {
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
			machine.Working = true
		} else if contentTokens[1] == "TURN OFF" {
			machine.Working = false
		} else {
			result = FAILURE
		}

	} else {
		result = FAILURE
	}

	data := append(contentTokens, result)
	fmt.Println("HANDLING Working COMMAND")
	utils.SendComplexMessage(client, "washing_machine_working_ack", machine.Id, data)
}

func (machine *WashingMachine) handleModeCommand(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")
	result := machine.setMode(contentTokens[1])

	data := append(contentTokens, result)
	fmt.Println("HANDLING Mode COMMAND")
	utils.SendComplexMessage(client, "wshing_machine_mode_ack", machine.Id, data)
}

func (machine *WashingMachine) setMode(modeStr string) string {
	rand.Seed(time.Now().UTC().UnixNano())

	chooser, _ := wr.NewChooser(
		wr.Choice{Item: SUCCESS, Weight: 8},
		wr.Choice{Item: FAILURE, Weight: 2},
	)
	result := chooser.Pick().(string)
	if modeStr != "WHITE" && modeStr != "COLOR" {
		result = FAILURE
	}

	stringArray := make([]string, len(machine.SupportedModes))
	for i, mode := range machine.SupportedModes {
		stringArray[i] = ToString(mode)
	}
	if !slices.Contains(stringArray, modeStr) {
		result = FAILURE
	}

	if result == SUCCESS {
		switch modeStr {
		case "WHITE":
			{
				machine.CurrentMode = WHITE_STRING
			}
		default:
			{
				machine.CurrentMode = COLOR_STRING
			}
		}
	} else {

	}

	return result
}

func (machine *WashingMachine) redirectCommand(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	content := tokens[1]
	contentTokens := strings.Split(content, "|")
	switch contentTokens[0] {
	case MODE_COMMAND:
		{
			machine.handleModeCommand(client, msg)
			break
		}
	case WORKING_COMMAND:
		{
			machine.handleWorkingCommand(client, msg)
		}
	case NEW_SCHEDULE_COMMAND:
		{
			machine.handleScheduleCommand(client, msg)
		}
	}
}

func (machine *WashingMachine) existsOverlapping(newSchedule utils.WashingMachineSchedule) bool {
	for _, schedule := range machine.Schedules {
		if (newSchedule.StartTime.Time.Before(schedule.EndTime.Time) || newSchedule.StartTime.Time.Equal(schedule.EndTime.Time)) &&
			(schedule.StartTime.Time.Before(newSchedule.EndTime.Time) || schedule.StartTime.Time.Equal(newSchedule.EndTime.Time)) {
			return true
		}
	}
	return false
}

func fetchDSchedules(id int64) []utils.WashingMachineSchedule {
	//TODO check
	url := "http://localhost:8080/api/washingMachine/public/simulation/schedules?deviceId=" + strconv.FormatInt(id, 10)
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

	var schedules []utils.WashingMachineSchedule

	err = json.Unmarshal(responseBody, &schedules)
	if err != nil {
		panic(err)
	}

	return schedules
}

func StartSimulation(device WashingMachine) {
	client := utils.MqttSetup(device.Id, device.redirectCommand)
	defer client.Disconnect(250)
	rand.Seed(time.Now().UTC().UnixNano())
	device.Schedules = fetchDSchedules(device.Id)
	for i, _ := range device.Schedules {
		device.Schedules[i].FixTimezone()
	}

	for {
		device.checkSchedule()
		fmt.Printf("MODE: %s\n", device.CurrentMode)
		fmt.Printf("WORKING: %t\n", device.Working)

		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
