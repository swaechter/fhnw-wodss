import { loginState, loginUserSuccess, loginUserFail } from "../actions";
import { apiServerUrl } from "./config";
var jwtDecode = require('jwt-decode');

export function login(credentials) {
    return fetch(new URL('/api/token', apiServerUrl), {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json',
            "Accept": "application/json",
            'Access-Control-Allow-Origin': '*'
        }),
        body: JSON.stringify(credentials)
    })
    .then(res => res.json())
    .then(json => setStoredToken(json));
}

export async function logout(){
    return localStorage.removeItem('token');
}

export async function getStoredToken(){
    let token = localStorage.getItem('token');
    var expiry = token && jwtDecode(token).exp;
    const now = new Date();
    if(expiry && now.getTime() < (expiry * 1000)){
        return {token};
    }
    throw new Error('No valid token found')  
}



export async function getJWT(dispatch, getState) {
    let authState = getState().auth
    let currentToken = authState.token
    if (authState.loginState != loginState.LOGGED_IN) {
        throw new Error('Failed to get Token')
    }
    if (tokenRenewNeeded(currentToken)) {
        try {
            response = await renewToken(currentToken)
            dispatch(loginUserSuccess(response))
            currentToken = res.token;
        }
        catch (e) {
            dispatch(loginUserFail(e))
        }
    }
    return currentToken;
}


async function setStoredToken(json){
    localStorage.setItem('token', json.token)
    return json
}

function renewToken(oldToken) {
    return fetch(new URL('/api/token', apiServerUrl), {
        method: 'PUT',
        headers: new Headers({
            'Content-Type': 'application/json',
            "Accept": "application/json",
            'Access-Control-Allow-Origin': '*'
        }),
        body: JSON.stringify({
            "token": oldToken
        })
    })
        .then(res => res.json());
}

function tokenRenewNeeded(token) {
    var expiry = jwtDecode(token).exp;
    //5 minutes before now
    const renewDate = new Date();
    renewDate.setTime(renewDate.getTime() - 300000)

    return renewDate.getTime() > (expiry * 1000);
}


