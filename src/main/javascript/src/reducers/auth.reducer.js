import {
	USER_LOGIN_BEGINN,
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
		token: "",
		tokenExpiry,
		employee: {}
	}
}

export function auth(state = initialState(), action) {
	switch (action.type) {
		case USER_LOGIN_BEGINN:
			return Object.assign(state, { loginState: loginState.FETCHING_JWT });
		case USER_LOGIN_SUCCESS:
			let token = action.payload.token;
			console.log(token)
			let decoded = jwtDecode(token);
			let tokenExpiry = decoded.exp;
			let employee = decoded.employee;
			return (
				{
					loginState: loginState.LOGGED_IN,
					token,
					tokenExpiry,
					employee
				}
			);
		case USER_LOGIN_FAIL:
		case USER_LOGOUT:
			return initialState();
		default:
			return state;
	}
}

