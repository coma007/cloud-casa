import { useState } from 'react';
import MenuItem from '../MenuItem/MenuItem'
import MenuCSS from './Menu.module.scss'

import Logo from "../../../assets/logo.png"

interface IMenuProps {admin : boolean, superadmin?: boolean, superadminInit?: boolean}


const Menu = ({admin, superadmin, superadminInit} : IMenuProps) => {
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
                        <MenuItem className={MenuCSS.nonMainOption} tooltipText="Estates overview" tooltip={true} image={undefined} path="/real-estate-overview" text={'ESTATES'} />
                        <MenuItem className={MenuCSS.nonMainOption} tooltipText="Devices overview" tooltip={true} image={undefined} path="/device-overview" text={'DEVICES'} />
                    </>)
                    
                }
                    { superadmin && 
                    (<MenuItem className={MenuCSS.nonMainOption} tooltipText="Register new admin" tooltip={true} image={undefined} path="/register/admin" text={'NEW ADMIN'} />)
                }
                 { superadminInit && 
                    (<MenuItem className={MenuCSS.nonMainOption} tooltipText="Register new admin" tooltip={true} image={undefined} path="/init/register/admin" text={'NEW ADMIN'} />)
                 }
            </div>
            <div className={MenuCSS.rightMenu}>
                <div className={`${MenuCSS.menuRight} ${MenuCSS.menuGrid}`}>
                <MenuItem className={MenuCSS.nonMainOption} tooltipText="Profile details" tooltip={true} image={undefined} path="/profile" text={'PROFILE'} />
                <MenuItem className={MenuCSS.nonMainOption} tooltipText="Sign out" tooltip={true} image={undefined} path="/logout" text={'LOGOUT'} />
                </div>
            </div>
        </div>
    )
}

export default Menu