import React, { useEffect, useState } from 'react';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import { RealEstateCreate } from '../RealEstate';
import { Address } from 'cluster';
import { City } from '../Location';
import { EstateService } from '../EstateService';


const RegisterForm = ({ formData, setFormData, countries, cities, selectedCity, setSelectedCity, selectedCountry, setSelectedCountry }) => {


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
        e.preventDefault();
        console.log(formData)
        EstateService.register(formData).then((value) => console.log(value));
    };

    return (
        <Container>
            <Form onSubmit={handleSubmit}>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="name">
                            <Form.Label>Name</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter name"
                                name="name"
                                value={formData.name}
                                onChange={(e) => handleInputChange(e)}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="type">
                            <Form.Label>Type</Form.Label>
                            <Form.Control
                                as="select"
                                name="type"
                                value={formData.type}
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
                            <Form.Label>Size</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Enter size"
                                name="size"
                                value={formData.size}
                                onChange={(e) => handleInputChange(e)}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>
                <Row className="mb-3">
                    <Col>
                        <Form.Group controlId="numberOfFloors">
                            <Form.Label>Number of Floors</Form.Label>
                            <Form.Control
                                type="number"
                                placeholder="Enter number of floors"
                                name="numberOfFloors"
                                value={formData.numberOfFloors}
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
                            <Form.Label>Country</Form.Label>
                            <Form.Control
                                as="select"
                                name="country"
                                value={selectedCountry  }
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
                            <Form.Label>City</Form.Label>
                            <Form.Control
                                as="select"
                                name="cityName"
                                value={selectedCity}
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
                            <Form.Label>Street Address</Form.Label>
                            <Form.Control
                                type="text"
                                placeholder="Enter street address"
                                name="street"
                                value={formData.address.address}
                                onChange={(e) => handleInputChange(e, 'address')}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>
                <Button variant="primary" type="submit">
                    Submit
                </Button>
            </Form>
        </Container>
    );
};

export default RegisterForm;
