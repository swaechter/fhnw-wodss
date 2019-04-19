import {
	FETCH_CONTRACTS_BEGIN,
	FETCH_CONTRACTS_SUCCESS,
	FETCH_CONTRACTS_FAIL
} from '../actions';
import { unique } from '../utils/filters';
import { removeTimeUTC } from '../utils/date';

const initialState = () => {
	return [];
}

export function contracts(state = initialState(), action) {
	switch (action.type) {
		case FETCH_CONTRACTS_BEGIN:
			return state;
		case FETCH_CONTRACTS_SUCCESS:
			let newEntries = action.payload.map((item) => {
				return {
					...item, 
					startDate: removeTimeUTC(new Date(item.startDate)),
					endDate: removeTimeUTC(new Date(item.endDate))}
				})
			let joinedArray = [...state, ...newEntries]
			return unique(joinedArray, (item) => item.id)
		case FETCH_CONTRACTS_FAIL:
			return [];
		default:
			return state;
	}
}