import { h, Component } from 'preact';

export default class SideMenu extends Component {

    render({ children }) {
        return (
            <nav class="col-md-2 d-none d-md-block bg-light sidebar">
                <div class="sidebar-sticky">
                    <ul class="nav flex-column">
                        {children}
                    </ul>                        
                </div>
            </nav>
        );
    }
}