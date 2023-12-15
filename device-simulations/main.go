package main

import (
	"device-simulations/air_conditioning"
	"device-simulations/house_battery"
	"device-simulations/lamp"
	"device-simulations/solar_panels"
	"device-simulations/vehicle_gate"
	"encoding/json"
	"io/ioutil"
	"net/http"
	"time"
)

type Device interface {
	solar_panels.SolarPanel | house_battery.HouseBattery | lamp.Lamp | vehicle_gate.VehicleGate |
		air_conditioning.AirConditioning
}

func main() {

	solarPanels := fetchDevices[solar_panels.SolarPanel]("solarPanelSystem/")
	houseBatteries := fetchDevices[house_battery.HouseBattery]("houseBattery/")
	lamps := fetchDevices[lamp.Lamp]("lamp/")
	gates := fetchDevices[vehicle_gate.VehicleGate]("vehicleGate/")
	airConditioners := fetchDevices[air_conditioning.AirConditioning]("airConditioning/")

	for _, item := range solarPanels {
		go solar_panels.StartSimulation(item)
	}
	for _, item := range houseBatteries {
		go house_battery.StartSimulation(item)
	}
	for _, item := range lamps {
		go lamp.StartSimulation(item)
	}
	for _, item := range gates {
		go vehicle_gate.StartSimulation(item)
	}
	for _, item := range airConditioners {
		go air_conditioning.StartSimulation(item)
	}
	for {
		time.Sleep(1 * time.Second)
	}
}

func fetchDevices[D Device](devicesUrl string) []D {
	responseBody := fetchData(devicesUrl)

	var devices []D

	// Unmarshal the JSON string into the slice
	err := json.Unmarshal(responseBody, &devices)
	if err != nil {
		panic(err)
	}

	return devices
}

func fetchData(deviceTypeUrl string) []byte {
	url := "http://localhost:8080/api/" + deviceTypeUrl + "public/simulation/getAll"
	resp, err := http.Get(url)
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()

	// Read the response body
	responseBody, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		panic(err)
	}
	return responseBody
}
