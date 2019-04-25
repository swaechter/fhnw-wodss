/*
 * action types
 */

export const SET_ERROR = 'SET_ERROR';
export const CLEAR_ERROR = 'CLEAR_ERROR';
/*
 * other constants
 */


/*
 * action creators
 */

export const setError = (error) => ({
    type: SET_ERROR,
    error
});

export const clearError = () => ({
    type: CLEAR_ERROR,
});
