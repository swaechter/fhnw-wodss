import { Component } from 'preact';
import { connect } from 'preact-redux';
import * as actions from '../actions';
import { loginState } from '../actions';
import reducers from '../reducers';
import LoginOrRegiseterPage from '../pages/login/loginOrRegiser';



@connect(reducers, actions)
export default class AuthLock extends Component {
	render({children}) {
        switch(this.props.auth.loginState){
            case loginState.LOGGED_IN:
                return ({children})
            default:
                return (<LoginOrRegiseterPage/>)
        }
	}
}
