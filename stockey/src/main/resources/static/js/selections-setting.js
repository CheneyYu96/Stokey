/**
 * Created by Alan on 2016/6/4.
 */

var userUrl = "http://localhost:9090/user/", getSelectionUrl = "getSelectionsByUser/", unCollectUrl = "selection/delete";
var getCollectionUrl = userUrl + "getSelectionsByUser";
var getPieUrl = "http://localhost:9090/analysis/getInfluencePie";
var imageUrl = "images/stock_images/";
var typeList = [];
var shareList = [];
//默认图片集
var stock_images = [];
for (var i = 0; i < 11; i++) {
    stock_images.push(imageUrl + i + ".jpg");
}
//加载界面时如果登录就加载自选股，没有登录就提示登录
$(document).ready(loadSelections());

/**
 * 选择跳转至个股界面
 * @param stockCode
 */
function select(stockCode) {
    var url = addParams("./selection.html", "stockCode", stockCode);
    url = addParams(url, "stockName", $('#' + stockCode + '-stock-name').text());
    url = addParams(url, "stockMarket", $('#' + stockCode + '-stock-market').text());
    url = addParams(url, "stockPrice", $('#' + stockCode + '-price-text').text());
    url = addParams(url, "isSelected", true);
    window.open(url);
}

/**
 * 取消关注某支股票
 * @param stockCode
 */
function uncollect(stockCode) {
    stopevt();
    swal({
        title: "确定取消收藏" + stockCode + "吗?",
        text: "删除后将无法撤销!",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "删除",
        cancelButtonText: "取消",
        closeOnConfirm: false
    }, function () {
        $.get(userUrl + unCollectUrl, {
                emailOrID: window.localStorage.getItem("username"),
                stockCode: stockCode
            }, function (result) {
                swal("取消收藏成功", "已取消收藏" + stockCode, "success");
                setTimeout(function () {
                    location.reload()
                }, 1500);
            }
        );
    });
}

/**
 * 加载自选股
 */
function loadSelections() {
    console.log("load selections...");
    if (window.localStorage.getItem("stockey_login") != "true") {
        //如果没有登录就直接返回，界面上面默认写的是请登录
        return;
    }

    //从localStorage中获得用户数据
    var userID = window.localStorage.getItem("storeID");

    var selectionsRows = document.getElementById("selections-rows");
    selectionsRows.innerHTML = "";
    //发送获取请求
    $.get(userUrl + getSelectionUrl, {
            storeID: window.localStorage.getItem("storeID")
        }, function (data) {
            var i = 0;
            var image_count = 0;
            var selectionRow;
            for (i = 0; i < data.length; i++) {
                //如果自选股不存在就跳过，防止错误
                if (data[i] == null) {
                    continue;
                }
                var selection = document.createElement("div");
                selection.setAttribute("class", "w-col w-col-3 w-col-medium-6");
                var tile = document.createElement("div");
                //tile.setAttribute("data-ix", "tile-1");//这行注释不能删，删除会报错
                var clickableBlock = document.createElement("div");
                clickableBlock.setAttribute("onclick", "select('" + data[i].stockCode + "')");
                clickableBlock.setAttribute("class", "selections-div bank-div");
                var uncollectDiv = document.createElement("div");
                uncollectDiv.setAttribute("style", "float:right;font-size: 20px");
                var uncollectA = document.createElement("a");
                uncollectA.setAttribute("onclick", "uncollect('" + data[i].stockCode + "')");
                uncollectA.setAttribute("style", "cursor: pointer;text-decoration:none;color: #ffffff;");
                uncollectA.innerHTML = "×";
                uncollectDiv.appendChild(uncollectA);
                var img = document.createElement("img");
                img.setAttribute("width", "100%");
                img.setAttribute("src", stock_images[i % 11]);
                img.setAttribute("class", "selection-image");
                var h2 = document.createElement("h2");
                h2.setAttribute('id', data[i].stockCode + '-stock-name');
                h2.innerHTML = data[i].name;
                var stockCode = document.createElement("div");
                stockCode.innerHTML = data[i].stockCode;
                var stockType = document.createElement("div");
                stockType.innerHTML = data[i].type;
                var hr = document.createElement("hr");
                var detailInfo = document.createElement("div");
                detailInfo.setAttribute("class", "w-row");
                var currentPriceDiv = document.createElement("div");
                currentPriceDiv.setAttribute("class", "w-col w-col-6");
                var h4CurrentPrice = document.createElement("h4");
                h4CurrentPrice.innerHTML = "当前价格";
                var currentPrice = document.createElement("div");
                currentPrice.setAttribute("class", "price-text");
                currentPrice.setAttribute('id', data[i].stockCode + '-price-text');
                currentPrice.innerHTML = data[i].latestDailyDataVO.close;
                currentPriceDiv.appendChild(h4CurrentPrice);
                currentPriceDiv.appendChild(currentPrice);
                var recentFloatDiv = document.createElement("div");
                recentFloatDiv.setAttribute("class", "w-col w-col-6");
                var h4RecentFloat = document.createElement("h4");
                h4RecentFloat.innerHTML = "上市地区";
                var recentFloat = document.createElement("div");
                recentFloat.setAttribute("class", "price-text");
                recentFloat.setAttribute('id', data[i].stockCode + '-stock-market');
                recentFloat.innerHTML = data[i].region;

                recentFloatDiv.appendChild(h4RecentFloat);
                recentFloatDiv.appendChild(recentFloat);
                detailInfo.appendChild(currentPriceDiv);
                detailInfo.appendChild(recentFloatDiv);

                clickableBlock.appendChild(uncollectDiv);
                clickableBlock.appendChild(img);
                clickableBlock.appendChild(h2);
                clickableBlock.appendChild(stockCode);
                clickableBlock.appendChild(stockType);
                clickableBlock.appendChild(hr);
                clickableBlock.appendChild(detailInfo);
                tile.appendChild(clickableBlock);
                selection.appendChild(tile);

                //4个换行
                if ((i % 4) == 0) {
                    selectionRow = document.createElement("div");
                    selectionRow.setAttribute("class", "w-row selection-rows");
                    selectionsRows.appendChild(selectionRow);
                }
                selectionRow.appendChild(selection);
            }
        }
    );
}

/**
 * 获取影响力饼图
 */
function getInfluencePie() {
    $('#piechart').show(300);
    var dom = document.getElementById('selections-chart');
    var piechart = echarts.init(dom);
    piechart.showLoading();
    $.get(getCollectionUrl, {
        storeID: localStorage.getItem("storeID")
    }, function (result) {
        var appendingUrl = "?";
        for (var i = 0; i < result.length; i++) {
            appendingUrl += "stockCodes=" + result[i].stockCode + "&";
        }
        $.get(getPieUrl + appendingUrl, {
            begin: getDefaultTimeRange()[0],
            end: getDefaultTimeRange()[1]
        }, function (result) {
            var stockMap = result.stockMap;
            var shareMap = result.shareMap;
            for (var item in stockMap) {
                typeList.push(stockMap[item].name);
                shareList.push({value: shareMap[item], name: stockMap[item].name});
            }
            configInfluencePie(piechart);
        });
    });
}

function configInfluencePie(chart) {
    option = {
        title: {
            text: '',
            x: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b} : {c} ({d}%)"
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data: typeList
        },
        series: [
            {
                name: 'Sources',
                type: 'pie',
                radius: '55%',
                center: ['50%', '60%'],
                data: shareList,
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ]
    };
    chart.hideLoading();
    chart.setOption(option);
    window.onresize = function () {
        chart.resize();
    };
    typeList = [];
    shareList = [];
}

function closePie() {
    $('#piechart').hide(300);
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