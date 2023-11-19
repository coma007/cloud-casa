import React from 'react';
export interface Address {
    street: string;
    latitude: number;
    longitude: number;
    [key: string]: string | number
}

export interface City {
    name: string;
    country: string;
    [key: string]: string
}