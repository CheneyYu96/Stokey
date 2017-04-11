/**
 * Created by Alan on 2016/6/17.
 */
var localhostUrl = "http://localhost:9090/";
var getSelectionsUrl = localhostUrl + "user/getSelectionsByUser";
var collectUrl = localhostUrl + "user/selection/add";
var uncollectUrl = localhostUrl + "user/selection/delete";
var trendUrl = localhostUrl + "analysis/getTrend/D";
var commentUrl = localhostUrl + "analysis/getComment";
var newsUrl = localhostUrl + "stock/news/get";
var recommendUrl = localhostUrl + "stock/recommend/moreStock";
var trendDateList = [];
var trendList = [];

//配置传入参数
var stockName = getParam(window.location.href, "stockName");
var stockCode = getParam(window.location.href, "stockCode");
var stockMarket = getParam(window.location.href, "stockMarket");
var stockPrice = getParam(window.location.href, "stockPrice");
var isSelected = getParam(window.location.href, "isSelected");
stockName = decodeURIComponent(stockName);
stockMarket = decodeURIComponent(stockMarket);
stockPrice = decodeURIComponent(stockPrice);
$(document).ready(function () {
    $(document).attr("title", stockName);
    $('#title').html(stockName);
    $('#price').html(stockPrice);
    $('#market').html(stockMarket);
//如果已经收藏就显示“已收藏”
    if (isSelected == "true") {
        $('.like-toggle').toggleClass('like-active');
        $('.like-toggle').next().toggleClass('hidden');
    }
    configNewsList();
});

//收藏按钮逻辑
$(function () {
    $('.like-toggle').click(function () {
        if ($(this).hasClass('like-active')) {
            $.get(uncollectUrl, {
                    emailOrID: window.localStorage.getItem("username"),
                    stockCode: stockCode
                }, function (result) {
                    if (result.success == true) {
                        $('.like-toggle').toggleClass('like-active');
                        $('.like-toggle').next().toggleClass('hidden');
                    }
                }
            )
        }
        else {
            $.ajax({
                url: collectUrl,
                type: "GET",
                data: {
                    emailOrID: window.localStorage.getItem("username"),
                    stockCode: stockCode
                },
                success: function (result, textStatus, xhr) {//配置数据
                    if (result.success == true) {
                        $(".like-toggle").toggleClass('like-active');
                        $(".like-toggle").next().toggleClass('hidden');
                    }
                }
            });
        }
    });
});

/**
 * 走势图前置条件
 */
function preTrendChart() {
    var dom = document.getElementById('stock-trendChart');
    var trendChart = echarts.init(dom);
    trendChart.showLoading();

    var defaultTimeRange = getDefaultTimeRange();
    $.get(trendUrl, {
        stockCode: stockCode,
        begin: defaultTimeRange[0],
        end: defaultTimeRange[1]
    }, function (result) {
        for (var i = 0; i < result.length; i++) {
            var date = result[i].date;
            trendList.push(result[i].y);
            trendDateList.push(date.year + "-" + date.monthValue + "-" + date.dayOfMonth);
        }
        configTrendChart(trendChart);
    })
}

/**
 * 配置走势图
 */
function configTrendChart(chart) {
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

function configNewsList() {
    var ul = document.getElementById('news');
    $.get(newsUrl, {
        stockCode: stockCode
    }, function (result) {
        for (var i = 0; i < result.length; i++) {
            var date = result[i].time.substr(1, 5);
            var title = result[i].brief;
            var link = result[i].link;

            var li = document.createElement('li');
            li.setAttribute('onclick', "myopen('" + link + "')");
            var div = document.createElement('div');
            var h6 = document.createElement('h6');
            h6.innerHTML = date;
            var h5 = document.createElement('h5');
            h5.innerHTML = title;
            div.appendChild(h6);
            div.appendChild(h5);
            li.appendChild(div);
            ul.appendChild(li);
        }
    });
}

/**
 * 打开网页
 * @param url
 */
function myopen(url) {
    window.open(url);
}

/**
 * 关闭新闻
 */
function closeNews() {
    $('#news-wrapper').hide(300);
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

$.get(commentUrl, {
    stockCode: stockCode
}, function (result) {
    $('#comment').html(result);
});

$('#trend-button').click(function () {
    $('#table').hide(300);
    $('#stock-macdChart').fadeOut(300);
    $('#stock-rsiChart').fadeOut(300);
    $('#stock-kdjChart').fadeOut(300);
    $('#stock-recommend').fadeOut(300);
    $('#stock-trendChart').fadeIn(300);
    preTrendChart();
});

/**
 * 股票推荐按钮
 */
$('#recommend-button').click(function () {
    $('#load').show();
    $('#table').hide(300);
    $('#stock-macdChart').fadeOut(300);
    $('#stock-rsiChart').fadeOut(300);
    $('#stock-kdjChart').fadeOut(300);
    $('#stock-trendChart').fadeOut(300);
    $('#stock-recommend').fadeIn(300);

    loadRecommendations();
});

$('#news-button').click(function () {
    $('#news-wrapper').show(300);
});

/**
 * 加载推荐股票
 */
function loadRecommendations() {
    $.get(getSelectionsUrl, {
        storeID: localStorage.getItem("storeID")
    }, function (result) {
        var selected = result[getRandom(0, result.length)];
        var appendingUrl = "?stockCodes=" + stockCode + "&stockCodes=" + selected.stockCode;
        $.get(recommendUrl + appendingUrl, function (result) {
            $('#load').hide();
            var stockRecommendDiv = document.getElementById('stock-recommend');
            stockRecommendDiv.innerHTML = " <h3 style=\"text-align: left\">股票推荐</h3> <hr/>";
            for (var i = 0; i < result.length; i++) {
                if (result[i].stockCode == stockCode) {
                    continue;
                }
                var div = document.createElement('div');
                div.setAttribute('class', 'w-col w-col-5 float-background');
                div.setAttribute('style', 'margin: 35px;padding: 20px;cursor:pointer');
                var dailyData = "今日停牌-";
                if (result[i].latestDailyDataVO != null) {
                    dailyData = result[i].latestDailyDataVO.close + "元";
                }
                var jumpUrl = "./selection.html?stockCode=" + result[i].stockCode + "&stockName="
                    + result[i].name + "&stockPrice=" + dailyData + "&stockMarket=" + result[i].region
                    + "&isSelected=false";
                div.setAttribute('onclick', "myopen('" + jumpUrl + "')");
                var name = document.createElement('h4');
                name.innerHTML = result[i].name;
                var stockCodeH5 = document.createElement('h5');
                stockCodeH5.innerHTML = result[i].stockCode;
                var hr = document.createElement("hr");
                var type = document.createElement("h5");
                type.innerHTML = result[i].type;
                var price = document.createElement("h5");
                price.innerHTML = dailyData;

                div.appendChild(name);
                div.appendChild(stockCodeH5);
                div.appendChild(hr);
                div.appendChild(type);
                div.appendChild(price);
                stockRecommendDiv.appendChild(div);
            }
        });
    });
}

/**
 * 获得随机数
 * @param min
 * @param max
 * @returns {number}
 */
function getRandom(min, max) {
    var r = Math.random() * (max - min);
    var re = Math.round(r + min);
    re = Math.max(Math.min(re, max), min);

    return re;
}