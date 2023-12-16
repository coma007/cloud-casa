import React, { useState } from 'react';
import DeviceRegistrationStepperCSS from "./DeviceRegistrationStepper.module.scss"
import Step1 from './Step1';
import AirConditioningStep from './AirConditioningStep';
import WashingMachineStep from './WashingMachineStep';
import ElectricVehicleChargerStep from './ElectricVehicleChargerStep';
import HouseBatteryStep from './HouseBatteryStep';
import SolarPanelSystem from './SolarPanelSystemStep';
import SolarPanelSystemStep from './SolarPanelSystemStep';
import VehicleGateStep from './VehicleGateStep';
import { DeviceService } from '../DeviceService';
import { DeviceCreate } from '../Device';
import Menu from '../../../components/navigation/Menu/Menu';
import PageTitle from '../../../components/view/PageTitle/PageTitle';

const StepperForm = () => {
  const [step, setStep] = useState(1);
  const [formData, setFormData] = useState({
    deviceName: '',
    powerSupplyType: 'AUTONOMOUS',
    energyConsumption: 0,
    realEstateName: '',
    picture: null,
    deviceType: 'AmbientSensor',
    minTemperature: 0,
    maxTemperature: 0,
    supportedModes: [],
    chargePower: 0,
    numOfSlots: 0,
    size: 0.0, 
    efficiency: 0.0,
    allowedVehicles: []
  });

  const handleNameChange = (e) => {
    const { name, value } = e.target;
    setFormData({
    ...formData,
    [name]: value,
    });
  };

  const handlePlatesChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value.split('\n').map((plate) => plate.trim()), // Split by new lines and trim extra spaces
    });
  };

  const handleSupportedModesChange = (e) => {
    const { name, options } = e.target as HTMLSelectElement;
    const selectedOptions = Array.from(options)
      .filter((option) => option.selected)
      .map((option) => option.value);
  
    setFormData({
      ...formData,
      [name]: selectedOptions,
    });
  }

  const handleChange = (e) => {
    const { name, value, files} = e.target;
  
    setFormData({
      ...formData,
      [name]: files || value,
    });
  };

  const nextStep = () => {
    if (formData.deviceType === "AmbientSensor" || formData.deviceType === "Lamp" || formData.deviceType === "SprinklerSystem") {
        register()
    } else {
        setStep(step + 1);
    }
  };

  const formNewDevice = () => {
    let device = {} as DeviceCreate;
    device.type = formData.deviceType;
    device.device = {};
    device.device.name = formData.deviceName;
    device.device.powerSupplyType = formData.powerSupplyType;
    device.device.energyConsumption = formData.energyConsumption;
    device.device.realEstateName = formData.realEstateName;
    if (device.type === "AirConditioning" || device.type === "WashingMachine") {
        device.device.supportedModes = formData.supportedModes
    }
    if (device.type === "AirConditioning") {
        device.device.minTemperature = formData.minTemperature
        device.device.maxTemperature = formData.maxTemperature
    }
    if (device.type === "VehicleGate") {
        device.device.allowedVehicles = formData.allowedVehicles
    }
    if (device.type === "ElectricVehicleCharger") {
        device.device.chargePower = formData.chargePower
        device.device.numOfSlots = formData.numOfSlots
    }
    if (device.type === "HouseBattery" || device.type === "SolarPanelSystem") {
        device.device.size = formData.size
    }
    if (device.type === "SolarPanelSystem") {
        device.device.efficiency = formData.efficiency
    }

    return device
  }

  const register = async () => {
    let newData = formNewDevice();
    console.log(newData)
    try {
        await DeviceService.register(newData).then((value) => console.log(value));
        alert("Success")
        setStep(1)
        setFormData({
            deviceName: '',
            powerSupplyType: 'AUTONOMOUS',
            energyConsumption: 0,
            realEstateName: '',
            picture: null,
            deviceType: 'AmbientSensor',
            minTemperature: 0,
            maxTemperature: 0,
            supportedModes: [],
            chargePower: 0,
            numOfSlots: 0,
            size: 0.0, 
            efficiency: 0.0,
            allowedVehicles: []
            // Add other fields for different device types if needed
          })
    } catch (error: any) {
        alert(error.response.data);
      }
  }

  const prevStep = () => {
    setStep(step - 1);
  };

  return (
      <div>
        <Menu admin={false} />
        <div>
          <PageTitle title="Register device" description="Register new device." />
        </div>
        <div className={DeviceRegistrationStepperCSS.form}>
          {step === 1 && (
              <Step1
              formData={formData}
              handleChange={handleChange}
              handleNameChange={handleNameChange}
              nextStep={nextStep}
              />
          )}
          {/* {step === 2 && formData.deviceType === "AmbientSensor" && <AmbientSensorStep formData={formData} handleChange={handleChange} prevStep={prevStep} />} */}
          {/* {step === 2 && formData.deviceType === "Lamp" && <LampStep formData={formData} handleChange={handleChange} prevStep={prevStep} />} */}
          {/* {step === 2 && formData.deviceType === "SprinklerSystem" && <SprinklerSystemStep formData={formData} handleChange={handleChange} prevStep={prevStep} />} */}
          {step === 2 && formData.deviceType === "AirConditioning" && <AirConditioningStep formData={formData} handleChange={handleChange} handleSelectChange={handleSupportedModesChange} nextStep={register} prevStep={prevStep} />}
          {step === 2 && formData.deviceType === "WashingMachine" && <WashingMachineStep formData={formData} handleChange={handleChange} handleSelectChange={handleSupportedModesChange} nextStep={register} prevStep={prevStep} />}
          {step === 2 && formData.deviceType === "ElectricVehicleCharger" && <ElectricVehicleChargerStep formData={formData} handleChange={handleChange} nextStep={register} prevStep={prevStep} />}
          {step === 2 && formData.deviceType === "HouseBattery" && <HouseBatteryStep formData={formData} handleChange={handleChange} nextStep={register} prevStep={prevStep} />}
          {step === 2 && formData.deviceType === "SolarPanelSystem" && <SolarPanelSystemStep formData={formData} handleChange={handleChange} nextStep={register} prevStep={prevStep} />}
          {step === 2 && formData.deviceType === "VehicleGate" && <VehicleGateStep formData={formData} handleChange={handlePlatesChange} nextStep={register} prevStep={prevStep} />}
        </div>
    </div>
  );
};

export default StepperForm;