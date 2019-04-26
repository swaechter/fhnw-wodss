import {Component} from 'preact';

import {connect} from 'preact-redux';
import reducers from '../reducers';
import * as actions from '../actions';

@connect(reducers, actions)
export default class CustomError extends Component {

    render({message, children}) {
        return (
            <div>
                <div className="alert alert-danger" role="alert">{message}</div>
                {children}
            </div>
        );
    }
}
