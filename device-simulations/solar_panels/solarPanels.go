package solar_panels

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"math"
	"strings"
	"time"
)

type SolarPanel struct {
	Id            string
	Effectiveness float64
	Size          float64
	Working       bool
}

func (panel *SolarPanel) ToggleWorking(working bool) {
	panel.Working = working
}

func calculateTimeOfDayEffectiveness() float64 {
	time := time.Now().Hour()
	if time > 10 && time < 15 {
		return 1.0
	} else if time > 8 && time < 17 {
		return 0.6
	} else {
		return 0
	}
}

func calculateOutput(size float64, effectiveness float64) float64 {
	output := size * 1000
	output = (output * effectiveness) / 100
	output *= 2.209
	output /= 1000
	output /= 24
	output /= 60
	timeEffectiveness := calculateTimeOfDayEffectiveness()
	output *= timeEffectiveness
	output = math.Floor(output*1000) / 1000
	if output < 0.001 && timeEffectiveness > 0 {
		output = 0.001
	}
	return output
}

func (panel *SolarPanel) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "-")
	if tokens[1] == "ON" {
		fmt.Printf("Device %s is turning ON\n", panel.Id)
		panel.ToggleWorking(true)
	} else {
		fmt.Printf("Device %s is turning OFF\n", panel.Id)
		panel.ToggleWorking(false)
	}
	//fmt.Printf("Received message: %s from topic: %s\n", msg.Payload(), msg.Topic())
}

func StartSimulation(deviceId string) {
	device := SolarPanel{
		Id:            deviceId,
		Effectiveness: 20,
		Size:          15,
		Working:       true,
	}
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)
	counter := 1
	for {
		if counter%4 == 0 && device.Working {
			value := fmt.Sprintf("%f", calculateOutput(device.Size, device.Effectiveness))
			message := deviceId + "-" + value
			fmt.Println(message)
			utils.SendMessage(client, "solar_panel_system", message)
			counter = 0
		}
		if device.Working {
			utils.Ping(deviceId, client)
		}
		if counter < 4 {
			counter++
		}
		time.Sleep(15 * time.Second)
	}
}
