import {
	USER_LOGIN_BEGINN,
	USER_LOGIN_SUCCESS,
	USER_LOGIN_FAIL,
	USER_LOGOUT,
	loginState
} from '../actions';

const initialState = () => {
	return {
		loginState: loginState.LOGGED_OUT,
		token: "",
		user: {}
	}
}

export function auth(state = initialState(), action) {
	switch (action.type) {
		case USER_LOGIN_BEGINN:
			return Object.assign(state, { loginState: loginState.FETCHING_JWT });
		case USER_LOGIN_SUCCESS:
			let jwt = action.payload.token;
			let user = { username: "dummyUser" }
			return (
				{
					loginState: loginState.LOGGED_IN,
					token: jwt,
					user
				}
			);
		case USER_LOGIN_FAIL:
		case USER_LOGOUT:
			return initialState();
		default:
			return state;
	}
}

