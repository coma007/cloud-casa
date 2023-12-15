package ambiental

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"time"
)

type Ambiental struct {
	Id      string
	Working bool
}

func (sensor *Ambiental) ToggleWorking(working bool) {
	sensor.Working = working
}

func calculateOutput() (float64, float64) {
	return 20, 30
}

func (sensor *Ambiental) messageHandler(client mqtt.Client, msg mqtt.Message) {
	//message := string(msg.Payload())
	//tokens := strings.Split(message, "-")
	//if tokens[1] == "ON" {
	//	fmt.Printf("Device %s is turning ON\n", sensor.Id)
	//	sensor.ToggleWorking(true)
	//} else {
	//	fmt.Printf("Device %s is turning OFF\n", sensor.Id)
	//	sensor.ToggleWorking(false)
	//}
	fmt.Printf("Received message: %s from topic: %s\n", msg.Payload(), msg.Topic())
}

func StartSimulation(deviceId string) {
	device := Ambiental{
		Id:      deviceId,
		Working: true,
	}
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)
	counter := 1
	for {
		if counter%4 == 0 && device.Working {
			temperature, humidity := calculateOutput()
			message := deviceId + "-" + fmt.Sprintf("%f", temperature) + "-" + fmt.Sprintf("%f", humidity)
			fmt.Println(message)
			utils.SendMessage(client, "ambiental", message)
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
