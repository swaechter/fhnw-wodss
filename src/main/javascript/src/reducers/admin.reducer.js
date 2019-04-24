import {
    CREATE_ADMIN_EMPLOYEES_FAIL,
    CREATE_ADMIN_EMPLOYEES_SUCCESS,
    DELETE_ADMIN_EMPLOYEES_FAIL,
    DELETE_ADMIN_EMPLOYEES_SUCCESS,
    FETCH_ADMIN_EMPLOYEES_FAIL,
    FETCH_ADMIN_EMPLOYEES_SUCCESS,
    UPDATE_ADMIN_EMPLOYEES_FAIL,
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
        case CREATE_ADMIN_EMPLOYEES_FAIL:
            return state;
        case FETCH_ADMIN_EMPLOYEES_SUCCESS:
            return action.employees;
        case FETCH_ADMIN_EMPLOYEES_FAIL:
            // TODO: Error handling
            return state;
        case UPDATE_ADMIN_EMPLOYEES_SUCCESS:
            // TODO: Update
            return state;
        case UPDATE_ADMIN_EMPLOYEES_FAIL:
            return state;
        case DELETE_ADMIN_EMPLOYEES_SUCCESS:
            return state;
        case DELETE_ADMIN_EMPLOYEES_FAIL:
            alert("DELETE FAIL");
            return state;
        default:
            return state;
    }
}
