import ProfilePageCSS from './ProfilePage.module.scss'
import React, { useState } from 'react'

import Profile from "../../../../../assets/menu/profile.png"
import Menu from '../../../../../components/navigation/Menu/Menu';
import InputField from '../../../../../components/forms/InputField/InputField';
import PageTitle from '../../../../../components/view/PageTitle/PageTitle';
import Button from '../../../../../components/forms/Button/Button';


const ProfilePage = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');

    (async function () {
        try {
            // const data = await AuthService.getUserData();
            // setFirstName(data.FirstName)
            // setLastName(data.LastName)
            // setEmail(data.Email)
            // setPhone(data.Phone)
        } catch (error: any) {
          alert(error.response.data);
      }
    })()
    return (
        <div>
        <Menu admin={false} />
        <div>
            <PageTitle title="Profile" description="View or edit your personal profile." />
            <div className={ProfilePageCSS.section}>
                <div className={ProfilePageCSS.subsection}>
                    <div className={ProfilePageCSS.mainInfo}>
                        <img src={Profile} />
                        <div className={ProfilePageCSS.input}>
                            <InputField usage='First Name' className={ProfilePageCSS.input} />
                            <InputField usage='Last Name' className={ProfilePageCSS.input} />
                        </div>
                    </div>
                    <InputField usage='Email' className={ProfilePageCSS.input} />
                    <InputField usage='Phone' className={ProfilePageCSS.input} />
                    <Button text='Save changes' onClick={undefined} submit='submit' />
                </div>
                <div className={`${ProfilePageCSS.subsectionRight} ${ProfilePageCSS.subsection}`}>
                    <div className={ProfilePageCSS.warning}>
                        It is recommended to change passwords often, for your security. <br /> <br />
                        To ensure the best security, you should not use one of your previous passwords.
                    </div>
                    <InputField usage='New Password' className={''}  />
                    <InputField usage='Confirm Password' className={''} />
                    <div>
                        <Button submit='submit' text='Change password' onClick={undefined} />
                    </div>
                </div>
            </div>
        </div>
    </div >
    )
}

export default ProfilePage