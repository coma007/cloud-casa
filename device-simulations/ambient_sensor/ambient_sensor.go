package ambient_sensor

import (
	"device-simulations/utils"
	"fmt"
	mqtt "github.com/eclipse/paho.mqtt.golang"
	"math"
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

	previousWinterStart := time.Date(now.Year()-1, 12, 1, 0, 0, 0, 0, time.UTC)
	nextYearSpringStart := time.Date(now.Year()+1, 3, 1, 0, 0, 0, 0, time.UTC)
	springStart := time.Date(now.Year(), 3, 1, 0, 0, 0, 0, time.UTC)
	if TimeIsBetween(now, winterStart, nextYearSpringStart) || TimeIsBetween(now, previousWinterStart, springStart) {
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
	now := time.Now()
	var min, max, night float64
	var isNight bool
	switch season {
	case Winter:
		{
			min = -3
			max = 15
			night = -6
			isNight = now.Hour() > 17 && now.Hour() < 8
		}
	case Summer:
		{
			min = 20
			max = 40
			night = -3
			isNight = now.Hour() > 21 && now.Hour() < 5
		}
	case Autumn:
		{
			min = 10
			max = 20
			night = -5
			isNight = now.Hour() > 19 && now.Hour() < 7
		}
	default:
		{
			min = 15
			max = 25
			night = -3
			isNight = now.Hour() > 20 && now.Hour() < 6
		}
	}
	temp := min + rand.Float64()*(max-min)
	if isNight {
		temp = temp - rand.Float64()*math.Abs(night)
	}
	if temp <= -10.0 {
		return -10.0
	}
	if temp >= 42.0 {
		return 42.0
	}
	return temp
}

func generateHumidity(season string) float64 {
	rand.Seed(time.Now().UnixNano())
	now := time.Now()
	var min, max, night float64
	var isNight bool
	switch season {
	case Winter:
		{
			min = 5
			max = 35
			night = -10
			isNight = now.Hour() > 17 && now.Hour() < 8
		}
	case Summer:
		{
			min = 1
			max = 45
			night = -5
			isNight = now.Hour() > 21 && now.Hour() < 5
		}
	case Autumn:
		{
			min = 10
			max = 45
			night = 0
			isNight = now.Hour() > 19 && now.Hour() < 7
		}
	default:
		{
			min = 5
			max = 40
			night = -1
			isNight = now.Hour() > 20 && now.Hour() < 6
		}
	}
	humidity := min + rand.Float64()*(max-min)
	if isNight {
		humidity = humidity - rand.Float64()*math.Abs(night)
	}
	return math.Min(humidity, 100)
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
	humidity := generateHumidity(previousSeason)

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

		fmt.Println("HUMIDITY: " + humidityS)
		fmt.Println("TEMPERATURE: " + temperatureS)

		utils.SendComplexMessage(client, "ambient_sensor", device.Id, data)
		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
		previousSeason = season
	}
}
