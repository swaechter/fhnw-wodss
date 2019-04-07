import { apiServerUrl } from "./config";
import { getJWT } from './auth.service'

getHeader = (token) => {
    let header = new Headers({
        'Content-Type': 'application/json',
        "Accept": "application/json",
        'Access-Control-Allow-Origin': '*'
    })
    if (token) {
        header.append('Authorization', 'Bearer '.concat(token))
    }
}

doGet(url) = async (dispatch, getState) => {
    let token = await getJWT(dispatch, getState);
    let headers = getHeader(token)
    let json = fetch(new URL(url, apiServerUrl), {
        method: 'GET',
        headers: headers,
    }).then(res => res.json())
    return json
}

doPost(url, payload) = async (dispatch, getState) => {
    let token = await getJWT(dispatch, getState);
    let headers = getHeader(token)
    let json = fetch(new URL(url, apiServerUrl), {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(payload)
    }).then(res => res.json())
    return json
}

doPut(url, payload) = async (dispatch, getState) => {
    let token = await getJWT(dispatch, getState);
    let headers = getHeader(token)
    let json = fetch(new URL(url, apiServerUrl), {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(payload)
    }).then(res => res.json())
    return json
}

doDelete(url) = async (dispatch, getState) => {
    let token = await getJWT(dispatch, getState);
    let headers = getHeader(token)
    let json = fetch(new URL(url, apiServerUrl), {
        method: 'DELETE',
        headers: headers,
    }).then(res => res.json())
    return json
}

