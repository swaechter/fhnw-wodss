import { doGet } from "../services/api.service";

/*
 * action types
 */

export const FETCH_PROJECTS_BEGINN = 'FETCH_PROJECTS_BEGINN';
export const FETCH_PROJECTS_SUCCESS = 'FETCH_PROJECTS_SUCCESS';
export const FETCH_PROJECTS_FAIL = 'FETCH_PROJECTS_FAIL';

/*
 * other constants
 */



/*
 * action creators
 */
export const fetchProjectsBeginn = () => ({
	type: FETCH_PROJECTS_BEGINN
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
	return (dispatch, getState) => {
		dispatch(fetchProjectsBeginn());
		doGet('/api/projects', dispatch, getState)
			.then((json) => dispatch(fetchProjectsSuccess(json)))
			.catch((err) => dispatch(fetchProjectsFail(err)))
	}
}





