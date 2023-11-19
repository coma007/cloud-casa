
import TooltipCSS from 'components/view/Tooltip/Tooltip.module.scss'

import { useState } from 'react'
import { useLocation } from 'react-router-dom'
import React from 'react'
import LoginForm from '../../components/LoginForm/LoginForm'
import { AuthService } from '../../services/AuthService'

const LoginPage = () => {
    let [isBasePage, setIsBasePage] = useState<boolean>(true);


    const resetPage = () => {
        setIsBasePage(true);
    }


    return (
        <div className="page pageTwoCols">
            {isBasePage === true ? (
                <div className="rightCol">
                    <div className="authTitle">
                        <h2>Sign in</h2>
                        <span >
                            Welcome back!
                            <br />Please enter your login details to access your account.
                        </span>
                    </div>
                    <LoginForm/>
                    <div className="authBottomMessage">
                        Do not have an account ?
                        <br /> <a href='register'>Sign up here.</a>
                    </div>
                </div >
            ) : null}
        </div>
    )
}

export default LoginPage

export const Logout = () => {

    AuthService.logout()
    window.location.href = "/login";

    return (
        <></>
    )
}
