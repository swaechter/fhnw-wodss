import {doDelete, doGet, doPost, getUrl} from "../services/api.service";
import {getCurrentToken} from "../services/auth.service";
import {dateToString} from "../utils/date";
import {clearError, setError} from "./error.actions";

/*
 * action types
 */

export const CREATE_ALLOCATION_SUCCESS = 'CREATE_ALLOCATION_SUCCESS';
export const FETCH_ALLOCATIONS_BEGIN = 'FETCH_ALLOCATIONS_BEGIN';
export const FETCH_ALLOCATIONS_SUCCESS = 'FETCH_ALLOCATIONS_SUCCESS';
export const FETCH_ALLOCATIONS_FAIL = 'FETCH_ALLOCATIONS_FAIL';
export const CLEAR_ALLOCATIONS = 'CLEAR_ALLOCATION';
export const DELETE_ALLOCATION_SUCCESS = 'DELETE_ALLOCATION_SUCCESS';

/*
 * other constants
 */


/*
 * action creators
 */

const createAllocationSuccess = (payload) => ({
    type: CREATE_ALLOCATION_SUCCESS,
    payload
})

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

const deleteAllocationSuccess = (id) => ({
    type: DELETE_ALLOCATION_SUCCESS,
    id
})

/**
 * async function calls
 */

export function createAllocationAsync(allocation) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/allocation");
            let token = await getCurrentToken(dispatch);
            let json = await doPost(url, allocation, token);
            dispatch(clearError());
            dispatch(createAllocationSuccess(json));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

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

export function deleteAllocationAsync(id) {
    return async (dispatch) => {
        let url = getUrl("/api/allocation/" + id);
        try {
            let token = await getCurrentToken(dispatch);
            await doDelete(url, token);
            dispatch(clearError());
            dispatch(deleteAllocationSuccess(id));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}
