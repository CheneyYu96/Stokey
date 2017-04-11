/**
 * Created by Alan on 2016/6/4.
 */
//Navbar Scroll Event
var lastScrollTop = 0;
var navbar = $('.nav');
$(window).scroll(function (event) {
    var st = $(this).scrollTop();
    if (st > lastScrollTop) {
        navbar.addClass('navbar-scroll-custom');
    } else {
        navbar.removeClass('navbar-scroll-custom');
    }
    lastScrollTop = st;
});