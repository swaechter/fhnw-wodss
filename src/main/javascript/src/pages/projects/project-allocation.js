import {Component} from 'preact';
import {connect} from "preact-redux";
import Layout from "../../components/layout";
import reducers from "../../reducers";
import * as actions from "../../actions";


@connect(reducers, actions)
export default class ProjectAllocationPage extends Component {

    render() {
        return (
            <Layout>
                <h2>Project Detail Page</h2>
                <p>Placeholder</p>
            </Layout>
        );
    }
}
