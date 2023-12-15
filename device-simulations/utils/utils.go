package utils

import (
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"os"
	"strconv"
)

func MqttSetup(deviceId int64, messageHandler func(client mqtt.Client, msg mqtt.Message)) mqtt.Client {
	//TODO
	//opts := mqtt.NewClientOptions().AddBroker("tcp://mqtt-broker:1883")
	opts := mqtt.NewClientOptions().AddBroker("tcp://localhost:1883")
	opts.SetClientID(strconv.FormatInt(deviceId, 10))
	opts.SetUsername("admin")
	opts.SetPassword("12345678")
	client := mqtt.NewClient(opts)
	if token := client.Connect(); token.Wait() && token.Error() != nil {
		fmt.Println(token.Error())
		os.Exit(1)
	}

	if token := client.Subscribe(strconv.FormatInt(deviceId, 10), 0, messageHandler); token.Wait() && token.Error() != nil {
		fmt.Println(token.Error())
		os.Exit(1)
	}

	fmt.Printf("Subscribed to topic: %d\n", deviceId)
	return client
}

func Ping(deviceId int64, client mqtt.Client) {
	SendMessage(client, "ping", deviceId, "PING")

	//fmt.Printf("Published message: %s to topic: %s\n", message, "ping")
}

func SendMessage(client mqtt.Client, topic string, deviceId int64, message string) {
	token := client.Publish(topic, 0, false, strconv.FormatInt(deviceId, 10)+"~"+message)
	token.Wait()
}
