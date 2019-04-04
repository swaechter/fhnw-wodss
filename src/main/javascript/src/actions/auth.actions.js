import authService from "../services/auth.service";

/*
 * action types
 */

export const USER_LOGIN_BEGINN = 'USER_LOGIN_BEGINN';
export const USER_LOGIN_SUCCESS = 'USER_LOGIN_SUCCESS';
export const USER_LOGIN_FAIL = 'USER_LOGIN_FAIL';

export const USER_LOGOUT_BEGINN = 'USER_LOGOUT_BEGINN';
export const USER_LOGOUT_SUCCESS = 'USER_LOGOUT_SUCCESS';
export const USER_LOGOUT_FAIL = 'USER_LOGOUT_FAIL';


/*
 * other constants
 */

 export const loginStatus = {
	LOGGED_OUT: 'LOGGED_OUT',
	FETCHING_JWT: 'FETCHING_JWT',
	LOGGED_IN: 'LOGGED_IN'
};

/*
 * action creators
 */
export const loginUserBegin = () => ({
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


export function logoutUser(index) {
	return { type: USER_LOGOUT };
}

/**
 * async function calls
 */
export function loginUserAsync(credentials) {
	return (dispatch) => {
	dispatch(loginUserBegin);
	return authService.login(credentials)
	.then(res => res.json)
	.then(json => dispatch(loginUserSuccess(payload)))
	.catch(err => dispatch(loginUserFail(payload)))	
	}
}



