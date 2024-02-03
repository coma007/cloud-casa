import { useEffect, useState } from "react";
import Menu from "../../../../components/navigation/Menu/Menu";
import PowerUsageCSS from "./PowerUsageReportPage.module.scss";
import Table, { TableRow } from "../../../../components/tables/Table/Table";
import FilterDate from "../../../device/details/inspect/filter/FilterDate";
import Button from "../../../../components/forms/Button/Button";
import { PowerUsageService } from "../service/PowerUsageService";
import Pagination from "../../../../components/tables/Pagination/Pagination";

const PowerUsageReportPage = () => {
    const [powerUsageForCity, setPowerUsageForCity] = useState(false);
    const [filterText, setFilterText] = useState("Show filters");
    const [isFilterVisible, setFilterVisible] = useState(false);
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [toDateMin, setToDateMin] = useState('');
    const [resetTable, setResetTable] = useState(false);

    const [showActivity, setShowActivity] = useState(false);
    const [allData, setAllData] = useState<any[]>([]);
    const [currentPage, setCurrentPage] = useState(1);
    const [numOfPages, setNumOfPages] = useState(1);

    const pageSize = 1;

    const width = [40, 40, 40]
    const [header, setHeader] =useState<TableRow>({
        rowData: [
            { content: "City Name", widthPercentage: width[0] },
            { content: "Power usage", widthPercentage: width[1] },
            { content: "Power production", widthPercentage: width[2] },
        ],
        onClick: undefined
    });

    const [data, setData] =useState<TableRow[]>([]);

    const fetchCityData = () => {
        (async () => {
            let newFromDate, newToDate;
            if (fromDate.length > 11 || fromDate == "") {
                newFromDate = fromDate
            } else {
                newFromDate = fromDate + "T00:00:00.000Z";
            }
            if (toDate.length > 11 || toDate == "") {
                newToDate = toDate
            } else {
                newToDate = toDate + "T00:00:00.000Z";
            }
            const fetchedData = await PowerUsageService.getCityData(newFromDate, newToDate);
            replaceData(fetchedData);
        })();
    };

    const replaceData = (fetchedData: any[]) => {
        let newNumOfPages = Math.floor(fetchedData.length / pageSize);
        if (newNumOfPages < fetchedData.length / pageSize) {
            newNumOfPages += 1;
        }
        setNumOfPages(newNumOfPages);
        
        setAllData(fetchedData);
    };

    useEffect(()=> {
        let newData = [] as TableRow[]
        if (allData.length > 0) {
            for (let i = pageSize * (currentPage - 1); i < Math.min(allData.length, pageSize * currentPage); i++) {
                let element = allData.at(i)
                console.log(element);
                newData.push({
                    rowData: [
                        { content: powerUsageForCity ? element.cityName.toLowerCase().replace(/(?:^|\s)\S/g, (char) => char.toUpperCase()) : element.name, widthPercentage: width[0] },
                        { content: element.powerUsage, widthPercentage: width[1] },
                        { content: element.powerProduction, widthPercentage: width[2] },
                    ],
                    onClick: undefined
                });
            };
        }
        setData(newData);
    }, [allData]);

    const fetchEstateData = () => {
        (async () => {
            let newFromDate, newToDate;
            if (fromDate.length > 11 || fromDate == "") {
                newFromDate = fromDate
            } else {
                newFromDate = fromDate + "T00:00:00.000Z";
            }
            if (toDate.length > 11 || toDate == "") {
                newToDate = toDate
            } else {
                newToDate = toDate + "T00:00:00.000Z";
            }
            const fetchedData = await PowerUsageService.getEstateData(newFromDate, newToDate);
            replaceData(fetchedData);
        })();
    };

    useEffect(() => {
        let width = [40,40,40]
        console.log(powerUsageForCity);
        if (powerUsageForCity) {
            setHeader({
                rowData: [
                    { content: "City Name", widthPercentage: width[0] },
                    { content: "Power usage", widthPercentage: width[1] },
                    { content: "Power production", widthPercentage: width[2] },
                ],
                onClick: undefined
            });
            fetchCityData();
        } else {
            setHeader({
                rowData: [
                    { content: "Estate Name", widthPercentage: width[0] },
                    { content: "Power usage", widthPercentage: width[1] },
                    { content: "Power production", widthPercentage: width[2] },
                ],
                onClick: undefined
            });
            fetchEstateData();
        }
    }, [powerUsageForCity, currentPage, resetTable])

    useEffect(() => {

    }, [])

    const handleFilterToggle = () => {
        if (isFilterVisible) {
            setFilterText("Show filters");
        }
        else {
            setFilterText("Hide filters")
        }
        setFilterVisible(!isFilterVisible);
    };

    const resetFilters = () => {

        if (fromDate == "" || toDate == "") {
            return;
        }
        setFromDate('');
        setToDate('');
        setResetTable(!resetTable);
    }

    const handleDateFilterClick = (from: string, to: string) => {
        let fromTime = new Date(from);
        let toTime = new Date(to);
        let diff = (toTime.getTime() - fromTime.getTime()) / 1000 / 60 / 60 / 24;
        if (diff > 30) {
            console.log('Too big diffrence');
            return;
        }
        setFromDate(from);
        setToDate(to);
        setCurrentPage(1);
        setResetTable(!resetTable);
    }

    const handleFromDateChange = (e) => {
        const selectedFromDate = e.target.value;
        setFromDate(selectedFromDate);

        const nextDay = new Date(selectedFromDate);
        nextDay.setDate(nextDay.getDate() + 1);
        setToDateMin(nextDay.toISOString().split('T')[0]);
        setToDate(nextDay.toISOString().split('T')[0]);
    };

    
    const changePage = (pageNumber: number) => {
        setCurrentPage(pageNumber)
    };

    return(
        <div>
            <Menu admin={true} />

            <div className={PowerUsageCSS.content}>
                <div className={PowerUsageCSS.smallButtonWrapper}>
                    <br></br>
                    <button onClick={() => setPowerUsageForCity(false)} className={PowerUsageCSS.smallButton}>real estates</button>
                    <button onClick={() => setPowerUsageForCity(true)} className={PowerUsageCSS.smallButton}>cities</button>
                </div>
                <div className={PowerUsageCSS.row}>
                    <Button text={filterText} onClick={handleFilterToggle} submit={undefined}></Button>
                    <Button text={"Reset filters"} onClick={resetFilters} submit={undefined} ></Button>
                </div>
                {isFilterVisible && 
                    (<>
                        <hr></hr>
                        <FilterDate showActivity={showActivity} fromDate={fromDate} toDate={toDate} handleSubmit={handleDateFilterClick} handleFromDateChange={handleFromDateChange} toDateMin={toDateMin} setToDate={setToDate}></FilterDate>
                        <hr></hr>
                    </>)
                }
                <div>
                    <Table headers={header} rows={data}></Table>
                    <div>
                        <Pagination currentPage={currentPage} numberOfPages={numOfPages} onClick={changePage} />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default PowerUsageReportPage;