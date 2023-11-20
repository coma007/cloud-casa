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
                <label htmlFor="minTemperature">Min temperature:</label>
                <input
                    type="number"
                    name="minTemperature"
                    value={formData.minTemperature}
                    onChange={handleChange}
                />
                <label htmlFor="maxTemperature">Max temperature:</label>
                <input
                    type="number"
                    name="maxTemperature"
                    value={formData.maxTemperature}
                    onChange={handleChange}
                />
                <label htmlFor="supportedModes">Supported modes:</label>
                <select
                    name="supportedModes"
                    multiple
                    value={formData.supportedModes}
                    onChange={handleSelectChange}
                >
                    <option value="COOLING">Cooling</option>
                    <option value="HEATING">Heating</option>
                </select>
                <button type="submit">Register</button>
                <button onClick={handlePrev}>Back</button>
            </form>
    </div>
    )
}

export default AirConditioningStep