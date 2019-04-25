
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

export const getMonday = (date) => {
    let day = date.getDay() || 7;
    if (day !== 1)
        date.setHours(-24 * (day - 1));
    return date;
}

export const getDateRange = (startDate, stopDate) => {
    stopDate = removeTimeUTC(stopDate)
    let dateArray = new Array();
    let currentDate = removeTimeUTC(startDate);
    while (currentDate <= stopDate) {
        dateArray.push(removeTimeUTC(new Date(currentDate)));
        currentDate = removeTimeUTC(new Date(currentDate).setDate(new Date(currentDate).getDate() + 1));
    }
    return dateArray;
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