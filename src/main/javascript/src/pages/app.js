import 'bootstrap/dist/css/bootstrap.min.css';
import './style.css';

import {createHashHistory} from 'history';
import {Component} from 'preact';
import {Router} from 'preact-router';
import Redirect from '../components/redirect';
import TodoPage from './todo/todo-page';
import ProjectPage from './projects/projects';
import AuthLock from '../components/auth-lock';
import MyAllocationsPage from './my-allocations/my-allocations';
import ManageEmployeesPage from "./admin/employees/manage-employees";
import CreateEmployeePage from "./admin/employees/create-employee";
import UpdateEmployeePage from "./admin/employees/update-employee";
import ContractsAdminPage from "./admin/contracts/contracts-admin";
import ProjectsAdminPage from "./admin/projects/projects-admin";

export default class App extends Component {

    render() {
        return (
            <AuthLock>
                <Router history={createHashHistory()}>
                    <TodoPage path='/todo'/>
                    <MyAllocationsPage path='/my-allocation'/>
                    <ProjectPage path='/project'/>
                    <ManageEmployeesPage path='/admin/employees'/>
                    <CreateEmployeePage path='/admin/employees/create'/>
                    <UpdateEmployeePage path='/admin/employees/update/:id'/>
                    <ContractsAdminPage path='/admin/contracts'/>
                    <ProjectsAdminPage path='/admin/projects'/>
                    <Redirect default to="/my-allocation"/>
                </Router>
            </AuthLock>
        );
    }
}
