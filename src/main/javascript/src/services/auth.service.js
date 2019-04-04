const _serverUrl = 'http://localhost:9000'

class AuthService {

    login(credentials) {
        var request = new Request(this._serverUrl+ '/api/token', {
            method: 'POST',
            headers: new Headers({
                'Content-Type': 'application/json'
            }),
            body: JSON.stringify(credentials)
        });
        return fetch(request);
    }
        
}

export default new AuthService;