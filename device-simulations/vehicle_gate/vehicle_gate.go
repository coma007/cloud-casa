package vehicle_gate

import (
	"device-simulations/utils"
	"fmt"
	"math"
	"math/rand"
	"slices"
	"strings"
	"time"

	mqtt "github.com/eclipse/paho.mqtt.golang"
)

type AuxVehicleGateMode string

const (
	PUBLIC_STRING  AuxVehicleGateMode = "PUBLIC"
	PRIVATE_STRING AuxVehicleGateMode = "PRIVATE"
)

type AuxVehicleGate struct {
	Id              int64              `json:"id"`
	AllowedVehicles []string           `json:"allowedVehicles"`
	CurrentMode     AuxVehicleGateMode `json:"currentMode"`
}

func (gate *AuxVehicleGate) ToModel() VehicleGate {
	var currentMode VehicleGateMode

	if gate.CurrentMode == PUBLIC_STRING {
		currentMode = PUBLIC
	} else {
		currentMode = PRIVATE
	}

	return VehicleGate{
		Id:              gate.Id,
		AllowedVehicles: gate.AllowedVehicles,
		CurrentMode:     currentMode,
		IsOpen:          false,
		VehiclesInside:  []string{},
	}
}

type VehicleGateMode int64

const (
	PUBLIC VehicleGateMode = iota
	PRIVATE
)

type VehicleGate struct {
	Id              int64
	AllowedVehicles []string
	CurrentMode     VehicleGateMode
	IsOpen          bool
	VehiclesInside  []string
}

func (gate *VehicleGate) ToggleOpen(open bool) {
	gate.IsOpen = open
}

func (gate *VehicleGate) ToggleMode(mode VehicleGateMode) {
	gate.CurrentMode = mode
}

func (gate *VehicleGate) AddVehicleInside(vehicle string) {
	gate.VehiclesInside = append(gate.VehiclesInside, vehicle)
	for i, v := range gate.AllowedVehicles {
		if v == vehicle {
			gate.AllowedVehicles = append(gate.AllowedVehicles[:i], gate.AllowedVehicles[i+1:]...)
			return
		}
	}
}

func (gate *VehicleGate) RemoveVehicleInside(vehicle string) {
	for i, v := range gate.VehiclesInside {
		if v == vehicle {
			gate.VehiclesInside = append(gate.VehiclesInside[:i], gate.VehiclesInside[i+1:]...)
			return
		}
	}
	gate.AllowedVehicles = append(gate.AllowedVehicles, vehicle)
}

func (gate *VehicleGate) GetRandomVehicleInside() string {
	randomIndex := rand.Intn(len(gate.VehiclesInside))
	return gate.VehiclesInside[randomIndex]
}

func (gate *VehicleGate) CanPass(licencePlates string) bool {
	if gate.CurrentMode == PUBLIC {
		return true
	}
	return slices.Contains(gate.AllowedVehicles, licencePlates)
}

func (gate *VehicleGate) DetectObject() bool {
	seed := rand.NewSource(time.Now().UnixNano())
	distance := rand.New(seed)
	return distance.Float64() < 0.3
}

func (gate *VehicleGate) ReadLicencePlates() string {
	seed := rand.NewSource(time.Now().UnixNano())
	allowedPprobability := rand.New(seed)
	if allowedPprobability.Float64() < 0.7 && len(gate.AllowedVehicles) > 0 {
		randomIndex := rand.Intn(len(gate.AllowedVehicles))
		return gate.AllowedVehicles[randomIndex]
	}
	return generateRandomLicensePlate()
}

func generateRandomLicensePlate() string {
	// Serbian - letter-letter-number-number-number-number-letter-letter
	return fmt.Sprintf("%s%s%d%s%s", randomLetter(), randomLetter(), randomDigits(4), randomLetter(), randomLetter())
}

func randomLetter() string {
	// ASCII values for uppercase letters are 65 to 90
	return string(rune('A' + rand.Intn(26)))
}

func randomDigits(numDigits int) int {
	// Generate a random number with the specified number of digits
	min := int(math.Pow(10, float64(numDigits-1)))
	max := int(math.Pow(10, float64(numDigits)) - 1)
	return rand.Intn(max-min+1) + min
}

func (gate *VehicleGate) messageHandler(client mqtt.Client, msg mqtt.Message) {
	message := string(msg.Payload())
	tokens := strings.Split(message, "~")
	if tokens[1] == "OPEN" {
		fmt.Printf("Device %s is OPEN\n", gate.Id)
		gate.ToggleOpen(true)
	} else if tokens[1] == "CLOSE" {
		fmt.Printf("Device %s is CLOSED\n", gate.Id)
		gate.ToggleOpen(false)
	} else if tokens[1] == "PRIVATE" {
		fmt.Printf("Device %s is set to PRIVATE mode\n", gate.Id)
		gate.ToggleMode(PRIVATE)
	} else if tokens[1] == "PUBLIC" {
		fmt.Printf("Device %s is set to PUBLIC mode\n", gate.Id)
		gate.ToggleMode(PUBLIC)
	}
}

func StartSimulation(device VehicleGate) {
	client := utils.MqttSetup(device.Id, device.messageHandler)
	defer client.Disconnect(250)

	for {
		detectedObjectIn := device.DetectObject()
		detectedObjectOut := device.DetectObject()
		if detectedObjectIn {
			licencePlates := device.ReadLicencePlates()
			utils.SendMessage(client, "vehicle_gate_licence_plates", device.Id, licencePlates)
			if device.CanPass(licencePlates) {
				device.AddVehicleInside(licencePlates)
				if !device.IsOpen {
					device.ToggleOpen(true)
					utils.SendMessage(client, "vehicle_gate_command", device.Id, "true")
				}
			}
		} else if detectedObjectOut && len(device.VehiclesInside) != 0 {
			licencePlates := device.GetRandomVehicleInside()
			utils.SendMessage(client, "vehicle_gate_licence_plates", device.Id, licencePlates)
			device.RemoveVehicleInside(licencePlates)
			if !device.IsOpen {
				device.ToggleOpen(true)
				utils.SendMessage(client, "vehicle_gate_command", device.Id, "true")
			}
		} else if device.IsOpen {
			device.ToggleOpen(false)
			utils.SendMessage(client, "vehicle_gate_command", device.Id, "false")
		}
		utils.Ping(device.Id, client)
		time.Sleep(15 * time.Second)
	}
}
