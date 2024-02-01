import random

import requests

from globals import DOMAIN


def getPowerSupply():
    return random.choice(["HOME", "AUTONOMOUS"])

def getToken(username, password):
    global DOMAIN
    data = {
        "Username": username,
        "Password": password
    }
    r = requests.post('http://' + DOMAIN + ':8080/api/login', json=data)
    # sleep(0.1)
    return r.text

