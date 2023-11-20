const VehicleGateStep = ({ formData, handleChange, prevStep, nextStep }) => {

    const handlePrev = (e) => {
        e.preventDefault();
        prevStep();
    };

    const handleNext = (e) => {
        e.preventDefault();
          console.log(formData.allowedVehicles)
        // Perform validation if needed
        nextStep();
    };

    return (
    <div className='form-container'>
            <form className='custom-form' onSubmit={handleNext}>
                <label htmlFor="allowedVehicles">Enter allowed vehicles (each row is one plate):</label>
                <textarea
                    name="allowedVehicles"
                    value={formData.allowedVehicles.join('\n')} // Join plates with new lines
                    onChange={handleChange}
                />
                <button type="submit">Register</button>
                <button onClick={handlePrev}>Back</button>
            </form>
    </div>
    )
}

export default VehicleGateStep