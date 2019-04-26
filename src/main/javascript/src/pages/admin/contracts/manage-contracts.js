import {Component} from 'preact';
import {connect} from 'preact-redux';

import reducers from '../../../reducers';
import * as actions from '../../../actions';
import Layout from '../../../components/layout';
import RoleLock from '../../../components/role-lock';
import {Link} from "preact-router/match";
import Error from "../../../components/error";

@connect(reducers, actions)
export default class ManageContractsPage extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.props.fetchAdminContracts();
        this.props.fetchAdminEmployees();
    }

    deleteContract(id) {
        this.props.deleteAdminContract(id);
    }

    getEmployeeNameForContract(contract) {
        let value = contract.employeeId;
        this.props.admin_employees.map(employee => {
            if (employee.id === contract.employeeId) {
                value = employee.firstName + " " + employee.lastName;
            }
        });
        return value;
    }

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <Error>
                        <h2>Manage Contracts</h2>
                        <table class="table ">
                            <thead>
                            <tr>
                                <th scope="col">Start Date</th>
                                <th scope="col">End Date</th>
                                <th scope="col">Pensum Percentage</th>
                                <th scope="col">Employee</th>
                                <th scope="col">
                                    <Link className="btn btn-success" href="/admin/contracts/create"
                                          role="button">Create</Link>
                                </th>
                            </tr>
                            </thead>
                            <tbody>
                            {this.props.admin_contracts.map(contract => (
                                <tr key={contract.id}>
                                    <td>{contract.startDate}</td>
                                    <td>{contract.endDate}</td>
                                    <td>{contract.pensumPercentage}</td>
                                    <td>{this.getEmployeeNameForContract(contract)}</td>
                                    <td>
                                        <button className="btn btn-danger" onClick={() => {
                                            this.deleteContract(contract.id)
                                        }}>Delete
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    </Error>
                </RoleLock>
            </Layout>
        );
    }
}
