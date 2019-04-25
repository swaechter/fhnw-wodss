import {doDelete, doGet, doPost, getUrl} from "../services/api.service";
import {getCurrentToken} from "../services/auth.service";
import {clearError, setError} from "./error.actions";

/*
 * action types
 */

export const CREATE_ADMIN_EMPLOYEES_SUCCESS = 'CREATE_ADMIN_EMPLOYEES_SUCCESS';
export const FETCH_ALL_ADMIN_EMPLOYEES_SUCCESS = 'FETCH_ALL_ADMIN_EMPLOYEES_SUCCESS';
export const FETCH_SINGLE_ADMIN_EMPLOYEES_SUCCESS = 'FETCH_SINGLE_ADMIN_EMPLOYEES_SUCCESS';
export const UPDATE_ADMIN_EMPLOYEES_SUCCESS = 'UPDATE_ADMIN_EMPLOYEES_SUCCESS';
export const DELETE_ADMIN_EMPLOYEES_SUCCESS = 'DELETE_ADMIN_EMPLOYEES_SUCCESS';
/*
 * other constants
 */


/*
 * action creators
 */

const createAdminEmployeesSuccess = (employee) => ({
    type: CREATE_ADMIN_EMPLOYEES_SUCCESS,
    employee
});

const fetchAllAdminEmployeesSuccess = (employees) => ({
    type: FETCH_ALL_ADMIN_EMPLOYEES_SUCCESS,
    employees
});

const fetchSingleAdminEmployeesSuccess = (employee) => ({
    type: FETCH_SINGLE_ADMIN_EMPLOYEES_SUCCESS,
    employee
});
const updateAdminEmployeesSuccess = (employees) => ({
    type: UPDATE_ADMIN_EMPLOYEES_SUCCESS,
    employees
});

const deleteAdminEmployeesSuccess = (id) => ({
    type: DELETE_ADMIN_EMPLOYEES_SUCCESS,
    id
});


/**
 * async function calls
 */

export function createAdminEmployee(employee, password, role) {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/employee");
            let token = await getCurrentToken(dispatch);
            url.searchParams.append('password', password);
            url.searchParams.append('role', role);
            let json = await doPost(url, employee, token);
            dispatch(clearError());
            dispatch(createAdminEmployeesSuccess(json));
            dispatch(fetchAllAdminEmployees())
        } catch (error) {
            dispatch(setError(error.message));
        }
    }
}

export function fetchAllAdminEmployees() {
    return async (dispatch) => {
        try {
            let url = getUrl("/api/employee");
            let token = await getCurrentToken(dispatch);
            let json = await doGet(url, token);
            dispatch(clearError());
            dispatch(fetchAllAdminEmployeesSuccess(json))
        } catch (error) {
            dispatch(setError(error.message))
        }
    }
}


export function deleteAdminEmployee(id) {
    return async (dispatch) => {
        let url = getUrl("/api/employee/" + id);
        try {
            let token = await getCurrentToken(dispatch);
            await doDelete(url, token);
            dispatch(clearError());
            dispatch(deleteAdminEmployeesSuccess(id))
            dispatch(fetchAllAdminEmployees())
        } catch (error) {
            dispatch(setError(error.message))
        }
    }
}
