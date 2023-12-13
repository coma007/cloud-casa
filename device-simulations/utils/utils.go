package utils

import (
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"os"
)

func MqttSetup(deviceId string, messageHandler func(client mqtt.Client, msg mqtt.Message)) mqtt.Client {
	opts := mqtt.NewClientOptions().AddBroker("tcp://mqtt-broker:1883") // MQTT broker URL
	opts.SetClientID(deviceId)                                          // Client ID
	opts.SetUsername("admin")
	opts.SetPassword("12345678")
	client := mqtt.NewClient(opts)
	if token := client.Connect(); token.Wait() && token.Error() != nil {
		fmt.Println(token.Error())
		os.Exit(1)
	}

	//topic = "serverMessages" // Specify the topic you want to subscribe to

	if token := client.Subscribe(deviceId, 0, messageHandler); token.Wait() && token.Error() != nil {
		fmt.Println(token.Error())
		os.Exit(1)
	}

	fmt.Printf("Subscribed to topic: %s\n", deviceId)
	return client
}

func Ping(deviceId string, client mqtt.Client) {
	message := deviceId + "-PING"

	SendMessage(client, "ping", message)

	//fmt.Printf("Published message: %s to topic: %s\n", message, "ping")
}

func SendMessage(client mqtt.Client, topic string, message string) {
	token := client.Publish(topic, 0, false, message)
	token.Wait()
}
