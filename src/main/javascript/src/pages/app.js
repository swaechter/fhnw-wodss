import { h, Component } from 'preact';

import { Router, route } from 'preact-router';


require('Bootstrap')
import 'bootstrap/dist/css/bootstrap.css';
import './style.css';
import Redirect from '../components/redirect';
import TodoPage from './todo/todo-page';
import { connect } from 'preact-redux';
import reducers from '../reducers';
import * as actions from '../actions';
import { loginState } from '../actions';
import ProjectPage from './projects/projects';
import LoginPage from './login/login';


@connect(reducers, actions)
export default class App extends Component {

	isAuthenticated = () => {
		return this.props.auth.loginState == loginState.LOGGED_IN
	}

	handleRoute = async e => {
	}

	render() {
		if (!this.isAuthenticated()) return (<LoginPage />)
		return (
			<Router onChange={this.handleRoute}>
				<TodoPage path='/todo' />
				<ProjectPage path='/project' />
				<Redirect default to="/project" />
			</Router>
		);
	}
}
