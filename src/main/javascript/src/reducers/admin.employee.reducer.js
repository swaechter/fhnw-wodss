import {
    CREATE_ADMIN_EMPLOYEES_SUCCESS,
    DELETE_ADMIN_EMPLOYEES_SUCCESS,
    FETCH_ADMIN_EMPLOYEES_SUCCESS,
    UPDATE_ADMIN_EMPLOYEES_SUCCESS
} from '../actions/admin.actions';

const initialState = () => {
    return [];
};

export function admin_employees(state = initialState(), action) {
    switch (action.type) {
        case CREATE_ADMIN_EMPLOYEES_SUCCESS:
            let employee = action.employee;
            return [
                ...state,
                employee
            ];
        case FETCH_ADMIN_EMPLOYEES_SUCCESS:
            return action.employees;
        case UPDATE_ADMIN_EMPLOYEES_SUCCESS:
            return state;
        case DELETE_ADMIN_EMPLOYEES_SUCCESS:
            return state;
        default:
            return state;
    }
}
