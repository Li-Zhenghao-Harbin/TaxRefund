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
    $("#invoicesCount").text("1234");
    $("#invoicesCountThisMonth").text(formatTitlePrefix("12", 1));
    $("#invoicesCountThisYear").text(formatTitlePrefix("34", 2));
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
                        data: ['Approved', 'Rejected']
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
                                {value: data.data.approvedCount, name: 'Rejected', itemStyle: {color: '#F44336'}}
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
                name: 'Applications',
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
                    {value: 654, name: 'Refunded', itemStyle: {color: '#2196F3'}},
                    {value: 238, name: 'Not Refunded', itemStyle: {color: '#FF9800'}}
                ]
            }
        ]
    };

    myChart.setOption(option);

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}

function initRefundAmountChart() {
    const chartDom = document.getElementById('refundAmountChart');
    const myChart = echarts.init(chartDom);

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
                name: 'Refund Amount',
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
                        formatter: '{b}\n${c}'
                    }
                },
                labelLine: {
                    show: false
                },
                data: [
                    {value: 125430, name: 'Cash', itemStyle: {color: '#FFC107'}},
                    {value: 92890, name: 'Bank Card', itemStyle: {color: '#9C27B0'}}
                ]
            }
        ]
    };

    myChart.setOption(option);

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}

function initRefundMethodsChart() {
    const chartDom = document.getElementById('refundMethodsChart');
    const myChart = echarts.init(chartDom);

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
                name: 'Refund Methods',
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
                    {value: 587, name: 'Cash', itemStyle: {color: '#FFC107'}},
                    {value: 305, name: 'Bank Card', itemStyle: {color: '#9C27B0'}}
                ]
            }
        ]
    };

    myChart.setOption(option);

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}

function initMixedChart() {
    const chartDom = document.getElementById('mixedChart');
    const myChart = echarts.init(chartDom);

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
            data: ['Applied Refund Amount', 'Customs Confirmed Amount', 'Application Forms']
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
                min: 0,
                max: 30000,
                interval: 5000,
                axisLabel: {
                    formatter: '￥{value}'
                }
            },
            {
                type: 'value',
                name: 'Applications',
                min: 0,
                max: 120,
                interval: 20,
                axisLabel: {
                    formatter: '{value}'
                }
            }
        ],
        series: [
            {
                name: 'Applied Refund Amount',
                type: 'bar',
                data: [18500, 19200, 20100, 23400, 24900, 26400, 27200, 28450, 27600, 26300, 24800, 23100],
                itemStyle: {
                    color: '#667eea'
                }
            },
            {
                name: 'Customs Confirmed Amount',
                type: 'bar',
                data: [16200, 17500, 18400, 21000, 22300, 23800, 24500, 25120, 24200, 22800, 21500, 19800],
                itemStyle: {
                    color: '#764ba2'
                }
            },
            {
                name: 'Application Forms',
                type: 'line',
                yAxisIndex: 1,
                data: [68, 72, 76, 85, 92, 98, 102, 98, 95, 88, 82, 76],
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

    window.addEventListener('resize', function() {
        myChart.resize();
    });
}