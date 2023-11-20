import { Row, Col } from 'react-bootstrap';
import RegisterForm from './RegisterForm';
import Map from './Map';
import { useEffect, useState } from 'react';
import { Address, City } from '../Location';
import { RealEstateCreate } from '../RealEstate';
import { LocationService } from './LocationService';

const RegisterPage = () => {

    const [address, setAddress] = useState<[Address, City]>([{
        address: '',
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
        if (countries.indexOf(address[1].country.toUpperCase()) == -1) {
            let temp = countries
            temp.push(address[1].country.toUpperCase())
            setCountries(temp)
        }
        setSelectedCountry(address[1].country.toUpperCase())
        setSelectedCity(address[1].name.toUpperCase())
    }, [address])

    const [countries, setCountries] = useState<string[]>([]);
    const [cities, setCities] = useState<string[]>([]);
    const [selectedCountry, setSelectedCountry] = useState<string>("");
    const [selectedCity, setSelectedCity] = useState<string>("");


    useEffect(() => {
        LocationService.getAllCountries().then((value) => { setCountries(value) })
    }, [])

    useEffect(() => {
        if (selectedCountry != "") {
            LocationService.getAllCities(selectedCountry).then((value) => { setCities(value.map(item => item.name)); console.log(value) })
        }
    }, [selectedCountry])

    useEffect(() => {
        if (cities.indexOf(selectedCity) == -1 && selectedCity != "") {
            LocationService.getAllCities(selectedCountry).then((value) => {
                value.push({ name: selectedCity, country: selectedCountry }); setCities(value.map(item => item.name));
            })
        }
    }, [selectedCity])


    return (
        <div className="container">
            <h2 className="mb-4">Register Real Estate</h2>
            <Row>
                <Col md={6}>
                    <div>
                        <RegisterForm formData={formData} setFormData={setFormData} countries={countries} cities={cities} selectedCity={selectedCity} setSelectedCity={setSelectedCity} selectedCountry={selectedCountry} setSelectedCountry={setSelectedCountry} />
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
