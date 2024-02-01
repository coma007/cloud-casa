import random
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(i):
    modes = ["COOLING", "VENTILATION", "AUTO", "HEATING"]
    supportedModes = random.sample(modes, random.randint(1, 4))
    return {
        "type": "AirConditioning",
        "device": {
            "name": "Klima"+str(i),
            "powerSupplyType": getPowerSupply(),
            "energyConsumption": random.uniform(0.5, 1.5),
            "realEstateId": 1,
            "minTemperature": random.randint(0, 5),
            "maxTemperature": random.randint(20, 25),
            "supportedModes": supportedModes
        }
    }
