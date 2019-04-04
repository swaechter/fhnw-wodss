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
import { loginStatus } from '../actions';


@connect(reducers, actions)
export default class App extends Component {

	isAuthenticated = () => {
		return this.props.auth.loginStatus == loginStatus.LOGGED_IN
	}

	handleRoute = async e =>{
		if (!this.isAuthenticated){
			route('/login', true);
		}
	}

	componentDidMount(){
		let credentials = {
			"emailAddress": "simon.waechter@students.fhnw.ch",
			"rawPassword": "123456aA"
		}
		this.props.loginUserAsync(credentials)
	}

	render() {
		let authed = this.isAuthenticated();
		return (
			<div>
				<Navbar>
					<li class="nav-item text-nowrap">
						<a class="nav-link" href="#">Sign out</a>
					</li>
				</Navbar>

				<div class="container-fluid">
				{this.isAuthenticated()?	
				(
					<SideMenu>
						<NavigationItem href='/dashboard' title='Dashboard'/>
						<NavigationItem href='/todo' title='Todo`s'/>
						<NavigationItem href='/login' title='Login'/>
					</SideMenu>
				)
				:(<div/>)
				}					
					<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
						<h2>Section title</h2>
						<Router onChange={this.handleRoute}>
							<div path='/dashboard'>
								<p>Irgendwas mit dashboard</p>
							</div>
							<div path='/login'>
								<p>Irgendwas mit Login: {JSON.stringify(this.state.auth)}</p>
							</div>
							<TodoPage path='/todo' />
						</Router>
					</main>
				</div>
			</div>
		);
	}
}
