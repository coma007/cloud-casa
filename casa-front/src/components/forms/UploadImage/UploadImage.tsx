import React from 'react'
import UploadImageCSS from './UploadImage.module.scss'
import Upload from '../../../assets/actions/upload.png'

const UploadImage = (props: { fileRef: string, className : string}) => {
    return (
        <span>
            <span className={`${props.className}`}>
                <label htmlFor="file-upload" className={UploadImageCSS.fileUpload}>
                    Upload image <img className={UploadImageCSS.image} src={Upload} />
                </label>
            </span>
            <input name="file" className={UploadImageCSS.input} ref={props.fileRef} id="file-upload" type="file" />
        </span>
        // <>
        //     <label htmlFor="file-upload" className={UploadImageCSS.fileUpload}>
        //         Upload image<img src={Upload} />
        //     </label>
        //     <input name="file" ref={props.fileRef} id="file-upload" type="file" />
        // </>
    )
}

export default UploadImage
