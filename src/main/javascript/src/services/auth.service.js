import { loginUserSuccess, loginUserFail } from "../actions";
import { apiServerUrl } from "./config";
import { handleErrors } from "./api.service"
var jwtDecode = require('jwt-decode');

export async function fetchToken(credentials) {
    let response = await fetch(new URL('/api/token', apiServerUrl), {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json',
            "Accept": "application/json",
        }),
        body: JSON.stringify(credentials)
    })

    if (response.ok) {
        let json = await response.json()
        setStoredToken(json.token);
        return json.token;
    }

    throw Error("Login failed. Check your credentials or contact your administrator")
}

export async function logout() {
    localStorage.removeItem('token');
    return;
}

export async function getCurrentToken(dispatch) {
    try {
        let currentToken = await getStoredTokenIfValid()
        if (shouldRenewToken(currentToken)) {
            currentToken = await renewToken(currentToken)
            dispatch(loginUserSuccess(currentToken))
        }
        return currentToken;
    }
    catch (e) {
        await logout()
        dispatch(loginUserFail(e))
        throw e
    }
}

async function renewToken(oldToken) {
    let response = await fetch(new URL('/api/token', apiServerUrl), {
        method: 'PUT',
        headers: new Headers({
            'Content-Type': 'application/json',
            "Accept": "application/json"
        }),
        body: JSON.stringify({'token': oldToken})
    })

    if (response.ok) {
        let json = await response.json()
        setStoredToken(json.token);
        return json.token;
    }

    throw Error("Login failed. Token expired")
}

export async function getStoredTokenIfValid() {
    let token = localStorage.getItem('token');
    var expiry = token && jwtDecode(token).exp;
    const now = new Date();
    if (expiry && now.getTime() < (expiry * 1000)) {
        return token;
    }
    throw new Error('No valid token found')
}

function setStoredToken(token) {
    localStorage.setItem('token', token)
}

function shouldRenewToken(token) {
    var expiry = jwtDecode(token).exp;
    //5 minutes before now
    var renewAfter = expiry - 300;
    var now = new Date();

    return now.getTime() / 1000 > renewAfter;
}


