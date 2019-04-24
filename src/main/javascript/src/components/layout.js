import {h, Component} from 'preact';
import NavigationItem from './navigation-item';
import Navbar from './navbar';
import SideMenu from './menu';

import {connect} from 'preact-redux';
import reducers from '../reducers';
import * as actions from '../actions';

@connect(reducers, actions)
export default class Layout extends Component {

    logout = e => {
        this.props.logoutUserAsync();
        e.preventDefault()
    }

    render({children}) {
        return (
            <div>
                <Navbar>
                    <li class="nav-item text-nowrap">
                        <a onClick={this.logout} class="nav-link" href="">Sign out</a>
                    </li>
                </Navbar>
                <div class="container-fluid">
                    <SideMenu>
                        <NavigationItem href='/my-allocation' title='My Allocations'/>
                        <NavigationItem href='/project' title='Projects'/>
                        <NavigationItem href='/todo' title='Todo`s'/>
                        <NavigationItem href='/admin/employees' title='Manage Employees'/>
                        <NavigationItem href='/admin/contracts' title='Manage Contracts'/>
                        <NavigationItem href='/admin/projects' title='Manage Projects'/>
                    </SideMenu>
                    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4" style={{marginTop: 20}}>
                        {children}
                    </main>
                </div>
            </div>
        );
    }
}
