import { loginUserSuccess, loginUserFail } from "../actions";
import { apiServerUrl } from "./config";
var jwtDecode = require('jwt-decode');

export async function fetchToken(credentials) {
    let token = await fetch(new URL('/api/token', apiServerUrl), {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json',
            "Accept": "application/json",
            'Access-Control-Allow-Origin': '*'
        }),
        body: JSON.stringify(credentials)
    })
        .then(res => res.json())
        .then(json => json.token)

    setStoredToken(token);
    return token;
}

export async function logout() {
    localStorage.removeItem('token');
    return;
}

export async function getCurrentToken(dispatch) {
    let currentToken = await getStoredToken()
    try {
        if (shouldRenewToken(currentToken)) {
            currentToken = await renewToken(currentToken)
            dispatch(loginUserSuccess(currentToken))
        }
    }
    catch (e) {
        dispatch(loginUserFail(e))
    }
    return currentToken;
}

async function renewToken(oldToken) {
    let token = await fetch(new URL('/api/token', apiServerUrl), {
        method: 'PUT',
        headers: new Headers({
            'Content-Type': 'application/json',
            "Accept": "application/json",
            'Access-Control-Allow-Origin': '*'
        }),
        body: JSON.stringify({
            "token": oldToken
        })
    }).then(res => res.json())
        .then(json => json.token);
    return token;
}

export async function getStoredToken() {
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
    const renewDate = new Date();
    renewDate.setTime(renewDate.getTime() - 300000)

    return renewDate.getTime() > (expiry * 1000);
}


