package main

import (
	"device-simulations/air_conditioning"
	"device-simulations/ambient_sensor"
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
	solar_panels.SolarPanel | house_battery.HouseBattery | lamp.Lamp | vehicle_gate.AuxVehicleGate |
		air_conditioning.AuxAirConditioning | ambient_sensor.AmbientSensor
}

func main() {
	solarPanels := fetchDevices[solar_panels.SolarPanel]("solarPanelSystem/")
	houseBatteries := fetchDevices[house_battery.HouseBattery]("houseBattery/")
	lamps := fetchDevices[lamp.Lamp]("lamp/")
	gates := fetchDevices[vehicle_gate.AuxVehicleGate]("vehicleGate/")
	airConditioners := fetchDevices[air_conditioning.AuxAirConditioning]("airConditioning/")
	sensors := fetchDevices[ambient_sensor.AmbientSensor]("ambientSensor/")

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
		go vehicle_gate.StartSimulation(item.ToModel())
	}
	for _, item := range airConditioners {
		go air_conditioning.StartSimulation(item.ToModel())
	}
	for _, item := range sensors {
		go ambient_sensor.StartSimulation(item)
	}
	for {
		time.Sleep(1 * time.Second)
	}
}

func fetchDevices[D Device](devicesUrl string) []D {
	responseBody := fetchData(devicesUrl)

	var devices []D

	err := json.Unmarshal(responseBody, &devices)
	if err != nil {
		panic(err)
	}

	return devices
}

func fetchData(deviceTypeUrl string) []byte {
	//TODO
	// url := "http://casa-back:8080/api/" + deviceTypeUrl + "public/simulation/getAll"
	url := "http://localhost:8080/api/" + deviceTypeUrl + "public/simulation/getAll"
	var resp *http.Response
	var err error

	for {
		resp, err = http.Get(url)
		if err == nil {
			break
		}
		time.Sleep(5 * time.Second)
	}
	defer resp.Body.Close()
	responseBody, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		panic(err)
	}
	return responseBody
}
