var token;
var users;
var userRolesMapper;
var userStatusMapper;

window.onload = function() {
    token = localStorage.getItem("auth_token");
    if (!token) {
        alert("Please login");
        window.location.href = "login.html";
    }
    // display user name
    $("#current_user").text("Current User: " + JSON.parse(localStorage.getItem("user")).name);
    // prepare layout
    initModal();
    // prepare functions
    buttonLogout();
    buttonDashboard();
    buttonAddUser();
    buttonQueryUser();
    buttonResetQuery();
    // get user roles
    $.ajax({
        type: "GET",
        contentType: "application/x-www-form-urlencoded",
        url: "http://localhost:8081/code/getUserRoles",
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                userRolesMapper = data.data;
                // load options
                var userRoleSelectHtml;
                userRolesMapper.forEach(item => {
                    userRoleSelectHtml += `
                            <option value="` + item.title + `">` + item.title + `</option>
                        `;
                });
                $("#addRole").html(userRoleSelectHtml);
                $("#addRole").on('change', function() {
                    resetAddRoleModalLayout();
                })
            } else {
                alert(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText || error);
        }
    });
    // get user status
    $.ajax({
        type: "GET",
        contentType: "application/x-www-form-urlencoded",
        url: "http://localhost:8081/code/getStatus",
        data: {
            "business": "User"
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                userStatusMapper = data.data;
                // load options
                var userStatusHtml;
                userStatusMapper.forEach(item => {
                    userStatusHtml += `
                            <option value="` + item.title + `">` + item.title + `</option>
                        `;
                });
                $("#editStatus").html(userStatusHtml);
            } else {
                alert(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText || error);
        }
    });
    getAllUsers();
}

function resetAddRoleModalLayout() {
    let currentRole = getUserRoleCode($("#addRole").val());
    if (currentRole == 1) {
        $("#addGroupCompany").show();
        $("#addGroupSellerTaxId").show();
        $("#addGroupCompany").val("");
        $("#addGroupSellerTaxId").val("");
    } else {
        $("#addGroupCompany").hide();
        $("#addGroupSellerTaxId").hide();
    }
}

function initModal() {
    // close modal by button
    $('.close-button, .btn-cancel').on('click', function() {
        const modalOverlay = $(this).closest('.modal-overlay');
        modalOverlay.removeClass('active');
        setTimeout(() => {
            modalOverlay.fadeOut();
        }, 300);
    });
    // close modal by other region
//    $('.modal-overlay').on('click', function(e) {
//        if (e.target === this) {
//            $(this).removeClass('active');
//            setTimeout(() => {
//                $(this).fadeOut();
//            }, 300);
//        }
//    });
    // save edited information
    $('#update').on('click', function() {
        let userName = $("#editUsername").val();
        let password = $("#editPassword").val();
        let role = getUserByName(userName).role;
        let status = getUserStatusCode($("#editStatus").val());
        let company = $("#editCompany").val();
        let sellerTaxId = $("#editSellerTaxId").val();
        if (password == "" || password == null) {
            alert("Password can not be null!");
            return;
        }
        if (role != 1) {
            company = sellerTaxId = "";
        } else {
            if (company == "" || company == null) {
                alert("Company can not be null!");
                return;
            }
            if (sellerTaxId == "" || sellerTaxId == null) {
                alert("Seller tax id can not be null!");
                return;
            }
        }
        $.ajax({
            type: "POST",
            contentType: "application/x-www-form-urlencoded",
            url: "http://localhost:8081/user/updateUser",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: {
                name: userName,
                password: password,
                role: role,
                status: status,
                company: company,
                sellerTaxId: sellerTaxId
            },
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    getAllUsers();
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
        $('#editModal').removeClass('active');
        setTimeout(() => {
            $('#editModal').fadeOut();
        }, 300);
    });
    $('#add').on('click', function() {
        let userName = $("#addUsername").val();
        let password = $("#addPassword").val();
        let role = getUserRoleCode($("#addRole").val());
//        let status = getUserStatusCode($("#addStatus").val());
        let company = $("#addCompany").val();
        let sellerTaxId = $("#addSellerTaxId").val();
        if (userName == "" || userName == null) {
            alert("Username can not be null");
            return;
        }
        if (password == "" || password == null) {
            alert("Password can not be null!");
            return;
        }
        if (role != 1) {
            company = sellerTaxId = "";
        } else {
            if (company == "" || company == null) {
                alert("Company can not be null!");
                return;
            }
            if (sellerTaxId == "" || sellerTaxId == null) {
                alert("Seller tax id can not be null!");
                return;
            }
        }
        $.ajax({
            type: "POST",
            contentType: "application/x-www-form-urlencoded",
            url: "http://localhost:8081/user/register",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: {
                name: userName,
                password: password,
                role: role,
                company: company,
                sellerTaxId: sellerTaxId
            },
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    getAllUsers();
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
        $('#addModal').removeClass('active');
        setTimeout(() => {
            $('#addModal').fadeOut();
        }, 300);
    });
}

function buttonAddUser() {
    $('#addUser').on('click', function() {
        $("#addUsername").val("");
        $("#addPassword").val("");
        $("#addModal").fadeIn().addClass('active');
        resetAddRoleModalLayout();
    });
}

function buttonQueryUser() {
    $('#queryUser').on('click', function() {
        var name = $("#inputSearchUser").val();
        if (name == "" || name == null) {
            getAllUsers();
            return;
        }
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/user/getUserByName",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: {
                "name": name
            },
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    users = [];
                    users.push(data.data);
                    let tableHTML = '';
                    if (users[0] != null) {
                        users.forEach((item, index) => {
                            tableHTML += `
                                <tr>
                                    <td>${item.name}</td>
                                    <td><span class="role-badge role-${item.role}">` + formatUserRole(item.role) + `</span></td>
                                    <td><span style="color: ` + (item.status == 1 ? "#00b894" : "red") + `;">` + formatUserStatus(item.status) + `</span></td>
                                    <td>
                                        <div class="actions">
                                            <button class="btn btn-view"><i class="fas fa-eye"></i> View</button>
                                            <button class="btn btn-edit"><i class="fas fa-edit"></i> Edit</button>
                                            <button class="btn btn-delete"><i class="fas fa-trash"></i> Delete</button>
                                        </div>
                                    </td>
                                </tr>
                            `;
                        });
                    }
                    $("#tb").html(tableHTML);
                    initTableOperations();
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
    });
}

function buttonLogout() {
    $('#logout').on('click', function() {
        if (confirm('Are you sure to logout?')) {
            $.ajax({
                type: "POST",
                contentType: "application/x-www-form-urlencoded",
                url: "http://localhost:8081/user/logout",
                xhrFields: { withCredentials: true },
                success: function(data) {
                    if (data.status == "success") {
                        localStorage.removeItem('auth_token');
                        window.location.href = "login.html";
                    } else {
                        alert(data.data.errorMessage);
                    }
                },
                error: function(xhr, status, error) {
                    alert(xhr.responseText || error);
                }
            });
            return false;
        }
    });
}

function buttonDashboard() {
    $('#dashboard').on('click', function() {
        window.location.href = "dashboard.html";
    });
}

function buttonResetQuery() {
    $('#resetQuery').on('click', function() {
        $("#inputSearchUser").val("");
        getAllUsers();
    });
}

function getAllUsers() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/user/getAllUsers",
        headers: {
            "Authorization": "Bearer " + token
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                users = data.data;
                let tableHTML = '';
                if (users != null) {
                    users.forEach((item, index) => {
                        tableHTML += `
                            <tr>
                                <td>${item.name}</td>
                                <td><span class="role-badge role-${item.role}">` + formatUserRole(item.role) + `</span></td>
                                <td><span style="color: ` + (item.status == 1 ? "#00b894" : "red") + `;">` + formatUserStatus(item.status) + `</span></td>
                                <td>
                                    <div class="actions">
                                        <button class="btn btn-view"><i class="fas fa-eye"></i> View</button>
                                        <button class="btn btn-edit"><i class="fas fa-edit"></i> Edit</button>
                                        <button class="btn btn-delete"><i class="fas fa-trash"></i> Delete</button>
                                    </div>
                                </td>
                            </tr>
                        `;
                    });

                }
                $("#tb").html(tableHTML);
                initTableOperations();
            } else {
                alert(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText || error);
        }
    });
}

function initTableOperations() {
    document.querySelectorAll('.btn-view').forEach(button => {
        button.addEventListener('click', function() {
            const username = this.closest('tr').querySelector('td:first-child').textContent;
            var user = getUserByName(username);
            var role = user.role;
            $('#viewUsername').val(user.name);
            $('#viewPassword').val(user.password);
            $('#viewRole').val(formatUserRole(role));
            $('#viewStatus').val(formatUserStatus(user.status));
            if (role != 1) {
                $("#viewGroupCompany").hide();
                $("#viewGroupSellerTaxId").hide();
            } else {
                $("#viewGroupCompany").show();
                $("#viewGroupSellerTaxId").show();
                $('#viewCompany').val(user.company);
                $('#viewSellerTaxId').val(user.sellerTaxId);
            }
            $("#viewModal").fadeIn().addClass('active');
        });
    });
    document.querySelectorAll('.btn-edit').forEach(button => {
        button.addEventListener('click', function() {
            const username = this.closest('tr').querySelector('td:first-child').textContent;
            var user = getUserByName(username);
            var role = user.role;
            $('#editUsername').val(user.name);
            $('#editPassword').val(user.password);
            $('#editRole').val(formatUserRole(role));
            $('#editStatus').val(formatUserStatus(user.status));
            if (role != 1) {
                $("#editGroupCompany").hide();
                $("#editGroupSellerTaxId").hide();
            } else {
                $("#editGroupCompany").show();
                $("#editGroupSellerTaxId").show();
                $('#editCompany').val(user.company);
                $('#editSellerTaxId').val(user.sellerTaxId);
            }
            $("#editModal").fadeIn().addClass('active');
        });
    });
    document.querySelectorAll('.btn-delete').forEach(button => {
        button.addEventListener('click', function() {
            const username = this.closest('tr').querySelector('td:first-child').textContent;
            if (confirm(`Are you sure you want to delete user: ${username}?`)) {
                $.ajax({
                    type: "POST",
                    contentType: "application/x-www-form-urlencoded",
                    url: "http://localhost:8081/user/delete",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    data: {
                        "name": username
                    },
                    xhrFields: { withCredentials: true },
                    success: function(data) {
                        if (data.status == "success") {
                            console.log(`${username} has been deleted!`);
                            getAllUsers();
                        } else {
                            alert(data.data.errorMessage);
                        }
                    },
                    error: function(xhr, status, error) {
                        alert(xhr.responseText || error);
                    }
                });
            }
        });
    });
}

function formatUserRole(role) {
    return userRolesMapper[role].title;
}

function formatUserStatus(status) {
    return userStatusMapper[status].title;
}

function getUserRoleCode(status) {
    for (var i = 0; i < userRolesMapper.length; i++) {
        if (userRolesMapper[i].title == status) {
            return userRolesMapper[i].code;
        }
    }
    return null;
}

function getUserStatusCode(status) {
    for (var i = 0; i < userStatusMapper.length; i++) {
        if (userStatusMapper[i].title == status) {
            return userStatusMapper[i].code;
        }
    }
    return null;
}


function getUserByName(name) {
    for (var i = 0; i < users.length; i++) {
        if (users[i].name == name) {
            return users[i];
        }
    }
    return null;
}