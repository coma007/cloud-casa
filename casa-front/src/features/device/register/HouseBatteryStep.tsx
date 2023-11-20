const HouseBatteryStep = ({ formData, handleChange, prevStep, nextStep }) => {

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
                <button type="submit">Register</button>
                <button onClick={handlePrev}>Back</button>
            </form>
    </div>
    )
}

export default HouseBatteryStep;