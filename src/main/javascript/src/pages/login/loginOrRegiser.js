import { h, Component } from 'preact';
import Navbar from '../../components/navbar';
import LoginForm from './login';
import RegisterForm from './register';

export default class LoginOrRegiseterPage extends Component {

    constructor(props) {
        super(props);
        this.state = {
            registering: false,
        };
    }

    toggleRegistering = () => {
        this.setState({ registering: !this.state.registering })
    }

    render() {
        let form = this.state.registering ? (<RegisterForm toggleRegister={this.toggleRegistering}/>): (<LoginForm toggleRegister={this.toggleRegistering}/>)
        return (
            <div>
                <Navbar />
                <div class="container-fluid">
                    <main role="main" class="col-md-5 col-lg-4 ml-md-auto mr-md-auto" style={{marginTop: 20}}>
                        {form}
                    </main>
                </div>
            </div>
        );
    }
}