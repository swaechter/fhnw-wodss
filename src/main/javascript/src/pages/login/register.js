import { h, Component } from 'preact';
import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';
import { route } from 'preact-router';
import { loginState } from '../../actions';
import Navbar from '../../components/navbar';
import Error from '../../components/error';

@connect(reducers, actions)
export default class RegisterForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            emailAddress: '',
            firstName: '',
            lastName: '',
            password: '',
            role: 'ADMINISTRATOR'
        };
    }

    handleChange = (event) => {
        const id = event.target.id;
        const value = event.target.value;
        this.setState({
            [id]: value
        });
    }


    handleSubmit = (event) => {
        let role = this.state.role;
        let password = this.state.password;
        let employee = {
            'emailAddress': this.state.emailAddress,
            'firstName': this.state.firstName,
            'lastName': this.state.lastName,
            'active': false
        };
        this.props.registerUserAsync(employee, password, role);
        event.preventDefault();
        this.props.toggleRegister()
    }

    render() {
        return (
            <div>
                <h2>Register</h2>
                <Error />
                <form onSubmit={this.handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="emailAddress">Email address</label>
                        <input className="form-control" id="emailAddress" type="email" placeholder="Enter email"
                            required minLength="1" maxLength="120"
                            value={this.state.emailAddress} onChange={this.handleChange} />
                    </div>
                    <div className="form-group">
                        <label htmlFor="firstName">First name</label>
                        <input className="form-control" id="firstName" type="text"
                            placeholder="Enter first name"
                            required minLength="1" maxLength="50"
                            value={this.state.firstName} onChange={this.handleChange} />
                    </div>
                    <div className="form-group">
                        <label htmlFor="lastName">Last name</label>
                        <input className="form-control" id="lastName" type="text" placeholder="Enter last name"
                            required minLength="1" maxLength="50"
                            value={this.state.lastName} onChange={this.handleChange} />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input className="form-control" id="password" type="password" placeholder="Password"
                            required
                            value={this.state.password} onChange={this.handleChange} />
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
                    <div class='row'>
                        <div class='col'>
                            <button onClick={this.props.toggleRegister} type='button' class="btn btn-secondary btn-block ">Back to login</button>
                        </div>
                        <div class='col'>
                            <button type="submit" class="btn btn-primary btn-block ">Register</button>
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}