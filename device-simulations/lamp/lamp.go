package lamp

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"time"
)

type Lamp struct {
	Id     int64 `json:"id"`
	LampOn bool  `json:"lampOn"`
}

func (lamp *Lamp) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	//tokens := strings.Split(message, "~")
	fmt.Println(message)
}

func StartSimulation(device Lamp) {
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)

	for {

		// Simulation

		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
