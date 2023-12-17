import Button from "../../../components/forms/Button/Button";
import DeviceRegistrationStepperCSS from "./DeviceRegistrationStepper.module.scss"

const WashingMachineStep =({ formData, handleChange, handleSelectChange, prevStep, nextStep }) => {

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
                <label htmlFor="supportedModes">Supported modes:</label>
                <select
                    name="supportedModes"
                    multiple
                    value={formData.supportedModes}
                    onChange={handleSelectChange}
                    className={`${DeviceRegistrationStepperCSS.input} ${DeviceRegistrationStepperCSS.multipleSelect}`}
                >
                    <option value="WHITE">White</option>
                    <option value="COLOR">Color</option>
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

export default WashingMachineStep;