from device import lamp_generator,gate_generator,\
    generic_generator, sprinkler_generator, solar_panel, battery_generator, charger_generator,\
    air_condition_generator, washing_machine_generator, ambiental_generator
from util import getToken



def main():
    userToken = getToken("milicasladakovic@gmail.com", "Pera12345")
    # generic_generator.generate(userToken, lamp_generator.generate, 1000)
    # generic_generator.generate(userToken, gate_generator.generate, 100)
    # generic_generator.generate(userToken, sprinkler_generator.generate, 1000)
    #
    # generic_generator.generate(userToken, solar_panel.generate, 1000)
    # generic_generator.generate(userToken, battery_generator.generate, 1000)
    # generic_generator.generate(userToken, charger_generator.generate, 1000)

    # generic_generator.generate(userToken, air_condition_generator.generate, 1000)
    # generic_generator.generate(userToken, washing_machine_generator.generate, 1000)
    # generic_generator.generate(userToken, ambiental_generator.generate, 1000)



if __name__ == '__main__':
    main()
