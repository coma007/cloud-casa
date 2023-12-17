
import TooltipCSS from 'components/view/Tooltip/Tooltip.module.scss'

import { useState } from 'react'
import { useLocation } from 'react-router-dom'
import React from 'react'
import LoginForm from '../../components/LoginForm/LoginForm'
import { AuthService } from '../../services/AuthService'
import RegisterAdminForm from '../../components/RegisterAdminForm/RegisterAdminForm'
import Menu from '../../../../../components/navigation/Menu/Menu'

const RegisterAdminPage = () => {
    let role = AuthService.getRole();

    return (
        <div>
            <Menu admin={true} superadmin={role === "super admin"} superadminInit={ role === "super admin init"}/>
            <div className="page pageTwoCols">
                <div className="rightCol">
                    <div className="authTitle">
                        <h2>Register new admin</h2>
                    </div>
                    <RegisterAdminForm/>
                </div >
            </div>
        </div>
    )
}

export default RegisterAdminPage