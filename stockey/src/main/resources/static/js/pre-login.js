/**
 * 预判断是否已经登录
 * Created by Alan on 2016/6/5.
 */

preLogin();

/**
 * 检查是否已经登录
 */
function checkLogin() {
    //window.localStorage.removeItem("stockey_login");
    return window.localStorage.getItem("stockey_login") == "true";
}

/**
 * 预登录
 */
function preLogin() {
    //如果已经登录过就直接加载登录后状态
    if (checkLogin()) {
        $('#login-signup-button').hide(300);
        $('.user-menu-toggle').show(300);
        var username = window.localStorage.getItem("username");
        $('#user-title').html(username);
        $('#user-name').html(username);
        console.log(username);
    }
}