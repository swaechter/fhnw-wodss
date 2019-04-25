import { h, Component } from 'preact';
import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';
import { route } from 'preact-router';
import { loginState } from '../../actions';
import Navbar from '../../components/navbar';
import Error from '../../components/error';

@connect(reducers, actions)
export default class LoginPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            emailAddress: "",
            rawPassword: ""
        };
        this.handleInputChange = this.handleInputChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    handleInputChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        });
    }

    handleSubmit(event) {
        this.props.loginUserAsync(this.state);
        event.preventDefault();
    }

    render() {
        this.props.restoreLoginAsync()
        return (
            <div>
                <Navbar />
                <div class="container-fluid">
                    <main role="main" class="col-md-5 col-lg-4 ml-md-auto mr-md-auto">
                        <h2>Login</h2>
                        <Error/>
                        <form onSubmit={this.handleSubmit}>
                            <div class="form-group">
                                <label for="exampleInputEmail1">Email address</label>
                                <input type="email" class="form-control"
                                    name="emailAddress"
                                    value={this.state.emailAddress}
                                    onChange={this.handleInputChange}
                                    placeholder="Enter email"
                                    autocomplete="email" />
                                <small id="emailHelp" class="form-text text-muted">We'll never share your email with anyone else.</small>
                            </div>
                            <div class="form-group">
                                <label for="exampleInputPassword1">Password</label>
                                <input type="password" class="form-control" id="exampleInputPassword1"
                                    placeholder="Password"
                                    autocomplete="current-password"
                                    name="rawPassword"
                                    value={this.state.rawPassword}
                                    onChange={this.handleInputChange} />
                            </div>
                            {(this.props.auth.loginState == loginState.LOGGED_OUT) ?
                                (<button type="submit" class="btn btn-primary">Login</button>) :
                                (<button class="btn btn-primary" type="button" disabled>
                                    <span class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true"></span>
                                    Loggin in
                          </button>)}
                        </form>
                    </main>
                </div>
            </div>
        );
    }
}