import {combineReducers} from 'redux';

import {auth} from './auth.reducer';
import {projects} from './projects.reducer'
import {allocations} from './allocations.reducer'
import {contracts} from './contracts.reducer'
import {admin_employees} from './admin.employee.reducer'
import {admin_projects} from './admin.project.reducer'
import {admin_contracts} from './admin.contract.reducer'
import {error} from "./error.reducer";

const reducers = combineReducers({
    auth,
    projects,
    allocations,
    contracts,
    admin_employees,
    admin_contracts,
    admin_projects,
    error
});

export default reducers;
