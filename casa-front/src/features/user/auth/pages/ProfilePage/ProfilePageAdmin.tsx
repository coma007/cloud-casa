import ProfilePageCSS from './ProfilePage.module.scss'
import React, { useState } from 'react'

import Profile from "../../../../../assets/menu/profile.png"
import Menu from '../../../../../components/navigation/Menu/Menu';
import InputField from '../../../../../components/forms/InputField/InputField';
import PageTitle from '../../../../../components/view/PageTitle/PageTitle';
import Button from '../../../../../components/forms/Button/Button';
import { PROFILE_IMG } from '../../../../../api';
import { AuthService } from '../../services/AuthService';
import { getRole } from '../../../../../routes/GuardedRoute';


const ProfilePageAdmin = () => {

    const [password, setPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');


    const handleChangePassword = () =>{
        (async function () {
            try {
                if(newPassword !== confirmPassword){
                    alert("Passwords must match");
                    return;
                }
                const data = await AuthService.changePassword({NewPassword: newPassword, OldPassword: password});
            } catch (error: any) {
            //   alert(error.response.data);
                console.log(error);
          }
        })()
  

    }
    return (
        <div>
        <Menu admin={true} />
        <div>
            <PageTitle title="Profile" description="View or edit your personal profile." />
            <div className={ProfilePageCSS.section}>
                <div className={`${ProfilePageCSS.subsectionRight} ${ProfilePageCSS.subsection}`}>
                    <div className={ProfilePageCSS.warning}>
                        It is recommended to change passwords often, for your security. <br /> <br />
                        To ensure the best security, you should not use one of your previous passwords.
                    </div>
                    <InputField usage='Old Password' className={''}  value={password} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                        setPassword(e.target.value);
                    }} />
                    <InputField usage='New Password' className={''}  value={newPassword} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                        setNewPassword(e.target.value);
                    }} />
                    <InputField usage='Confirm Password' className={''} value={confirmPassword} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                        setConfirmPassword(e.target.value);
                    }} />
                    <div>
                        <Button submit='submit' text='Change password' onClick={handleChangePassword} />
                    </div>
                </div>
            </div>
        </div>
    </div >
    )
}

export default ProfilePageAdmin