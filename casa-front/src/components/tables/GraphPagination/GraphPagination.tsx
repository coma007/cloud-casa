import { useEffect, useState } from "react";
import Button from "../../forms/Button/Button";
import PaginationCSS from "./GraphPagination.module.scss"

const Pagination = (props : {currentPage: number, numberOfPages: number, onClick : (pageNumber : number) => void, children}) => {
    console.log(props)
    
    return (
        <div className={PaginationCSS.pagination}>
            <div className={PaginationCSS.paginationButton}>
                <Button disabled={props.currentPage == 1} onClick={() => props.onClick(props.currentPage - 1)} text={"<"} submit={undefined} />
            </div>
            <div className={PaginationCSS.graph}>
                {props.children}
            </div>
            <div className={PaginationCSS.paginationButton}>
                <Button disabled={props.currentPage == props.numberOfPages} onClick={() => props.onClick(props.currentPage + 1)} text={">"} submit={undefined} />
            </div>
        </div>
    )
}

export default Pagination;