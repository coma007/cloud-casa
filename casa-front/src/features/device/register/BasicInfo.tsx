import Button from '../../../components/forms/Button/Button';
import UploadImage from '../../../components/forms/UploadImage/UploadImage';
import DeviceRegistrationStepperCSS from "./DeviceRegistrationStepper.module.scss"

const Step1 = ({ formData, handleChange, handleNameChange, nextStep }) => {
    // Render form inputs for the first step
    // Include inputs for device name, power supply type, energy consumption, real estate name, picture, and device type selection
    // Use formData and handleChange to manage input values
  

    const handleNext = (e) => {
      e.preventDefault();
    //   console.log(formData.deviceName)
      // Perform validation if needed
      nextStep();
    };
  
    return (
    <div className='form-container'>
        <form className='custom-form' onSubmit={handleNext}>
            <label htmlFor="deviceName" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Device name:</label>
            <input
                type="text"
                name="deviceName"
                value={formData.deviceName}
                onChange={handleNameChange}
            />
            <label htmlFor="fileInput" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Upload Picture:</label>
            <UploadImage fileRef={formData.picture} className={''} />
            <label htmlFor="energyConsumption" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Energy consumption:</label>
            <input
                type="number"
                name="energyConsumption"
                value={formData.energyConsumption}
                onChange={handleChange}
            />
            <label htmlFor="powerSupplyType" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Power Supply Type:</label>
            <select
                name="powerSupplyType"
                value={formData.powerSupplyType}
                onChange={handleChange}
                className={DeviceRegistrationStepperCSS.input}
            >
                <option value="AUTONOMOUS">Autonomous</option>
                <option value="HOME">Home</option>
            </select>
            <label htmlFor="deviceType" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Device Type:</label>
            <select
                name="deviceType"
                value={formData.deviceType}
                onChange={handleChange}
                className={`${DeviceRegistrationStepperCSS.input}`}
            >
                <option value="AmbientSensor">Ambient Sensor</option>
                <option value="AirConditioning">Air Conditioning</option>
                <option value="WashingMachine">Washing Machine</option>
                <option value="Lamp">Lamp</option>
                <option value="SprinklerSystem">Sprinkler System</option>
                <option value="VehicleGate">Vehicle Gate</option>
                <option value="SolarPanelSystem">Solar Panel System</option>
                <option value="HouseBattery">House Battery</option>
                <option value="ElectricVehicleCharger">Electric Vehicle Charger</option>
            </select>
            <div className={`${DeviceRegistrationStepperCSS.button} ${DeviceRegistrationStepperCSS.marginTop}`}>
                <Button text={'Next'} onClick={nextStep} submit={undefined} />
            </div>
        </form>
    </div>
    );
  };

export default Step1;