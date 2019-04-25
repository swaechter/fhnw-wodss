import { h, Component } from 'preact';
import { Link } from 'preact-router/match';

export default class Navbar extends Component {

    render({ children }) {
        return (
            <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
                <Link class="navbar-brand col-sm-3 col-md-2 mr-0" href='/my-allocation'>PlanQ</Link>
                <ul class="navbar-nav px-3">
                    {children}
                </ul>
            </nav>
        );
    }
}
