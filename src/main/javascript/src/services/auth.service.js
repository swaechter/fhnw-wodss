import { loginState, loginUserSuccess, loginUserFail } from "../actions";
import { apiServerUrl } from "./config";

    export function login(credentials) {
        return fetch(apiServerUrl + '/api/token', {
            method: 'POST',
            headers: new Headers({
                'Content-Type': 'application/json',
                "Accept": "application/json",
                'Access-Control-Allow-Origin': '*'
            }),
            body: JSON.stringify(credentials)
        }).then(res => res.json());
    }

    export function renewToken(oldToken) {
        return fetch(apiServerUrl + '/api/token', {
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


    export async function getJWT(dispatch, getState){
        let authState = getState().auth
        let currentToken = auth.token
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

    export function tokenRenewNeeded(token) {
        var jwt = require('jsonwebtoken');
        const expiry = jwt.decode(token).exp;
        const now = new Date();
        //5 minutes before now
        const renewDate = now.setTime(now.getTime() - 300000)
        return renewDate.getTime() > (expiry * 1000);
    }


