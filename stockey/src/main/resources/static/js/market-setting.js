/**
 * Created by Alan on 2016/6/17.
 */
var localhostUrl = "http://localhost:9090/";
var searchUrl = "access/stock/search";
var commentUrl = "analysis/getComment";
var stockCode = "bm000300";

/**
 * 获取当前大盘价格
 */
$.get(localhostUrl + searchUrl, {
        stockCode: stockCode
    },
    function (result) {
        var dailyData = result.latestDailyDataVO;
        var close = "今日停牌-";
        if (dailyData != null) {
            close = dailyData.close;
        }
        $('#price').html("当前价格: " + close);
    }
);

$.get(localhostUrl+commentUrl,{
    stockCode:stockCode
},function(result){
    $('#comment').html(result);
});

