import { h, Component } from 'preact';
import NavigationItem from './navigation-item';
import Navbar from './navbar';
import SideMenu from './menu';

import { connect } from 'preact-redux';
import reducers from '../reducers';
import * as actions from '../actions';

@connect(reducers, actions)
export default class Layout extends Component {

    render({ children }) {
        return (
            <div>
				<Navbar>
					<li class="nav-item text-nowrap">
						<a onClick={this.props.logoutUserAsync} class="nav-link" href="">Sign out</a>
					</li>
				</Navbar>
				<div class="container-fluid">
					<SideMenu>
						<NavigationItem href='/project' title='Projekte' />
						<NavigationItem href='/todo' title='Todo`s' />
					</SideMenu>
					<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">
                     {children}
                    </main>
				</div>
			</div>
        );
    }
}