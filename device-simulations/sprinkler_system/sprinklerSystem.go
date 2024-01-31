package sprinkler_system

import (
	"device-simulations/utils"
	"encoding/json"
	"fmt"
	"strconv"
	"strings"
	"time"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type SprinklerSystem struct {
	Id          int64                   `json:"id"`
	SprinklerOn bool                    `json:"sprinklerOn"`
	ForceStart  bool                    `json:"forceStart"`
	ForceQuit   bool                    `json:"forceQuit"`
	Schedule    SprinklerSystemSchedule `json:"schedule"`
}

type SprinklerSystemSchedule struct {
	StartTime     time.Time `json:"startTime"`
	EndTime       time.Time `json:"endTime"`
	ScheduledDays []bool    `json:"scheduledDays"`
}

func (s *SprinklerSystemSchedule) UnmarshalJSON(data []byte) error {
	var temp struct {
		StartTime     float64 `json:"startTime"`
		EndTime       float64 `json:"endTime"`
		ScheduledDays []bool  `json:"scheduledDays"`
	}

	if err := json.Unmarshal(data, &temp); err != nil {
		return err
	}

	s.StartTime = time.Unix(0, int64(temp.StartTime*1e9)).UTC()
	s.EndTime = time.Unix(0, int64(temp.EndTime*1e9)).UTC()
	s.ScheduledDays = temp.ScheduledDays

	return nil
}

func (sprinkler *SprinklerSystem) ToggleWorking(working bool) {
	if sprinkler.ShouldWorkNow() && working == false {
		sprinkler.ForceQuit = true
	} else if !sprinkler.ShouldWorkNow() && working == true {
		sprinkler.ForceStart = true
	}
	if working == true {
		sprinkler.ForceQuit = false
	} else {
		sprinkler.ForceStart = false
	}
	sprinkler.SprinklerOn = working
}

func (sprinkler *SprinklerSystem) ScheduleWork(schedule SprinklerSystemSchedule) {
	sprinkler.Schedule = schedule
}

func (sprinkler *SprinklerSystem) ShouldWorkToday() bool {
	currentDay := int(time.Now().Weekday())
	return sprinkler.Schedule.ScheduledDays[currentDay-1]
}

func (sprinkler *SprinklerSystem) ShouldWorkThisHour() bool {
	location, err := time.LoadLocation("Europe/Belgrade")
	if err != nil {
		return false
	}
	currentTime := time.Now().In(location)

	startHour, startMin, startSec := sprinkler.Schedule.StartTime.Clock()
	endHour, endMin, endSec := sprinkler.Schedule.EndTime.Clock()

	isAfterStartTime := currentTime.Hour() > startHour || (currentTime.Hour() == startHour && currentTime.Minute() > startMin) || (currentTime.Hour() == startHour && currentTime.Minute() == startMin && currentTime.Second() >= startSec)
	isBeforeEndTime := currentTime.Hour() < endHour || (currentTime.Hour() == endHour && currentTime.Minute() < endMin) || (currentTime.Hour() == endHour && currentTime.Minute() == endMin && currentTime.Second() < endSec)

	return isAfterStartTime && isBeforeEndTime
}

func (sprinkler *SprinklerSystem) ShouldWorkNow() bool {
	if sprinkler.ForceStart == true {
		return true
	}
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

func (sprinkler *SprinklerSystem) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	if tokens[1] == "ON" {
		sprinkler.ToggleWorking(!sprinkler.SprinklerOn)
		fmt.Printf("Device %s is ON: %t\n", sprinkler.Id, sprinkler.SprinklerOn)
		message := strconv.FormatBool(sprinkler.SprinklerOn) + "|" + tokens[2]
		utils.SendMessage(client, "sprinkler_command", sprinkler.Id, message)

	} else if tokens[1] == "SCHEDULE" {
		user := strings.Split(tokens[2], "|")[0]
		scheduleJson := strings.Split(tokens[2], "|")[1]
		fmt.Printf("Device %s changed SCHEDULE: %t\n", sprinkler.Id, scheduleJson)

		var schedule SprinklerSystemSchedule
		err := json.Unmarshal([]byte(scheduleJson), &schedule)
		if err != nil {
			fmt.Println("Error:", err)
			return
		}
		sprinkler.ScheduleWork(schedule)

		utils.SendMessage(client, "sprinkler_schedule", sprinkler.Id, user)

	}
}

func StartSimulation(device SprinklerSystem) {
	device.SprinklerOn = false
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)
	counter := 1
	for {
		if counter%4 == 0 {
			shouldWork := device.ShouldWorkNow()
			if shouldWork != device.SprinklerOn {
				utils.SendMessage(client, "sprinkler_command", device.Id, strconv.FormatBool(shouldWork)+"|SIMULATION")
				device.SprinklerOn = shouldWork
			}
		}
		utils.Ping(device.Id, client)
		counter++
		time.Sleep(15 * time.Second)
	}
}
