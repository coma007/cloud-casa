import random
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(i):
    return {
    "type": "ElectricVehicleCharger",
    "device": {
        "name": "Punjac"+str(i),
        "powerSupplyType": getPowerSupply(),
        "energyConsumption": random.uniform(0.5, 1.5),
        "realEstateId": 1,
        "chargePower": random.randint(40, 60),
        "numOfSlots": random.randint(1, 5)
        }
    }
