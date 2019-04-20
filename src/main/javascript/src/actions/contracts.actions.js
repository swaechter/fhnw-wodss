import { doGet, getUrl } from "../services/api.service";
import { getCurrentToken } from "../services/auth.service";

/*
 * action types
 */

export const FETCH_CONTRACTS_BEGIN = 'FETCH_CONTRACTS_BEGIN';
export const FETCH_CONTRACTS_SUCCESS = 'FETCH_CONTRACTS_SUCCESS';
export const FETCH_CONTRACTS_FAIL = 'FETCH_CONTRACTS_FAIL';

/*
 * other constants
 */



/*
 * action creators
 */
const fetchContractsBegin = () => ({
	type: FETCH_CONTRACTS_BEGIN
})


const fetchContractsSuccess = (payload) => ({
	type: FETCH_CONTRACTS_SUCCESS,
	payload
})

const fetchContractsFail = (error) => ({
	type: FETCH_CONTRACTS_FAIL,
	error
})


/**
 * async function calls
 */
export function fetchContractsAsync() {
	return async (dispatch) => {
		dispatch(fetchContractsBegin());
		try {
			let token = await getCurrentToken(dispatch);
			let json = await doGet("/api/contract", token)
			dispatch(fetchContractsSuccess(json))
		} catch (error) {
			dispatch(fetchContractsFail(error))
		}
	}
}





