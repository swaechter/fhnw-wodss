import {Component} from 'preact';
import Layout from "../../../components/layout";
import RoleLock from "../../../components/role-lock";
import {connect} from "preact-redux";
import reducers from "../../../reducers";
import * as actions from "../../../actions";
import Error from "../../../components/error";

@connect(reducers, actions)
export default class CreateEmployeePage extends Component {

    constructor(props) {
        super(props);
        this.clearValues();
    };

    componentDidMount() {
        this.props.clearError();
    }

    clearValues() {
        this._emailAddress = '';
        this._firstName = '';
        this._lastName = '';
        this._password = '';
        this._role = 'ADMINISTRATOR';
    }

    handleChange = (event) => {
        const value = event.target.value;
        switch (event.target.id) {
            case "emailAddress":
                this._emailAddress = value;
                break;
            case "firstName":
                this._firstName = value;
                break;
            case "lastName":
                this._lastName = value;
                break;
            case "password":
                this._password = value;
                break;
            case "role":
                this._role = value;
                break;
        }
    };

    createEmployee() {
        const employee = {
            'emailAddress': this._emailAddress,
            'firstName': this._firstName,
            'lastName': this._lastName,
            'active': true
        };
        const password = this._password;
        const role1 = this._role;
        this.props.createAdminEmployee(employee, password, role1);
    }

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <Error>
                        <form>
                            <div className="form-group">
                                <label htmlFor="emailAddress">Email address</label>
                                <input className="form-control" id="emailAddress" type="email" placeholder="Enter email"
                                       required minLength="1" maxLength="120" onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="firstName">First name</label>
                                <input className="form-control" id="firstName" type="text"
                                       placeholder="Enter first name"
                                       required minLength="1" maxLength="50" onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="lastName">Last name</label>
                                <input className="form-control" id="lastName" type="text" placeholder="Enter last name"
                                       required minLength="1" maxLength="50" onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="password">Password</label>
                                <input className="form-control" id="password" type="password" placeholder="Password"
                                       required onChange={this.handleChange}/>
                            </div>
                            <div className="form-group">
                                <label htmlFor="role">Role</label>
                                <select className="form-control" id="role" required onChange={this.handleChange}>
                                    <option value="ADMINISTRATOR">Administrator</option>
                                    <option value="PROJECTMANAGER">Project Manager</option>
                                    <option value="DEVELOPER">Developer</option>
                                </select>
                            </div>
                            <button onClick={() => this.createEmployee()} type="submit"
                                    className="btn btn-primary float-right">Submit
                            </button>
                        </form>
                    </Error>
                </RoleLock>
            </Layout>
        );
    }
}

