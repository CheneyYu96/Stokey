/**
 * Created by Alan on 2016/6/6.
 */
setTimeout(function () {
    if ($(document).attr("title") == "Scheme") {
        $("html,body").animate({"scrollTop": "280px"}, 1000);
    } else {
        $("html,body").animate({"scrollTop": "340px"}, 1000);
    }
}, 3000);