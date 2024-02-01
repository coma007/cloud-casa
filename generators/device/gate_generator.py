import random
import string
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(i):
    allowed_vehicle = [''.join(random.choices(string.ascii_uppercase + string.digits, k=6)) for _ in range(100)]
    return {
        "type": "VehicleGate",
        "device": {
            "name": "Kapija" + str(i),
            "powerSupplyType": getPowerSupply(),
            "energyConsumption": random.uniform(0.1, 2.0),
            "realEstateId": 1,
            "allowedVehicles": allowed_vehicle
        }
    }
