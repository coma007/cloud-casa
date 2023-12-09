import { useState } from 'react';
import MenuItem from '../MenuItem/MenuItem'
import MenuCSS from './Menu.module.scss'

import Logo from "../../../assets/logo.png"

interface IMenuProps {admin : boolean}

const Menu = ({admin} : IMenuProps) => {
    const [createIsOpen, setCreateIsOpen] = useState(false);
    const openCreateModal = () => {
        setCreateIsOpen(true);
    };

    const closeCreateModal = () => {
        setCreateIsOpen(false);
    };
    return (
        <div className={MenuCSS.menu}>
            <MenuItem className={MenuCSS.logo} tooltipText="" tooltip={false} image={Logo} path="/" text={''} />
            <div className={MenuCSS.menuGrid}>
                { admin ? 
                    (<MenuItem className={MenuCSS.nonMainOption} tooltipText="Requests overview" tooltip={true} image={undefined} path="/profile" text={'REQUESTS'} />)
                    :
                    (<>
                        <MenuItem className={MenuCSS.nonMainOption} tooltipText="Estates overview" tooltip={true} image={undefined} path="/profile" text={'ESTATES'} />
                        <MenuItem className={MenuCSS.nonMainOption} tooltipText="Devices overview" tooltip={true} image={undefined} path="/logout" text={'DEVICES'} />
                    </>)
                }
            </div>
            <div className={MenuCSS.rightMenu}>
                <div className={`${MenuCSS.menuRight} ${MenuCSS.menuGrid}`}>
                <MenuItem className={MenuCSS.nonMainOption} tooltipText="Profile details" tooltip={true} image={undefined} path="/profile" text={'PROFILE'} />
                <MenuItem className={MenuCSS.nonMainOption} tooltipText="Sign out" tooltip={true} image={undefined} path="/logout" text={'LOGOUT'} />
                </div>
            </div>
            {/* <MenuItem className={MenuCSS.margin} tooltipText="Certificates overview" tooltip={true} image={Certificate} path="/certificates" />
            <MenuItem className={MenuCSS.margin} tooltipText="Verify certificate" tooltip={true} image={Verify} path="/verify" />
            <MenuItem className={MenuCSS.margin} tooltipText="New certificate" tooltip={true} image={Create} path="" onClick={openCreateModal} />
            <MenuItem className={MenuCSS.margin} tooltipText="Requests" tooltip={true} image={Request} path="/requests" />
            
            <RequestCreatePage okCreateModal={null} createIsOpen={createIsOpen} closeCreateModal={closeCreateModal} /> */}
        </div>
    )
}

export default Menu