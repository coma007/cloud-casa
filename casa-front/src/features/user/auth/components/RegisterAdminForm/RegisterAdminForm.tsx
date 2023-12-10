import RegisterAdminFormCSS from './RegisterAdminForm.module.scss'
import React, { useRef, useState } from 'react';
import { useNavigate } from "react-router-dom";
import * as yup from 'yup'
import { Formik, Form, Field, ErrorMessage } from 'formik';
import ErrorMsg from '../../../../components/error/ErrorMsg';
import Button from '../../../../components/forms/Button/Button';
import InputField from '../../../../components/forms/InputField/InputField';
import { AuthService } from '../../services/AuthService';

const RegisterAdminForm = () => {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  
  const passwordValidator = yup.string().min(8, "password is too short")
    .matches(/[a-z]+/, "password needs to contain lowercase letter")
    .matches(/[A-Z]+/, "password needs to contain uppercase letter")
    .matches(/[0-9]+/, "password needs to contain number letter")
    .required();

  const schema = yup.object().shape({
    email: yup.string().email().required(),
    password: passwordValidator,
    "confirm password": passwordValidator.oneOf([yup.ref('password')], 'Passwords must match'),
  })



  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      await AuthService.registerAdmin({ Username: email, Password: password})
      navigate("/login")
    } catch (error: any) {
      alert(error.response.data);
    }
  }

  return (
    <Formik
      initialValues={{
        email: "",
        password: "",
        "confirm password": "",
      }}
      validateOnChange
      validationSchema={schema}
      onSubmit={handleSubmit}
    >
      {({ errors, touched, setFieldValue, validateForm, isValid }) => (
        <Form>
          <div className={RegisterAdminFormCSS.grid}>
            <div>
              <Field component={InputField} className={RegisterAdminFormCSS.inlineInput} usage="Email" value={email} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setEmail(e.target.value);
                setFieldValue("email", e.target.value);
              }} />
              <ErrorMsg val={errors["email"]} />
            </div>
            <div>
              <Field component={InputField} className={RegisterAdminFormCSS.inlineInput} usage="Password" value={password} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setPassword(e.target.value);
                setFieldValue("password", e.target.value);
              }} />
              <ErrorMsg val={errors["password"]} />
            </div>
            <div>
              <Field component={InputField} className={`alignRight ${RegisterAdminFormCSS.inlineInput}`} usage="Confirm password" value={confirmPassword} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setConfirmPassword(e.target.value);
                setFieldValue("confirm password", e.target.value);
              }} />
              <ErrorMsg val={errors["confirm password"]} customClass="alignRight" />
            </div>
          </div>


          <div className={RegisterAdminFormCSS.button}>
            <span className="alignRight">
              <Button submit="submit" onClick={null} text="Get started" />
            </span>
          </div>
        </Form>
      )}
    </Formik >
  )
}
export default RegisterAdminForm