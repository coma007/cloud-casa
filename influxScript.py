from influxdb_client import InfluxDBClient, Point
from influxdb_client.client.write_api import SYNCHRONOUS
from datetime import datetime, timedelta
import math
import os
import random
import string
import json

# Ambient sensor

WINTER = "Winter"
SUMMER = "Summer"
AUTUMN = "Autumn"
SPRING = "Spring"

def determine_season(time = None):
    if time is None:
        now = datetime.now()
    else:
        now = time
    summer_start = datetime(now.year, 6, 1, 0, 0, 0, 0)
    autumn_start = datetime(now.year, 9, 1, 0, 0, 0, 0)
    winter_start = datetime(now.year, 12, 1, 0, 0, 0, 0)

    previous_winter_start = datetime(now.year - 1, 12, 1, 0, 0, 0, 0)
    next_year_spring_start = datetime(now.year + 1, 3, 1, 0, 0, 0, 0)
    spring_start = datetime(now.year, 3, 1, 0, 0, 0, 0)

    if time_is_between(now, winter_start, next_year_spring_start) or time_is_between(now, previous_winter_start, spring_start):
        return WINTER
    elif time_is_between(now, summer_start, autumn_start):
        return SUMMER
    elif time_is_between(now, autumn_start, winter_start):
        return AUTUMN
    else:
        return SPRING

def generate_temperature(season, current_date = None):
    if current_date == None:
        random.seed(datetime.now().timestamp())
    else:
        random.seed(current_date.timestamp())
    now = datetime.now()
    is_night = False

    if season == WINTER:
        min_temp, max_temp, night_temp = -3, 15, -6
        is_night = 17 < now.hour < 8
    elif season == SUMMER:
        min_temp, max_temp, night_temp = 20, 40, -3
        is_night = 21 < now.hour < 5
    elif season == AUTUMN:
        min_temp, max_temp, night_temp = 10, 20, -5
        is_night = 19 < now.hour < 7
    else:
        min_temp, max_temp, night_temp = 15, 25, -3
        is_night = 20 < now.hour < 6

    temp = min_temp + random.uniform(0, 1) * (max_temp - min_temp)

    if is_night:
        temp -= random.uniform(0, 1) * abs(night_temp)

    return min(max(temp, -10.0), 42.0) * 1.0

def generate_humidity(season, current_date = None):
    if current_date == None:
        random.seed(datetime.now().timestamp())
    else:
        random.seed(current_date.timestamp())
    now = datetime.now()
    is_night = False

    if season == WINTER:
        min_humidity, max_humidity, night_humidity = 5, 35, -10
        is_night = 17 < now.hour < 8
    elif season == SUMMER:
        min_humidity, max_humidity, night_humidity = 1, 45, -5
        is_night = 21 < now.hour < 5
    elif season == AUTUMN:
        min_humidity, max_humidity, night_humidity = 10, 45, 0
        is_night = 19 < now.hour < 7
    else:
        min_humidity, max_humidity, night_humidity = 5, 40, -1
        is_night = 20 < now.hour < 6

    humidity = min_humidity + random.uniform(0, 1) * (max_humidity - min_humidity)

    if is_night:
        humidity -= random.uniform(0, 1) * abs(night_humidity)

    return min(humidity, 100) * 1.0

def time_is_between(t, min_time, max_time):
    if min_time > max_time:
        min_time, max_time = max_time, min_time
    return min_time <= t <= max_time

# =================================================================

def generate_charger_command():
    slot = random.randint(1,3)
    max_percentage = random.randint(80,100)
    chance = random.random()
    if chance < 0.2:
        return "Maximum battery percentage set to " + str(max_percentage) + " on slot " + str(slot)
    elif chance < 0.4:
        return "Charging started on slot " + str(slot)
    elif chance < 0.6:
        return "Charging failed to start on slot " + str(slot)
    elif chance < 0.8:
        return "Charging ended on slot " + str(slot)
    else:
        return "Charging failed to end on slot " + str(slot)


# ===================================

# Solar panel

def calculate_time_of_day_effectiveness(current_day= None):
    if current_day is None:
        current_hour = datetime.now().hour
    else:
        current_hour = current_day.hour
    if 10 < current_hour < 15:
        return 1.0
    elif 8 < current_hour < 17:
        return 0.6
    else:
        return 0

def calculate_output(size, effectiveness, current_day= None):
    output = size * 1000
    output = (output * effectiveness) / 100
    output *= 2.209
    output /= 1000
    output /= 24
    output /= 60
    time_efficiency = calculate_time_of_day_effectiveness(current_day)
    output *= time_efficiency
    output = round(output, 3)
    
    if output < 0.001 and time_efficiency > 0:
        output = 0.001
    
    return output * 1.0

def generate_solar_panel_command():
    chance = random.random()
    if chance < 0.5:
        return "TURN ON"
    else:
        return "TURN OFF"
    
# ==============================================

# Lamp

def calculate_brightness(current_day=None):
    if current_day is None:
        current_time = datetime.now().hour
    else:
        current_time = current_day.hour
    start_time = 3
    end_time = 21

    # Normal distribution for brightness amount during the day between 3 and 21 o'clock
    mean = (start_time + end_time) / 2
    std_dev = (end_time - start_time) / 6
    brightness = math.exp(-(math.pow(current_time - mean, 2.0) / (2.0 * math.pow(std_dev, 2.0))))

    noise = random.uniform(0, 1) * 0.1 - 0.05
    brightness = max(0, min(1, brightness + noise))

    return brightness * 1.0

def generate_lamp_command():
    chance = random.random()
    return chance < 0.5


# ===============================

# Sprinkler

def generate_sprinkler_command():
    chance_on = random.random()
    chance_schedule = random.random()
    return chance_on < 0.5, chance_schedule < 0.5

# ============================

# Gate

def generate_random_license_plate():
    # Serbian - letter-letter-number-number-number-number-letter-letter
    return f"{random_letter()}{random_letter()}{random_digits(3)}{random_letter()}{random_letter()}"

def random_letter():
    # ASCII values for uppercase letters are 65 to 90
    return random.choice(string.ascii_uppercase)

def random_digits(num_digits):
    # Generate a random number with the specified number of digits
    min_val = 10 ** (num_digits - 1)
    max_val = (10 ** num_digits) - 1
    return random.randint(min_val, max_val)

# =============================

# AC

def generate_ac_status():
    chance = random.random()
    if chance > 0.5:
        return "TURN ON"
    return "TURN OFF"


def generate_ac_execution():
    chance = random.random()
    if chance > 0.5:
        return "SUCCESS"
    return "FAILURE"

def generate_ac_mode():
    chance = random.random()
    if chance < 0.25:
        return "COOLING"
    elif chance < 0.5:
        return "HEATING"
    elif chance < 0.75:
        return "AUTO"
    return "VENTILATION"

def generate_ac_schedule(id, start_date):
    start_time = start_date.isoformat()
    end_time = (start_date + timedelta(minutes=random.randint(10,60))).isoformat()
    override_chance = random.random()
    activated_chance = random.random()
    working_chance = random.random()
    temperature = random.randint(15,25)
    repeating_chance = random.random()
    increment = random.randint(1,5)
    mode_chance = random.random()
    if mode_chance > 0.4:
        mode = generate_ac_mode()
    else:
        mode = None
    return json.dumps({"id": id,
            "startTime":start_time,
            "endTime":end_time,
            "override":override_chance > 0.5,
            "activated":activated_chance > 0.5,
            "working":working_chance > 0.5,
            "mode": mode,
            "temperature":temperature,
            "repeating":repeating_chance > 0.5,
            "repeatingDaysIncrement":increment})

# ===========================

# Machine

def generate_machine_mode():
    chance = random.random()
    if chance < 0.5:
        return "COLOR"
    return "WHITE"

def generate_machine_schedule(id, start_date):
    start_time = start_date.isoformat()
    end_time = (start_date + timedelta(minutes=random.randint(10,60))).isoformat()
    activated_chance = random.random()
    working_chance = random.random()
    mode_chance = random.random()
    if mode_chance > 0.4:
        mode = generate_machine_mode()
    else:
        mode = None
    return json.dumps({"id": id,
            "startTime":start_time,
            "endTime":end_time,
            "activated":activated_chance > 0.5,
            "working":working_chance > 0.5,
            "mode": mode})

# InfluxDB connection parameters
url = 'http://192.168.139.105:8086/'
token = 'yc-jzAQc7uoLFLCWsk2YVmYOF9r_6mgqTQxtFY3ZTN_46r4VHFfC-fnAi-Qfayl5hQeYC9HxnI0Z1fYQHU9eSQ=='
org = 'casa'
bucket = 'casa-bucket'
batch_size = 43200
write_batch_size = 10000

measurements = [
    "online",
    "air_conditioning_working_ack",
    "air_conditioning_new_schedule_ack",
    "air_conditioning_temperature_ack",
    "air_conditioning_mode_ack",
    "air_conditioning_mode_command",
    "air_conditioning_temperature_command",
    "air_conditioning_working_command",
    "air_conditioning_new_schedule_command",
    "ambient_sensor",
    "electric_vehicle_charger_command",
    "electric_vehicle_charger_power_usage",
    "house_battery_import_export",
    "house_battery_state",
    "house_battery_power_usage",
    "solar_panel_system",
    "solar_panel_system_command",
    "lamp_brightness",
    "lamp_command",
    "sprinkler_command",
    "sprinkler_schedule",
    "vehicle_gate_licence_plates",
    "vehicle_gate_command",
    "vehicle_gate_mode",
    "vehicle_gate_vehicles",
    "washing_machine_mode_command",
    "washing_machine_new_schedule_command",
    "washing_machine_working_command",
    "washing_machine_mode_ack",
    "washing_machine_new_schedule_ack",
    "washing_machine_working_ack"
]

# measurements = [
#     "house_battery_import_export",
#     "house_battery_state",
#     "house_battery_power_usage",
# ]
battery_ids = [1,2,3,4,5]

users = ["nemanja.majstorovic3214@gmail.com", "nemanja.dutina@gmail.com", "milica.sladakovic@gmail.com", "thiaslsf@gmail.com"]
ids = [1,2]
device_types = [10,20,30,40,50,60,70,80,90]
client = InfluxDBClient(url=url, token=token, org=org)

data = []
write_api = client.write_api(write_options=SYNCHRONOUS)  

for m in measurements:
    for i in range(2, -1, -1):
        current_time = datetime.utcnow() - timedelta(days=30 * i)

        for j in range(batch_size):
            match m:
                case "online":
                    for t in device_types:
                        if t != 80:
                            for k in ids:
                                chance = random.random()
                                if chance > 0.6:
                                    data.append(Point(m)
                                        .tag("id", t + k)
                                        .time(current_time - timedelta(minutes=j), write_precision='ms')
                                        .field("is_online", True))
                        else:
                            for k in battery_ids:
                                chance = random.random()
                                if chance > 0.6:
                                    data.append(Point(m)
                                        .tag("id", t + k)
                                        .time(current_time - timedelta(minutes=j), write_precision='ms')
                                        .field("is_online", True))
                case "air_conditioning_working_ack":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point("air_conditioning_execution")
                                .tag("id", 10 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", generate_ac_status())
                                .field("executed", generate_ac_execution())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "air_conditioning_new_schedule_ack":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point("air_conditioning_execution")
                                .tag("id", 10 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", generate_ac_schedule(10 + k, current_time - timedelta(minutes=j)))
                                .field("executed", generate_ac_execution())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "air_conditioning_temperature_ack":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point("air_conditioning_execution")
                                .tag("id", 10 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", str(random.randint(15,25) * 1.0))
                                .field("executed", generate_ac_execution())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "air_conditioning_mode_ack":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point("air_conditioning_execution")
                                .tag("id", 10 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", generate_ac_mode())
                                .field("executed", generate_ac_execution())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "air_conditioning_mode_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 10 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("type", "mode command")
                                .field("mode", generate_ac_mode())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "air_conditioning_temperature_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 10 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("type", "temperature command")
                                .field("temperature", str(random.randint(15,25) * 1.0))
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "air_conditioning_working_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 10 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("type", "working command")
                                .field("working", generate_ac_status())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "air_conditioning_new_schedule_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 10 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("type", "new schedule command")
                                .field("airConditionSchedule", generate_ac_schedule(10 + k, current_time - timedelta(minutes=j)))
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "ambient_sensor":
                    for k in ids:
                        data.append(Point(m)
                            .tag("id", 90 + k)
                            .time(current_time - timedelta(minutes=j), write_precision='ms')
                            .field("temperature", generate_temperature(determine_season(current_time - timedelta(minutes=j)), current_time - timedelta(minutes=j)))
                            .field("humidity", generate_humidity(determine_season(current_time - timedelta(minutes=j)), current_time - timedelta(minutes=j))))
                case "electric_vehicle_charger_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 80 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", generate_charger_command())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "electric_vehicle_charger_power_usage":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 80 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("power", random.randint(30,50) * 1.0)
                                .field("slotNum", random.randint(1,3)))
                case "house_battery_import_export":
                    for k in battery_ids:
                        chance = random.random()
                        if chance < 0.5:
                            type = "export"
                        else:
                            type = "import"
                        data.append(Point(m)
                            .tag("id", 70 + k)
                            .time(current_time - timedelta(minutes=j), write_precision='ms')
                            .field("type", type)
                            .field("value", random.randint(0,6) * 1.0))
                case "house_battery_state":
                    for k in battery_ids:
                        data.append(Point(m)
                            .tag("id", 70 + k)
                            .time(current_time - timedelta(minutes=j), write_precision='ms')
                            .field("currentState", random.randint(25,75) * 1.0))
                case "house_battery_power_usage":
                    for k in battery_ids:
                        data.append(Point(m)
                            .tag("id", 70 + k)
                            .time(current_time - timedelta(minutes=j), write_precision='ms')
                            .field("power", random.randint(0,20) * 1.0))
                case "solar_panel_system":
                    for k in ids:
                        data.append(Point(m)
                            .tag("id", 60 + k)
                            .time(current_time - timedelta(minutes=j), write_precision='ms')
                            .field("power", calculate_output(random.randint(10,20) * 1.0, random.randint(30,40) * 1.0, current_time - timedelta(minutes=j))))
                case "solar_panel_system_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 60 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", generate_solar_panel_command())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "lamp_brightness":
                    for k in ids:
                        data.append(Point(m)
                            .tag("id", 30 + k)
                            .time(current_time - timedelta(minutes=j), write_precision='ms')
                            .field("brightness", calculate_brightness(current_time - timedelta(minutes=j))))
                case "lamp_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 30 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("is_on", generate_lamp_command())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "sprinkler_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            on, schedule = generate_sprinkler_command()
                            data.append(Point(m)
                                .tag("id", 40 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("is_on", on)
                                .field("is_schedule", schedule)
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "vehicle_gate_licence_plates":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 50 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("licence_plates", generate_random_license_plate()))
                case "vehicle_gate_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            open = generate_lamp_command()
                            data.append(Point(m)
                                .tag("id", 50 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("is_open", open)
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "vehicle_gate_mode":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            private = generate_lamp_command()
                            data.append(Point(m)
                                .tag("id", 50 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("is_private", private)
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "washing_machine_mode_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 20 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("type", "mode command")
                                .field("mode", generate_machine_mode())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "washing_machine_new_schedule_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 20 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("type", "new schedule command")
                                .field("washingMachineSchedule", generate_machine_schedule(20 + k, current_time - timedelta(minutes=j)))
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "washing_machine_working_command":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point(m)
                                .tag("id", 20 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("type", "working command")
                                .field("working", generate_ac_status())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "washing_machine_mode_ack":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point("washing_machine_execution")
                                .tag("id", 20 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", generate_machine_mode())
                                .field("executed", generate_ac_execution())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "washing_machine_new_schedule_ack":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point("washing_machine_execution")
                                .tag("id", 20 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", generate_machine_schedule(20 + k, current_time - timedelta(minutes=j)))
                                .field("executed", generate_ac_execution())
                                .field("user", users[random.randint(0, len(users)-1)]))
                case "washing_machine_working_ack":
                    for k in ids:
                        chance = random.random()
                        if chance > 0.7:
                            data.append(Point("washing_machine_execution")
                                .tag("id", 20 + k)
                                .time(current_time - timedelta(minutes=j), write_precision='ms')
                                .field("command", generate_ac_status())
                                .field("executed", generate_ac_execution())
                                .field("user", users[random.randint(0, len(users)-1)]))
            if len(data) > write_batch_size:
                try:
                    write_api.write(bucket=bucket, org=org, record=data)
                    data = []
                except Exception as e:
                    print(f"An error occurred: {e}")
                    data = []
                    
    print(m + " finished")

# Close the InfluxDB connection
client.close()
