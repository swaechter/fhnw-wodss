import {Component} from 'preact';
import {connect} from 'preact-redux';

import reducers from '../../../reducers';
import * as actions from '../../../actions';
import Layout from '../../../components/layout';
import RoleLock from '../../../components/role-lock';
import {Link} from "preact-router/match";

@connect(reducers, actions)
export default class ManageEmployeesPage extends Component {

    constructor(props) {
        super(props);
        this.props.fetchAllAdminEmployees();
    }

    createEmployee() {
        //alert("New employee triggered:" + JSON.stringify(data))
    }

    updateEmployee(data) {
        //alert("Employee updated:" + JSON.stringify(data))
    }

    deleteEmployee(id) {
        this.props.deleteAdminEmployee(id);
    }

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <h2>Manage Employees (Administrator)</h2>
                    <table class="table ">
                        <thead>
                        <tr>
                            <th scope="col">Email</th>
                            <th scope="col">First Name</th>
                            <th scope="col">Last Name</th>
                            <th scope="col">Activated</th>
                            <th scope="col">Role</th>
                            <th scope="col">
                                Actions
                                &nbsp;&nbsp;&nbsp;
                                <button>
                                    <Link href="/admin/employees/create" role="button">Create</Link>
                                </button>
                            </th>
                        </tr>
                        </thead>
                        <tbody>
                        {this.props.admin_employees.map(employee => (
                            <tr key={employee.id}>
                                <td>{employee.emailAddress}</td>
                                <td>{employee.firstName}</td>
                                <td>{employee.lastName}</td>
                                <td>{employee.active + ''}</td>
                                <td>{employee.role}</td>
                                <td>
                                    <button onClick={() => {
                                        this.updateEmployee(employee)
                                    }}>Update
                                    </button>
                                    &nbsp;&nbsp;&nbsp;
                                    <button onClick={() => {
                                        this.deleteEmployee(employee.id)
                                    }}>Delete
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </RoleLock>
                < /Layout>
                    );
                    }
                    }
