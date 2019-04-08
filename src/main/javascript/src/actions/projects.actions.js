import { doGet } from "../services/api.service";
import { getCurrentToken } from "../services/auth.service";

/*
 * action types
 */

export const FETCH_PROJECTS_BEGIN = 'FETCH_PROJECTS_BEGIN';
export const FETCH_PROJECTS_SUCCESS = 'FETCH_PROJECTS_SUCCESS';
export const FETCH_PROJECTS_FAIL = 'FETCH_PROJECTS_FAIL';

/*
 * other constants
 */



/*
 * action creators
 */
export const fetchProjectsBegin = () => ({
	type: FETCH_PROJECTS_BEGIN
})


export const fetchProjectsSuccess = (payload) => ({
	type: FETCH_PROJECTS_SUCCESS,
	payload
})

export const fetchProjectsFail = (error) => ({
	type: FETCH_PROJECTS_FAIL,
	error
})


/**
 * async function calls
 */
export function fetchProjectsAsync() {
	return async (dispatch) => {
		dispatch(fetchProjectsBegin());
		try {
			let token = await getCurrentToken(dispatch);
			let json = await doGet('/api/project', token)
			dispatch(fetchProjectsSuccess(json))
		} catch (error) {
			dispatch(fetchProjectsFail(error))
		}
	}
}





