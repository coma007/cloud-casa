import React from 'react';
import CardCSS from './Card.module.scss';

const Card = (props: { children: React.ReactNode }) => {
    return (
        <div className={CardCSS.card}>
            {props.children}
        </div>
    );
};

export default Card;
