const _serverUrl = 'https://localhost:8000'

class AuthService {

    login(credentials) {
        return fetch(_serverUrl + '/api/token', {
            method: 'POST',
            headers: new Headers({
                'Content-Type': 'application/json',
                "Accept" : "application/json",
                'Access-Control-Allow-Origin':'*'
            }),
            body: JSON.stringify(credentials)
        });
    }
}

export default new AuthService();