import { h, Component } from 'preact';

export default class Navbar extends Component {

    render({ children }) {
        return (
            <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
                <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="/">Planrrr</a>
                <ul class="navbar-nav px-3">
                    {children}
                </ul>
            </nav>
        );
    }
}