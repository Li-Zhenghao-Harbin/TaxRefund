$(document).ready(function() {
    // token
    token = localStorage.getItem("auth_token");
    if (!token) {
        alert("Please login");
        window.location.href = "login.html";
    }
    // initialize charts
    initOverview();
    initItemsChart();
    initApplicationsChart();
    initRefundAmountChart();
    initRefundMethodsChart();
    initMixedChart();

    // back to management page
    $('.logout-button').on('click', function() {
        window.location.href = "management.html";
    });
});

function initOverview() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/visual/getOverview",
        headers: {
            "Authorization": "Bearer " + token
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                var d = data.data;
                $("#invoiceCount").text(d.invoiceYearCount);
                $("#invoiceCountThisMonth").text(formatTitlePrefix(d.invoiceMonthCount, 1));
                $("#invoiceCountThisYear").text(formatTitlePrefix(d.invoiceYearCount, 2));
                $("#applicationFormCount").text(d.applicationFormYearCount);
                $("#applicationFormCountThisMonth").text(formatTitlePrefix(d.applicationFormMonthCount, 1));
                $("#applicationFormCountThisYear").text(formatTitlePrefix(d.applicationFormYearCount, 2));
                $("#applicationFormTotalAmount").text(formatAmount(d.applicationFormYearTotalAmount));
                $("#applicationFormTotalAmountThisMonth").text(formatTitlePrefix(formatAmount(d.applicationFormMonthTotalAmount), 1));
                $("#applicationFormTotalAmountThisYear").text(formatTitlePrefix(formatAmount(d.applicationFormYearTotalAmount), 2));
                $("#applicationFormCustomsConfirmAmount").text(formatAmount(d.applicationFormYearCustomsConfirmAmount));
                $("#applicationFormCustomsConfirmAmountThisMonth").text(formatTitlePrefix(formatAmount(d.applicationFormMonthCustomsConfirmAmount), 1));
                $("#applicationFormCustomsConfirmAmountThisYear").text(formatTitlePrefix(formatAmount(d.applicationFormYearCustomsConfirmAmount), 2));
            } else {
                console.log(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            console.log(xhr.responseText || error);
        }
    });
}

function formatTitlePrefix(title, prefix) {
    if (prefix == 1) {
        return "This Month: " + title;
    } else {
        return "This Year: " + title;
    }
    return null;
}

function initItemsChart() {
    const chartDom = document.getElementById('itemsChart');
    const myChart = echarts.init(chartDom);

    $.ajax({
        type: "GET",
        url: "http://localhost:8081/visual/getItemsPie",
        headers: {
            "Authorization": "Bearer " + token
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                const option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b}: {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        right: 10,
                        top: 'center',
                        data: ['Approved', 'Rejected', 'Not Reviewed']
                    },
                    series: [
                        {
                            name: 'Items',
                            type: 'pie',
                            radius: ['40%', '70%'],
                            avoidLabelOverlap: false,
                            itemStyle: {
                                borderRadius: 10,
                                borderColor: '#fff',
                                borderWidth: 2
                            },
                            label: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                label: {
                                    show: true,
                                    fontSize: '18',
                                    fontWeight: 'bold'
                                }
                            },
                            labelLine: {
                                show: false
                            },
                            data: [
                                {value: data.data.rejectedCount, name: 'Approved', itemStyle: {color: '#4CAF50'}},
                                {value: data.data.approvedCount, name: 'Rejected', itemStyle: {color: '#F44336'}},
                                {value: data.data.notReviewedCount, name: 'Not Reviewed', itemStyle: {color: '#88ABDA'}}
                            ]
                        }
                    ]
                };

                myChart.setOption(option);
            } else {
                console.log(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            console.log(xhr.responseText || error);
        }
    });

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}

function initApplicationsChart() {
    const chartDom = document.getElementById('applicationsChart');
    const myChart = echarts.init(chartDom);
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/visual/getApplicationFormsPie   ",
        headers: {
            "Authorization": "Bearer " + token
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                const option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b}: {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        right: 10,
                        top: 'center',
                        data: ['Refunded', 'Not Refunded']
                    },
                    series: [
                        {
                            name: 'Application forms',
                            type: 'pie',
                            radius: ['40%', '70%'],
                            avoidLabelOverlap: false,
                            itemStyle: {
                                borderRadius: 10,
                                borderColor: '#fff',
                                borderWidth: 2
                            },
                            label: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                label: {
                                    show: true,
                                    fontSize: '18',
                                    fontWeight: 'bold'
                                }
                            },
                            labelLine: {
                                show: false
                            },
                            data: [
                                {value: data.data.taxRefundedCount, name: 'Refunded', itemStyle: {color: '#2196F3'}},
                                {value: data.data.notTaxRefundedCount, name: 'Not Refunded', itemStyle: {color: '#FF9800'}}
                            ]
                        }
                    ]
                };
                myChart.setOption(option);
            } else {
                console.log(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            console.log(xhr.responseText || error);
        }
    });

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}

function initRefundAmountChart() {
    const chartDom = document.getElementById('refundAmountChart');
    const myChart = echarts.init(chartDom);
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/visual/getCustomsConfirmAmountPie",
        headers: {
            "Authorization": "Bearer " + token
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                const option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b}: {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        right: 10,
                        top: 'center',
                        data: ['Cash', 'Bank Card']
                    },
                    series: [
                        {
                            name: 'Customs Confirmed Amount',
                            type: 'pie',
                            radius: ['40%', '70%'],
                            avoidLabelOverlap: false,
                            itemStyle: {
                                borderRadius: 10,
                                borderColor: '#fff',
                                borderWidth: 2
                            },
                            label: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                label: {
                                    show: true,
                                    fontSize: '18',
                                    fontWeight: 'bold',
                                    formatter: '{b}\n￥{c}'
                                }
                            },
                            labelLine: {
                                show: false
                            },
                            data: [
                                {value: data.data.cash, name: 'Cash', itemStyle: {color: '#FFC107'}},
                                {value: data.data.bankCard, name: 'Bank Card', itemStyle: {color: '#9C27B0'}}
                            ]
                        }
                    ]
                };
                myChart.setOption(option);
            } else {
                console.log(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            console.log(xhr.responseText || error);
        }
    });

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}

function initRefundMethodsChart() {
    const chartDom = document.getElementById('refundMethodsChart');
    const myChart = echarts.init(chartDom);
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/visual/getTaxRefundMethodPie",
        headers: {
            "Authorization": "Bearer " + token
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                const option = {
                    tooltip: {
                        trigger: 'item',
                        formatter: '{a} <br/>{b}: {c} ({d}%)'
                    },
                    legend: {
                        orient: 'vertical',
                        right: 10,
                        top: 'center',
                        data: ['Cash', 'Bank Card']
                    },
                    series: [
                        {
                            name: 'Tax Refund Methods',
                            type: 'pie',
                            radius: ['40%', '70%'],
                            avoidLabelOverlap: false,
                            itemStyle: {
                                borderRadius: 10,
                                borderColor: '#fff',
                                borderWidth: 2
                            },
                            label: {
                                show: false,
                                position: 'center'
                            },
                            emphasis: {
                                label: {
                                    show: true,
                                    fontSize: '18',
                                    fontWeight: 'bold'
                                }
                            },
                            labelLine: {
                                show: false
                            },
                            data: [
                                {value: data.data.cash, name: 'Cash', itemStyle: {color: '#FFC107'}},
                                {value: data.data.bankCard, name: 'Bank Card', itemStyle: {color: '#9C27B0'}}
                            ]
                        }
                    ]
                };
                myChart.setOption(option);
            } else {
                console.log(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            console.log(xhr.responseText || error);
        }
    });

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}

function initMixedChart() {
    const chartDom = document.getElementById('mixedChart');
    const myChart = echarts.init(chartDom);
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/visual/getMonthlyStatistic",
        headers: {
            "Authorization": "Bearer " + token
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                var d = getMonthlyData(data.data);
                const option = {
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'cross',
                            crossStyle: {
                                color: '#999'
                            }
                        }
                    },
                    legend: {
                        data: ['Applied Tax Refund Amount', 'Customs Confirmed Amount', 'Application Forms Count']
                    },
                    xAxis: [
                        {
                            type: 'category',
                            data: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                            axisPointer: {
                                type: 'shadow'
                            }
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value',
                            name: 'Amount',
                            axisLabel: {
                                formatter: '￥{value}'
                            }
                        },
                        {
                            type: 'value',
                            name: 'Application forms',
                            axisLabel: {
                                formatter: '{value}'
                            }
                        }
                    ],
                    series: [
                        {
                            name: 'Applied Tax Refund Amount',
                            type: 'bar',
                            data: d.totalAmountSum,
                            itemStyle: {
                                color: '#667eea'
                            }
                        },
                        {
                            name: 'Customs Confirmed Amount',
                            type: 'bar',
                            data: d.customsConfirmAmountSum,
                            itemStyle: {
                                color: '#764ba2'
                            }
                        },
                        {
                            name: 'Application Forms Count',
                            type: 'line',
                            yAxisIndex: 1,
                            data: d.applicationFormCount,
                            itemStyle: {
                                color: '#4CAF50'
                            },
                            lineStyle: {
                                width: 3
                            }
                        }
                    ]
                };
                myChart.setOption(option);
            } else {
                console.log(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            console.log(xhr.responseText || error);
        }
    });

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}

function getMonthlyData(data) {
    var applicationFormCount = [];
    var customsConfirmAmountSum = [];
    var totalAmountSum = [];
    for (var i = 0; i < data.length; i++) {
        applicationFormCount.push(data[i].applicationFormCount);
        customsConfirmAmountSum.push(data[i].customsConfirmAmountSum);
        totalAmountSum.push(data[i].totalAmountSum);
    }
    var obj = {
        "applicationFormCount": applicationFormCount,
        "customsConfirmAmountSum": customsConfirmAmountSum,
        "totalAmountSum": totalAmountSum
    }
    return obj;
}