import {
	USER_LOGIN_BEGINN,
	USER_LOGIN_SUCCESS,
	USER_LOGOUT_FAIL,
	loginStatus
} from '../actions';

const initialState = {
	loginStatus : loginStatus.LOGGED_OUT,
	token : "",
	user : {} 
}

export function auth(state = initialState, action) {
	switch (action.type) {
		case USER_LOGIN_BEGINN:
			return Object.assign(status,{loginStatus : loginStatus.FETCHING_JWT});
		case USER_LOGIN_SUCCESS:
			let jwt = action.payload;
			let user = {username: "dummyUser"}
			return (
				{
					loginStatus: loginStatus.LOGGED_IN,
					token: jwt,
					user
				}
			);
		case USER_LOGOUT_FAIL:
			return initialState;
		default:
			return state;
	}
}

