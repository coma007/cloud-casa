import React from 'react'
import EmailFormCSS from "./EmailForm.module.scss"
import * as yup from 'yup' 
import { Formik, Form, Field, ErrorMessage } from 'formik';
import { useState } from 'react'
import Button from '../../../../../components/forms/Button/Button';
import ErrorMsg from '../../../../../components/error/ErrorMsg';
import InputField from '../../../../../components/forms/InputField/InputField';

const EmailForm = (props: { onClick: any }) => {
    const [email, setEmail] = useState('');

    const schema = yup.object().shape({
        email: yup.string().email().required(),
    })


    return (
        <Formik
            initialValues={{
            email: '',
            }}
            validationSchema={schema}
            onSubmit = {props.onClick}
        >
            {({ errors, touched, setFieldValue }) => (
            <Form>
                <Field name="email" component={InputField} className={EmailFormCSS.input} usage="Email" value={email} onChange={(e:React.ChangeEvent<HTMLInputElement>) => {
                    setEmail(e.target.value);
                    setFieldValue("email", e.target.value);
                  }}/>
                <ErrorMsg val={errors["email"]} customClass={EmailFormCSS.margin}/>
                <span className="alignRight">
                    <Button submit={"submit"} onClick={null} text="Send code" />
                </span>
            </Form>
            )}
        </Formik>
    )
}

export default EmailForm