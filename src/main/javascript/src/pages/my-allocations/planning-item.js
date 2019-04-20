import { h, Component } from 'preact';


export default class PlanningItem extends Component {

    render({ width, color ,text}) {
        let style = {
            "width": width+"%", 
            "background-color": color
        }
        return (
            <div class="progress-bar" role="progressbar" style={style} title={text}></div>
        );
    }
}