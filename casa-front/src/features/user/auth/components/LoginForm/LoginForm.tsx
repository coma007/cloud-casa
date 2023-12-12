import { useRef, useState } from 'react'
import { useNavigate } from "react-router-dom";
import * as yup from 'yup' 
import { Formik, Form, Field, ErrorMessage } from 'formik';
import ReactDOM, { render } from 'react-dom'
import React from 'react'
import { AuthService } from '../../services/AuthService';
import LoginFormCSS from "./LoginForm.module.scss"
import InputField from '../../../../../components/forms/InputField/InputField';
import ErrorMsg from '../../../../../components/error/ErrorMsg';
import Button from '../../../../../components/forms/Button/Button';

const LoginForm = () => {

  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const onClick = () => {
    (async function () {
      try {
          const jwt = await AuthService.login({ Username: username, Password: password });
          localStorage.setItem("token", jwt);
          navigate("/")
          
      } catch (error: any) {
        if (error.response.status == 403) {
        } else {
          alert(error.response.data);
        }
    }
    })()
  }

  const schema = yup.object().shape({
    username: yup.string().required(),
    password: yup.string().min(8, "password is too short")
    .matches( /[a-z]+/, "password needs to contain lowercase letter")
    .matches( /[A-Z]+/, "password needs to contain uppercase letter")
    .matches( /[0-9]+/, "password needs to contain number letter")
    .required(),
  })

  const handleSubmit = async () => {
    try {
      const jwt = await AuthService.login({ Username: username, Password: password});
      // TODO change flow bellow
      localStorage.setItem("token", jwt)
      navigate("/")
  } catch (error: any) {
    alert(error.response.data);
}
}


  return ( 
    <Formik
       initialValues={{
         password: '',
         username: '',
         token: '',
       }}
       validationSchema={schema}
       validateOnChange
       onSubmit={handleSubmit}
     >
       {({ errors, touched, setFieldValue, validateForm, isValid, handleSubmit }) => (

          <Form className={LoginFormCSS.form}> 
            <Field name="username" component={InputField} className={LoginFormCSS.input} usage="Username" value={username} onChange={(e:React.ChangeEvent<HTMLInputElement>) => {
                    setUsername(e.target.value);
                    setFieldValue("username", e.target.value);
                  }}/>
            <ErrorMsg val={errors["username"]} />

            <Field name="password" component={InputField} className={LoginFormCSS.input} usage="Password" value={password} onChange={(e:React.ChangeEvent<HTMLInputElement>) => {
                    setPassword(e.target.value);
                    setFieldValue("password", e.target.value);
                  }} />
            <ErrorMsg val={errors["password"]} />
            <div className={LoginFormCSS.button}>
              <span className="alignRight">
                <Button onClick={onClick} text="Sign in" submit={"submit"} />
              </span>
            </div>
          </Form >
      )}
    </Formik>      
    )

}


export default LoginForm