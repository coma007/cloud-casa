
import { MouseEventHandler, useEffect, useState } from "react"
import { useLocation, useNavigate } from 'react-router-dom';
import Menu from "../../../components/navigation/Menu/Menu";
import PageTitle from "../../../components/view/PageTitle/PageTitle";
import DeviceOverviewPageCSS from "./DeviceOverviewPage.module.scss"
import Table, { TableRow } from "../../../components/tables/Table/Table";
import Button from "../../../components/forms/Button/Button";
import { RealEstate } from "../../estate/RealEstate";
import { DeviceService } from "../DeviceService";
import { WebSocketService } from "../../../api/websocket/WebSocketService";
// import * as SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import SockJS from "sockjs-client";
import { DeviceDetails, Permission } from "../Device";
import { AuthService } from "../../user/auth/services/AuthService";
import PermissionModal from "../../estate/permission/PermissionModal";
import Pagination from "../../../components/tables/Pagination/Pagination";

const DeviceOverviewPage = () => {

    const [withdrawIsOpen, setWithdrawModalIsOpen] = useState(false);
    const [selectedDevice, setSelectedDevice] = useState<RealEstate|undefined>(undefined);
    const [tableData, setTableData] = useState<TableRow[]>([]);
    const [allDevices, setAllDevices] = useState<DeviceDetails[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [numOfPages, setNumOfPages] = useState(1);

    const pageSize = 10;

    let [show, setShow] = useState(false);
    let [currentPermission, setCurrentPermission] = useState<Permission>();

    let location = useLocation()

    useEffect(() => {
        console.log(location.state);
        if (location.state !== null && location.state.id !== undefined) {
            (async function () {
                try {
                    const fetchedDevices = await DeviceService.getAllByRealEstate(location.state.id);
                    // const fetchedDevices = [{} as RealEstate]
                    populateData(fetchedDevices);
                } catch (error) {
                    console.error(error);
                }
            })()
        } else {
            (async function () {
                try {
                    const fetchedDevices = await DeviceService.getAll();
                    // const fetchedDevices = [{} as RealEstate]
                    populateData(fetchedDevices);
                } catch (error) {
                    console.error(error);
                }
            })()
        }
    }, [location.state]);


    const navigate = useNavigate();

    // useEffect(() => {
    //     (async function () {
    //         try {
    //             const fetchedDevices = await DeviceService.getAllByOwner();
    //             // const fetchedDevices = [{} as RealEstate]
    //             populateData(fetchedDevices);
    //         } catch (error) {
    //             console.error(error);
    //         }
    //     })()
    // }, []);
    
    const headers: TableRow = { 
        rowData: [
            { content: "Name", widthPercentage: 30},
            { content: "Real estate name", widthPercentage: 30},
            { content: "Status", widthPercentage: 30},
            { content: "Energy consumption", widthPercentage: 30},
            { content: "Type", widthPercentage: 30},
            { content: "Permission", widthPercentage: 10}
        ],
        onClick: undefined
}

    const showDetails = (deviceId : number, deviceType : string) => {
        // alert(deviceId + " " + deviceType)
        navigate("/device-details", {state : {id: deviceId, type: deviceType}})
    }

    const handleGivePermissionModal = (device) =>{
        let permission: Permission = {
            Kind: "device",
            ResourceId: device!.id,
            Type: "MODERATOR",
            UserId: null,
        }
        setCurrentPermission(permission);
        console.log("OPEN MODAL")
        setShow(true);

    }

    const populateData = (devices: DeviceDetails[]) => {
        // console.log(Math.floor(devices.length / 5))
        let newNumOfPages = Math.floor(devices.length / pageSize);
        if (newNumOfPages < devices.length / pageSize) {
            newNumOfPages += 1;
        }
        setNumOfPages(newNumOfPages)
        setAllDevices(devices)
    }

    useEffect(()=> {
        let data: TableRow[] = []
        if (allDevices.length > 0) {
            for (let i = pageSize * (currentPage - 1); i < Math.min(allDevices.length, pageSize * currentPage); i++) {
                let device = allDevices.at(i)
                let deviceType = device!.type;
                while (deviceType.includes('_')) {
                    deviceType = deviceType.replace('_', ' ');
                }
                let rowData = [
                    { content: device!.name, widthPercentage: 30},
                    { content: device!.realEstateName, widthPercentage: 30},
                    { content: device!.powerSupplyType.toUpperCase(), widthPercentage: 30},
                    { content: device!.energyConsumption, widthPercentage: 30},
                    { content: (deviceType.charAt(0).toUpperCase() + deviceType.substr(1).toLowerCase()), widthPercentage: 30},
                    { content: "Permission", widthPercentage: 10, onClick: (event)  => {event?.stopPropagation(); handleGivePermissionModal(device)}}
                ]
                // if(AuthService.getUsername() !== device!.owner!.email){
                //     rowData.pop();
                //     rowData.push({ content: "", widthPercentage: 10});
                // }
                 
                data.push({
                    rowData: rowData,
                    onClick: () => {showDetails(device!.id, device!.type)}
                });
            }
            // devices.forEach(device => {
            //     let deviceType = device.type;
            //     while (deviceType.includes('_')) {
            //         deviceType = deviceType.replace('_', ' ');
            //     }
            //     data.push({
            //         rowData: [
            //             { content: device.name, widthPercentage: 30},
            //             { content: device.realEstateName, widthPercentage: 30},
            //             { content: device.powerSupplyType.toUpperCase(), widthPercentage: 30},
            //             { content: device.energyConsumption, widthPercentage: 30},
            //             { content: (deviceType.charAt(0).toUpperCase() + deviceType.substr(1).toLowerCase()), widthPercentage: 30},
            //         ],
            //         onClick: () => {showDetails(device.id, device.type)}
            //     });
            // });
        }
        setTableData(data);
    }, [allDevices, currentPage])

    let isLoaded = false;
    
    const newDevice = () => {
        navigate("/register-device")
    }

    const changePage = (pageNumber: number) => {
        setCurrentPage(pageNumber)
    }


    return (
        <div>
            <Menu admin={false} />
            <div>
            <PermissionModal show={show} setShow={setShow} permission={currentPermission} />
                <div className={DeviceOverviewPageCSS.header}>
                    <PageTitle title="Devices overview" description="Take a detailed view of your devices." />
                    <div className={DeviceOverviewPageCSS.alignRight}>
                        <Button text={"New device"} onClick={newDevice} submit={undefined} />
                    </div>
                </div>
                <div className={DeviceOverviewPageCSS.table} >
                    <Table headers={headers} rows={tableData} />
                    <div>
                        <Pagination currentPage={currentPage} numberOfPages={numOfPages} onClick={changePage} />
                    </div>
                </div>
                
                {/* <ModalWindow
                    height="75%"
                    isOpen={withdrawIsOpen}
                    closeWithdrawalModal={closeWithdrawModal}
                    okWithdrawalModal={okWithdrawModal}
                    title="Withdraw certificate"
                    buttonText="WITHDRAW" >
                    <p>To withdraw the certificate, you need to provide us some more info on why you want to withdraw it. </p>
                    <textarea placeholder='Write your reason ...'></textarea>
                    <p className={ModalWindowCSS.warning}>Please note that if the certificate is revoked, all in the chain below it is automatically retracted. This means that all certificates signed by this certificate, as well as certificates signed by those certificates will be automatically revoked.</p>
                </ModalWindow> */}
            </div>
        </div>
    )
}

export default DeviceOverviewPage