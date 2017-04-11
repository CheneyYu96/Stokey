/**
 * Created by Alan on 2016/6/3.
 */
function addParams(url, key, value) {
    if (url.charAt(url.length - 1) != "?" && url.charAt(url.length - 1) != "&") {
        url += "?";
    }
    return url + transMap(key, value);
}

function transMap(key, value) {
    return key + "=" + value + "&";
}

/**
 *���Ŀ���������
 * @param sHref
 * @param sArgName
 * @returns {string}
 * @constructor
 */
function getParam(sHref, sArgName) {
    var args = sHref.split("?");
    var retval = "";

    if (args[0] == sHref) /*����Ϊ��*/
    {
        return retval;
        /*�������κδ���*/
    }
    var str = args[1];
    args = str.split("&");
    for (var i = 0; i < args.length; i++) {
        str = args[i];
        var arg = str.split("=");
        if (arg.length <= 1) continue;
        if (arg[0] == sArgName) retval = arg[1];
    }
    return retval;
}