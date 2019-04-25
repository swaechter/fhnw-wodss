import { h, Component } from 'preact';
import reducers from '../../reducers';
import * as actions from '../../actions';
import { connect } from 'preact-redux';
import { loginState } from '../../actions';
import Error from '../../components/error';

@connect(reducers, actions)
export default class LoginForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            emailAddress: "",
            rawPassword: ""
        };
    }

    handleInputChange = (event) => {
        const target = event.target;
        const value = target.value;
        const name = target.name;

        this.setState({
            [name]: value
        });
    }


    handleSubmit = (event) => {
        this.props.loginUserAsync(this.state);
        event.preventDefault();
    }

    render() {
        this.props.restoreLoginAsync()
        return (
            <div>
                <h2>Login</h2>
                <Error />
                {(this.props.auth.loginState == loginState.REGISTERED_OK)
                 ? (<div className="alert alert-success" role="alert">
                        <strong>Success!</strong> You are now registered. Request activation with your administrator.
                    </div>)
                 : null
                }
                <form onSubmit={this.handleSubmit}>
                    <div class="form-group">
                        <label for="emailAddress">Email address</label>
                        <input type="email" class="form-control"
                            id='emailAddress'
                            name="emailAddress"
                            value={this.state.emailAddress}
                            onChange={this.handleInputChange}
                            placeholder="Enter email"
                            autocomplete="email" />
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" class="form-control" id="password"
                            placeholder="Password"
                            autocomplete="current-password"
                            name="rawPassword"
                            value={this.state.rawPassword}
                            onChange={this.handleInputChange} />
                    </div>
                    <div class='row'>
                        <div class='col'>
                            <button onClick={this.props.toggleRegister} class="btn btn-secondary btn-block" type="button">Register</button>
                        </div>
                        <div class='col'>
                            {this.props.auth.loginState == loginState.FETCHING_JWT
                                    ?(
                                        <button class="btn btn-primary" type="button" disabled>
                                            <span class="spinner-grow spinner-grow-sm" role="status" aria-hidden="true" />
                                            Logging in
                                        </button>
                                    )
                                    : (<button type="submit" class="btn btn-primary btn-block ">Login</button>)
                            }
                        </div>
                    </div>
                </form>
            </div>
        );
    }
}