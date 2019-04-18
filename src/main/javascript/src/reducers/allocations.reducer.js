import {
	FETCH_ALLOCATIONS_BEGIN,
	FETCH_ALLOCATIONS_SUCCESS,
	FETCH_ALLOCATIONS_FAIL
} from '../actions';

const initialState = () => {
	return [];
}

export function allocations(state = initialState(), action) {
	switch (action.type) {
		case FETCH_ALLOCATIONS_BEGIN:
			return state;
		case FETCH_ALLOCATIONS_SUCCESS:
			return action.payload;
		case FETCH_ALLOCATIONS_FAIL:
			return [];
		default:
			return state;
	}
}