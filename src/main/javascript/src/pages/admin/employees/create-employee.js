import {Component} from 'preact';
import Layout from "../../../components/layout";
import RoleLock from "../../../components/role-lock";
import {connect} from "preact-redux";
import reducers from "../../../reducers";
import * as actions from "../../../actions";
import Error from "../../../components/error";
import {Link} from "preact-router/match";

@connect(reducers, actions)
export default class CreateEmployeePage extends Component {

    constructor(props) {
        super(props);
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {
        this.clearFields();
    }

    clearFields() {
        this.setState({
            emailAddress: '',
            firstName: '',
            lastName: '',
            password: '',
            role: 'ADMINISTRATOR'
        });
    }

    handleChange(event) {
        const id = event.target.id;
        const value = event.target.value;
        this.setState({
            [id]: value
        });
    }

    handleSubmit(event) {
        let role = this.state.role;
        let password = this.state.password;
        let employee = {
            'emailAddress': this.state.emailAddress,
            'firstName': this.state.firstName,
            'lastName': this.state.lastName,
            'active': false
        };
        this.props.createAdminEmployee(employee, password, role);
        this.clearFields();
        event.preventDefault();
    }

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <Error>
                        <h2>Create Employee</h2>
                        <form>
                            <div className="form-group">
                                <label htmlFor="emailAddress">Email address</label>
                                <input className="form-control" id="emailAddress" type="email" placeholder="Enter email"
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
                                <input className="form-control" id="lastName" type="text" placeholder="Enter last name"
                                       required minLength="1" maxLength="50"
                                       value={this.state.lastName} onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="password">Password</label>
                                <input className="form-control" id="password" type="password" placeholder="Password"
                                       required
                                       value={this.state.password} onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="role">Role</label>
                                <select className="form-control" id="role" required value={this.state.role}
                                        onChange={this.handleChange}>
                                    <option value="ADMINISTRATOR">Administrator</option>
                                    <option value="PROJECTMANAGER">Project Manager</option>
                                    <option value="DEVELOPER">Developer</option>
                                </select>
                            </div>
                            <Link href="/admin/employees" role="button">Back to overview</Link>
                            <button onClick={this.handleSubmit} type="submit"
                                    className="btn btn-primary float-right">Create
                            </button>
                        </form>
                    </Error>
                </RoleLock>
            </Layout>
        );
    }
}
