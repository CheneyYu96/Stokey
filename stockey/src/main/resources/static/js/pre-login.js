/**
 * Ԥ�ж��Ƿ��Ѿ���¼
 * Created by Alan on 2016/6/5.
 */

preLogin();

/**
 * ����Ƿ��Ѿ���¼
 */
function checkLogin() {
    //window.localStorage.removeItem("stockey_login");
    return window.localStorage.getItem("stockey_login") == "true";
}

/**
 * Ԥ��¼
 */
function preLogin() {
    //����Ѿ���¼����ֱ�Ӽ��ص�¼��״̬
    if (checkLogin()) {
        $('#login-signup-button').hide(300);
        $('.user-menu-toggle').show(300);
        var username = window.localStorage.getItem("username");
        $('#user-title').html(username);
        $('#user-name').html(username);
        console.log(username);
    }
}