import {Component} from 'preact';
import {connect} from 'preact-redux';

import reducers from '../../../reducers';
import * as actions from '../../../actions';
import Layout from '../../../components/layout';
import RoleLock from '../../../components/role-lock';

@connect(reducers, actions)
export default class ProjectsAdminPage extends Component {

    render() {
        return (
            <Layout>
                <RoleLock allowedRoles={['Administrator']}>
                    <h2>Projects Page (Administrator)</h2>
                    <p>Placeholder</p>
                </RoleLock>
            </Layout>
        );
    }
}
