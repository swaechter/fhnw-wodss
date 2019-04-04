import { combineReducers } from 'redux';

import { todos } from './todo.reducer';
import { auth } from './auth.reducer';

const reducers = combineReducers({
	todos,
	auth
});

export default reducers;
