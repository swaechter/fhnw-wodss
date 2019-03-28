import { combineReducers } from 'redux';
import app from './components/app/reducer';
import auth from './components/auth/reducer';

export default combineReducers({
	app,
	auth
});