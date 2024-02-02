import random
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(i):
    return {
    "type": "SolarPanelSystem",
    "device": {
        "name": "Paneli"+ str(i),
        "powerSupplyType": getPowerSupply(),
        "energyConsumption": random.uniform(1.0, 2.0),
        "realEstateId": 1,
        "size": random.uniform(10.0, 30.0),
        "efficiency": random.uniform(10.0, 50.0),
        }
    }
