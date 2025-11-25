var applicationFormStatusMapper;
// applicant information
var applicantName;
var applicantId;
var applicantCountry;
var bankCardNumber;
var bankCardHolder;
var bankName;
var applicationForms = [];
var finalTaxRefundAmount = 0;
var selectedApplicationForms = [];
var finalTaxRefundDate;
// tax refund methods
var taxRefundMethods = [];
var selectedRefundMethod = 1;

$(document).ready(function() {
    // progress bar parameters
    let currentStep = 1;
    // token
    token = localStorage.getItem("auth_token");
    if (!token) {
        alert("Please login");
        window.location.href = "login.html";
    }
    // prepare layout
    loadCountries($("#applicantCountry"));
    getTaxRefundMethods();
    getApplicationFormStatus();
    // initialize progress bar
    updateProgressBar(1);

    function updateProgressBar(step) {
        $('.progress-step').removeClass('active');
        $(`.progress-step[data-step="${step}"]`).addClass('active');
        const progressBar = $('#progressBar');
        const progressSteps = $('.progress-step');
        const progressPercent = ((step - 1) / (progressSteps.length - 1)) * 100;
        progressBar.css('--progress', `${progressPercent}%`);
        progressBar[0].style.setProperty('--progress', `${progressPercent}%`);
    }

    function showStep(step) {
        $('.step-content').removeClass('active');
        $(`#step${step}`).addClass('active');
        currentStep = step;
        updateProgressBar(step);
    }

    function validateStep1() {
        applicantName = $('#applicantName').val().trim();
        applicantId = $('#applicantId').val().trim();
        applicantCountry = $('#applicantCountry').val();
        if (applicantName == "" || applicantName == null) {
            alert('Applicant name can not be null!');
            return false;
        }
        if (applicantId == "" || applicantId == null) {
            alert('Applicant id can not be null!');
            return false;
        }
        if (applicantCountry == "" || applicantCountry == null) {
            alert('Applicant country can not be null!');
            return false;
        }
        return true;
    }

    function validateStep2() {
        if (selectedApplicationForms.length === 0) {
            alert('Please select at least one application form.');
            return false;
        }

        return true;
    }

    function validateStep4() {
        if (!selectedRefundMethod) {
            alert('Please select a tax refund method!');
            return false;
        }
        if (selectedRefundMethod == 2) {
            bankCardNumber = $('#bankCardNumber').val().trim();
            bankCardHolder = $('#bankCardHolder').val().trim();
            bankName = $('#bankName').val().trim();
            if (bankCardNumber == "" || bankCardNumber == null) {
                alert('Bank card number can not be null!');
                return false;
            }
            if (bankCardHolder == "" || bankCardHolder == null) {
                alert('Bank card holder can not be null!');
                return false;
            }
            if (bankName == "" || bankName == null) {
                alert('Bank name can not be null');
                return false;
            }
        }
        return true;
    }

    function updateReceipt() {
        $('#receiptName').text(applicantName);
        $('#receiptId').text(applicantId);
        $('#receiptCountry').text(applicantCountry);
        $('#receiptMethod').text(formatTaxRefundMethod(selectedRefundMethod));
        $('#receiptApplicationForms').text(selectedApplicationForms.join(', '));
        $('#receiptDate').text(formattedDate);
    }

    $('#passportScan').on('click', function() {
        $('#applicantName').val('John Smith');
        $('#applicantId').val('PASS123456');
        $('#applicantCountry').val('United States');
    });

    $('#step1Next').on('click', function() {
        if (validateStep1()) {
            $.ajax({
                type: "GET",
                url: "http://localhost:8081/applicationForm/getApplicationFormsByApplicant",
                headers: {
                    "Authorization": "Bearer " + token
                },
                data: {
                    "applicantName": applicantName,
                    "applicantId": applicantId,
                    "applicantCountry": applicantCountry
                },
                xhrFields: { withCredentials: true },
                success: function(data) {
                    if (data.status == "success") {
                        applicationForms = data.data;
                        if (applicationForms != null) {
                            initializeApplicationsTable();
                            showStep(2);
                        } else {
                            alert('There are no application forms!');
                        }
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

    $('#step2Prev').on('click', function() {
        showStep(1);
    });

    $('#step2Next').on('click', function() {
        if (validateStep2()) {
            showStep(3);
        }
    });

    // select application forms
    $(document).on('change', '.select-checkbox', function() {
        var applicationFormNumber = $(this).data('app');
        var isChecked = $(this).is(':checked');
        var customsConfirmAmount = $(this).data('amount');
        if (isChecked) {
            if (!selectedApplicationForms.includes(applicationFormNumber)) {
                selectedApplicationForms.push(applicationFormNumber);
                finalTaxRefundAmount += customsConfirmAmount;
            }
        } else {
            selectedApplicationForms = selectedApplicationForms.filter(app => app !== applicationFormNumber);
            finalTaxRefundAmount -= customsConfirmAmount;
        }
        $("#finalTaxRefundAmount").text("Total Tax Refund Amount: ￥ " + finalTaxRefundAmount);
        console.log('Selected applications:', selectedApplicationForms);
    });

    $('#step3Submit').on('click', function() {
        $("#alertSubmittedSuccessfully").show();
        $('#step3Next').show();
    });

    $('#step3Next').on('click', function() {
        initializeRefundOptions();
        showStep(4);
    });

    $('#step3Prev').on('click', function() {
        showStep(2);
    });

    $('#step4Prev').on('click', function() {
        showStep(3);
    });

    $('#step4Next').on('click', function() {
        if (validateStep4()) {
            if (selectedRefundMethod == 2) {
                $.ajax({
                    type: "POST",
                    url: "http://localhost:8081/taxRefund/taxRefundByBankCard",
                    contentType: "application/json",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    data: JSON.stringify({
                        "applicationFormNumbers": selectedApplicationForms,
                        "bankCardNumber": bankCardNumber,
                        "bankCardHolder": bankCardHolder,
                        "bankName": bankName
                    }),
                    xhrFields: { withCredentials: true },
                    success: function(data) {
                        if (data.status == "success") {
                            showStep(5);
                            updateReceipt();
                        } else {
                            alert(data.data.errorMessage);
                        }
                    },
                    error: function(xhr, status, error) {
                        alert(xhr.responseText || error);
                    }
                });
            } else {
                $.ajax({
                    type: "POST",
                    url: "http://localhost:8081/taxRefund/taxRefundByCash",
                    contentType: "application/json",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    data: JSON.stringify({
                        "applicationFormNumbers": selectedApplicationForms,
                    }),
                    xhrFields: { withCredentials: true },
                    success: function(data) {
                        if (data.status == "success") {
                            showStep(5);
                            updateReceipt();
                        } else {
                            alert(data.data.errorMessage);
                        }
                    },
                    error: function(xhr, status, error) {
                        alert(xhr.responseText || error);
                    }
                });
            }
        }
    });

    $(document).on('click', '.refund-option', function() {
        $('.refund-option').removeClass('selected');
        $(this).addClass('selected');
        selectedRefundMethod = $(this).data('method');
        if (selectedRefundMethod === 2) {
            $('#bankCardDetails').removeClass('hidden');
        } else {
            $('#bankCardDetails').addClass('hidden');
        }
    });

    $('#step5Prev').on('click', function() {
        showStep(4);
    });

    $('#step5Print').on('click', function() {
        $.ajax({
            type: "POST",
            url: "http://localhost:8081/taxRefund/printReceipt",
            headers: {
                "Authorization": "Bearer " + token
            },
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    alert(data.data);
                    location.reload();
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
    });
});

function initializeRefundOptions() {
    const optionsContainer = $('#refundOptions');
    optionsContainer.empty();
    taxRefundMethods.forEach(method => {
        const option = `
            <div class="refund-option" data-method="${method.code}">
                <div class="option-icon">
                    <i class="fas fa-${method.icon}"></i>
                </div>
                <h3>${method.title}</h3>
            </div>
        `;
        optionsContainer.append(option);
    });
}

function initializeApplicationsTable() {
    var tableBody = $('#applicationsTable');
    tableBody.empty();
    applicationForms.forEach(applicationForm => {
        var row = `
            <tr data-app="${applicationForm.applicationFormNumber}">
                <td>${applicationForm.applicationFormNumber}</td>
                <td>` + formatAmount(applicationForm.totalAmount) + `</td>
                <td>` + formatAmount(applicationForm.customsConfirmAmount) + `</td>
                <td><span class="status-badge status-`+ applicationForm.status +`">` + formatApplicationFormStatus(applicationForm.status) + `</span></td>`;
                if (applicationForm.status == 2) {
                    row += `<td class="checkbox-container">
                        <input type="checkbox" class="select-checkbox" data-app="${applicationForm.applicationFormNumber}" data-amount="${applicationForm.customsConfirmAmount}">
                    </td>`;
                }
           row += `</tr>
        `;
        tableBody.append(row);
    });
}

function getTaxRefundMethods() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/code/getTaxRefundMethods",
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                taxRefundMethods = data.data;
                taxRefundMethods[0].icon = 'money-bill-wave';
                taxRefundMethods[1].icon = 'credit-card';
            } else {
                alert(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText || error);
        }
    });
}

function getApplicationFormStatus() {
    $.ajax({
        type: "GET",
        contentType: "application/x-www-form-urlencoded",
        url: "http://localhost:8081/code/getStatus",
        data: {
            "business": "Application form"
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                applicationFormStatusMapper = data.data;
            } else {
                alert(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText || error);
        }
    });
}

function formatApplicationFormStatus(code) {
    for (var i = 0; i < applicationFormStatusMapper.length; i++) {
        if (applicationFormStatusMapper[i].code == code) {
            return applicationFormStatusMapper[i].title;
        }
    }
    return null;
}

function formatTaxRefundMethod(code) {
    for (var i = 0; i > taxRefundMethods.length; i++) {
        if (taxRefundMethods[i].code == code) {
            return taxRefundMethods[i].title;
        }
    }
    return null;
}