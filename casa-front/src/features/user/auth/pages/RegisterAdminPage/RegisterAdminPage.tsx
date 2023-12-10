
import TooltipCSS from 'components/view/Tooltip/Tooltip.module.scss'

import { useState } from 'react'
import { useLocation } from 'react-router-dom'
import React from 'react'
import LoginForm from '../../components/LoginForm/LoginForm'
import { AuthService } from '../../services/AuthService'
import RegisterAdminForm from '../../components/RegisterAdminForm/RegisterAdminForm'

const RegisterAdminPage = () => {


    return (
        <div className="page pageTwoCols">
            <div className="rightCol">
                <div className="authTitle">
                    <h2>Register new admin</h2>
                    <span >
                        Welcome back!
                        <br />Please enter username and password
                    </span>
                </div>
                <RegisterAdminForm/>
            </div >
        </div>
    )
}

export default RegisterAdminPage