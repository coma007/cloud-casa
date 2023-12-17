import { useEffect, useState } from "react";
import Button from "../../forms/Button/Button";
import PaginationCSS from "./Pagination.module.scss"

const Pagination = (props : {currentPage: number, numberOfPages: number, onClick : (pageNumber : number) => void}) => {
    const[startPages, setStartPages] = useState<number[]>([])
    const[currentPages, setCurrentPages] = useState<number[]>([])
    const[endPages, setEndPages] = useState<number[]>([])
    const[showFirstDots, setShowFirstDots] = useState(false)
    const[showSecondDots, setShowSecondDots] = useState(false)
    useEffect(() => {
        if (props.currentPage < 7) {
            setShowFirstDots(false)
        } else {
            setShowFirstDots(true)
        }
        if (props.numberOfPages - props.currentPage < 7) {
            setShowSecondDots(false)
        } else {
            setShowSecondDots(true)
        }

        const newStartPages : number[] = [];
        // console.log(props.numberOfPages)
        for (let i = 0; i < Math.min(3, props.numberOfPages); i++) {
            newStartPages.push(i);
        }
        setStartPages(newStartPages);

        const newCurrentPages : number[] = []
        for (let i = Math.max(props.currentPage - 3, 3); i < Math.min(props.currentPage + 3, props.numberOfPages); i++) {
            newCurrentPages.push(i)
        }
        setCurrentPages(newCurrentPages)

        const newEndPages : number[] = []
        for (let i = Math.max(props.numberOfPages - 3, props.currentPage + 3); i < props.numberOfPages; i++) {
            newEndPages.push(i)
        }
        setEndPages(newEndPages)
    }, [props])

    return (
        <div className={PaginationCSS.pagination}>
            {startPages.map(index => (
                <span className={PaginationCSS.paginationButton}>
                    <Button onClick={() => props.onClick(index+1)} text={(index+1) + ""} submit={undefined} />
                </span>
            ))}
            {
                (showFirstDots) && <p>...</p>
            }
            {currentPages.map(index => (
                <span className={PaginationCSS.paginationButton}>
                    <Button onClick={() => props.onClick(index+1)} text={(index+1) + ""} submit={undefined} />
                </span>
            ))}
            {
                (showSecondDots) && <p>...</p>
            }
            {endPages.map(index => (
                <span className={PaginationCSS.paginationButton}>
                    <Button onClick={() => props.onClick(index+1)} text={(index+1) + ""} submit={undefined} />
                </span>
            ))}
        </div>
    )
}

export default Pagination;