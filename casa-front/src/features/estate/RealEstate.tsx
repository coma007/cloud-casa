import React from 'react';
import { Address, City } from './Location';
import { RealEstateRequest } from '../request/RealEstateRequest';
import { User } from '../user/auth/types/User';

export interface RealEstateCreate {
    name: string;
    address: Address;
    city: City;
    type: string;
    size: number;
    numberOfFloors: number;
    file: File | null,
    [key: string]: string | number | Address | City | File | null;
}

export interface RealEstateCreateFlat {
    name: string;
    address: string;
    cityName: string;
    cityCountry: string
    type: string;
    size: number;
    numberOfFloors: number;
    longitude: number;
    latitude: number;
    file: File | null;

}

export interface RealEstate {
    id: number
    name: string;
    address: Address;
    city: City;
    type: string;
    size: number;
    numberOfFloors: number;
    request: RealEstateRequest;
    owner: User;
    [key: string]: string | number | Address | City | RealEstateRequest | User;
}



