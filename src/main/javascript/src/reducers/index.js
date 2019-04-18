import { combineReducers } from 'redux';

import { todos } from './todo.reducer';
import { auth } from './auth.reducer';
import { projects } from './projects.reducer'
import { allocations } from './allocations.reducer'

const reducers = combineReducers({
	todos,
	auth,
	projects,
	allocations
});

export default reducers;
