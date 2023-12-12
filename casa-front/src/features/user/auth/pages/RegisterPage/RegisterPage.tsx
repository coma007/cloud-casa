
import React from 'react'
import RegisterForm from '../../components/RegisterForm/RegisterForm'
import Banner from '../../../../../components/navigation/Banner/Banner'

const RegisterPage = () => {
  return (
    <div className="page pageTwoCols">
      <div>
        <Banner />
      </div>
      <div className="rightCol">
        <div className="authTitle">
          <h2>Sign up</h2>
          <span >
            Welcome !
            Sign up now to get started.
          </span>
        </div>
        <RegisterForm />
        <div className="authBottomMessage">
          Already have an account ?
          <br /> <a href='login'>Sign in here.</a>
        </div>
      </div >
    </div>
  )
}

export default RegisterPage