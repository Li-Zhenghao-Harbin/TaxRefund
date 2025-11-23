jQuery(document).ready(function() {
    $("#login").on("click", function(){
        var name = $("#username").val();
        var password = $("#password").val();
        if (name == null || name == "") {
            alert("User name can not be null");
            return;
        }
        if (password == null || password == "") {
            alert("Password can not be null");
            return;
        }
        $.ajax({
            type: "POST",
            contentType: "application/x-www-form-urlencoded",
            url: "http://localhost:8081/user/login",
            data: {
                "name": name,
                "password": password,
            },
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    // save token to local storage
                    var token = data.data.token;
                    localStorage.setItem("auth_token", token);
                    localStorage.setItem("user", JSON.stringify(data.data.user));
                    console.log("login with token");
                    if (data.data.user.role == "0") {
                        window.location.href = 'management.html';
                    }
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
        return false;
    });
});