function formatDate(dateStr) {
    const [datePart, timePart] = dateStr.split('T');
    const [hours, minutes] = timePart.split(':');
    const seconds = timePart.split(':')[2].substring(0, 2);
    return `${datePart} ${hours}:${minutes}:${seconds}`;
}

function formatAmount(str) {
    if (str == null) {
        return '￥0';
    }
    return '￥' + str;
}

function loadCountries(obj) {
    obj.html("");
    countries.forEach(country => {
        var optionsRow = `<option value="${country.Code}">${country.Name}</option>`;
        obj.append(optionsRow);
    });
}

function getCountryNameByCode(code) {
    for (var i = 0; i < countries.length; i++) {
        if (countries[i].Code == code) {
            return countries[i].Name;
        }
    }
    return null;
}