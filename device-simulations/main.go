package main

import (
	"encoding/json"
	"fmt"
	"github.com/eclipse/paho.mqtt.golang"
	"io/ioutil"
	"net/http"
	"os"
	"time"
)

func messageHandler(client mqtt.Client, msg mqtt.Message) {
	fmt.Printf("Received message: %s from topic: %s\n", msg.Payload(), msg.Topic())
}

type Device struct {
	Name string `json:"name"`
	Type string `json:"type"`
}

func main() {
	url := "http://localhost:8080/api/device/public/simulation/getAll"

	resp, err := http.Get(url)
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()

	fmt.Println("Response Status:", resp.Status)

	// Read the response body
	responseBody, err := ioutil.ReadAll(resp.Body)

	var data []Device

	// Unmarshal the JSON string into the slice
	err = json.Unmarshal(responseBody, &data)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	// Print the parsed data (as a slice of maps)
	fmt.Println("Parsed JSON:")

	if err != nil {
		panic(err)
	}

	devices := make(map[string]string)
	for _, item := range data {
		devices[item.Name] = item.Name
	}
	//devices["Senzor1"] = "Senzor1"
	//devices["Klima1"] = "Klima1"
	//devices["Klima2"] = "Klima2"
	//devices["Klima3"] = "Klima3"
	for k, _ := range devices {
		go startSimulation(k, "ping")
	}
	for {
		time.Sleep(1 * time.Second)
	}
}

func startSimulation(deviceName string, topic string) {
	opts := mqtt.NewClientOptions().AddBroker("tcp://mqtt-broker:1883") // MQTT broker URL
	opts.SetClientID(deviceName)                                        // Client ID
	opts.SetUsername("admin")
	opts.SetPassword("12345678")
	client := mqtt.NewClient(opts)
	if token := client.Connect(); token.Wait() && token.Error() != nil {
		fmt.Println(token.Error())
		os.Exit(1)
	}
	defer client.Disconnect(250)

	//topic = "serverMessages" // Specify the topic you want to subscribe to

	if token := client.Subscribe(deviceName, 0, messageHandler); token.Wait() && token.Error() != nil {
		fmt.Println(token.Error())
		os.Exit(1)
	}

	fmt.Printf("Subscribed to topic: %s\n", deviceName)

	// Keep the program running to receive messages
	// For demonstration purposes, you can add a sleep or use a blocking mechanism
	// You might use a channel, select statement, or other mechanisms in an actual application
	for {
		message := deviceName + "~PING"

		token := client.Publish(topic, 0, false, message)
		token.Wait()

		fmt.Printf("Published message: %s to topic: %s\n", message, topic)
		time.Sleep(15 * time.Second)
	}
}
