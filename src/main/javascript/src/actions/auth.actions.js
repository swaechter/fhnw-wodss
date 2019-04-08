import { login, getStoredToken, logout } from "../services/auth.service";

/*
 * action types
 */

export const USER_LOGIN_BEGINN = 'USER_LOGIN_BEGINN';
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
	type: USER_LOGIN_BEGINN
})


export const loginUserSuccess = (payload) => ({
	type: USER_LOGIN_SUCCESS,
	payload
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
		login(credentials)
			.then(json => dispatch(loginUserSuccess(json)))
			.catch(err => dispatch(loginUserFail(err)));
	}
}

export function restoreLoginAsync() {
	return (dispatch) => getStoredToken()
		.then(json => dispatch(loginUserSuccess(json)))
		.catch(err => {err})
}

export function logoutUserAsync() {
	return (dispatch) => logout()
		.then(() => dispatch(logoutUser()))
}


