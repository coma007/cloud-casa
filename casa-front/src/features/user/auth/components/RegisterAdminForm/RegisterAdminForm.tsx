import RegisterAdminFormCSS from './RegisterAdminForm.module.scss'
import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from "react-router-dom";
import * as yup from 'yup'
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { AuthService } from '../../services/AuthService';
import ChangePasswordModal from '../ChangePasswordModal/ChangePasswordModal';
import { UserService } from '../../../UserService';
import InputField from '../../../../../components/forms/InputField/InputField';
import ErrorMsg from '../../../../../components/error/ErrorMsg';
import Button from '../../../../../components/forms/Button/Button';

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

  const [showModal, setShowModal] = useState(false);

  useEffect(() => {
    (async function () {
        try {
            const init = await UserService.isSuperAndInit();
            // localStorage.setItem("token", init.Token);
            setShowModal(init);
        } catch (error) {
            console.error(error);
        }
    })()
}, []);



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
    <React.Fragment>
      <ChangePasswordModal show={showModal} setShow={setShowModal}/>
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
            <div>
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
    </React.Fragment>
  )
}
export default RegisterAdminForm