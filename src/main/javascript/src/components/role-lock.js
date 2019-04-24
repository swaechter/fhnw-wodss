import { Component } from 'preact';
import { connect } from 'preact-redux';
import * as actions from '../actions';
import { loginState } from '../actions';
import reducers from '../reducers';
import Layout from './layout';
import { Link } from 'preact-router/match';

@connect(reducers, actions)
export default class RoleLock extends Component {
    render({ allowedRoles, children }) {
        console.log()
        let allowed = allowedRoles
            .map(str => str.toUpperCase())
            .includes(this.props.auth.employee.role.toUpperCase())
        if (allowed) {
            return ({ children })
        } else {
            return (
                    <div class="jumbotron jumbotron-fluid danger">
                        <div class="container">
                            <h1 class="display-4">Access deinied</h1>
                            <p class="lead">You are not allowed to see this content!</p>
                            <Link className="btn btn-primary btn-lg" href="/my-allocation" role="button">Take me Home</Link>
                        </div>
                    </div>
                )
        }
    }
}
