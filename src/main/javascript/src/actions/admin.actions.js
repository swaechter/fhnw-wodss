import {doDelete, doGet, getUrl} from "../services/api.service";
import {getCurrentToken} from "../services/auth.service";

/*
 * action types
 */

export const CREATE_ADMIN_EMPLOYEES_SUCCESS = 'CREATE_ADMIN_EMPLOYEES_SUCCESS';
export const CREATE_ADMIN_EMPLOYEES_FAIL = 'CREATE_ADMIN_EMPLOYEES_FAIL';
export const FETCH_ADMIN_EMPLOYEES_SUCCESS = 'FETCH_ADMIN_EMPLOYEES_SUCCESS';
export const FETCH_ADMIN_EMPLOYEES_FAIL = 'FETCH_ADMIN_EMPLOYEES_FAIL';
export const UPDATE_ADMIN_EMPLOYEES_SUCCESS = 'UPDATE_ADMIN_EMPLOYEES_SUCCESS';
export const UPDATE_ADMIN_EMPLOYEES_FAIL = 'UPDATE_ADMIN_EMPLOYEES_FAIL';
export const DELETE_ADMIN_EMPLOYEES_SUCCESS = 'DELETE_ADMIN_EMPLOYEES_SUCCESS';
export const DELETE_ADMIN_EMPLOYEES_FAIL = 'DELETE_ADMIN_EMPLOYEES_FAIL';
/*
 * other constants
 */


/*
 * action creators
 */

const createAdminEmployeesSuccess = (employees) => ({
    type: CREATE_ADMIN_EMPLOYEES_SUCCESS,
    employees
});

const createAdminEmployeesFail = (error) => ({
    type: CREATE_ADMIN_EMPLOYEES_FAIL,
    error
});

const fetchAdminEmployeesSuccess = (employees) => ({
    type: FETCH_ADMIN_EMPLOYEES_SUCCESS,
    employees
});

const fetchAdminEmployeesFail = (error) => ({
    type: FETCH_ADMIN_EMPLOYEES_FAIL,
    error
});

const updateAdminEmployeesSuccess = (employees) => ({
    type: UPDATE_ADMIN_EMPLOYEES_SUCCESS,
    employees
});

const updateAdminEmployeesFail = (error) => ({
    type: UPDATE_ADMIN_EMPLOYEES_FAIL,
    error
});

const deleteAdminEmployeesSuccess = (id) => ({
    type: DELETE_ADMIN_EMPLOYEES_SUCCESS,
    id
});

const deleteAdminEmployeesFail = (error) => ({
    type: DELETE_ADMIN_EMPLOYEES_FAIL,
    error
});

/**
 * async function calls
 */
export function fetchAdminEmployees() {
    return async (dispatch) => {
        let url = getUrl("/api/employee");
        try {
            let token = await getCurrentToken(dispatch);
            let json = await doGet(url, token);
            dispatch(fetchAdminEmployeesSuccess(json))
        } catch (error) {
            dispatch(fetchAdminEmployeesFail(error))
        }
    }
}


export function deleteAdminEmployee(id) {
    return async (dispatch) => {
        let url = getUrl("/api/employee/" + id);
        try {
            let token = await getCurrentToken(dispatch);
            await doDelete(url, token);
            dispatch(deleteAdminEmployeesSuccess(id))
            dispatch(fetchAdminEmployees())
        } catch (error) {
            dispatch(deleteAdminEmployeesFail(error))
        }
    }
}
