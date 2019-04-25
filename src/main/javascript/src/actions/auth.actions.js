import { fetchToken, logout, getStoredTokenIfValid, getCurrentToken } from "../services/auth.service";
import { clearError, setError } from "./error.actions";
import { getUrl, doPost } from "../services/api.service";

/*
 * action types
 */

export const USER_LOGIN_BEGIN = 'USER_LOGIN_BEGIN';
export const USER_LOGIN_SUCCESS = 'USER_LOGIN_SUCCESS';
export const USER_LOGIN_FAIL = 'USER_LOGIN_FAIL';

export const USER_LOGOUT = 'USER_LOGOUT';

export const REGISTER_USER_SUCCESS = 'REGISTER_USER_SUCCESS';
export const REGISTER_USER_FAIL = 'REGISTER_USER_FAIL';


/*
 * other constants
 */

export const loginState = {
	LOGGED_OUT: 'LOGGED_OUT',
	FETCHING_JWT: 'FETCHING_JWT',
	LOGGED_IN: 'LOGGED_IN',
	REGISTERED_OK: 'REGISTERED_OK'
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

function registerUserSucess(){
	return { type: REGISTER_USER_SUCCESS}
}

function registerUserFail(){
	return { type: REGISTER_USER_FAIL}
}

/**
 * async function calls
 */
export function loginUserAsync(credentials) {
	return async (dispatch) => {
		try{
			dispatch(loginUserBegin());
			dispatch(clearError())
			let token = await fetchToken(credentials)
			dispatch(loginUserSuccess(token))
		}catch(error){
			dispatch(loginUserFail(error))
			dispatch(setError(error.message))
		}
	}
}

export function restoreLoginAsync() {
	return (dispatch) => 
	getStoredTokenIfValid()
		.then(token => dispatch(loginUserSuccess(token)))
		.catch(err => {})
}

export function logoutUserAsync() {
	return (dispatch) => {
		dispatch(clearError())
		logout()
		.then(() => dispatch(logoutUser()))
	}
}

export function registerUserAsync(employee, password, role) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/employee");
            url.searchParams.append('password', password);
            url.searchParams.append('role', role);
            let json = await doPost(url, employee, null);
            dispatch(clearError());
            dispatch(registerUserSucess(json));
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}



