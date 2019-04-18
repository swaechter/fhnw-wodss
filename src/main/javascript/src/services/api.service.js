import { apiServerUrl } from "./config";

function getHeader(token) {
    let header = new Headers({
        'Content-Type': 'application/json',
        "Accept": "application/json",
        'Access-Control-Allow-Origin': '*'
    })
    if (token) {
        header.append('Authorization', 'Bearer '.concat(token))
    }
    return header;
}

function handleErrors(response) {
    if (!response.ok) {
        throw Error(response.statusText);
    }
    return response;
}

export function getUrl(url){
    return new URL(url, apiServerUrl)
}

export async function doGet(url, token=null) {
    let headers = getHeader(token)
    let json = fetch(new URL(url, apiServerUrl), {
        method: 'GET',
        headers: headers,
    }).then(handleErrors)
    .then(res => res.json())
    return json;
}


export async function doPost(url, payload, token=null) {
    let headers = getHeader(token)
    let json = fetch(new URL(url, apiServerUrl), {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(payload)
    }).then(handleErrors)
    .then(res => res.json())
    return json;
}


export async function doPut(url, payload, token=null) {
    let headers = getHeader(token)
    let json = fetch(new URL(url, apiServerUrl), {
        method: 'PUT',
        headers: headers,
        body: JSON.stringify(payload)
    }).then(handleErrors)
    .then(res => res.json())
    return json;
}


export async function doDelete(url, token=null) {
    let headers = getHeader(token)
    let json = fetch(new URL(url, apiServerUrl), {
        method: 'DELETE',
        headers: headers,
    }).then(handleErrors)
    .then(res => res.json())
    return json;
}
