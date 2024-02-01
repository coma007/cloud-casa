import random
import string
from time import sleep

import requests
from globals import DOMAIN
from util import getPowerSupply

def generate(user_token, data_gen, num):
    global DOMAIN
    headers = {'Authorization': f'Bearer {user_token}'}

    for i in range(num):
        data = data_gen(i)
        r = requests.post('http://' + DOMAIN +':8080/api/device/register', json=data,
        headers=headers)
        # sleep(0.1)
        # print(r.content)
        # print()

    # sleep(5)
    # print(f"Status Code: {r.status_code}, Response: {r.request.body}")