import React from 'react';

export interface RealEstateRequest {
    id: number;
    approved: boolean;
    comment: string;
    [key: string]: boolean | number | string;
}
