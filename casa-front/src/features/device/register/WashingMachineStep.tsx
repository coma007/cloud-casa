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
                >
                    <option value="WHITE">White</option>
                    <option value="COLOR">Color</option>
                </select>
                <button type="submit">Register</button>
                <button onClick={handlePrev}>Back</button>
            </form>
    </div>
    )
}

export default WashingMachineStep;