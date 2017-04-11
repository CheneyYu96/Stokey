/**
 * Created by Alan on 2016/6/5.
 */
var detailUrl = "./selection.html";
var localhostUrl = "http://localhost:9090/";
var searchUrl = localhostUrl + "access/stock/search";
var collectUrl = localhostUrl + "user/selection/add";
var uncollectUrl = localhostUrl + "user/selection/delete";
var getCollectionUrl = localhostUrl + "user/getSelectionsByUser";

$("#inpt_search")
    .on('blur', function () {
        if ($(this).val().length == 0)
            $(this).parent('label').removeClass('active');
    })
    .on('focus', function () {
        $(this).parent('label').addClass('active');
    })
    .bind('keyup', function (event) {
        if (event.keyCode == "13") {
            //回车执行查询
            $('#load').show();
            var stockCode = $('#inpt_search').val();
            $.ajax({
                url: searchUrl,
                type: "GET",
                data: {
                    stockCode: stockCode
                },
                success: function (jsonData, textStatus, xhr) {//配置数据
                    var result = jsonData[0];
                    $('#load').hide();
                    $("#popup").fadeIn(300);
                    $('#stockCode').html(result.stockCode);
                    $('#stockName').html(result.name);
                    $('#stockType').html(result.type);
                    $('#stockMarket').html("市场: " + result.market);
                    if (result.latestDailyDataVO == null) {
                        $('#stockPrice').html("今日停牌-");
                    } else {
                        $('#stockPrice').html("当前价格: " + result.latestDailyDataVO.close);
                    }
                    $('#stockInfo').html("公司信息: " + result.info);
                },
                complete: function (xhr, textStatus) {//输入有误
                    $('#load').hide();
                    if (xhr.status == 500) {
                        swal({
                            title: "没有搜索到此股票",
                            text: "请检查输入代码是否正确",
                            type: "error",
                            confirmButtonText: "确认"
                        });
                    }
                    if (xhr.status == 400) {
                        swal({
                            title: "Oops...",
                            text: "网络错误了.",
                            type: "error",
                            confirmButtonText: "确认"
                        });
                    }
                }
            });
        }
    });

$('#cancel').click(function () {
    $("#popup").fadeOut(300);
});

$('#detail').click(function () {
    var selectedStockCode = $('#inpt_search').val();

    //获取该用户的收藏列表，用于判断是否已经是收藏过的股票
    $.get(getCollectionUrl, {
        storeID: window.localStorage.getItem("storeID")
    }, function (result) {
        var isSelected = false;
        for (var i = 0; i < result.length; i++) {
            if (result[i] == selectedStockCode) {
                isSelected = true;
                break;
            }
        }
        //配置跳转url
        var url = addParams(detailUrl, "stockCode", selectedStockCode);
        url = addParams(url, "stockName", $('#stockName').text());
        url = addParams(url, "stockMarket", $('#stockMarket').text());
        url = addParams(url, "stockPrice", $('#stockPrice').text());
        if (isSelected) {
            url = addParams(url, "isSelected", true);
        } else {
            url = addParams(url, "isSelected", false);
        }
        window.open(url);
    });
});

$('#collect').click(function () {
    //收藏逻辑
    $.ajax({
        url: collectUrl,
        type: "GET",
        data: {
            emailOrID: window.localStorage.getItem("username"),
            stockCode: $('#inpt_search').val()
        },
        success: function (result, textStatus, xhr) {//配置数据
            if (result.success == true) {
                swal({
                    title: "收藏成功",
                    text: "开启全新的StockEy探索！",
                    type: "success",
                    confirmButtonText: "确认",
                    closeOnConfirm: false
                }, function () {
                    window.location.reload();
                });
            } else {
                swal({
                    title: "收藏失败",
                    text: "您已经收藏了这支股票！",
                    type: "error",
                    confirmButtonText: "确认"
                });
            }
        }
    });
});