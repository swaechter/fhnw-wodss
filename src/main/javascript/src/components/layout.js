import {Component} from 'preact';
import NavigationItem from './navigation-item';
import Navbar from './navbar';
import SideMenu from './menu';

import {connect} from 'preact-redux';
import reducers from '../reducers';
import * as actions from '../actions';
import OptionalRoleLock from "./optional-role-lock";

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
                        <OptionalRoleLock allowedRoles={['Administrator']}>
                            <NavigationItem href='/admin/employees' title='Employees'/>
                        </OptionalRoleLock>
                        <OptionalRoleLock allowedRoles={['Administrator']}>
                            <NavigationItem href='/admin/contracts' title='Contracts'/>
                        </OptionalRoleLock>
                    </SideMenu>
                    <main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4" style={{marginTop: 20}}>
                        {children}
                    </main>
                </div>
            </div>
        );
    }
}
