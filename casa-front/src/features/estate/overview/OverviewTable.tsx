import React from 'react';
import Table from 'react-bootstrap/Table';

const OverviewTable = ({ items }) => {
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
                    <th>Status</th>
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
                            <td>{item.request.approved === false && item.request.declined === false ? <>{"in progress"}</> : <>{item.request.approved ? "approved" : "declined"}</>}</td>
                        </tr>
                    ))}
                </tbody>
                : <>no items to show</>}
        </Table>
    );
};

export default OverviewTable;
