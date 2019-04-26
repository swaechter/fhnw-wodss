import {
    CREATE_ADMIN_CONTRACTS_SUCCESS,
    DELETE_ADMIN_CONTRACTS_SUCCESS,
    FETCH_ADMIN_CONTRACTS_SUCCESS,
    UPDATE_ADMIN_CONTRACTS_SUCCESS
} from '../actions/admin.actions';

const initialState = () => {
    return [];
};

export function admin_contracts(state = initialState(), action) {
    switch (action.type) {
        case CREATE_ADMIN_CONTRACTS_SUCCESS:
            let employee = action.employee;
            return [
                ...state,
                employee
            ];
        case FETCH_ADMIN_CONTRACTS_SUCCESS:
            return action.employees;
        case UPDATE_ADMIN_CONTRACTS_SUCCESS:
            return state;
        case DELETE_ADMIN_CONTRACTS_SUCCESS:
            return state;
        default:
            return state;
    }
}
