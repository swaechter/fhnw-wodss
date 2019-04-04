import { h, Component } from 'preact';
import { Match, Link } from 'preact-router/match';

export default class NavigationItem extends Component {
    
    render({ href, title, children }) {
        return (
                <li class="nav-item">
                <Link className="nav-link" activeClassName="active" href={href}>
                    {children}
                    {title}
                </Link>
             </li>

        )
    }
}