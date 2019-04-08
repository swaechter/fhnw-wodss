import {
	FETCH_PROJECTS_BEGIN,
	FETCH_PROJECTS_SUCCESS,
	FETCH_PROJECTS_FAIL
} from '../actions';

const initialState = () => {
	return [];
}

export function projects(state = initialState(), action) {
	switch (action.type) {
		case FETCH_PROJECTS_BEGIN:
			return state;
		case FETCH_PROJECTS_SUCCESS:
			return action.payload;
		case FETCH_PROJECTS_FAIL:
			return [];
		default:
			return state;
	}
}