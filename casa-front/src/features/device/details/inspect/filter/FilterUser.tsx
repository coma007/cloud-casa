import React, { useEffect, useState } from 'react';
import FilterCSS from './Filter.module.scss'
import InputField from '../../../../../components/forms/InputField/InputField'
import Button from '../../../../../components/forms/Button/Button';
import { ReactSearchAutocomplete } from 'react-search-autocomplete'
import { User } from '../../../../user/auth/types/User';
import { UserService } from '../../../../user/UserService';


const FilterUser = (props: { username: string, onInputChange: any, handleSubmit: any , text?: string}) => {

    const [items, setItems] = useState<User[]>([]);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const users = await UserService.getAllUsers();
                setItems(users);
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        };

        fetchData();
    }, []);


    const handleOnSelect = (item) => {
        props.onInputChange(item.email);
    }

    const formatResult = (item, searchString) => {
        return (
            <>
                <span style={{ display: 'block', textAlign: 'left' }}><b>{item.firstName} {item.lastName}</b></span>
                <span style={{ display: 'block', textAlign: 'left' }}><small>{item.email}</small></span>
            </>
        )
    }


    return (
        <div className={FilterCSS.row}>
            <span className={FilterCSS.autocomplete}>
                <label>Insert username:</label>
                <ReactSearchAutocomplete<User>
                    items={items}
                    resultStringKeyName={"email"}
                    fuseOptions={{ keys: ["firstName", "lastName", "email"] }}
                    onSelect={handleOnSelect}
                    autoFocus
                    formatResult={(item, searchString: string) => formatResult(item, searchString)}
                    styling={
                        {
                            height: "15px",
                            border: "1px solid gray",
                            borderRadius: "10px",
                            boxShadow: "0px",
                            backgroundColor: "white",
                            hoverBackgroundColor: "#eee",
                            color: "#212121",
                            fontSize: "16px",
                            fontFamily: "Lato",
                            lineColor: "rgb(232, 234, 237)",
                        }
                    }
                />
            </span>
            <span>
                {props.handleSubmit && <Button text={props.text || 'Filter'} onClick={props.handleSubmit} submit={undefined} />}
            </span>
        </div>
    )
}

export default FilterUser