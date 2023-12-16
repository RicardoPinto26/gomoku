import {Problem} from "../../http/media/Problem";
import {NavigateFunction} from "react-router-dom";
import React from "react";

export function handleError(
    err: Error | Problem,
    setError: React.Dispatch<React.SetStateAction<string | null>>,
    navigate?: NavigateFunction
) {
    if (navigate != undefined && err instanceof Problem && err.status === 401) {
        navigate("/login")
    } else if (err instanceof Problem) {
        console.log('Problem title is: ' + err.title)
        setError(err.title)
    } else {
        console.log(err)
        setError(err.message)
    }
}