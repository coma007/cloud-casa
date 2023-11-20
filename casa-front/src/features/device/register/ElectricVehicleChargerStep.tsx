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
                <label htmlFor="numOfSlots">Number of slots:</label>
                <input
                    type="number"
                    name="numOfSlots"
                    value={formData.numOfSlots}
                    onChange={handleChange}
                />
                <button type="submit">Register</button>
                <button onClick={handlePrev}>Back</button>
            </form>
    </div>
    )
}

export default ElectricVehicleChargerStep;