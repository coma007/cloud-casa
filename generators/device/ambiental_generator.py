import random
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(i):
    return {
        "type": "AmbientSensor",
        "device": {
            "name": "Senzor"+str(i),
            "powerSupplyType": getPowerSupply(),
            "energyConsumption": random.uniform(0.5, 1.5),
            "realEstateId": 1
        }
    }
