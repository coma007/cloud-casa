package sprinkler_system

import (
	"device-simulations/utils"
	"fmt"
	"strconv"
	"strings"
	"time"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type SprinklerSystem struct {
	Id          int64                   `json:"id"`
	SprinklerOn bool                    `json:"sprinklerOn"`
	ForceQuit   bool                    `json:"forceQuit"`
	Schedule    SprinklerSystemSchedule `json:"schedule"`
}

type SprinklerSystemSchedule struct {
	StartTime     time.Time `json:"startTime"`
	EndTime       time.Time `json:"endTime"`
	ScheduledDays []bool    `json:"scheduledDays"`
}

func (sprinkler *SprinklerSystem) ToggleWorking(working bool) {
	if sprinkler.ShouldWorkNow() && working == false {
		sprinkler.ForceQuit = true
	}
	sprinkler.SprinklerOn = working
}

func (sprinkler *SprinklerSystem) ScheduleWork() {

}

func (sprinkler *SprinklerSystem) ShouldWorkToday() bool {
	return true
}

func (sprinkler *SprinklerSystem) ShouldWorkThisHour() bool {
	return true
}

func (sprinkler *SprinklerSystem) ShouldWorkNow() bool {
	if sprinkler.ShouldWorkToday() {
		if sprinkler.ShouldWorkThisHour() {
			return !sprinkler.ForceQuit
		} else {
			sprinkler.ForceQuit = false
		}
	} else {
		sprinkler.ForceQuit = false
	}
	return false
}

func processMessage(message string) {
	fmt.Println(message)
}

func (sprinkler *SprinklerSystem) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	if tokens[1] == "ON" {
		sprinkler.ToggleWorking(!sprinkler.SprinklerOn)
		fmt.Printf("Device %s is ON: %t\n", sprinkler.Id, sprinkler.SprinklerOn)
		message := strconv.FormatBool(sprinkler.SprinklerOn) + "|" + tokens[2]
		utils.SendMessage(client, "sprinkler_command", sprinkler.Id, message)
	} else if tokens[1] == "SCHEDULE" {
		fmt.Printf("Device %s changed SCHEDULE: %t\n", sprinkler.Id)
		processMessage(tokens[2])
		sprinkler.ScheduleWork()
	}
}

func StartSimulation(device SprinklerSystem) {
	device.SprinklerOn = false
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)
	counter := 1
	for {
		if counter%4 == 0 {
			device.SprinklerOn = device.ShouldWorkNow()
		}
		utils.Ping(device.Id, client)
		counter++
		time.Sleep(15 * time.Second)
	}
}
