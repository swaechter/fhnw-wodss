import { combineReducers } from 'redux';

import { todos, visibilityFilter } from './todo.reducer';

const reducers = combineReducers({
	visibilityFilter,
	todos
});

export default reducers;
