
import React from 'react'
import RegisterForm from '../../components/RegisterForm/RegisterForm'

const RegisterPage = () => {
  return (
    <div className="page pageTwoCols">
      <div className="rightCol">
        <div className="authTitle">
          <h2>Sign up</h2>
          <span >
            Welcome !
            <br />Sign up now to get started.
          </span>
        </div>
        <RegisterForm />
        <div className="oauth">
          Or use alternative way to sign up <br />
        </div>
        <div className="authBottomMessage">
          Already have an account ?
          <br /> <a href='login'>Sign in here.</a>
        </div>
      </div >
    </div>
  )
}

export default RegisterPage