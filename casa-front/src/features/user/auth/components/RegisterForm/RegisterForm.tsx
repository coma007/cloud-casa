import RegisterFormCSS from './RegisterForm.module.scss'
import React, { useRef, useState } from 'react';
import { useNavigate } from "react-router-dom";
import * as yup from 'yup'
import { Formik, Form, Field, ErrorMessage } from 'formik';
import ErrorMsg from '../../../../components/error/ErrorMsg';
import Button from '../../../../components/forms/Button/Button';
import InputField from '../../../../components/forms/InputField/InputField';
import { AuthService } from '../../services/AuthService';

const RegisterForm = () => {

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  
  let fileRef = useRef<HTMLInputElement>(null);

  const passwordValidator = yup.string().min(8, "password is too short")
    .matches(/[a-z]+/, "password needs to contain lowercase letter")
    .matches(/[A-Z]+/, "password needs to contain uppercase letter")
    .matches(/[0-9]+/, "password needs to contain number letter")
    .required();

    const fileSchema = yup.object().shape({
      file: yup.mixed()
        .test("is-file-too-big", "File exceeds 100MB", () => {
          let valid = true;
          const files = fileRef?.current?.files;
          if (files) {
            const fileArr = Array.from(files);
            fileArr.forEach((file) => {
              const size = file.size / 1024 / 1024;
              if (size > 100) {
                valid = false;
              }
            });
          }
          return valid;
        })
        .test(
          "is-file-of-correct-type",
          "File is not of supported type",
          () => {
            let valid = true;
            const files = fileRef?.current?.files;
            if (files) {
              const fileArr = Array.from(files);
              fileArr.forEach((file) => {
                const type = file.type.split("/")[1];
                const validTypes = [
                  "png",
                  "jpg",
                  "jpeg"
                ];
                if (!validTypes.includes(type)) {
                  valid = false;
                }
              });
            }
            return valid;
          }
        )
    })

  const schema = yup.object().shape({
    "first name": yup.string().required(),
    "last name": yup.string().required(),
    email: yup.string().email().required(),
    username: yup.string().email().required(),
    password: passwordValidator,
    "confirm password": passwordValidator.oneOf([yup.ref('password')], 'Passwords must match'),
    "file": fileSchema
  })



  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      if(fileRef?.current?.files === null) return;
      await AuthService.register({ Email: email, Password: password, FirstName: firstName, LastName: lastName, File: fileRef?.current?.files[0]! })
      navigate("/login")
    } catch (error: any) {
      alert(error.response.data);
    }
  }

  return (
    <Formik
      initialValues={{
        "first name": "",
        "last name": "",
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
          <div className={RegisterFormCSS.grid}>
            <div>
              <Field name="first name" component={InputField} className={RegisterFormCSS.inlineInput} usage="First name" value={firstName} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setFirstName(e.target.value);
                setFieldValue("first name", e.target.value);
              }} />
              <ErrorMsg val={errors["first name"]} />
            </div>
            <div>
              <Field component={InputField} className={`alignRight ${RegisterFormCSS.inlineInput}`} usage="Last name" value={lastName} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setLastName(e.target.value);
                setFieldValue("last name", e.target.value);
              }} />
              <ErrorMsg val={errors["last name"]} customClass="alignRight" />
            </div>
            <div>
              <Field component={InputField} className={RegisterFormCSS.inlineInput} usage="Email" value={email} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setEmail(e.target.value);
                setFieldValue("email", e.target.value);
              }} />
              <ErrorMsg val={errors["email"]} />
            </div>
            <div>
              <Field component={InputField} className={RegisterFormCSS.inlineInput} usage="Password" value={password} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setPassword(e.target.value);
                setFieldValue("password", e.target.value);
              }} />
              <ErrorMsg val={errors["password"]} />
            </div>
            <div>
              <Field component={InputField} className={`alignRight ${RegisterFormCSS.inlineInput}`} usage="Confirm password" value={confirmPassword} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                setConfirmPassword(e.target.value);
                setFieldValue("confirm password", e.target.value);
              }} />
              <ErrorMsg val={errors["confirm password"]} customClass="alignRight" />
            </div>
            <input name="file" ref={fileRef} id="file-upload" type="file" />
                    <ErrorMessage name="file" />
          </div>


          <div className={RegisterFormCSS.button}>
            <span className="alignRight">
              <Button submit="submit" onClick={null} text="Get started" />
            </span>
          </div>
        </Form>
      )}
    </Formik >
  )
}
export default RegisterForm