import {Component} from 'preact';
import Layout from "../../../components/layout";
import RoleLock from "../../../components/role-lock";
import {connect} from "preact-redux";
import reducers from "../../../reducers";
import * as actions from "../../../actions";
import Error from "../../../components/error";
import {Link} from "preact-router/match";

@connect(reducers, actions)
export default class UpdateEmployeePage extends Component {

    constructor(props) {
        super(props);

        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);

        this.props.clearError();
        this.state = {
            emailAddress: '',
            firstName: '',
            lastName: '',
            active: true
        };
    }


    async componentDidMount() {
        await this.props.fetchAllAdminEmployees();
        const employee = this.props.admin_employees.find(item => item.id === this.props.id);
        if (employee != null) {
            this.setState({
                emailAddress: employee.emailAddress,
                firstName: employee.firstName,
                lastName: employee.lastName,
                active: employee.active
            });
        }
    }

    handleChange(event) {
        const id = event.target.id;
        const value = event.target.value;
        this.setState({
            [id]: value
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.updateAdminEmployee(this.props.id, {
            'emailAddress': this.state.emailAddress,
            'firstName': this.state.firstName,
            'lastName': this.state.lastName,
            'active': this.state.active
        });
        this.setState({
            emailAddress: '',
            firstName: '',
            lastName: '',
            active: true
        });
    }

    render() {
        if (this.state.emailAddress != null) {
            return (
                <Layout>
                    <RoleLock allowedRoles={['Administrator']}>
                        <Error>
                            <form>
                                <div className="form-group">
                                    <label htmlFor="emailAddress">Email address</label>
                                    <input className="form-control" id="emailAddress" type="email"
                                           placeholder="Enter email"
                                           required minLength="1" maxLength="120"
                                           value={this.state.emailAddress} onChange={this.handleChange}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="firstName">First name</label>
                                    <input className="form-control" id="firstName" type="text"
                                           placeholder="Enter first name"
                                           required minLength="1" maxLength="50"
                                           value={this.state.firstName} onChange={this.handleChange}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="lastName">Last name</label>
                                    <input className="form-control" id="lastName" type="text"
                                           placeholder="Enter last name"
                                           required minLength="1" maxLength="50"
                                           value={this.state.lastName} onChange={this.handleChange}/>
                                </div>
                                <div className="form-group">
                                    <label htmlFor="role">Status</label>
                                    <select className="form-control" id="active" required value={this.state.active}
                                            onChange={this.handleChange}>
                                        <option value="true">Active</option>
                                        <option value="false">Disabled</option>
                                    </select>
                                </div>
                                <Link className="btn btn-primary" href="/admin/employees" role="button">Back to
                                    overview</Link>
                                <button onClick={this.handleSubmit} type="submit"
                                        className="btn btn-primary float-right">Update
                                </button>
                            </form>
                        </Error>
                    </RoleLock>
                </Layout>
            );
        } else {
            return (
                <Layout>
                    <RoleLock allowedRoles={['Administrator']}>
                    </RoleLock>
                </Layout>
            );
        }
    }
}
