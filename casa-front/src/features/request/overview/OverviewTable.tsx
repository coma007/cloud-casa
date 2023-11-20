import React from 'react';
import { Button } from 'react-bootstrap';
import Table from 'react-bootstrap/Table';
import { RequestService } from '../RequestService';

const OverviewTable = ({ items }) => {

    const manage = (index, item, approve) => {
        if (approve === true) {
            item.request.approved = true;
        }
        if (approve === false) {
            item.request.declined = true;
        }
        items[index] = item;
        RequestService.manage(item.request);
    }

    return (
        <Table striped bordered hover>
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Size</th>
                    <th>Number of Floors</th>
                    <th>Address</th>
                    <th>City</th>
                    <th>Country</th>
                    <th>Owner</th>
                    <th></th>
                </tr>
            </thead>
            {items !== undefined ?
                <tbody>
                    {items.map((item, index) => (
                        <tr key={index}>
                            <td>{item.name}</td>
                            <td>{item.type}</td>
                            <td>{item.size}</td>
                            <td>{item.numberOfFloors}</td>
                            <td>{item.address?.address}</td>
                            <td>{item.city?.name}</td>
                            <td>{item.city?.country}</td>
                            <td>{item.owner?.email}</td>
                            <td>{item.request.approved === false && item.request.declined === false ? <><Button onClick={() => manage(index, item, true)}>Approve</Button> <Button variant="danger" onClick={() => manage(index, item, false)}>Decline</Button></> : <>{item.request.approved ? "approved" : "declined"}</>}</td>
                        </tr>
                    ))}
                </tbody>
                : <tbody><tr><td>no items to show</td></tr></tbody>}
        </Table >
    );
};

export default OverviewTable;
