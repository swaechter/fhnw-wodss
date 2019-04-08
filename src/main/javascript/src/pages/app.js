import { h, Component } from 'preact';

import { Router, route } from 'preact-router';


require('Bootstrap')
import 'bootstrap/dist/css/bootstrap.css';
import './style.css';
import NavigationItem from '../components/navigation-item';
import Navbar from '../components/navbar';
import SideMenu from '../components/menu';
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
			<div>
				<Navbar>
					<li class="nav-item text-nowrap">
						<a onClick={this.props.logoutUserAsync} class="nav-link" href="">Sign out</a>
					</li>
				</Navbar>
				<div class="container-fluid">
					<SideMenu>
						<NavigationItem href='/todo' title='Todo`s' />
						<NavigationItem href='/project' title='Projekte' />
					</SideMenu>
					<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
						<h2>Section title</h2>
						<Router onChange={this.handleRoute}>
							<TodoPage path='/todo' />
							<ProjectPage path='/project' />
							<Redirect default to="/project" />
						</Router>
					</main>
				</div>
			</div>
		);
	}
}
