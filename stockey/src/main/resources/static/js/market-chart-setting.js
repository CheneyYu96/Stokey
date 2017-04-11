/**
 * Created by Alan on 2016/5/24.
 */
var analysisUrl = "http://localhost:9090/analysis/", candleStickUrl = "getCandlestickLine/", macdUrl = "getMacd/",
    rsiUrl = "getRsi/", kdjUrl = "getKdj/", volumeUrl = "getVolume/", trendUrl = "getTrend/D", marketCode = "bm000300";
var candleStickReady = false, kdjReady = false, macdReady = false, rsiReady = false, volumeReady = false;
var dateList = [];
var dailyDataList = [];
var ma5List = [], ma10List = [], ma20List = [], ma30List = [];
var macdList = [];
var kdjList = [];
var rsiList = [];
var volumeList = [];
var trendDateList = [];
var trendList = [];

var app = angular.module("market", []);
app.controller('setRange', function ($scope, $http) {
    console.log("controller start!");
    //初始化echarts
    var kdjDom = document.getElementById('stock-kdjChart'), macdDom = document.getElementById('stock-macdChart'),
        rsiDom = document.getElementById('stock-rsiChart');
    var KDJchart = echarts.init(kdjDom), MACDchart = echarts.init(macdDom), RSIchart = echarts.init(rsiDom);
    KDJchart.showLoading();
    RSIchart.showLoading();
    MACDchart.showLoading();

    var defaultTimeRange = getDefaultTimeRange();
    console.log("start time: " + defaultTimeRange[0] + " end time: " + defaultTimeRange[1]);
    refresh($scope, $http, KDJchart, MACDchart, RSIchart, defaultTimeRange[0], defaultTimeRange[1]);

    $scope.refresh = function () {
        KDJchart.showLoading();
        RSIchart.showLoading();
        MACDchart.showLoading();
        console.log("Start time:" + document.getElementById("start-time").value
            + "\nEnd Time:" + document.getElementById("end-time").value);
        //选择日期范围并规范化为yyyy-MM-dd
        var startTime = configDate(document.getElementById("start-time").value);
        var endTime = configDate(document.getElementById("end-time").value);
        console.log(startTime + "   " + endTime);
        refresh($scope, $http, KDJchart, MACDchart, RSIchart, startTime, endTime);
    }
});

/**
 * 刷新图表
 * @param $scope
 * @param $http
 * @param KDJChart
 * @param MACDChart
 * @param RSIChart
 * @param startTime
 * @param endTime
 */
function refresh($scope, $http, KDJChart, MACDChart, RSIChart, startTime, endTime) {

    $http.get(analysisUrl + candleStickUrl + "D", {
        params: {
            stockCode: marketCode,
            begin: startTime,
            end: endTime
        }
    })
        .success(function (response) {
            var jsonData = JSON.parse(JSON.stringify(response));
            var dailyData = [];
            for (var i = 0; i < jsonData.length; i++) {
                dailyData = [];
                var time = jsonData[i].date;
                dateList.push(time.year + "-" + time.monthValue + "-" + time.dayOfMonth);
                dailyData.push(jsonData[i].open, jsonData[i].close, jsonData[i].high, jsonData[i].low);
                ma5List.push(jsonData[i].ma5);
                ma10List.push(jsonData[i].ma10);
                ma20List.push(jsonData[i].ma20);
                ma30List.push(jsonData[i].ma30);
                dailyDataList.push(dailyData);
            }
            candleStickReady = true;
            configTable($scope);
            configKDJChart(KDJChart);
            configMACDChart(MACDChart);
            configRSIChart(RSIChart);
        })
        .error(function () {
            swal("Oops...", "获得K线图数据失败！", "error");
        });

    //获得成交量
    $http.get(analysisUrl + volumeUrl + "D", {
        params: {
            stockCode: marketCode,
            begin: startTime,
            end: endTime
        }
    })
        .success(function (response) {
            var jsonData = JSON.parse((JSON.stringify(response)));
            for (var i = 0; i < jsonData.length; i++) {
                volumeList.push(jsonData[i].volume);
            }
            volumeReady = true;
            configKDJChart(KDJChart);
            configMACDChart(MACDChart);
            configRSIChart(RSIChart);
        })
        .error(function () {
            swal("Oops...", "获得成交量数据失败！", "error");
        });


    //获取kdj数据
    $http.get(analysisUrl + kdjUrl + "D", {
            params: {
                stockCode: marketCode,
                begin: startTime,
                end: endTime
            }
        }
    )
        .success(function (response) {
            //处理KDJ返回值
            var jsonData = JSON.parse(JSON.stringify(response));
            var kTemp = [], dTemp = [], jTemp = [];
            for (var i = 0; i < jsonData.length; i++) {
                kTemp.push(jsonData[i].kValue);
                dTemp.push(jsonData[i].dValue);
                jTemp.push(jsonData[i].jValue);
            }
            kdjList.push(kTemp);
            kdjList.push(dTemp);
            kdjList.push(jTemp);
            kdjReady = true;
            configKDJChart(KDJChart);
        })
        .error(function () {
            swal("Oops...", "获得KDJ数据失败！", "error");
        });

    //获取macd数据
    $http.get(analysisUrl + macdUrl + "D", {
            params: {
                stockCode: marketCode,
                begin: startTime,
                end: endTime
            }
        }
    )
        .success(function (response) {
            //处理MACD返回值
            var jsonData = JSON.parse(JSON.stringify(response));
            var difTemp = [], demTemp = [], oscTemp = [];
            for (var i = 0; i < jsonData.length; i++) {
                difTemp.push(jsonData[i].dif);
                demTemp.push(jsonData[i].dem);
                oscTemp.push(jsonData[i].osc);
            }
            macdList.push(difTemp);
            macdList.push(demTemp);
            macdList.push(oscTemp);
            macdReady = true;
            configMACDChart(MACDChart);
        })
        .error(function () {
            swal("Oops...", "获得MACD数据失败！", "error");
        });

    //获取rsi数据
    $http.get(analysisUrl + rsiUrl + "D", {
            params: {
                stockCode: marketCode,
                begin: startTime,
                end: endTime
            }
        }
    )
        .success(function (response) {
            //处理RSI返回值
            var jsonData = JSON.parse(JSON.stringify(response));
            var rsi6 = [], rsi12 = [], rsi24 = [];
            for (var i = 0; i < jsonData.length; i++) {
                rsi6.push(jsonData[i].rsi6);
                rsi12.push(jsonData[i].rsi12);
                rsi24.push(jsonData[i].rsi24);
            }
            rsiList.push(rsi6);
            rsiList.push(rsi12);
            rsiList.push(rsi24);
            rsiReady = true;
            configRSIChart(RSIChart);
        })
        .error(function () {
            swal("Oops...", "获得RSI数据失败！", "error");
        });
}

/**
 * 配置KDJ表格
 * @param chart
 */
function configKDJChart(chart) {
    //都加载完成再开始绘制
    if (candleStickReady && kdjReady && volumeReady) {
        option = {
            title: {
                text: 'K线图'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                padding: 35,
                data: ['kchart', 'MA5', 'MA10', 'MA20', 'MA30', 'K Value', 'D Value', 'J Value', '成交量']
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataZoom: {show: true},
                    dataView: {show: true, readOnly: false},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            grid: [{
                left: 50,
                right: 50,
                height: '50%'
            }, {
                left: 50,
                right: 50,
                top: '70%',
                height: '20%'
            }],
            dataZoom: [{
                show: true,
                realtime: true,
                start: 50,
                end: 100,
                xAxisIndex: [0, 1]
            }, {
                type: 'inside',
                realtime: true,
                start: 50,
                end: 100,
                xAxisIndex: [0, 1]
            }],
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    splitLine: {show: false},
                    axisLine: {onZero: true},
                    data: dateList
                },
                {
                    gridIndex: 1,
                    type: 'category',
                    boundaryGap: false,
                    splitLine: {show: false},
                    axisLine: {onZero: true},
                    data: dateList,
                    position: 'top'
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    scale: true,
                    boundaryGap: [0.01, 0.01],
                    splitArea: {
                        show: true,
                        areaStyle: {
                            color: ['rgba(250,250,250,0.1)', 'rgba(200,200,200,0.08)']
                        }
                    }
                },
                {
                    type: 'value',
                    gridIndex: 1,
                    splitLine: {show: false},
                    nameLocation: 'start',
                    axisLabel: {
                        formatter: function (value, index) {
                            return (value / 100000000.0) + "亿";
                        }
                    }
                },
                {
                    type: 'value',
                    gridIndex: 1
                }
            ],
            series: [
                {
                    name: 'kchart',
                    type: 'candlestick',
                    data: dailyDataList,
                    markPoint: {
                        label: {
                            normal: {
                                formatter: function (param) {
                                    return param != null ? Math.round(param.value) : '';
                                }
                            }
                        },
                        data: [
                            {
                                name: '最高值',
                                type: 'max',
                                valueDim: 'highest'
                            },
                            {
                                name: '最低值',
                                type: 'min',
                                valueDim: 'lowest'
                            },
                            {
                                name: '收盘平均价',
                                type: 'average',
                                valueDim: 'close'
                            }
                        ]
                    },
                    markLine: {
                        symbol: ['none', 'none'],
                        data: [
                            [
                                {
                                    name: '从最低到最高',
                                    type: 'min',
                                    valueDim: 'lowest',
                                    symbol: 'circle',
                                    symbolSize: 10,
                                    label: {
                                        normal: {show: false},
                                        emphasis: {show: false}
                                    }
                                },
                                {
                                    type: 'max',
                                    valueDim: 'highest',
                                    symbol: 'circle',
                                    symbolSize: 10,
                                    label: {
                                        normal: {show: false},
                                        emphasis: {show: false}
                                    }
                                }
                            ],
                            {
                                name: '收盘最低价',
                                type: 'min',
                                valueDim: 'close'
                            },
                            {
                                name: '收盘最高价',
                                type: 'max',
                                valueDim: 'close'
                            }
                        ]
                    }
                },
                {
                    name: 'MA5',
                    type: 'line',
                    data: ma5List,
                    lineStyle: {
                        normal: {opacity: 0.5}
                    }
                },
                {
                    name: 'MA10',
                    type: 'line',
                    data: ma10List,
                    lineStyle: {
                        normal: {opacity: 0.5}
                    }
                },
                {
                    name: 'MA20',
                    type: 'line',
                    data: ma20List,
                    lineStyle: {
                        normal: {opacity: 0.5}
                    }
                },
                {
                    name: 'MA30',
                    type: 'line',
                    data: ma30List,
                    lineStyle: {
                        normal: {opacity: 0.5}
                    }
                },
                {
                    name: 'K Value',
                    type: 'line',
                    data: kdjList[0],
                    xAxisIndex: 1,
                    yAxisIndex: 2
                },
                {
                    name: 'D Value',
                    type: 'line',
                    data: kdjList[1],
                    xAxisIndex: 1,
                    yAxisIndex: 2
                },
                {
                    name: 'J Value',
                    type: 'line',
                    data: kdjList[2],
                    xAxisIndex: 1,
                    yAxisIndex: 2
                },
                {
                    name: '成交量',
                    type: 'bar',
                    data: volumeList,
                    xAxisIndex: 1,
                    yAxisIndex: 1,
                    itemStyle: {
                        normal: {opacity: 0.3}
                    }
                }
            ]
        };

        chart.hideLoading();
        chart.setOption(option);
        window.onresize = function () {
            chart.resize();
        };
        initData();
    }
}

/**
 * 配置MACD表格
 * @param chart
 */
function configMACDChart(chart) {
    //都加载完成再开始绘制
    if (candleStickReady && macdReady && volumeReady) {
        option = {
            title: {
                text: 'K线图'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                padding: 35,
                data: ['kchart', 'MA5', 'MA10', 'MA20', 'MA30', 'DIF', 'DEF', 'OSC', '成交量']
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataZoom: {show: true},
                    dataView: {show: true, readOnly: false},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            grid: [{
                left: 50,
                right: 50,
                height: '50%'
            }, {
                left: 50,
                right: 50,
                top: '70%',
                height: '20%'
            }],
            dataZoom: [{
                show: true,
                realtime: true,
                start: 50,
                end: 100,
                xAxisIndex: [0, 1]
            }, {
                type: 'inside',
                realtime: true,
                start: 50,
                end: 100,
                xAxisIndex: [0, 1]
            }],
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    axisLine: {onZero: true},
                    data: dateList
                },
                {
                    gridIndex: 1,
                    type: 'category',
                    boundaryGap: false,
                    axisLine: {onZero: true},
                    data: dateList,
                    position: 'top'
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    scale: true,
                    boundaryGap: [0.01, 0.01]
                },
                {
                    type: 'value',
                    gridIndex: 1,
                    splitLine: {show: false},
                    nameLocation: 'start',
                    axisLabel: {
                        formatter: function (value, index) {
                            return (value / 10000.0) + "万";
                        }
                    }
                },
                {
                    type: 'value',
                    gridIndex: 1
                }
            ],
            series: [
                {
                    name: 'kchart',
                    type: 'candlestick',
                    data: dailyDataList,
                    markPoint: {
                        label: {
                            normal: {
                                formatter: function (param) {
                                    return param != null ? Math.round(param.value) : '';
                                }
                            }
                        },
                        data: [
                            {
                                name: '最高值',
                                type: 'max',
                                valueDim: 'highest'
                            },
                            {
                                name: '最低值',
                                type: 'min',
                                valueDim: 'lowest'
                            },
                            {
                                name: '收盘平均价',
                                type: 'average',
                                valueDim: 'close'
                            }
                        ]
                    },
                    markLine: {
                        symbol: ['none', 'none'],
                        data: [
                            [
                                {
                                    name: '从最低到最高',
                                    type: 'min',
                                    valueDim: 'lowest',
                                    symbol: 'circle',
                                    symbolSize: 10,
                                    label: {
                                        normal: {show: false},
                                        emphasis: {show: false}
                                    }
                                },
                                {
                                    type: 'max',
                                    valueDim: 'highest',
                                    symbol: 'circle',
                                    symbolSize: 10,
                                    label: {
                                        normal: {show: false},
                                        emphasis: {show: false}
                                    }
                                }
                            ],
                            {
                                name: '收盘最低价',
                                type: 'min',
                                valueDim: 'close'
                            },
                            {
                                name: '收盘最高价',
                                type: 'max',
                                valueDim: 'close'
                            }
                        ]
                    }
                },
                {
                    name: 'MA5',
                    type: 'line',
                    data: ma5List
                },
                {
                    name: 'MA10',
                    type: 'line',
                    data: ma10List
                },
                {
                    name: 'MA20',
                    type: 'line',
                    data: ma20List
                },
                {
                    name: 'MA30',
                    type: 'line',
                    data: ma30List
                },
                {
                    name: 'DIF',
                    type: 'line',
                    data: macdList[0],
                    xAxisIndex: 1,
                    yAxisIndex: 2
                },
                {
                    name: 'DEF',
                    type: 'line',
                    data: macdList[1],
                    xAxisIndex: 1,
                    yAxisIndex: 2
                },
                {
                    name: 'OSC',
                    type: 'line',
                    data: macdList[2],
                    xAxisIndex: 1,
                    yAxisIndex: 2
                },
                {
                    name: '成交量',
                    type: 'bar',
                    data: volumeList,
                    xAxisIndex: 1,
                    yAxisIndex: 1,
                    itemStyle: {
                        normal: {opacity: 0.3}
                    }
                }
            ]
        };

        chart.hideLoading();
        chart.setOption(option);
        window.onresize = function () {
            chart.resize();
        };
        initData();
    }
}

/**
 * 配置RSI表格
 * @param chart
 */
function configRSIChart(chart) {
    //都加载完成再开始绘制
    if (candleStickReady && rsiReady && volumeReady) {
        option = {
            title: {
                text: 'K线图'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                padding: 35,
                data: ['kchart', 'MA5', 'MA10', 'MA20', 'MA30', 'RSI 6', 'RSI 12', 'RSI 24', '成交量']
            },
            toolbox: {
                show: true,
                feature: {
                    mark: {show: true},
                    dataZoom: {show: true},
                    dataView: {show: true, readOnly: false},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            grid: [{
                left: 50,
                right: 50,
                height: '50%'
            }, {
                left: 50,
                right: 50,
                top: '70%',
                height: '20%'
            }],
            dataZoom: [{
                show: true,
                realtime: true,
                start: 50,
                end: 100,
                xAxisIndex: [0, 1]
            }, {
                type: 'inside',
                realtime: true,
                start: 50,
                end: 100,
                xAxisIndex: [0, 1]
            }],
            xAxis: [
                {
                    type: 'category',
                    boundaryGap: false,
                    axisLine: {onZero: true},
                    data: dateList
                },
                {
                    gridIndex: 1,
                    type: 'category',
                    boundaryGap: false,
                    axisLine: {onZero: true},
                    data: dateList,
                    position: 'top'
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    scale: true,
                    boundaryGap: [0.01, 0.01]
                },
                {
                    type: 'value',
                    gridIndex: 1,
                    splitLine: {show: false},
                    nameLocation: 'start',
                    axisLabel: {
                        formatter: function (value, index) {
                            return (value / 10000.0) + "万";
                        }
                    }
                },
                {
                    type: 'value',
                    gridIndex: 1
                }
            ],
            series: [
                {
                    name: 'kchart',
                    type: 'k',
                    data: dailyDataList
                },
                {
                    name: 'MA5',
                    type: 'line',
                    data: ma5List
                },
                {
                    name: 'MA10',
                    type: 'line',
                    data: ma10List
                },
                {
                    name: 'MA20',
                    type: 'line',
                    data: ma20List
                },
                {
                    name: 'MA30',
                    type: 'line',
                    data: ma30List
                },
                {
                    name: 'RSI 6',
                    type: 'line',
                    data: kdjList[0],
                    xAxisIndex: 1,
                    yAxisIndex: 1
                },
                {
                    name: 'RSI 12',
                    type: 'line',
                    data: kdjList[1],
                    xAxisIndex: 1,
                    yAxisIndex: 2
                },
                {
                    name: 'RSI 24',
                    type: 'line',
                    data: kdjList[2],
                    xAxisIndex: 1,
                    yAxisIndex: 2
                },
                {
                    name: '成交量',
                    type: 'bar',
                    data: volumeList,
                    xAxisIndex: 1,
                    yAxisIndex: 1,
                    itemStyle: {
                        normal: {opacity: 0.3}
                    }
                }
            ]
        };

        chart.hideLoading();
        chart.setOption(option);
        window.onresize = function () {
            chart.resize();
        };
        initData();
    }
}

/**
 * 配置表格
 * @param $scope
 */
function configTable($scope) {
    $scope.stock_data = [];
    var i = 0;
    for (i = 0; i < dateList.length; i++) {
        var daily_data = new Map();
        daily_data.put("date", dateList[i]);
        daily_data.put("open", dailyDataList[i][0]);
        daily_data.put("close", dailyDataList[i][1]);
        daily_data.put("high", dailyDataList[i][2]);
        daily_data.put("low", dailyDataList[i][3]);
        daily_data.put("ma5", ma5List[i]);
        $scope.stock_data.push(daily_data.container);
    }
}

/**
 * 走势图前置条件
 */
function preTrendChart() {
    var dom = document.getElementById('stock-trendChart');
    var trendChart = echarts.init(dom);
    trendChart.showLoading();

    var defaultTimeRange = getDefaultTimeRange();
    $.get(analysisUrl + trendUrl, {
        stockCode: marketCode,
        begin: defaultTimeRange[0],
        end: defaultTimeRange[1]
    }, function (result) {
        for(var i=0;i<result.length;i++){
            var date = result[i].date;
            trendList.push(result[i].y);
            trendDateList.push(date.year+"-"+date.monthValue+"-"+date.dayOfMonth);
        }
        configTrendChart(trendChart);
    })
}

/**
 * 配置走势图
 */
function configTrendChart(chart){
    option = {
        title: {
            text: '3个月走势图'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            padding: 35,
            data: ['走势']
        },
        toolbox: {
            show: true,
            feature: {
                mark: {show: true},
                dataZoom: {show: true},
                restore: {show: true},
                saveAsImage: {show: true},
                magicType: {type: ['line', 'bar']}
            }
        },
        grid: [{
            left: 50,
            right: 50,
            height: '75%'
        }],
        dataZoom: [{
            show: true,
            realtime: true,
            start: 50,
            end: 100
        }],
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                axisLine: {onZero: true},
                data: trendDateList
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                boundaryGap: [0.01, 0.01]
            }
        ],
        series: [
            {
                name: '走势',
                type: 'line',
                data: trendList
            }
        ]
    };

    chart.hideLoading();
    chart.setOption(option);
    window.onresize = function () {
        chart.resize();
    };
    trendDateList = [];
    trendList = [];
}

/**
 * 更改日期格式为yyyy-MM-dd
 * @param time
 */
function configDate(time) {
    return amplify(time.split("/")[2]) + "-" + amplify(time.split("/")[0]) + "-" + amplify(time.split("/")[1]);
}

/**
 * 补全
 * @param integer
 * @returns {*}
 */
function amplify(integer) {
    if (integer.length == 1 || integer < 10) {
        integer = "0" + integer;
    }
    return integer;
}

/**
 * 初始化
 */
function initData() {
    if (candleStickReady && kdjReady && macdReady && rsiReady && volumeReady) {
        candleStickReady = false;
        kdjReady = false;
        macdReady = false;
        rsiReady = false;
        volumeReady = false;
        dateList = [];
        dailyDataList = [];
        ma5List = [];
        ma10List = [];
        ma20List = [];
        ma30List = [];
        kdjList = [];
        macdList = [];
        rsiList = [];
        volumeList = [];
    }
}

/**
 * 默认设置最近三个月的时间
 * @returns {Array}
 */
function getDefaultTimeRange() {
    var currentDate = new Date();
    var lastMonth = (currentDate.getMonth() - 2) % 12;
    var lastDate = currentDate.getDate();
    var lastYear = currentDate.getFullYear();
    if (lastMonth == 0) {
        lastYear -= 1;
        lastMonth = 12;
    }
    if (lastDate >= 29) {
        if (lastMonth == 2) {
            lastDate = 28;
        } else {
            lastDate = 30;
        }
    }
    var dateRange = [];
    dateRange.push(lastYear + "-" + amplify(lastMonth) + "-" + amplify(lastDate));
    dateRange.push(currentDate.getFullYear() + "-"
        + amplify(currentDate.getMonth() + 1) + "-"
        + amplify(currentDate.getDate()));
    return dateRange;
}

function Map() {
    this.container = new Object();
}


Map.prototype.put = function (key, value) {
    this.container[key] = value;
};

$('#table-button').click(function () {
    $('#stock-kdjChart').hide(300);
    $('#stock-macdChart').hide(300);
    $('#stock-rsiChart').hide(300);
    $('#stock-trendChart').hide(300);
    $('#table').show(300);
});

$('#kdj-button').click(function () {
    $('#table').hide(300);
    $('#stock-trendChart').fadeOut(300);
    $('#stock-macdChart').fadeOut(300);
    $('#stock-rsiChart').fadeOut(300);
    $('#stock-kdjChart').fadeIn(300);
});

$('#macd-button').click(function () {
    $('#table').hide(300);
    $('#stock-trendChart').fadeOut(300);
    $('#stock-rsiChart').fadeOut(300);
    $('#stock-kdjChart').fadeOut(300);
    $('#stock-macdChart').fadeIn(300);
});

$('#rsi-button').click(function () {
    $('#table').hide(300);
    $('#stock-macdChart').fadeOut(300);
    $('#stock-kdjChart').fadeOut(300);
    $('#stock-trendChart').fadeOut(300);
    $('#stock-rsiChart').fadeIn(300);
});

$('#trend-button').click(function () {
    $('#table').hide(300);
    $('#stock-macdChart').fadeOut(300);
    $('#stock-rsiChart').fadeOut(300);
    $('#stock-kdjChart').fadeOut(300);
    $('#stock-trendChart').fadeIn(300);
    preTrendChart();
});