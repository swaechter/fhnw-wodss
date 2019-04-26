import {doGet, getUrl} from "../services/api.service";
import {getCurrentToken} from "../services/auth.service";
import {dateToString} from "../utils/date";

/*
 * action types
 */

export const FETCH_ALLOCATIONS_BEGIN = 'FETCH_ALLOCATIONS_BEGIN';
export const FETCH_ALLOCATIONS_SUCCESS = 'FETCH_ALLOCATIONS_SUCCESS';
export const FETCH_ALLOCATIONS_FAIL = 'FETCH_ALLOCATIONS_FAIL';
export const CLEAR_ALLOCATIONS = 'CLEAR_ALLOCATION';

/*
 * other constants
 */


/*
 * action creators
 */
const fetchAllocationsBegin = () => ({
    type: FETCH_ALLOCATIONS_BEGIN
})

export const clearAllocations = () => ({
    type: CLEAR_ALLOCATIONS
})


const fetchAllocationsSuccess = (payload) => ({
    type: FETCH_ALLOCATIONS_SUCCESS,
    payload
})

const fetchAllocationsFail = (error) => ({
    type: FETCH_ALLOCATIONS_FAIL,
    error
})


/**
 * async function calls
 */
export function fetchAllocationsAsync(employeeId = null, projectId = null, fromDate = null, toDate = null) {
    return async (dispatch) => {
        dispatch(fetchAllocationsBegin());
        let url = getUrl("/api/allocation");
        if (employeeId) url.searchParams.append('employeeId', employeeId)
        if (projectId) url.searchParams.append('projectId', projectId)
        if (fromDate) url.searchParams.append('fromDate', dateToString(fromDate))
        if (toDate) url.searchParams.append('toDate', dateToString(toDate))
        try {
            let token = await getCurrentToken(dispatch);
            let json = await doGet(url, token)
            dispatch(fetchAllocationsSuccess(json))
        } catch (error) {
            dispatch(fetchAllocationsFail(error))
        }
    }
}





