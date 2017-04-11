/**
 * Created by Alan on 2016/6/3.
 */
function select(stockCode, stockName) {
    var newUrl = addParams("./selection.html", "stockCode", stockCode);
    newUrl = addParams(newUrl, "stockName", stockName);
    window.location.href = newUrl;
}