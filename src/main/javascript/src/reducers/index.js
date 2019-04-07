import { combineReducers } from 'redux';

import { todos } from './todo.reducer';
import { auth } from './auth.reducer';
import { projects } from './projects.reducer'

const reducers = combineReducers({
	todos,
	auth,
	projects
});

export default reducers;
