const _serverUrl = 'https://localhost:8000'

class AuthService {

    login(credentials) {
        var request = new Request(_serverUrl+ '/api/token', {
            method: 'POST',
            headers: new Headers({
                'Content-Type': 'application/json',
            }),
            body: JSON.stringify(credentials)
        });
        return fetch(request);
    }
        
}

export default new AuthService;