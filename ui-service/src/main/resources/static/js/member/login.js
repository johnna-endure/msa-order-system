$(document).ready(function () {

    //로그인 기능은 보류. 게이트 웨이에 구현하도록 하자.
    $("#login").click(function () {
        location.href="http://localhost:9000/login";
    });

    $("#register").click(function() {
        location.href="/register"
    });

});