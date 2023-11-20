import React from 'react';
import { Address, City } from './Location';
import { RealEstateRequest } from '../request/RealEstate';

export interface RealEstateCreate {
    name: string;
    address: Address;
    city: City;
    type: string;
    size: number;
    numberOfFloors: number;
    [key: string]: string | number | Address | City;
}

export interface RealEstate {
    name: string;
    address: Address;
    city: City;
    type: string;
    size: number;
    numberOfFloors: number;
    request: RealEstateRequest;
    [key: string]: string | number | Address | City | RealEstateRequest;
}



