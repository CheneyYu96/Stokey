/**
 * Created by Alan on 2016/6/17.
 */
var localhostUrl = "http://localhost:9090/", candleStickUrl = localhostUrl + "analysis/getCandlestickLine/D",
    selectionUrl = localhostUrl + "user/getSelectionsByUser", ziplineUrl = localhostUrl + "strategy/recipe/run";
var dateList = [];
var dailyDataList = [];
var ma5List = [], ma10List = [], ma20List = [], ma30List = [];
var stockList = new Map();
var portfolioList = [];
var schemeDateList = [];
var levarageList = [];

/**
 * 添加自选股列表
 */
$(document).ready(function () {
    if (window.localStorage.getItem("stockey_login") != "true") {
        //如果没有登录就直接返回，界面上面默认写的是请登录
        //return;
    }
    $('#selections-rows').hide();
    $('#scheme-main').show();
    $.get(selectionUrl, {
        storeID: window.localStorage.getItem("storeID")
    }, function (result) {
        var stockSelect = document.getElementById("stock-selection");
        for (var i = 0; i < result.length; i++) {
            var option = document.createElement("option");
            option.innerHTML = result[i].name + " " + result[i].stockCode;
            stockSelect.appendChild(option);
        }
        updateChart();
    });
});

/**
 * 近期图表设置
 */
function updateChart() {
    if ($('#stock-selection').val() != null) {
        var recentChart = echarts.init(document.getElementById("recent-chart"));
        recentChart.showLoading();
        $.get(candleStickUrl, {
            stockCode: $('#stock-selection').val().split(" ")[1],
            begin: configDate(getStartTime()),
            end: configDate(getEndTime())
        }, function (result) {
            var jsonData = JSON.parse(JSON.stringify(result));
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
            configChart(recentChart);
        });
    }
}

/**
 * 配置图表
 * @param chart
 */
function configChart(chart) {
    option = {
        title: {
            text: ''
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            padding: 35,
            data: ['kchart', 'MA5', 'MA10', 'MA20', 'MA30']
        },
        grid: {
            left: 50,
            right: 50,
            height: '65%'
        },
        dataZoom: {
            show: true,
            realtime: true,
            start: 75,
            end: 100
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                axisLine: {onZero: true},
                data: dateList
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

/**
 * 清空数据
 */
function initData() {
    dateList = [];
    dailyDataList = [];
    ma5List = [];
    ma10List = [];
    ma20List = [];
    ma30List = [];
}

/**
 * 设置区域切换
 */
$('#basic-button').click(function (event) {
    $('#basic-setting').show(300);
    $('#if-setting').hide(300);
    $('#advance-setting').hide(300);
});

$('#if-button').click(function (event) {
    $('#basic-setting').hide(300);
    $('#if-setting').show(300);
    $('#advance-setting').hide(300);
});

$('#advance-button').click(function (event) {
    $('#basic-setting').hide(300);
    $('#if-setting').hide(300);
    $('#advance-setting').show(300);
});


/**
 * 保存滑动条件
 */
function saveSlideCondition() {
    //获取界面数据
    var relation = $('#price-select').val().split(" ")[1];
    var open = $('#price-if-open').val();
    var close = $('#price-if-close').val();
    var thenParam = $('#price-then-scheme-item').html().split(" ")[1];
    var thenValue = $('#price-scheme-input').val();

    //向Map中添加数据
    var priceCondition = new Map();
    //这里stockCondition是一个Map
    priceCondition.put("relation", relation);
    priceCondition.put("short", open);
    priceCondition.put("long", close);
    priceCondition.put("thenParam", thenParam);
    priceCondition.put("thenValue", thenValue);
    stockList.put("priceCondition", priceCondition);

    //界面展示
    var conditionTable = document.getElementById("condition-table");
    var tbody = document.createElement("tbody");
    var tr = document.createElement('tr');
    var td = document.createElement('td');
    var titleDiv = document.createElement('div');
    titleDiv.setAttribute('style', 'float: left;font-size: 18px;font-weight: bold');
    titleDiv.innerHTML = "股价滑动条件";
    var relationDiv = document.createElement('div');
    relationDiv.setAttribute('style', 'float: right;font-size: 18px;font-weight: bold');
    relationDiv.innerHTML = relation;
    var clearDiv = document.createElement('div');
    clearDiv.setAttribute('class', 'w-clearfix');
    var ifh5 = document.createElement('h5');
    ifh5.innerHTML = "IF";
    var openDiv = document.createElement('div');
    openDiv.innerHTML = "短期滑动天数: " + open;
    var closeDiv = document.createElement('div');
    closeDiv.innerHTML = "长期滑动天数: " + close;
    var thenh5 = document.createElement('h5');
    thenh5.innerHTML = "THEN";
    var thenDiv = document.createElement('div');
    thenDiv.innerHTML = thenParam + ": " + thenValue + "股";

    td.appendChild(titleDiv);
    td.appendChild(relationDiv);
    td.appendChild(clearDiv);
    td.appendChild(ifh5);
    td.appendChild(openDiv);
    td.appendChild(closeDiv);
    td.appendChild(thenh5);
    td.appendChild(thenDiv);
    tr.appendChild(td);
    tbody.appendChild(tr);
    conditionTable.appendChild(tbody);

    $('#price-ifthen-wrapper').hide(300);
    console.log("添加了slide条件");
    console.log(stockList);
}

/**
 * 保存上升条件
 */
function saveUpCondition() {
    //获取界面数据
    var relation = $('#up-select').val().split(" ")[1];
    var upDays = $('#up-if-day').val();
    var thenParam = $('#up-then-scheme-item').html().split(" ")[1];
    var thenValue = $('#up-scheme-input').val();

    //向Map中添加数据
    var upCondition = new Map();
    upCondition.put("relation", relation);
    upCondition.put("up_days", upDays);
    upCondition.put("thenParam", thenParam);
    upCondition.put("thenValue", thenValue);
    stockList.put("upCondition", upCondition);

    //界面展示
    var conditionTable = document.getElementById("condition-table");
    var tbody = document.createElement("tbody");
    var tr = document.createElement('tr');
    var td = document.createElement('td');
    var titleDiv = document.createElement('div');
    titleDiv.setAttribute('style', 'float: left;font-size: 18px;font-weight: bold');
    titleDiv.innerHTML = "股价连续上升条件";
    var relationDiv = document.createElement('div');
    relationDiv.setAttribute('style', 'float: right;font-size: 18px;font-weight: bold');
    relationDiv.innerHTML = relation;
    var clearDiv = document.createElement('div');
    clearDiv.setAttribute('class', 'w-clearfix');
    var ifh5 = document.createElement('h5');
    ifh5.innerHTML = "IF";
    var openDiv = document.createElement('div');
    openDiv.innerHTML = "连续上升天数: " + upDays + "天";
    var closeDiv = document.createElement('div');
    var thenh5 = document.createElement('h5');
    thenh5.innerHTML = "THEN";
    var thenDiv = document.createElement('div');
    thenDiv.innerHTML = thenParam + ": " + thenValue + "股";

    td.appendChild(titleDiv);
    td.appendChild(relationDiv);
    td.appendChild(clearDiv);
    td.appendChild(ifh5);
    td.appendChild(openDiv);
    td.appendChild(thenh5);
    td.appendChild(thenDiv);
    tr.appendChild(td);
    tbody.appendChild(tr);
    conditionTable.appendChild(tbody);

    $('#up-ifthen-wrapper').hide(300);
    console.log("添加了up条件");
    console.log(stockList);
}

/**
 * 保存下降条件
 */
function saveDownCondition() {
//获取界面数据
    var relation = $('#down-select').val().split(" ")[1];
    var downDays = $('#down-if-day').val();
    var thenParam = $('#down-then-scheme-item').html().split(" ")[1];
    var thenValue = $('#down-scheme-input').val();

    //向Map中添加数据
    var downCondition = new Map();
    //这里stockCondition是一个Map
    downCondition.put("relation", relation);
    downCondition.put("down_days", downDays);
    downCondition.put("thenParam", thenParam);
    downCondition.put("thenValue", thenValue);
    stockList.put("downCondition", downCondition);

    //界面展示
    var conditionTable = document.getElementById("condition-table");
    var tbody = document.createElement("tbody");
    var tr = document.createElement('tr');
    var td = document.createElement('td');
    var titleDiv = document.createElement('div');
    titleDiv.setAttribute('style', 'float: left;font-size: 18px;font-weight: bold');
    titleDiv.innerHTML = "股价连续下降条件";
    var relationDiv = document.createElement('div');
    relationDiv.setAttribute('style', 'float: right;font-size: 18px;font-weight: bold');
    relationDiv.innerHTML = relation;
    var clearDiv = document.createElement('div');
    clearDiv.setAttribute('class', 'w-clearfix');
    var ifh5 = document.createElement('h5');
    ifh5.innerHTML = "IF";
    var openDiv = document.createElement('div');
    openDiv.innerHTML = "连续下降天数: " + downDays + "天";
    var thenh5 = document.createElement('h5');
    thenh5.innerHTML = "THEN";
    var thenDiv = document.createElement('div');
    thenDiv.innerHTML = thenParam + ": " + thenValue + "股";

    td.appendChild(titleDiv);
    td.appendChild(relationDiv);
    td.appendChild(clearDiv);
    td.appendChild(ifh5);
    td.appendChild(openDiv);
    td.appendChild(thenh5);
    td.appendChild(thenDiv);
    tr.appendChild(td);
    tbody.appendChild(tr);
    conditionTable.appendChild(tbody);

    $('#down-ifthen-wrapper').hide(300);
    console.log("添加了down条件");
    console.log(stockList);
}

/**
 * 绑定主界面其他按钮
 */
$('#price-cancel').click(function () {
    $('#price-ifthen-wrapper').hide(300);
});
$('#price-condition-button').click(function () {
    $('#price-ifthen-wrapper').show(300);
});
$('#up-condition-button').click(function () {
    $('#up-ifthen-wrapper').show(300);
});
$('#up-cancel').click(function () {
    $('#up-ifthen-wrapper').hide(300);
});
$('#down-condition-button').click(function () {
    $('#down-ifthen-wrapper').show(300);
});
$('#down-cancel').click(function () {
    $('#down-ifthen-wrapper').hide(300);
});

function setOptions() {
    $('#advance-setting').hide(300);
    $('#basic-setting').hide(300);
    $('#if-setting').show(300);
}
function advance() {
    $('#basic-setting').hide(300);
    $('#advance-setting').show(300);
}

/**
 * 绑定指标界面按钮
 */
$('#price-then-buy').click(function () {
    $('#price-then-scheme').show(300);
    $('#price-then-scheme-title').html('买入指标设置');
    $('#price-then-scheme-item').html('买入股数 BUY');
});

$('#price-then-sell').click(function () {
    $('#price-then-scheme').show(300);
    $('#price-then-scheme-title').html('卖出指标设置');
    $('#price-then-scheme-item').html('卖出股数 SELL');
});
$('#up-then-buy').click(function () {
    $('#up-then-scheme').show(300);
    $('#up-then-scheme-title').html('买入指标设置');
    $('#up-then-scheme-item').html('买入股数 BUY');
});

$('#up-then-sell').click(function () {
    $('#up-then-scheme').show(300);
    $('#up-then-scheme-title').html('卖出指标设置');
    $('#up-then-scheme-item').html('卖出股数 SELL');
});
$('#down-then-buy').click(function () {
    $('#down-then-scheme').show(300);
    $('#down-then-scheme-title').html('买入指标设置');
    $('#down-then-scheme-item').html('买入股数 BUY');
});

$('#down-then-sell').click(function () {
    $('#down-then-scheme').show(300);
    $('#down-then-scheme-title').html('卖出指标设置');
    $('#down-then-scheme-item').html('卖出股数 SELL');
});

/**
 * 添加股票
 */
function addStock() {
    //在界面添加表格内容
    var stockTable = document.getElementById("stock-table");
    var rawStock = $('#stock-selection').val();
    var stockCode = rawStock.split(" ")[1];
    var stockName = rawStock.split(" ")[0];
    var tr = document.createElement("tr");
    var td = document.createElement("td");
    var tbody = document.createElement("tbody");
    td.setAttribute("id", stockCode);

    td.setAttribute("style", "cursor: pointer");
    var stockDeleteDiv = document.createElement("div");
    stockDeleteDiv.setAttribute("style", "float: right;font-size: 30px;padding-left: 10px");
    var stockDeleteA = document.createElement("a");
    stockDeleteA.setAttribute("style", "cursor:pointer;text-decoration: none");
    stockDeleteA.setAttribute("onclick", "deleteStock('" + stockCode + "')");
    stockDeleteA.innerHTML = "×";
    stockDeleteDiv.appendChild(stockDeleteA);
    var stockInfoDiv = document.createElement("div");
    stockInfoDiv.setAttribute("style", "float: left;");
    var stockInfoA = document.createElement("a");
    stockInfoA.setAttribute("style", "font-weight: bold;text-decoration:none;font-size: 18px");
    //设置股票名
    stockInfoA.innerHTML = stockName + "<br>";
    stockInfoDiv.appendChild(stockInfoA);
    stockInfoDiv.innerHTML += stockCode;
    var stockSettingDiv = document.createElement("div");
    stockSettingDiv.setAttribute("style", "float: right;font-size: 16px");
    var startFund = $('#start-fund').val();
    if (startFund == "") {
        startFund = 100000;
    }
    stockSettingDiv.innerHTML =
        startFund + "元<br>" + configDate(getStartTime()) + " ~ " + configDate(getEndTime());

    td.appendChild(stockDeleteDiv);
    td.appendChild(stockInfoDiv);
    td.appendChild(stockSettingDiv);
    tr.appendChild(td);
    tbody.appendChild(tr);
    stockTable.appendChild(tbody);

    //向map添加内容
    var stockCondition = new Map();
    stockCondition.put('stockCode', stockCode);
    stockCondition.put("begin", configDate(getStartTime()));
    stockCondition.put("end", configDate(getEndTime()));
    stockCondition.put("startFund", startFund);
    stockList.put("stockInfo", stockCondition);
}

/**
 * 删除股票
 * @param stockCode
 */
function deleteStock(stockCode) {
    $('#stock-table').find('tr').each(function () {
        if ($(this).children('td').attr("id") == stockCode) {
            $(this).remove();
            stockList.remove(stockCode);
        }
    });
}

/**
 * 说明书
 */
function openInstruction() {
    window.open("./scheme_instruction.html");
}

/**
 * 运行策略
 */
function startScheme() {
    $('#load').show();
    $('#result').show();
    $.get("./maps/scheme_stub.json", function (result) {
        $('#load').hide();
        for (var item in result) {
            schemeDateList.push(item.split("T")[0]);
            portfolioList.push(item.portfolio_value);
        }
    });
    var request = {
        begin_date: stockInfo.get("begin"),
        end_date: stockInfo.get("end"),
        capital: stockInfo.get("startFund"),
        code: $('#scheme-code').val()
    };
    var requestJson = $.toJSON(request);
    console.log(requestJson);
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": ziplineUrl,
        "method": "POST",
        "headers": {
            "content-type": "application/json",
            "cache-control": "no-cache"
        },
        "processData": false,
        "data": requestJson
    };
    $.ajax(settings).done(function (result) {
        result = JSON.parse(result);
        for (var item in result) {
            schemeDateList.push(item.split("T")[0]);
            var info = result[item];
            portfolioList.push(info.portfolio_value);
            var levarageValue = info.gross_leverage;
            levarageList.push(levarageValue);
        }
        $('#load').hide();
        //console.log(schemeDateList);
        //console.log(levarageList);
        configSchemeChart();
    });
}

/**
 * 运行进阶策略
 */
function startAdvancedScheme() {
    $('#load').show();
    $('#result').show();

    var stockInfo = stockList.get("stockInfo");
    console.log(stockInfo);
    var request = {
        begin_date: stockInfo.get("begin"),
        end_date: stockInfo.get("end"),
        capital: stockInfo.get("startFund"),
        code: $('#scheme-code').val()
    };
    var requestJson = $.toJSON(request);
    console.log(requestJson);
    var settings = {
        "async": true,
        "crossDomain": true,
        "url": ziplineUrl,
        "method": "POST",
        "headers": {
            "content-type": "application/json",
            "cache-control": "no-cache"
        },
        "processData": false,
        "data": requestJson
    };
    $.ajax(settings).done(function (result) {
        result = JSON.parse(result);
        for (var item in result) {
            schemeDateList.push(item.split("T")[0]);
            var info = result[item];
            portfolioList.push(info.portfolio_value);
            var levarageValue = info.gross_leverage;
            levarageList.push(levarageValue);
        }
        $('#load').hide();
        //console.log(schemeDateList);
        //console.log(levarageList);
        configSchemeChart();
    });
}

/**
 * 配置策略图表
 */
function configSchemeChart() {
    var dom = document.getElementById('result-chart');
    var chart = echarts.init(dom);

    option = {
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            feature: {
                magicType: {show: true, type: ['line', 'bar']},
                restore: {show: true},
                saveAsImage: {show: true}
            }
        },
        legend: {
            data: ['总资金', '影响力']
        },
        xAxis: [
            {
                type: 'category',
                scale: true,
                data: schemeDateList
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                name: '总资金'
            },
            {
                type: 'value',
                scale: true,
                name: '影响力'
            }
        ],
        dataZoom: {
            show: true,
            realtime: true,
            start: 50,
            end: 100
        },
        series: [
            {
                name: '总资金',
                type: 'bar',
                data: portfolioList
            },
            {
                name: '影响力',
                type: 'line',
                data: levarageList,
                yAxisIndex: 1
            }
        ]
    };

    chart.hideLoading();
    chart.setOption(option);
    window.onresize = function () {
        chart.resize();
    };
    levarageList = [];
    schemeDateList = [];
    portfolioList = [];
}

function Map() {
    this.container = new Object();
}

Map.prototype.put = function (key, value) {
    this.container[key] = value;
};

Map.prototype.remove = function (key) {
    delete this.container[key];
};

Map.prototype.get = function (key) {
    return this.container[key];
};


/**
 * 按钮交互
 */
$(".button-fill").hover(function () {
    $(this).children(".button-inside").addClass('full');
}, function () {
    $(this).children(".button-inside").removeClass('full');
});

/**
 * 更改日期格式为yyyy-MM-dd
 * @param time
 */
function configDate(time) {
    return (amplify(time.split("/")[2]) + "-" + amplify(time.split("/")[0]) + "-" + amplify(time.split("/")[1]));
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

function getEvent() {
    if (document.all) {
        return window.event; //如果是ie
    }
    func = getEvent.caller;
    while (func != null) {
        var arg0 = func.arguments[0];
        if (arg0) {
            if ((arg0.constructor == Event || arg0.constructor == MouseEvent) || (typeof(arg0) == "object" && arg0.preventDefault && arg0.stopPropagation)) {
                return arg0;
            }
        }
        func = func.caller;
    }
    return null;
}

function stopevt() {
    var ev = getEvent();
    if (ev.stopPropagation) {
        ev.stopPropagation();
    } else if (window.ev) {
        window.ev.cancelBubble = true;
    }
}
