
import { useEffect, useState } from "react"
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

const DeviceOverviewPage = () => {

    const [withdrawIsOpen, setWithdrawModalIsOpen] = useState(false);
    const [selectedDevice, setSelectedDevice] = useState<RealEstate|undefined>(undefined);
    const [tableData, setTableData] = useState<TableRow[]>([]);

    useEffect(() => {
        (async function () {
            try {
                // const fetchedDevices = await DeviceService.getAllByOwner();
                const fetchedDevices = [{} as RealEstate]
                populateData(fetchedDevices);
            } catch (error) {
                console.error(error);
            }
        })()
    }, []);

    // const [socket, setSocket] = useState<WebSocket | null>(null);

    // useEffect(() => {
    //     WebSocketService.createSocket(setSocket);
    // }, []);

    // const processValue = (message: any) => {
    //     console.log(message);
    // }

    // useEffect(() => {
    //     WebSocketService.defineSocket(socket, "myTopic", processValue);
    // }, [socket]);
    

    const headers: TableRow = { 
        rowData: [
            { content: "Name", widthPercentage: 30},
            { content: "Power supply", widthPercentage: 30},
            { content: "Energy consumption", widthPercentage: 30},
            { content: "Type", widthPercentage: 30}
        ],
        onClick: undefined
}

    const showDetails = (realDevice : RealEstate | string) => {
        alert(realDevice)
    }

    const populateData = (devices: RealEstate[]) => {
        let data: TableRow[] = []
        if (devices !== undefined) {
            devices.forEach(device => {
                data.push({
                    rowData: [
                        { content: "Klima 1", widthPercentage: 30},
                        { content: "AUTONOMOUS", widthPercentage: 30},
                        { content: "5", widthPercentage: 30},
                        { content: "Air conditioning", widthPercentage: 30},
                    ],
                    onClick: () => {showDetails(device)}
                });
            });
        }
        setTableData(data);
    }
    let isLoaded = false;

    let socket = new SockJS("http://localhost:8080/socket");
    let stompClient = Stomp.over(socket);

    stompClient.connect({}, () =>{
      isLoaded = true;
      openSocket();
    });
    
    function handleMessage(message: {body: string}){
        let activeDriversLocations: [{latitude: number, longitude: number}] = JSON.parse(message.body);
        // refreshActiveDrivers(activeDriversLocations);
    }
    function openSocket() {
        if(isLoaded){
          stompClient!.subscribe('/active/vehicle/location', (message: {body: string}) =>{
          handleMessage(message);
          });
        }
      }
    


    return (
        <div>
            <Menu admin={false} />
            <div>
                <div className={DeviceOverviewPageCSS.header}>
                    <PageTitle title="Devices overview" description="Take a detailed view of your devices." />
                    <div className={DeviceOverviewPageCSS.alignRight}>
                        <Button text={"New device"} onClick={undefined} submit={undefined} />
                    </div>
                </div>
                <div className={DeviceOverviewPageCSS.table} >
                    <Table headers={headers} rows={tableData} />
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