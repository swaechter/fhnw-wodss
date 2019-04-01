/*
 * action types
 */

export const USER_LOGIN = 'USER_LOGIN';
export const USER_LOGOUT = 'USER_LOGOUT';

/*
 * other constants
 */

/*
 * action creators
 */

export function loginUser(username, password) {
	return { 
		type: USER_LOGIN,
		credentials: {
			username,
			password
		} 
	 };
}

export function logoutUser(index) {
	return { type: USER_LOGOUT };
}
