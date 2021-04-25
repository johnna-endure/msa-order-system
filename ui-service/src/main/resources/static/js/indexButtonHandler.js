$(document).ready(function () {

    $("#member-button").click(function () {
        location.href="/login";
    });

    $("#test").click(function () {
        // location.href="http://localhost:9000/login"
        location.href="http://localhost:9000/api/member-service/members"
    });

});

