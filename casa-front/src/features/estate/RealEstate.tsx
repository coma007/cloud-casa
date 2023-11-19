import React from 'react';
import { Address, City } from './Location';

export interface RealEstateCreate {
    name: string;
    address: Address;
    type: string;
    size: number;
    numberOfFloors: number;
    city: City;
    [key : string] : string | number | Address | City;
}

