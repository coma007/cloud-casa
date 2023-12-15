package ambient_sensor

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"math/rand"
	"time"
)

type AmbientSensor struct {
	Id      int64 `json:"id"`
	Working bool
}

const (
	Summer string = "summer"
	Autumn        = "autumn"
	Winter        = "winter"
	Spring        = "spring"
)

const minTemp = -5
const maxTemp = 40

const minHumidity = 0
const maxHumidity = 100

func (sensor *AmbientSensor) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	//tokens := strings.Split(message, "~")
	fmt.Println(message)
	// TODO: Message handler
}

func determineSeason() string {
	now := time.Now()
	//springStart := time.Date(now.Year(), 3, 1, 0, 0, 0, 0, time.UTC)
	summerStart := time.Date(now.Year(), 6, 1, 0, 0, 0, 0, time.UTC)
	autumnStart := time.Date(now.Year(), 9, 1, 0, 0, 0, 0, time.UTC)
	winterStart := time.Date(now.Year(), 12, 1, 0, 0, 0, 0, time.UTC)

	nextYearSpringStart := time.Date(now.Year()+1, 3, 1, 0, 0, 0, 0, time.UTC)
	if TimeIsBetween(now, winterStart, nextYearSpringStart) {
		return Winter
	} else if TimeIsBetween(now, summerStart, autumnStart) {
		return Summer
	} else if TimeIsBetween(now, autumnStart, winterStart) {
		return Autumn
	} else {
		return Spring
	}
}

func generateTemperature(season string) float64 {
	rand.Seed(time.Now().UnixNano())
	var min, max float64
	switch season {
	case Winter:
		{
			min = -3
			max = 15
		}
	case Summer:
		{
			min = 20
			max = 40
		}
	case Autumn:
		{
			min = 10
			max = 20
		}
	default:
		{
			min = 15
			max = 25
		}
	}
	return min + rand.Float64()*(max-min)
}

func generateHumidity(season string) float64 {
	rand.Seed(time.Now().UnixNano())
	var min, max float64
	switch season {
	case Winter:
		{
			min = 5
			max = 70
		}
	case Summer:
		{
			min = 0
			max = 40
		}
	case Autumn:
		{
			min = 30
			max = 100
		}
	default:
		{
			min = 0
			max = 100
		}
	}
	return min + rand.Float64()*(max-min)
}

func TimeIsBetween(t, min, max time.Time) bool {
	if min.After(max) {
		min, max = max, min
	}
	return (t.Equal(min) || t.After(min)) && (t.Equal(max) || t.Before(max))
}

func StartSimulation(device AmbientSensor) {
	device.Working = true
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)
	previousSeason := determineSeason()
	temperature := generateTemperature(previousSeason)
	//humidity := generateHumidity(previousSeason)
	humidity := 0.0

	temperatureShifts := make([]float64, 0)
	temperatureShifts = append(temperatureShifts, 0.01, -0.01)

	humidityShifts := make([]float64, 0)
	humidityShifts = append(humidityShifts, 0.3, 0.2, 0.1, -0.1, -0.2, -0.3)
	for {
		season := determineSeason()
		if season != previousSeason {
			temperature = generateTemperature(season)
			humidity = generateHumidity(season)
		} else {
			temperatureShift := temperatureShifts[rand.Intn(len(temperatureShifts))]
			if temperature+temperatureShift > minTemp && temperature+temperatureShift < maxTemp {
				temperature += temperatureShift
			}

			humidityShift := humidityShifts[rand.Intn(len(humidityShifts))]
			if humidity+humidityShift > minHumidity && humidity+humidityShift < maxHumidity {
				humidity += humidityShift
			}

		}

		data := make([]string, 0)
		temperatureS := fmt.Sprintf("%f", temperature)
		humidityS := fmt.Sprintf("%f", humidity)
		data = append(data, temperatureS, humidityS)

		utils.SendComplexMessage(client, "house_battery", device.Id, data)
		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
		previousSeason = season
	}
}
