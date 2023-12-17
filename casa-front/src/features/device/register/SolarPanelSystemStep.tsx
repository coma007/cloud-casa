import Button from "../../../components/forms/Button/Button";
import DeviceRegistrationStepperCSS from "./DeviceRegistrationStepper.module.scss"

const SolarPanelSystemStep = ({ formData, handleChange, prevStep, nextStep }) => {

    const handlePrev = (e) => {
        e.preventDefault();
        prevStep();
    };

    const handleNext = (e) => {
        e.preventDefault();
          console.log(formData.size)
        // Perform validation if needed
        nextStep();
    };

    return (
    <div className='form-container'>
            <form className='custom-form' onSubmit={handleNext}>
                <label htmlFor="size">Size:</label>
                <input
                    type="number"
                    name="size"
                    step="any"
                    value={formData.size}
                    onChange={handleChange}
                />
                <label htmlFor="efficiency" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Efficiency:</label>
                <input
                    type="number"
                    name="efficiency"
                    step="any"
                    value={formData.efficiency}
                    onChange={handleChange}
                />
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

export default SolarPanelSystemStep;