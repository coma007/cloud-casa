import React, { useEffect, useState } from 'react';
import { Form, Button, Container, Row, Col } from 'react-bootstrap';
import { RealEstateCreate } from '../RealEstate';
import { Address } from 'cluster';
import { City } from '../Location';
import { EstateService } from '../EstateService';


const RegisterForm = ({ formData, setFormData }) => {


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
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        EstateService.register(formData);
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
                                value={formData.city.country}
                                onChange={(e) => handleInputChange(e, 'address')}
                            >
                                <option value="">Select country</option>
                                <option value="Country1">Country1</option>
                                <option value="Country2">Country2</option>
                                <option value={formData.city.country}>{formData.city.country}</option>
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
                                value={formData.city.name}
                                onChange={(e) => handleInputChange(e, 'city')}
                            >
                                <option value="">Select city</option>
                                <option value="City1">City1</option>
                                <option value="City2">City2</option>
                                <option value={formData.city.name}>{formData.city.name}</option>
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
                                value={formData.address.street}
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
