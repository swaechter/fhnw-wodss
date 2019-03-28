import Storage from './storage';
class Session{
	constructor(){
		this._token = false,
		this._user = false;
		this.storage = new Storage('sessionStorage');
		return {
			check: this.check.bind(this),
			user: this.user.bind(this),
			token: this.token.bind(this),
			set: this.set.bind(this),
			logout: this.logout.bind(this)
		};
	}
	check(){
		if (!this._token) this._token = this.storage.get('_token');
		return (this._token !== '' && this._token !== null && typeof this._token !== 'undefined');
	}
	user(){
		if (!this._user) this._user = this.storage.get('_user');
		return this._user;
	}
	token(){
		if (!this._token) this._token = this.storage.get('_token');
		return this._token;
	}
	set(key, value){
		return this.storage.set(key, value);
	}
	logout(){
		this.storage.remove('_user');
		this.storage.remove('_token');
	}
}
export default new Session();