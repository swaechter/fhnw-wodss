import { Component } from 'preact';
import { connect } from 'preact-redux';
import * as actions from '../actions';
import { loginState } from '../actions';
import reducers from '../reducers';
import LoginPage from '../pages/login/login';


@connect(reducers, actions)
export default class AuthLock extends Component {
	render({children}) {
        switch(this.props.auth.loginState){
            case loginState.LOGGED_IN:
                return ({children});
            case loginState.LOGGED_OUT:
                return (<LoginPage/>)
            case loginState.FETCHING_JWT:
                return (<p>Loggin in...</p>)
        }
	}
}
