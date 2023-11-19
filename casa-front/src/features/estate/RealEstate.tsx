import React from 'react';
import { Address, City } from './Location';

export interface RealEstateCreate {
    name: string;
    address: Address;
    city: City;
    type: string;
    size: number;
    numberOfFloors: number;
    [key: string]: string | number | Address | City;
}

