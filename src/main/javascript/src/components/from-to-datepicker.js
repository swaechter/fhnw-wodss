import { Component } from 'preact'
import Flatpickr from './preact-flatpickr'


export default class FromToDatePicker extends Component {

  onInputChanged = (dates) => {
    if (dates.length == 2) {
      this.props.onRangeUpdated(...dates)
    }
  }

  getFlatpickerConfig = (from, to) => {
    return {
      mode: "range",
      weekNumbers: true,
      defaultDate: [from, to],
      "locale": {
        "firstDayOfWeek": 1 // start week on Monday
      }
    }

  }

  render({ from, to }) {
    return (
      <div class='row'>
        <div class="col">
          <div class='float-right'>
            <div class="input-group input-group-sm mb-3">
              <div class="input-group-prepend" style={{width: '220px'}}>
                <Flatpickr className='form-control'
                  config={this.getFlatpickerConfig(from, to)}
                  value={[from, to]}
                  onChange={this.onInputChanged}>
                </Flatpickr>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}