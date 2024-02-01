import random
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(i):
    return {
    "type": "HouseBattery",
    "device": {
        "name": "Baterija"+str(i),
        "powerSupplyType": getPowerSupply(),
        "energyConsumption": random.uniform(0.5, 1.5),
        "realEstateId": 1,
        "size": random.uniform(40.0, 60.0)
    }
}
