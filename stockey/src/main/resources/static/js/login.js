/**
 * Created by Alan on 2016/6/4.
 */

var userUrl = "http://localhost:9090/user/", logInUrl = "logIn/", registerUrl = "registerAccount/";

function showLogin() {
    $('.wrapper').show(500);
}

function closeLogin() {
    $('.wrapper').hide(500);
}

function tabLogin() {
    $('#signup').hide();
    $('#signup-tab').attr("class", "tab");
    $('#login').show();
    $('#login-tab').attr("class", "tab active");
}

function tabSignup() {
    $('#signup').show();
    $('#signup-tab').attr("class", "tab active");
    $('#login').hide();
    $('#login-tab').attr("class", "tab");
}

$("#login-button").click(function (event) {
    event.preventDefault();

    $('form').fadeOut(500);
    $('#logining').show(500);
    $('.wrapper').addClass('form-success');

    //发送登录请求
    $.get(userUrl + logInUrl, {
        email: $('#username').val(),
        password: $('#password').val()
    }, function (result) {
        if (result.success == true) {
            //保存用户登录信息
            window.localStorage.setItem("stockey_login", "true");
            window.localStorage.setItem("username", result.bundle.userID);
            window.localStorage.setItem("storeID", result.bundle.storeID);
            //window.localStorage.removeItem("username");
            //window.localStorage.setItem("username", "Alan");
            //1秒加载后刷新页面
            setTimeout(function () {
                window.location.reload();
            }, 1000);
        } else {
            sweetAlert("Oops...", result.reason, "error");
            $('form').show(500);
            $('#logining').hide(500);
            $('.wrapper').removeClass('form-success');
        }
    })
});

$("#sign-button").click(function (event) {
    event.preventDefault();

    if ($('#signup_password').val() != $('#confirm_password').val()) {
        swal("Oops...", "2次密码不匹配...", "error");
    } else {
        $('form').fadeOut(500);
        $('#signing').show(500);
        $('.wrapper').addClass('form-success');

        //发送登录请求
        $.get(userUrl + registerUrl, {
            userID: $('#signup_username').val(),
            email: $('#signup_email').val(),
            password: $('#signup_password').val()
        }, function (result) {
            if (result.success == true) {
                //保存用户登录信息
                window.localStorage.setItem("stockey_login", "true");
                window.localStorage.setItem("username", result.bundle.userID);
                window.localStorage.setItem("storeID", result.bundle.storeID);
                //window.localStorage.removeItem("username");
                swal({
                    title: "注册成功!",
                    text: "您可以登录邮箱进行验证",
                    type: "success",
                    confirmButtonText: "确认",
                    closeOnConfirm: true
                }, function () {
                    window.location.reload();
                });
            } else {
                sweetAlert("Oops...", result.reason, "error");
                $('form').show(500);
                $('#signing').hide(500);
                $('.wrapper').removeClass('form-success');
            }
        });
    }
});

$(function () {
    var isopen_usermenu = false;
    /**
     * Open and close usermenu event
     */
    $(".user-menu-toggle").on("click", function () {
        if (!isopen_usermenu) {
            // Show menu
            $(".user-menu").show();
            isopen_usermenu = true;
        } else {
            // Close menu
            $(".user-menu").hide();
            isopen_usermenu = false;
        }
    });
});

/**
 * 页面跳转
 * @param url
 */
function link(url) {
    window.location.href = url;
}

/**
 * 登出
 */
function logout() {
    window.localStorage.removeItem("stockey_login");
    window.localStorage.removeItem("username");
    window.localStorage.removeItem("storeID");
    window.location.reload();
}
