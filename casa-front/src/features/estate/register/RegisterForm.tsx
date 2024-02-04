import React, {  } from 'react';
import { Form, Container, Row, Col } from 'react-bootstrap';
import { Address } from 'cluster';
import { City } from '../Location';
import { EstateService } from '../EstateService';
import RegisterPageCSS from "./RegisterPage.module.scss"
import Button from '../../../components/forms/Button/Button';
import UploadImage from '../../../components/forms/UploadImage/UploadImage';


const RegisterForm = ({ formData, setFormData, countries, cities, selectedCity, setSelectedCity, selectedCountry, setSelectedCountry, fileRef }) => {


    const handleInputChange = (
        e: any,
        nestedKey?: any
    ) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            ...(nestedKey
                ? {
                    [nestedKey]: {
                        ...(prevData[nestedKey] as Record<string, string | number | Address | City>),
                        [name]: value,
                    },
                }
                : { [name]: value }),
        }));

        if (nestedKey == "country") {
            setSelectedCountry(value);
        }
        if (nestedKey == "city") {
            setSelectedCity(value);
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
        if(fileRef?.current?.files === null) return;
        formData.file = fileRef?.current?.files[0]!;
        e.preventDefault();
        console.log(formData)
        EstateService.register(formData).then((value) => console.log(value));
    };

    return (
        <Container className={RegisterPageCSS.center}>
            <Form onSubmit={handleSubmit}>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="name">
                            <Form.Label className={RegisterPageCSS.marginBottom}>Name</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter name"
                                name="name"
                                value={formData.name}
                                className={RegisterPageCSS.input}
                                onChange={(e) => handleInputChange(e)}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="type">
                            <Form.Label className={RegisterPageCSS.marginBottom}>Type</Form.Label>
                            <Form.Control
                                as="select"
                                name="type"
                                value={formData.type}
                                className={RegisterPageCSS.input}
                                onChange={(e) => handleInputChange(e)}
                                required
                            >
                                <option value="">Select type</option>
                                <option value="APPARTMENT">APPARTMENT</option>
                                <option value="HOUSE">HOUSE</option>
                            </Form.Control>
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="size">
                            <Form.Label className={RegisterPageCSS.marginBottom}>Size</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Enter size"
                                name="size"
                                value={formData.size}
                                className={RegisterPageCSS.input}
                                onChange={(e) => handleInputChange(e)}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="image">
                            <UploadImage className={""} fileRef={fileRef}/>
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="numberOfFloors">
                            <Form.Label className={RegisterPageCSS.marginBottom}>Number of Floors</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Enter number of floors"
                                name="numberOfFloors"
                                value={formData.numberOfFloors}
                                className={RegisterPageCSS.input}
                                onChange={(e) => handleInputChange(e)}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>
                <hr /> {/* Separator */}
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="country">
                            <Form.Label className={RegisterPageCSS.marginBottom}>Country</Form.Label>
                            <Form.Control
                                as="select"
                                name="country"
                                value={selectedCountry  }
                                className={RegisterPageCSS.input}
                                onChange={(e) => handleInputChange(e, 'country')}
                            >
                                <option value="">Select country</option>
                                {countries.map((country, index) => (
                                    <option key={index} value={country}>
                                        {country}
                                    </option>
                                ))}
                            </Form.Control>
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="city">
                            <Form.Label className={RegisterPageCSS.marginBottom}>City</Form.Label>
                            <Form.Control
                                as="select"
                                name="cityName"
                                value={selectedCity}
                                className={RegisterPageCSS.input}
                                onChange={(e) => handleInputChange(e, 'city')}
                            >
                                <option value="">Select city</option>
                                {cities.map((city, index) => (
                                    <option key={index} value={city}>
                                        {city}
                                    </option>
                                ))}
                            </Form.Control>
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="street">
                            <Form.Label className={RegisterPageCSS.marginBottom}>Street Address</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter street address"
                                name="street"
                                value={formData.address.address}
                                className={RegisterPageCSS.input}
                                onChange={(e) => handleInputChange(e, 'address')}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>
                <Button text={'Register'} onClick={handleSubmit} submit={undefined} />
            </Form>
        </Container>
    );
};

export default RegisterForm;
