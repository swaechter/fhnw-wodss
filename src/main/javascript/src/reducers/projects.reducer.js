import {
	FETCH_PROJECTS_BEGIN,
	FETCH_PROJECTS_SUCCESS,
	FETCH_PROJECTS_FAIL, FETCH_PROJECTS_AND_ITS_ALLOCATION_BEGIN
} from "../actions";

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

	{project, allocations}
}





