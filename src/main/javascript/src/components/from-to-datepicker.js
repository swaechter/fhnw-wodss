import { Component } from 'preact'
import Flatpickr from './preact-flatpickr'
import { getMonday, removeTimeUTC, addDays } from '../utils/date';


export default class FromToDatePicker extends Component {
  constructor(props) {
    super(props)
    let today = removeTimeUTC(new Date())
    let monday = getMonday(today)
    let friday = addDays(monday,4)
    this.state = {
      flatpickrConfig: {
        mode: "range",
        weekNumbers: true,
        defaultDate: [monday, friday],
        // disable: [
        //   function (date) {
        //     return (date.getDay() == 0 || date.getDay() == 6)
        //   }
        // ],
        "locale": {
          "firstDayOfWeek": 1 // start week on Monday
      }
      }
    }
    this.props.onRangeUpdated(monday,friday)
  }

  onInputChanged = (dates) => {
    if (dates.length == 2) {
      this.props.onRangeUpdated(...dates)
    }
  } 
  

  render() {
    return (
      <div class='row'>
        <div class="col">
          <div class='float-right'>
            <div class="input-group input-group-sm mb-3">
              <div class="input-group-prepend">
                <Flatpickr className='form-control'
                  config={this.state.flatpickrConfig}
                  value={this.state.date}
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