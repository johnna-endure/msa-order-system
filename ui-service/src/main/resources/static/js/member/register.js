$(document).ready(function () {
    //회원가입 버튼
    $("#register-button").click(function (){
        let username = $("#username").val();
        let password = $("#password").val();

        let data = {
            "username":username,
            "password":password
        };

        $.ajax({
            url: "http://localhost:9000/api/member-service/member",
            method: "post",
            contentType: "application/json",
            data: JSON.stringify(data),
            success() {
                console.log("성공")
            },
            error() {
                console.log("에러")
            }
        })
    });
});