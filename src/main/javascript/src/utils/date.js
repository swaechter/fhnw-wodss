
export const removeTimeUTC = (date) => {
    let result = new Date(date)
    result.setUTCHours(0)
    result.setUTCMinutes(0)
    result.setUTCSeconds(0)
    result.setUTCMilliseconds(0)
    return result
}

export const checkDateRangeOverlap = (start1, end1, start2, end2) => {
    return (end2 >= start1 && start2 <= end1)
}

export const getUTCMonday = (date) => {
    let day = date.getDay() || 7;
    if (day !== 1)
        date.setUTCHours(-24 * (day - 1));
    return date;
}

export const getBusinessDays = (startDate, stopDate) => {
    stopDate = removeTimeUTC(stopDate)
    let dateArray = new Array();
    let currentDate = removeTimeUTC(startDate);
    while (currentDate <= stopDate) {
        let date = new Date(Date.UTC(currentDate.getUTCFullYear(),currentDate.getUTCMonth(), currentDate.getUTCDate()))
        if(isBusinessDay(date)){
            dateArray.push(date)
        }
        currentDate = new Date(currentDate.getTime() + 86400000) // + 1 day in ms
    }
    return dateArray;
}

function isBusinessDay(date){
    return !(date.getDay() == 0 || date.getDay() == 6) 
}

export const addDays = (date, days) => {
    let result = new Date(date);
    result.setDate(result.getDate() + days);
    return result;
}

export const dateToString = (date) => {
    let d = removeTimeUTC(new Date(date))
    let month = '' + (d.getMonth() + 1)
    let day = '' + d.getDate()
    let year = d.getFullYear()
    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;
    return [year, month, day].join('-');
}