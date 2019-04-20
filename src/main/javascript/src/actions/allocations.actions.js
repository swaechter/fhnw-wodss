import { doGet, getUrl } from "../services/api.service";
import { getCurrentToken } from "../services/auth.service";

/*
 * action types
 */

export const FETCH_ALLOCATIONS_BEGIN = 'FETCH_ALLOCATIONS_BEGIN';
export const FETCH_ALLOCATIONS_SUCCESS = 'FETCH_ALLOCATIONS_SUCCESS';
export const FETCH_ALLOCATIONS_FAIL = 'FETCH_ALLOCATIONS_FAIL';

/*
 * other constants
 */



/*
 * action creators
 */
const fetchAllocationsBegin = () => ({
	type: FETCH_ALLOCATIONS_BEGIN
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
export function fetchAllocationsAsync(employeeId=null,projectId=null) {
	return async (dispatch) => {
		dispatch(fetchAllocationsBegin());
		let url = getUrl("/api/allocation");
		if (employeeId) url.searchParams.append('employeeId',employeeId)
		if (projectId) url.searchParams.append('projectId',projectId)
		try {
			let token = await getCurrentToken(dispatch);
			let json = await doGet(url, token)
			dispatch(fetchAllocationsSuccess(json))
		} catch (error) {
			dispatch(fetchAllocationsFail(error))
		}
	}
}





