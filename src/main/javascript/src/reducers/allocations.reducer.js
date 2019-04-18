import {
	FETCH_ALLOCATIONS_BEGIN,
	FETCH_ALLOCATIONS_SUCCESS,
	FETCH_ALLOCATIONS_FAIL
} from '../actions';
import { unique } from '../utils/filters';

const initialState = () => {
	return [];
}

export function allocations(state = initialState(), action) {
	switch (action.type) {
		case FETCH_ALLOCATIONS_BEGIN:
			return state;
		case FETCH_ALLOCATIONS_SUCCESS:
			let newEntries = action.payload.map((item) => {
				return {
					...item, 
					startDate: new Date(item.startDate),
					endDate: new Date(item.endDate)}
				})
			let joinedArray = [...state, ...newEntries]
			return unique(joinedArray, (item) => item.id)
		case FETCH_ALLOCATIONS_FAIL:
			return [];
		default:
			return state;
	}
}