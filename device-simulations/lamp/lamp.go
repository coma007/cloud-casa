package lamp

import (
	"device-simulations/utils"
	"fmt"
	"math"
	"math/rand"
	"strings"
	"time"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type Lamp struct {
	Id     int64 `json:"id"`
	LampOn bool  `json:"lampOn"`
}

func (lamp *Lamp) ToggleWorking(working bool) {
	lamp.LampOn = working
}

func calculateBrightness() float64 {

	currentTime := time.Now().Hour()
	startTime := 3
	endTime := 21

	// normal distribution for brightness amount during the day between 3 and 21 o clock
	mean := float64(startTime+endTime) / 2
	stdDev := float64(endTime-startTime) / 6
	brightness := math.Exp(-(math.Pow(float64(currentTime)-mean, 2.0) / (2.0 * math.Pow(stdDev, 2.0))))

	noise := rand.Float64()*0.1 - 0.05
	brightness = math.Max(0, math.Min(1, brightness+noise))

	return brightness
}

func (lamp *Lamp) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	if tokens[1] == "ON" {
		fmt.Printf("Device %s is turning ON\n", lamp.Id)
		lamp.ToggleWorking(true)
	} else {
		fmt.Printf("Device %s is turning OFF\n", lamp.Id)
		lamp.ToggleWorking(false)
	}
}

func StartSimulation(device Lamp) {
	device.LampOn = true
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)
	counter := 1
	for {
		if counter%4 == 0 {
			brightness := calculateBrightness()
			value := fmt.Sprintf("%f", brightness)
			utils.SendMessage(client, "lamp_brightness", device.Id, value)
			if brightness < 0.3 && device.LampOn == false {
				device.ToggleWorking(true)
				utils.SendMessage(client, "lamp_command", device.Id, "true")
			} else if brightness >= 0.3 && device.LampOn == true {
				device.ToggleWorking(false)
				utils.SendMessage(client, "lamp_command", device.Id, "false")
			}
		} else {
			utils.Ping(device.Id, client)
		}
		counter++
		time.Sleep(15 * time.Second)
	}
}
