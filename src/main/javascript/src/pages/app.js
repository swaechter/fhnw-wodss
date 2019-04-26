import 'bootstrap/dist/css/bootstrap.min.css';
import './style.css';

import {createHashHistory} from 'history';
import {Component} from 'preact';
import {Router} from 'preact-router';
import Redirect from '../components/redirect';
import ProjectPage from './projects/projects';
import AuthLock from '../components/auth-lock';
import MyAllocationsPage from './my-allocations/my-allocations';
import ManageEmployeesPage from "./admin/employees/manage-employees";
import CreateEmployeePage from "./admin/employees/create-employee";
import UpdateEmployeePage from "./admin/employees/update-employee";
import ManageContractsPage from "./admin/contracts/manage-contracts";
import CreateContractPage from "./admin/contracts/create-contract";
import ManageProjectsPage from "./admin/projects/manage-projects";
import CreateProjectPage from "./admin/projects/create-projects";

export default class App extends Component {

    render() {
        return (
            <AuthLock>
                <Router history={createHashHistory()}>
                    <MyAllocationsPage path='/my-allocation'/>
                    <ProjectPage path='/project'/>
                    <ManageEmployeesPage path='/admin/employees'/>
                    <CreateEmployeePage path='/admin/employees/create'/>
                    <UpdateEmployeePage path='/admin/employees/update/:id'/>
                    <ManageContractsPage path='/admin/contracts'/>
                    <CreateContractPage path='/admin/contracts/create'/>
                    <ManageProjectsPage path='/admin/projects'/>
                    <CreateProjectPage path='/admin/projects/create'/>
                    <Redirect default to="/my-allocation"/>
                </Router>
            </AuthLock>
        );
    }
}
