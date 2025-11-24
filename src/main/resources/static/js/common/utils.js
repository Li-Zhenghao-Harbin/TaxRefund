function formatDate(dateStr) {
    const [datePart, timePart] = dateStr.split('T');
    const [hours, minutes] = timePart.split(':');
    return `${datePart} ${hours}:${minutes}`;
}

function formatAmount(str) {
    return '￥' + str;
}