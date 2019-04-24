import {Component} from 'preact';
import {connect} from 'preact-redux';

import reducers from '../../../reducers';
import * as actions from '../../../actions';
import Layout from '../../../components/layout';
import RoleLock from '../../../components/role-lock';

@connect(reducers, actions)
export default class EmployeesAdminPage extends Component {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        this.props.fetchAdminEmployees();
    }

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <h2>Employees Page (Administrator)</h2>
                    {this.props.admin_employees.map(i => (
                        <div>
                            <p>Email address: {i.emailAddress}</p>
                            <p>First name: {i.firstName}</p>
                            <p>Last name: {i.lastName}</p>
                        </div>
                    ))}
                </RoleLock>
            </Layout>
        );
    }
}
