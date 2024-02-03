import React, { useEffect, useRef } from 'react'
import PermissionModalCSS from "./PermissionModalCSS.module.scss"
import * as yup from 'yup'
import { Formik, Form, Field, ErrorMessage, FormikProps, FormikValues } from 'formik';
import { useState } from 'react'
import Modal from 'react-bootstrap/Modal';
import { Permission } from '../../device/Device';
import { DeviceService } from '../../device/DeviceService';
import ModalWindow from '../../../components/view/Modal/ModalWindow';
import FilterUser from '../../device/details/inspect/filter/FilterUser';
import { UserService } from '../../user/UserService';
import { EstateService } from '../EstateService';
import Button from '../../../components/forms/Button/Button';

const PermissionModal = (props: { show: any, setShow: any, estate: any }) => {
    const [toggleIsOn, setToggleIsOn] = useState(false);
    const [username, setUsername] = useState<string>("");
    const [isOwner, setIsOwner] = useState(false);
    const [message, setMessage] = useState("");

    useEffect(() => {
        (async function () {
            try {
                if (props.estate.id > 0) {
                    // const fetchedDevice = await DeviceService.getDeviceDetails(props.estate.id );
                    // console.log(fetchedDevice)
                    const fetchedIsOwner = await EstateService.isOwner(props.estate.id );
                    setIsOwner(fetchedIsOwner);
                }
            } catch (error) {
                console.error(error);
            }
        })()
    }, [props.estate]);

    const formRef = useRef<FormikProps<FormikValues>>(null);

    const handleIsOnClick = () => {
        setToggleIsOn(!toggleIsOn);
    };

    const handleIsOpenClick = () => {
        setToggleIsOn(!toggleIsOn);

    };

    const handleModeChange = (e) => {
        
        // setUsername(m);
    };

    const handleGivePermission = ()=> {

        (async () => {
            try{
            const userId = await UserService.getIdByUsername(username);
            let permission: Permission = {
                Kind: "real estate",
                ResourceId: props.estate!.id,
                Type: 'MODERATOR',
                UserId: userId
            } 
            const result = await DeviceService.createPermission(permission);
            console.log(result);
            setMessage("Already exists");
        } catch (error) {
            setMessage("Error occured while giving permission")
            console.error(error);
        }
        })();
    }


    const handleRemovePermission = ()=> {

        (async () => {
            try{
            const userId = await UserService.getIdByUsername(username);
            let permission: Permission = {
                Kind: "real estate",
                ResourceId: props.estate!.id,
                Type: 'MODERATOR',
                UserId: userId
            } 
            const result = await DeviceService.deletePermission(permission);
            console.log(result);
            setMessage("Valid");
        } catch (error) {
            setMessage("Error occured while removing permission")
            console.error(error);
        }
        setUsername("");
        setUsername(username);
        })();
    }

    const closeModal = () => {
        props.setShow(false);

    };

    useEffect(() => {
        (async () => {
            try{
                const userId = await UserService.getIdByUsername(username);
                let permission: Permission = {
                    Kind: "real estate",
                    ResourceId: props.estate!.id,
                    Type: 'MODERATOR',
                    UserId: userId
                } 
                const result = await DeviceService.permissionExists(permission);
                if(result)
                    setMessage("Already exists");
                else
                    setMessage("Valid");
                console.log(result);
        } catch (error) {
            setMessage("Invalid")
            console.error(error);
        }
        console.log(message);
        })();
    }, [username]);

    const usernameChanged =(newUsername)=>{
        setUsername(newUsername);
    }

    return (
        <ModalWindow
            height="65%"
            width='45%'
            isOpen={props.show}
            closeWithdrawalModal={closeModal}
            okWithdrawalModal={null}
            title="Permissions"
            formId='NULL VALUE'
            noDiscard=""
            buttonText={""} >
            <div>
                {isOwner && <div className={PermissionModalCSS.right}>
                <div>
                    <FilterUser username={username} onInputChange={usernameChanged} handleSubmit={undefined} text='Give permission'></FilterUser>
                                    <hr></hr>
                    <p>{message}</p>
                </div>
                </div>}
                <div className={PermissionModalCSS.buttons}>
                    <Button className={PermissionModalCSS.give} text={'Give permission'} onClick={handleGivePermission} submit={undefined}></Button>
                    <Button className={PermissionModalCSS.remove} text={'Remove permission'} onClick={handleRemovePermission} submit={undefined}></Button>
                </div>
            </div>
        </ModalWindow>
    )
}

export default PermissionModal