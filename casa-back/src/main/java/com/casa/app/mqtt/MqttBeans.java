package com.casa.app.mqtt;

import com.casa.app.device.DeviceStatusService;
import com.casa.app.device.home.air_conditioning.AirConditioningMeasurement;
import com.casa.app.device.home.ambient_sensor.AmbientSensorMeasurement;
import com.casa.app.device.home.washing_machine.WashingMachineMeasurement;
import com.casa.app.device.large_electric.electric_vehicle_charger.ElectricVehicleChargerMeasurement;
import com.casa.app.device.large_electric.house_battery.HouseBatteryMeasurement;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystemMeasurement;
import com.casa.app.device.measurement.MeasurementType;
import com.casa.app.device.outdoor.lamp.LampBrightnessMeasurement;
import com.casa.app.device.outdoor.lamp.LampCommandMeasurement;
import com.casa.app.device.outdoor.lamp.LampService;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystemMeasurement;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateCommandMeasurement;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateLicencePlatesMeasurement;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateModeMeasurement;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateService;
import com.casa.app.device.large_electric.house_battery.HouseBatteryService;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystemService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Configuration
public class MqttBeans {

    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        options.setServerURIs(new String[] {"tcp://localhost:1883"});
        options.setUserName("admin");
        String pass = "12345678";
        options.setPassword(pass.toCharArray());
        options.setCleanSession(true);

        factory.setConnectionOptions(options);
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                mqttClientFactory(), "#");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Autowired
            private DeviceStatusService deviceStatusService;
            @Autowired
            private LampService lampService;
            @Autowired
            private VehicleGateService vehicleGateService;
            private SolarPanelSystemService solarPanelSystemService;
            @Autowired
            private HouseBatteryService houseBatteryService;

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                String content = message.getPayload().toString();
                Long id = Long.parseLong(content.split("~")[0]);
                content = content.split("~")[1];
                switch (topic) {
                    case ("ping"):
                        deviceStatusService.pingHandler(id);
                        break;
                    case (MeasurementType.airConditioning):
                        // call service handler here
                        break;
                    case (MeasurementType.ambientSensor):
                        // call service handler here
                        break;
                    case (MeasurementType.washingMachine):
                        // call service handler here
                        break;
                    case (MeasurementType.electricVehicleCharger):
                        // call service handler here
                        break;
                    case (MeasurementType.houseBattery):
                        houseBatteryService.handleMessage(id, content);
                        break;
                    case (MeasurementType.solarPanelSystem):
                        solarPanelSystemService.handleMessage(id, content);
                        break;
                    case (MeasurementType.lampBrightness):
                        lampService.brightnessHandler(id, content);
                        break;
                    case (MeasurementType.lampCommand):
                        lampService.commandHandler(id, content, "SIMULATION");
                        break;
                    case (MeasurementType.sprinklerSystem):
                        // call service handler here
                        break;
                    case (MeasurementType.vehicleGateLicencePlates):
                        vehicleGateService.licencePlatesHandler(id, content);
                        break;
                    case (MeasurementType.vehicleGateCommand):
                        vehicleGateService.commandHandler(id, content);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());

        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("#");

        return messageHandler;
    }



}
