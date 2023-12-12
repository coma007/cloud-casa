import React, { useRef } from 'react'
import ChangePasswordModalCSS from "./ChangePasswordModal.module.scss"
import * as yup from 'yup' 
import { Formik, Form, Field, ErrorMessage, FormikProps, FormikValues } from 'formik';
import { useState } from 'react'
import Modal from 'react-bootstrap/Modal';
import { AuthService } from '../../services/AuthService';
import InputField from '../../../../../components/forms/InputField/InputField';
import ErrorMsg from '../../../../../components/error/ErrorMsg';
import Button from '../../../../../components/forms/Button/Button';

const ChangePasswordModal = (props: { show: any, setShow: any }) => {
    const [oldPassword, setOldPassword] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const formRef = useRef<FormikProps<FormikValues>>(null);

    const passwordValidator = yup.string().min(8, "password is too short")
    .matches(/[a-z]+/, "password needs to contain lowercase letter")
    .matches(/[A-Z]+/, "password needs to contain uppercase letter")
    .matches(/[0-9]+/, "password needs to contain number letter")
    .required();

    const schema = yup.object().shape({
        "old password": passwordValidator,
        "new password": passwordValidator,
        "confirm password": passwordValidator.oneOf([yup.ref('new password')], 'Passwords must match'),
      })

      const handleButtonSubmit = async () => {
        if (formRef.current)
            formRef.current.handleSubmit();
      }
      const handleFormSubmit = async () => {
            try {
                await AuthService.changePassword({ OldPassword: oldPassword, NewPassword: newPassword });
                props.setShow(false);
              } catch (error: any) {
                alert(error.response.data);
              }

      }


    return (
        <div
        className="modal show"
        style={{ display: 'block', position: 'initial' }}
      >
        <Modal show={props.show}>
          <Modal.Header>
            <Modal.Title>Change password</Modal.Title>
          </Modal.Header>
  
          <Modal.Body>
            <Formik
                innerRef={formRef} 
                initialValues={{
                    "old password": "",
                    "new password": "",
                    "confirm password": "",
                }}
                validationSchema={schema}
                onSubmit = {handleFormSubmit}
            >
                {({ errors, touched, setFieldValue }) => (
                <Form>
                    <div>
                        <Field component={InputField} className={ChangePasswordModalCSS.inlineInput} usage="Old password" value={oldPassword} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                            setOldPassword(e.target.value);
                            setFieldValue("old password", e.target.value);
                        }} />
                        <ErrorMsg val={errors["old password"]} />
                    </div>
                    <div>
                        <Field component={InputField} className={ChangePasswordModalCSS.inlineInput} usage="Password" value={newPassword} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                            setNewPassword(e.target.value);
                            setFieldValue("new password", e.target.value);
                        }} />
                        <ErrorMsg val={errors["new password"]} />
                    </div>
                    <div>
                        <Field component={InputField} className={`alignRight ${ChangePasswordModalCSS.inlineInput}`} usage="Confirm password" value={confirmPassword} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                            setConfirmPassword(e.target.value);
                            setFieldValue("confirm password", e.target.value);
                        }} />
                        <ErrorMsg val={errors["confirm password"]} customClass="alignRight" />
                    </div>
                </Form>
                )}
            </Formik>
          </Modal.Body>
  
          <Modal.Footer>
            <span className="alignRight">
                <Button submit={"button"}    onClick={handleButtonSubmit} text="Change" />
            </span>
          </Modal.Footer>
        </Modal>
      </div>

    )
}

export default ChangePasswordModal