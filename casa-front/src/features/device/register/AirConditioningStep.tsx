import Button from "../../../components/forms/Button/Button";
import DeviceRegistrationStepperCSS from "./DeviceRegistrationStepper.module.scss"

const AirConditioningStep = ({ formData, handleChange, handleSelectChange, prevStep, nextStep }) => {

    const handlePrev = (e) => {
        e.preventDefault();
        prevStep();
    };

    const handleNext = (e) => {
        e.preventDefault();
          console.log(formData.supportedModes)
        // Perform validation if needed
        nextStep();
    };

    return (
    <div className='form-container'>
            <form className='custom-form' onSubmit={handleNext}>
                <label htmlFor="minTemperature" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Min temperature:</label>
                <input
                    type="number"
                    name="minTemperature"
                    value={formData.minTemperature}
                    onChange={handleChange}
                />
                <label htmlFor="maxTemperature" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Max temperature:</label>
                <input
                    type="number"
                    name="maxTemperature"
                    value={formData.maxTemperature}
                    onChange={handleChange}
                />
                <label htmlFor="supportedModes" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Supported modes:</label>
                <select
                    name="supportedModes"
                    multiple
                    value={formData.supportedModes}
                    onChange={handleSelectChange}
                    className={`${DeviceRegistrationStepperCSS.input} ${DeviceRegistrationStepperCSS.multipleSelect}`}
                >
                    <option value="COOLING">Cooling</option>
                    <option value="HEATING">Heating</option>
                    <option value="AUTO">Auto</option>
                    <option value="VENTILATION">Ventilation</option>
                </select>
                <div className={DeviceRegistrationStepperCSS.button}>
                    <Button text={"Register"} onClick={handleNext} submit={undefined} />
                    <div className={DeviceRegistrationStepperCSS.marginLeft}>
                        <Button text={"Back"} onClick={handlePrev} submit={undefined} />
                    </div>
                </div>
            </form>
    </div>
    )
}

export default AirConditioningStep