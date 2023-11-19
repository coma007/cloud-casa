import { Row, Col } from 'react-bootstrap';
import RegisterForm from './RegisterForm';
import Map from './Map';
import { useEffect, useState } from 'react';
import { Address, City } from '../Location';
import { RealEstateCreate } from '../RealEstate';

const RegisterPage = () => {

    const [address, setAddress] = useState<[Address, City]>([{
        street: '',
        latitude: 0,
        longitude: 0,
    }, {
        name: '',
        country: '',
    }]);

    const [formData, setFormData] = useState<RealEstateCreate>({
        name: '',
        type: '',
        size: 0,
        numberOfFloors: 0,
        address: address[0],
        city: address[1]
    });

    useEffect(() => {
        setFormData({
            ...formData, address: address[0], city: address[1]
        })
    }, [address])

    return (
        <div className="container">
            <h2 className="mb-4">Register Real Estate</h2>
            <Row>
                <Col md={6}>
                    <div>
                        <RegisterForm formData={formData} setFormData={setFormData} />
                    </div>
                </Col>
                <Col md={6}>
                    <Map setAddress={setAddress}></Map>
                </Col>
            </Row>
        </div>
    );
};

export default RegisterPage;
