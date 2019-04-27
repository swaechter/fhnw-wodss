import {
    CLEAR_ALLOCATIONS, DELETE_ALLOCATION_SUCCESS,
    FETCH_ALLOCATIONS_BEGIN,
    FETCH_ALLOCATIONS_FAIL,
    FETCH_ALLOCATIONS_SUCCESS
} from '../actions';
import {removeTimeUTC} from '../utils/date';

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
                    startDate: removeTimeUTC(new Date(item.startDate)),
                    endDate: removeTimeUTC(new Date(item.endDate))
                }
            })
            return newEntries
        case CLEAR_ALLOCATIONS:
        case FETCH_ALLOCATIONS_FAIL:
            return [];
        case DELETE_ALLOCATION_SUCCESS:
            let allocations = state.slice();
            allocations = allocations.filter(allocation => allocation.id !== action.id);
            return allocations;
        default:
            return state;
    }
}
