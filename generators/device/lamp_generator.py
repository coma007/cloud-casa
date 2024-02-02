import random
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(i):
    return {
    "type": "Lamp",
    "device": {
        "name": "Lampa"+str(i),
        "powerSupplyType": getPowerSupply(),
        "energyConsumption": random.uniform(0.1, 2.0),
        "realEstateId": 1
        }
    }
