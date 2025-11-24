var token;
var applicationFormNumber;
var readyToReview = false;

$(document).ready(function() {
    token = localStorage.getItem("auth_token");
    if (!token) {
        alert("Please login");
        window.location.href = "login.html";
    }
    // display user name
    $("#currentUser").text("Current User: " + JSON.parse(localStorage.getItem("user")).name);
    // query application form
    $('#search').on('click', function() {
        applicationFormNumber = $('#applicationFormNumberSearchInput').val().trim();
        if (!applicationFormNumber) {
            alert('Application form number can not be null!');
            return;
        }
        var applicationForm;
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/applicationForm/getApplicationFormByApplicationFormNumber",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: {
                "applicationFormNumber": applicationFormNumber
            },
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    if (data.data != null) {
                        readyToReview = true;
                        displayApplicationForm(data.data);
                    } else {
                        $('#itemsTableBody').html('<tr class="no-data"><td colspan="5">No application found with the provided number.</td></tr>');
                        $('#applicationInfo').hide();
                        alert('No application found with the provided number.');
                    }
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
    });
    // review application form
    $('#reviewBtn').on('click', function() {
        if (!readyToReview) {
            alert('No application form is available!');
            return;
        }
        var rejectedItems = [];
        $('.reject-checkbox:checked').each(function() {
            var itemId = $(this).data('item');
            rejectedItems.push(itemId);
        });
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "http://localhost:8081/applicationForm/reviewApplicationForm",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: JSON.stringify({
                "applicationFormNumber": applicationFormNumber,
                "rejectItems": rejectedItems
            }),
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    alert("Application form successfully reviewed!");
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
        reset();
    });
    // reset
    $('#reset').on('click', function() {
        reset();
    });
    // logout
    $('.logout-button').on('click', function() {
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
});

function reset() {
    $("#applicationFormNumberSearchInput").val("");
    $("#applicationInfo").hide();
    readyToReview = false;
}

function displayApplicationForm(applicationForm) {
    $('#applicationFormNumber').text(applicationForm.applicationFormNumber);
    $('#applicantName').text(applicationForm.applicantName);
    $('#applicantId').text(applicationForm.applicantId);
    $('#applicantCountry').text(applicationForm.applicantCountry);
    $('#applicationFormIssueDate').text(formatDate(applicationForm.issueDate));
    $('#applicationFormTotalAmount').text(formatAmount(applicationForm.totalAmount));
    $('#applicationInfo').show();
    $('#itemsTableBody').html("");
    applicationForm.invoices.forEach(invoice => {
        invoice.items.forEach(item => {
            var row = `
                <tr data-id="${item.id}">
                    <td>${item.itemName}</td>
                    <td>${item.quantity}</td>
                    <td>￥${item.unitPrice}</td>
                    <td>` + formatAmount(item.quantity * item.unitPrice) + `</td>
                    <td class="checkbox-container">
                        <input type="checkbox" class="reject-checkbox" data-item="${item.id}">
                    </td>
                </tr>
            `;
            $('#itemsTableBody').append(row);
        });
    });
}