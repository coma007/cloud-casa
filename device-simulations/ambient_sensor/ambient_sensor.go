package ambient_sensor

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"time"
)

type AmbientSensor struct {
	Id      int64 `json:"id"`
	Working bool
}

func (sensor *AmbientSensor) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	//tokens := strings.Split(message, "~")
	fmt.Println(message)
	// TODO: Message handler
}

func StartSimulation(device AmbientSensor) {
	device.Working = true
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)

	for {

		// TODO: Simulation

		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
