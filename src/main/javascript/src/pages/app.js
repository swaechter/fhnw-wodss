import { h, Component } from 'preact';

import { Router } from 'preact-router';


require('Bootstrap')
import 'bootstrap/dist/css/bootstrap.css';
import './style.css';
import NavigationItem from '../components/navigation-item';
import Navbar from '../components/navbar';
import SideMenu from '../components/menu';
import TodoPage from './todo/todo-page';

export default class App extends Component {


	render() {
		return (
			<div>
				<Navbar>
					<li class="nav-item text-nowrap">
						<a class="nav-link" href="#">Sign out</a>
					</li>
				</Navbar>

				<div class="container-fluid">
					<SideMenu>
						<NavigationItem href='/dashboard' title='Dashboard'>
						</NavigationItem>
						<NavigationItem href='/todo' title='Todo`s' featherIcon='file'></NavigationItem>
					</SideMenu>

					<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
						<h2>Section title</h2>
						<Router>
							<div path='/dashboard'>
								<p>Irgendwas mit dashboard</p>
							</div>
							<TodoPage path='/todo' />
						</Router>
					</main>
				</div>
			</div>
		);
	}
}
