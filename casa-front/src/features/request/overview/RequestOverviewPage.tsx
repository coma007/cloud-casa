
import { useEffect, useState } from "react"
import Menu from "../../../components/navigation/Menu/Menu";
import PageTitle from "../../../components/view/PageTitle/PageTitle";
import RequestOverviewPageCSS from "./RequestOverviewPage.module.scss"
import Table, { TableRow } from "../../../components/tables/Table/Table";
import Button from "../../../components/forms/Button/Button";
import ImageButton from "../../../components/tables/ImageButton/ImageButton"
import { RequestService } from "../RequestService";
import { RealEstate } from "../../estate/RealEstate";
import Accept from "../../../assets/actions/accept.png"
import Decline from "../../../assets/actions/decline.png"
import ModalWindow from "../../../components/view/Modal/ModalWindow";


const RequestOverviewPage = () => {

    const [withdrawIsOpen, setWithdrawModalIsOpen] = useState(false);
    const [selectedRequest, setSelectedRequest] = useState<RealEstate | undefined>(undefined);
    const [tableData, setTableData] = useState<TableRow[]>([]);
    const [declineReason, setDeclineReason] = useState<string>("");

    useEffect(() => {
        fetchData();
    }, []);


    const populateData = (requests: RealEstate[]) => {
        let data: TableRow[] = []
        if (requests !== undefined) {
            requests.forEach(request => {
                data.push({
                    rowData: [
                        { content: request.name, widthPercentage: 20 },
                        { content: request.type.toUpperCase(), widthPercentage: 12 },
                        { content: request.size, widthPercentage: 5 },
                        { content: request.numberOfFloors, widthPercentage: 8 },
                        { content: request.city?.country, widthPercentage: 15 },
                        { content: "10.10.2023.", widthPercentage: 15 },
                        { content: request.owner?.email, widthPercentage: 15 },
                        { content: <ImageButton path={Accept} tooltipText="Accept" onClick={() => openAcceptModal(request)} />, widthPercentage: 5 },
                        { content: <ImageButton path={Decline} tooltipText="Decline" onClick={() => openDeclineModal(request, "")} />, widthPercentage: 5 },
                    ],
                    onClick: () => { showDetails(request) }
                });
            });
        }
        setTableData(data);
    }

    const fetchData = async () => {
        try {
            const fetchedRequests = await RequestService.getAllUnresolved();
            populateData(fetchedRequests);
        } catch (error) {
            console.error(error);
        }
    };

    const [declineIsOpen, setDeclineModalIsOpen] = useState(false);

    const openDeclineModal = (request: RealEstate, reason: string) => {
        setDeclineReason(declineReason)
        setSelectedRequest(request)
        setDeclineModalIsOpen(true);
    };

    const closeDeclineModal = () => {
        setDeclineModalIsOpen(false);
    };

    const okDeclineModal = async () => {
        console.log(selectedRequest)
        if (selectedRequest) {
            selectedRequest.request.declined = true
            selectedRequest.request.comment = declineReason
            await RequestService.manage(selectedRequest.request)
        }
        await fetchData()
        setDeclineModalIsOpen(false);
    };

    const handleReasonChange = (event: any) => {
        setDeclineReason(event.target.value);
    };

    const [acceptIsOpen, setAcceptModalIsOpen] = useState(false);

    const openAcceptModal = (request: RealEstate) => {
        setSelectedRequest(request)
        setAcceptModalIsOpen(true);
    };

    const closeAcceptModal = () => {
        setAcceptModalIsOpen(false);
    };

    const okAcceptModal = async () => {
        console.log(selectedRequest)
        if (selectedRequest) {
            selectedRequest.request.approved = true
            await RequestService.manage(selectedRequest.request)
        }
        await fetchData()
        setAcceptModalIsOpen(false);
    };

    const headers: TableRow = {
        rowData: [
            { content: "Name", widthPercentage: 20 },
            { content: "Type", widthPercentage: 12 },
            { content: "Size", widthPercentage: 5 },
            { content: "Floors", widthPercentage: 8 },
            { content: "Country", widthPercentage: 15 },
            { content: "Request date", widthPercentage: 15 },
            { content: "Requested by", widthPercentage: 15 },
            { content: "", widthPercentage: 5 },
            { content: "", widthPercentage: 5 }
        ],
        onClick: undefined
    }

    const showDetails = (realEstate: RealEstate | string) => {
        alert(realEstate)
    }


    return (
        <div>
            <Menu admin={true} />
            <div>
                <div className={RequestOverviewPageCSS.header}>
                    <PageTitle title="Requests overview" description="Take a detailed view of estates requests." />
                </div>
                <div className={RequestOverviewPageCSS.table} >
                    <Table headers={headers} rows={tableData} />
                </div>
                <ModalWindow
                    height="53%"
                    isOpen={declineIsOpen}
                    closeWithdrawalModal={closeDeclineModal}
                    okWithdrawalModal={okDeclineModal}
                    title="Decline request"
                    buttonText="Decline" >
                    <p>Estate: <strong>{selectedRequest?.name}</strong><br />To decline the request, you need to provide us some more info on why you want to decline it.</p>
                    <textarea placeholder='Write your reason ...' value={declineReason} onChange={handleReasonChange}></textarea>
                </ModalWindow>
                <ModalWindow
                    height="33%"
                    isOpen={acceptIsOpen}
                    closeWithdrawalModal={closeAcceptModal}
                    okWithdrawalModal={okAcceptModal}
                    title="Accept request"
                    buttonText="Accept" >
                    <p>Estate: <strong>{selectedRequest?.name}</strong><br />Are you sure you want to accept the request ?</p>
                </ModalWindow>
            </div>
        </div>
    )
}

export default RequestOverviewPage