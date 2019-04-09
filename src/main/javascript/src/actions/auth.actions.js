import { fetchToken, logout, getStoredTokenIfValid } from "../services/auth.service";

/*
 * action types
 */

export const USER_LOGIN_BEGIN = 'USER_LOGIN_BEGIN';
export const USER_LOGIN_SUCCESS = 'USER_LOGIN_SUCCESS';
export const USER_LOGIN_FAIL = 'USER_LOGIN_FAIL';

export const USER_LOGOUT = 'USER_LOGOUT';


/*
 * other constants
 */

export const loginState = {
	LOGGED_OUT: 'LOGGED_OUT',
	FETCHING_JWT: 'FETCHING_JWT',
	LOGGED_IN: 'LOGGED_IN'
};

/*
 * action creators
 */
const loginUserBegin = () => ({
	type: USER_LOGIN_BEGIN
})


export const loginUserSuccess = (token) => ({
	type: USER_LOGIN_SUCCESS,
	token
})

export const loginUserFail = (error) => ({
	type: USER_LOGIN_FAIL,
	error
})


function logoutUser() {
	return { type: USER_LOGOUT };
}

/**
 * async function calls
 */
export function loginUserAsync(credentials) {
	return (dispatch) => {
		dispatch(loginUserBegin());
		fetchToken(credentials)
			.then(token => dispatch(loginUserSuccess(token)))
			.catch(err => dispatch(loginUserFail(err)));
	}
}

export function restoreLoginAsync() {
	return (dispatch) => 
	getStoredTokenIfValid()
		.then(token => dispatch(loginUserSuccess(token)))
		.catch(err => {})
}

export function logoutUserAsync() {
	return (dispatch) => 
	logout()
		.then(() => dispatch(logoutUser()))
}


