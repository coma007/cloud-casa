
import { useEffect, useState } from "react"
import Menu from "../../../components/navigation/Menu/Menu";
import PageTitle from "../../../components/view/PageTitle/PageTitle";
import EstateOverviewPageCSS from "./EstateOverviewPage.module.scss"
import Table, { TableRow } from "../../../components/tables/Table/Table";
import { RealEstate } from "../RealEstate";
import { EstateService } from "../EstateService";
import Button from "../../../components/forms/Button/Button";

const EstateOverviewPage = () => {

    const [withdrawIsOpen, setWithdrawModalIsOpen] = useState(false);
    const [selectedEstate, setSelectedEstate] = useState<RealEstate|undefined>(undefined);
    const [tableData, setTableData] = useState<TableRow[]>([]);

    useEffect(() => {
        (async function () {
            try {
                const fetchedEstates = await EstateService.getAllByOwner();
                populateData(fetchedEstates);
            } catch (error) {
                console.error(error);
            }
        })()
    }, []);

    const headers: TableRow = { 
        rowData: [
            { content: "Name", widthPercentage: 20},
            { content: "Type", widthPercentage: 12},
            { content: "Size", widthPercentage: 5},
            { content: "Number of floors", widthPercentage: 13},
            { content: "Address", widthPercentage: 15},
            { content: "City", widthPercentage: 15},
            { content: "Country", widthPercentage: 15},
            { content: "Status", widthPercentage: 5}
        ],
        onClick: undefined
}

    const showDetails = (realEstate : RealEstate | string) => {
        alert(realEstate)
    }

    const populateData = (estates: RealEstate[]) => {
        let data: TableRow[] = []
        if (estates !== undefined) {
            estates.forEach(estate => {
                data.push({
                    rowData: [
                        { content: estate.name, widthPercentage: 20},
                        { content: estate.type.toUpperCase(), widthPercentage: 12},
                        { content: estate.size, widthPercentage: 5},
                        { content: estate.numberOfFloors, widthPercentage: 13},
                        { content: estate.address?.address, widthPercentage: 15},
                        { content: estate.city?.name, widthPercentage: 15},
                        { content: estate.city?.country, widthPercentage: 15},
                        { content: <i> {estate.request.approved === false && estate.request.declined === false ? <>{"in progress".toUpperCase()}</> : <>{estate.request.approved ? "approved".toUpperCase() : "declined".toUpperCase()}</>}</i>, widthPercentage: 10},
                    ],
                    onClick: () => {showDetails(estate)}
                });
            });
        }
        setTableData(data);
    }


    return (
        <div>
            <Menu admin={false} />
            <div>
                <div className={EstateOverviewPageCSS.header}>
                    <PageTitle title="Estates overview" description="Take a detailed view of your estates." />
                    <div className={EstateOverviewPageCSS.alignRight}>
                        <Button text={"New estate"} onClick={undefined} submit={undefined} />
                    </div>
                </div>
                <div className={EstateOverviewPageCSS.table} >
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

export default EstateOverviewPage