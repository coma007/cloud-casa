import Button from "../../../components/forms/Button/Button";
import DeviceRegistrationStepperCSS from "./DeviceRegistrationStepper.module.scss"

const ElectricVehicleChargerStep = ({ formData, handleChange, prevStep, nextStep }) => {

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
                <label htmlFor="chargePower">Charge power:</label>
                <input
                    type="number"
                    name="chargePower"
                    value={formData.chargePower}
                    onChange={handleChange}
                />
                <label htmlFor="numOfSlots" className={`${DeviceRegistrationStepperCSS.marginTop}`}>Number of slots:</label>
                <input
                    type="number"
                    name="numOfSlots"
                    value={formData.numOfSlots}
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

export default ElectricVehicleChargerStep;