

import random
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(i):
    modes = ["WHITE", "COLOR"]
    supportedModes = random.sample(modes, random.randint(1, 2))
    return {
        "type": "WashingMachine",
        "device": {
            "name": "Masina" + str(i),
            "powerSupplyType": getPowerSupply(),
            "energyConsumption": random.uniform(1.0, 2.0),
            "realEstateId": 1,
            "supportedModes": supportedModes
        }
    }
