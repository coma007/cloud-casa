
import { useEffect, useState } from "react"
import Menu from "../../../components/navigation/Menu/Menu";
import PageTitle from "../../../components/view/PageTitle/PageTitle";
import EstateOverviewPageCSS from "./EstateOverviewPage.module.scss"
import Table, { TableRow } from "../../../components/tables/Table/Table";
import { RealEstate } from "../RealEstate";
import { EstateService } from "../EstateService";
import Button from "../../../components/forms/Button/Button";
import { useNavigate } from "react-router-dom";
import { UserService } from "../../user/UserService";
import PermissionModal from "../permission/PermissionModal";
import { AuthService } from "../../user/auth/services/AuthService";
import { Permission } from "../../device/Device";
import Pagination from "../../../components/tables/Pagination/Pagination";

const EstateOverviewPage = () => {

    const [withdrawIsOpen, setWithdrawModalIsOpen] = useState(false);
    const [selectedEstate, setSelectedEstate] = useState<RealEstate|undefined>(undefined);
    const [tableData, setTableData] = useState<TableRow[]>([]);
    const [allEstates, setAllEstates] = useState<RealEstate[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [numOfPages, setNumOfPages] = useState(1);

    const pageSize = 10;

    const navigate = useNavigate()

    useEffect(() => {
        (async function () {
            try {
                const fetchedEstates = await EstateService.getAll();
                populateData(fetchedEstates);
            } catch (error) {
                console.error(error);
            }
        })()
    }, []);

    let data = [
        { content: "Name", widthPercentage: 20},
        { content: "Type", widthPercentage: 12},
        { content: "Size", widthPercentage: 5},
        { content: "Floors", widthPercentage: 8},
        { content: "Address", widthPercentage: 15},
        { content: "City", widthPercentage: 10},
        { content: "Country", widthPercentage: 10},
        { content: "Status", widthPercentage: 10},
        { content: "Permission", widthPercentage: 10}
      
    ]


    const headers: TableRow = { 
        rowData: data,
        onClick: undefined
    }

    const handleGivePermissionModal = (estate) =>{
        let permission: Permission = {
            Kind: "real estate",
            ResourceId: estate!.id,
            Type: "MODERATOR",
            UserId: null,
        }
        setCurrentPermission(permission);
        console.log("OPEN MODAL")
        setShow(true);

    }

    const populateData = (estates: RealEstate[]) => {
        let newNumOfPages = Math.floor(estates.length / pageSize);
        if (newNumOfPages < estates.length / pageSize) {
            newNumOfPages += 1;
        }
        setNumOfPages(newNumOfPages)
        setAllEstates(estates)
    }

    useEffect(()=> {
        let data: TableRow[] = []
        // if (estates !== undefined) {
        //     estates.forEach(estate => {
        //         let rowData = [
        //             { content: estate.name, widthPercentage: 20},
        //             { content: estate.type.toUpperCase(), widthPercentage: 12},
        //             { content: estate.size, widthPercentage: 5},
        //             { content: estate.numberOfFloors, widthPercentage: 8},
        //             { content: estate.address?.address, widthPercentage: 15},
        //             { content: estate.city?.name, widthPercentage: 10},
        //             { content: estate.city?.country, widthPercentage: 10},
        //             { content: <i> {estate.request.approved === false && estate.request.declined === false ? <>{"in progress".toUpperCase()}</> : <>{estate.request.approved ? "approved".toUpperCase() : "declined".toUpperCase()}</>}</i>, widthPercentage: 10},
        //             { content: "Permission", widthPercentage: 10, onClick: () => {handleGivePermissionModal(estate)}}
        //         ]
        //         if(AuthService.getUsername() !== estate!.owner!.email)
        //             rowData.pop();
        //         data.push({
        //             rowData: rowData,
        //             onClick: () => {showDetails(estate)}
        if (allEstates.length > 0) {
            for (let i = pageSize * (currentPage - 1); i < Math.min(allEstates.length, pageSize * currentPage); i++) {
                let estate = allEstates.at(i)
                let rowData = [
                    { content: estate!.name, widthPercentage: 20},
                    { content: estate!.type.toUpperCase(), widthPercentage: 12},
                    { content: estate!.size, widthPercentage: 5},
                    { content: estate!.numberOfFloors, widthPercentage: 8},
                    { content: estate!.address?.address, widthPercentage: 15},
                    { content: estate!.city?.name, widthPercentage: 10},
                    { content: estate!.city?.country, widthPercentage: 10},
                    { content: <i> {estate!.request.approved === false && estate!.request.declined === false ? <>{"in progress".toUpperCase()}</> : <>{estate!.request.approved ? "approved".toUpperCase() : "declined".toUpperCase()}</>}</i>, widthPercentage: 10},
                    { content: "Permission", widthPercentage: 10, onClick: (event) => {event.stopPropagation(); handleGivePermissionModal(estate)}}
                ]
                // data.push({
                //     rowData: [
                //         { content: estate!.name, widthPercentage: 20},
                //         { content: estate!.type.toUpperCase(), widthPercentage: 12},
                //         { content: estate!.size, widthPercentage: 5},
                //         { content: estate!.numberOfFloors, widthPercentage: 8},
                //         { content: estate!.address?.address, widthPercentage: 15},
                //         { content: estate!.city?.name, widthPercentage: 15},
                //         { content: estate!.city?.country, widthPercentage: 15},
                //         { content: <i> {estate!.request.approved === false && estate!.request.declined === false ? <>{"in progress".toUpperCase()}</> : <>{estate!.request.approved ? "approved".toUpperCase() : "declined".toUpperCase()}</>}</i>, widthPercentage: 10},
                //         { content: "Permission", widthPercentage: 10, onClick: () => {handleGivePermissionModal(estate)}}
                //     ],
                //     onClick: () => {showDetails(estate!)}
                // });
                if(AuthService.getUsername() !== estate!.owner!.email)
                    rowData.pop();
                data.push({
                    rowData: rowData,
                    onClick: () => {showDetails(estate!)}
                })
            }
            // estates.forEach(estate => {
            //     data.push({
            //         rowData: [
            //             { content: estate.name, widthPercentage: 20},
            //             { content: estate.type.toUpperCase(), widthPercentage: 12},
            //             { content: estate.size, widthPercentage: 5},
            //             { content: estate.numberOfFloors, widthPercentage: 8},
            //             { content: estate.address?.address, widthPercentage: 15},
            //             { content: estate.city?.name, widthPercentage: 15},
            //             { content: estate.city?.country, widthPercentage: 15},
            //             { content: <i> {estate.request.approved === false && estate.request.declined === false ? <>{"in progress".toUpperCase()}</> : <>{estate.request.approved ? "approved".toUpperCase() : "declined".toUpperCase()}</>}</i>, widthPercentage: 10},
            //         ],
            //         onClick: () => {showDetails(estate)}
            //     });
            // });
        }
        setTableData(data);
    }, [allEstates, currentPage])

    const createNew = () => {
        navigate("/register-real-estate");
    }

    const showDetails = (realEstate : RealEstate) => {
        console.log(realEstate)
        navigate("/device-overview", {state : {id: realEstate.id}})
    }

    let [show, setShow] = useState(false);
    let [currentPermission, setCurrentPermission] = useState<Permission>();

 

    const changePage = (pageNumber: number) => {
        setCurrentPage(pageNumber)
    }

    return (
        <div>
            <Menu admin={false} />
            <div>
                <PermissionModal show={show} setShow={setShow} permission={currentPermission} />
                <div className={EstateOverviewPageCSS.header}>
                    <PageTitle title="Estates overview" description="Take a detailed view of your estates." />
                    <div className={EstateOverviewPageCSS.alignRight}>
                        <Button text={"New estate"} onClick={createNew} submit={undefined} />
                    </div>
                </div>
                <div className={EstateOverviewPageCSS.table} >
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

export default EstateOverviewPage