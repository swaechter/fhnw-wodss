import { h, Component } from 'preact';

import { Router, route } from 'preact-router';


require('Bootstrap')
import 'bootstrap/dist/css/bootstrap.css';
import './style.css';
import NavigationItem from '../components/navigation-item';
import Navbar from '../components/navbar';
import SideMenu from '../components/menu';
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
		if (!this.isAuthenticated()) {
			route('/login', true);
		} else if (e.url == '/login') {
			route('/dashboard');
		}
	}


	render() {
		return (
			<div>
				<Navbar>
					{this.isAuthenticated() ?
						(
							<li class="nav-item text-nowrap">
								<a onClick={this.props.logoutUser} class="nav-link" href="#">Sign out</a>
							</li>
						)
						: (<div />)
					}
				</Navbar>
				<div class="container-fluid">
					{this.isAuthenticated() ?
						(
							<SideMenu>
								<NavigationItem href='/dashboard' title='Dashboard' />
								<NavigationItem href='/todo' title='Todo`s' />
								<NavigationItem href='/project' title='Projekte' />
							</SideMenu>
						)
						: (<div />)
					}
					<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
						<h2>Section title</h2>
						<Router onChange={this.handleRoute}>
							<div path='/:dashboard?'>
								<p>Irgendwas mit dashboard</p>
							</div>
							<LoginPage path='/login' />
							<TodoPage path='/todo' />
							<ProjectPage path='/project'  />
						</Router>
					</main>
				</div>
			</div>
		);
	}
}
