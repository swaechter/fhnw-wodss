import {
	USER_LOGIN_BEGIN,
	USER_LOGIN_SUCCESS,
	USER_LOGIN_FAIL,
	USER_LOGOUT,
	loginState
} from '../actions';
var jwtDecode = require('jwt-decode');


const initialState = () => {
	let tokenExpiry = new Date().getTime();
	return {
		loginState: loginState.LOGGED_OUT,
		employee: {}
	}
}

export function auth(state = initialState(), action) {
	switch (action.type) {
		case USER_LOGIN_BEGIN:
			return { ...state, loginState: loginState.FETCHING_JWT };
		case USER_LOGIN_SUCCESS:
			let token = action.token;
			let decoded = jwtDecode(token);
			let employee = decoded.employee;
			if (employee.active) {
				return (
					{
						loginState: loginState.LOGGED_IN,
						employee
					}
				);
			} else {
				return initialState();
			}

		case USER_LOGIN_FAIL:
		case USER_LOGOUT:
			return initialState();
		default:
			return state;
	}
}

