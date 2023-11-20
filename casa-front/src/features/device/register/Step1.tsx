import './Step1.css'

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
            <label htmlFor="deviceName">Device name:</label>
            <input
                type="text"
                name="deviceName"
                value={formData.deviceName}
                onChange={handleNameChange}
            />
            <label htmlFor="fileInput">Upload Picture:</label>
            <input
                type="file"
                id="fileInput"
                name="picture"
                ref={formData.picture}
                onChange={handleChange}
            />
            <label htmlFor="energyConsumption">Energy consumption:</label>
            <input
                type="number"
                name="energyConsumption"
                value={formData.energyConsumption}
                onChange={handleChange}
            />
            <label htmlFor="powerSupplyType">Power Supply Type:</label>
            <select
                name="powerSupplyType"
                value={formData.powerSupplyType}
                onChange={handleChange}
            >
                <option value="AUTONOMOUS">Autonomous</option>
                <option value="HOME">Home</option>
            </select>
            <label htmlFor="deviceType">Device Type:</label>
            <select
                name="deviceType"
                value={formData.deviceType}
                onChange={handleChange}
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
            <button type="submit">Next</button>
        </form>
    </div>
    );
  };

export default Step1;