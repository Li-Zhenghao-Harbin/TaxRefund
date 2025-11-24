var token;
var invoices;
var applicationForms;
var invoiceStatusMapper;
var applicationFormStatusMapper;
var currentTabId = "invoices";

$(document).ready(function() {
    token = localStorage.getItem("auth_token");
    if (!token) {
        alert("Please login");
        window.location.href = "login.html";
    }
    // prepare layout
    getAllInvoices();
    // get invoice status
    $.ajax({
        type: "GET",
        contentType: "application/x-www-form-urlencoded",
        url: "http://localhost:8081/code/getStatus",
        data: {
            "business": "Invoice"
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                invoiceStatusMapper = data.data;
            } else {
                alert(data.data.errorMessage);
            }
        },
        error: function(xhr, status, error) {
            alert(xhr.responseText || error);
        }
    });
    // display user name
    $("#currentUser").text("Current User: " + JSON.parse(localStorage.getItem("user")).name);
    // switch tab
    $('.tab').on('click', function() {
        const tabId = $(this).data('tab');
        currentTabId = tabId;
        $('.tab').removeClass('active');
        $(this).addClass('active');
        $('.tab-content').removeClass('active');
        $(`#${tabId}-tab`).addClass('active');
        if (tabId == "invoices") {
            getAllInvoices();
        } else {
            getAllApplicationForms();
        }
    });
    // search
    $("#searchInvoice").on('click', function() {
        var searchNumber = $("#invoiceSearchInput").val();
        if (searchNumber == "" || searchNumber == null) {
            getAllInvoices();
            return;
        }
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/invoice/getInvoiceByInvoiceNumber",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: {
                "invoiceNumber": searchNumber
            },
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    invoices = [];
                    invoices.push(data.data);
                    let tableHTML = '';
                    if (invoices != null) {
                        invoices.forEach((item, index) => {
                            tableHTML += `
                                <tr>
                                    <td>` + item.invoiceNumber +`</td>
                                    <td>` + formatAmount(item.totalAmount) + `</td>
                                    <td>` + formatDate(item.issueDate) + `</td>
                                    <td><span class="status-badge status-`+ item.status +`">` + formatInvoiceStatus(item.status) + `</span></td>
                                    <td>
                                        <div class="actions">
                                            <button class="btn btn-view"><i class="fas fa-eye"></i> View</button>`;
                                            if (item.status != -1) {
                                                tableHTML += `<button class="btn btn-discard"><i class="fas fa-times"></i> Discard</button>`;
                                            }
                                        tableHTML +=
                                        `</div>
                                    </td>
                                </tr>
                            `;
                        });
                    }
                    $("#invoicesTable").html(tableHTML);
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
    $("#searchApplicationForm").on('click', function() {
        var searchNumber = $("#applicationFormSearchInput").val();
        if (searchNumber == "" || searchNumber == null) {
            getAllApplicationForms();
            return;
        }
        $.ajax({
            type: "GET",
            url: "http://localhost:8081/applicationForm/getApplicationFormByApplicationFormNumber",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: {
                "applicationFormNumber": searchNumber
            },
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    applicationForms = [];
                    applicationForms.push(data.data);
                    let tableHTML = '';
                    if (applicationForms != null) {
                        applicationForms.forEach((item, index) => {
                            tableHTML += `
                                <tr>
                                    <td>` + item.applicationFormNumber +`</td>
                                    <td>` + item.applicantName + `</td>
                                    <td>` + item.applicantId + `</td>
                                    <td>` + item.applicantCountry + `</td>
                                    <td>` + formatDate(item.issueDate) + `</td>
                                    <td>` + formatAmount(item.totalAmount) + `</td>
                                    <td>
                                        <div class="actions">
                                            <button class="btn btn-view"><i class="fas fa-eye"></i> View</button>
                                        </div>
                                    </td>
                                </tr>
                            `;
                        });
                    }
                    $("#applicationFormTable").html(tableHTML);
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
    // reset
    $('#resetInvoiceQuery').on('click', function() {
        $("#invoiceSearchInput").val("");
        getAllInvoices();
    });
    $('#resetApplicationFormQuery').on('click', function() {
        $("#applicationFormSearchInput").val("");
        getAllApplicationForms();
    });
    // split page
    $('.pagination button').on('click', function() {
        $(this).closest('.pagination').find('.active').removeClass('active');
        $(this).addClass('active');
        alert(`Loading page ${$(this).text()}`);

    });
    // add invoice/application form
    $('.add-button').on('click', function() {
        const tabContent = $(this).closest('.tab-content');
        const isInvoice = tabContent.attr('id') === 'invoices-tab';
        const type = isInvoice ? 'invoice' : 'application form';

        alert(`Opening create ${type} form...`);

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
    // close modal
    $('.close-button, .btn-cancel').on('click', function() {
        const modalOverlay = $(this).closest('.modal-overlay');
        modalOverlay.removeClass('active');
        setTimeout(() => {
            modalOverlay.fadeOut();
        }, 300);
    });
});

function getAllInvoices() {
    let issueMerchantName = JSON.parse(localStorage.getItem("user")).name;
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/invoice/getInvoicesByIssueMerchantName",
        headers: {
            "Authorization": "Bearer " + token
        },
        data: {
            "issueMerchantName": issueMerchantName
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                invoices = data.data;
                let tableHTML = '';
                if (invoices != null) {
                    invoices.forEach((item, index) => {
                        tableHTML += `
                            <tr>
                                <td>` + item.invoiceNumber +`</td>
                                <td>` + formatAmount(item.totalAmount) + `</td>
                                <td>` + formatDate(item.issueDate) + `</td>
                                <td><span class="status-badge status-`+ item.status +`">` + formatInvoiceStatus(item.status) + `</span></td>
                                <td>
                                    <div class="actions">
                                        <button class="btn btn-view"><i class="fas fa-eye"></i> View</button>`;
                                        if (item.status != -1) {
                                            tableHTML += `<button class="btn btn-discard"><i class="fas fa-times"></i> Discard</button>`;
                                        }
                                    tableHTML +=
                                    `</div>
                                </td>
                            </tr>
                        `;
                    });
                }
                $("#invoicesTable").html(tableHTML);
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

function getAllApplicationForms() {
    let issueMerchantName = JSON.parse(localStorage.getItem("user")).name;
    $.ajax({
        type: "GET",
        url: "http://localhost:8081/applicationForm/getApplicationFormsByIssueMerchantName",
        headers: {
            "Authorization": "Bearer " + token
        },
        data: {
            "issueMerchantName": issueMerchantName
        },
        xhrFields: { withCredentials: true },
        success: function(data) {
            if (data.status == "success") {
                applicationForms = data.data;
                let tableHTML = '';
                if (applicationForms != null) {
                    applicationForms.forEach((item, index) => {
                        tableHTML += `
                            <tr>
                                <td>` + item.applicationFormNumber +`</td>
                                <td>` + item.applicantName + `</td>
                                <td>` + item.applicantId + `</td>
                                <td>` + item.applicantCountry + `</td>
                                <td>` + formatDate(item.issueDate) + `</td>
                                <td>` + formatAmount(item.totalAmount) + `</td>
                                <td>
                                    <div class="actions">
                                        <button class="btn btn-view"><i class="fas fa-eye"></i> View</button>
                                    </div>
                                </td>
                            </tr>
                        `;
                    });
                }
                $("#applicationFormTable").html(tableHTML);
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
            if (currentTabId == "invoices") {
                const invoiceNumber = this.closest('tr').querySelector('td:first-child').textContent;
                var invoice = getInvoiceFromLocalInvoicesByInvoiceNumber(invoiceNumber);
                $("#viewInvoiceNumber").val(invoiceNumber);
                $("#viewInvoiceTotalAmount").val(formatAmount(invoice.totalAmount));
                $("#viewInvoiceIssueDate").val(formatDate(invoice.issueDate));
                $("#viewInvoiceStatus").val(formatInvoiceStatus(invoice.status));
                $("#viewInvoiceCompany").val(invoice.company);
                $("#viewInvoiceSellerTaxId").val(invoice.sellerTaxId);
                $("#invoiceViewModal").fadeIn().addClass('active');
                // items table
                var itemsTableHtml = "";
                invoice.items.forEach(item => {
                    itemsTableHtml += `
                        <tr>
                           <td>` + item.itemName +`</td>
                           <td>` + item.quantity + `</td>
                           <td>` + formatAmount(item.unitPrice) + `</td>
                           <td>` + formatAmount(item.quantity * item.unitPrice) + `</td>
                       </tr>
                    `;
                });
                $("#invoiceItemsTable").html(itemsTableHtml);
            } else {
                const applicationFormNumber = this.closest('tr').querySelector('td:first-child').textContent;
                var applicationForm = getApplicationFormFromLocalApplicationFormsByApplicationFormNumber(applicationFormNumber);
                $("#viewApplicationFormNumber").val(applicationFormNumber);
                $("#viewApplicantName").val(applicationForm.applicantName);
                $("#viewApplicantId").val(applicationForm.applicantId);
                $("#viewApplicantCountry").val(applicationForm.applicantCountry);
                $("#viewApplicationFormIssueDate").val(formatDate(applicationForm.issueDate));
                $("#viewApplicationFormTotalAmount").val(applicationForm.totalAmount);
                $("#applicationFormViewModal").fadeIn().addClass('active');
                // invoices table
                var invoicesTableHtml = "";
                applicationForm.invoices.forEach(invoice => {
                    invoicesTableHtml += `
                        <tr>
                           <td>` + invoice.invoiceNumber +`</td>
                           <td>` + formatAmount(invoice.totalAmount) + `</td>
                           <td>` + formatDate(invoice.issueDate) + `</td>
                       </tr>
                    `;
                });
                $("#applicationFormInvoicesTable").html(invoicesTableHtml);
            }
        });
    });
    document.querySelectorAll('.btn-discard').forEach(button => {
        button.addEventListener('click', function() {
            const invoiceNumber = this.closest('tr').querySelector('td:first-child').textContent;
            var invoice = getInvoiceFromLocalInvoicesByInvoiceNumber(invoiceNumber);
            if (confirm("Are you sure to discard this invoice (" + invoiceNumber +")?")) {
                $.ajax({
                    type: "POST",
                    contentType: "application/x-www-form-urlencoded",
                    url: "http://localhost:8081/invoice/discardInvoice",
                    headers: {
                        "Authorization": "Bearer " + token
                    },
                    data: {
                        "invoiceNumber": invoiceNumber
                    },
                    xhrFields: { withCredentials: true },
                    success: function(data) {
                        if (data.status == "success") {
                            alert("Invoice (" + invoiceNumber + ") has been discarded!" );
                            getAllInvoices();
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

function getInvoiceFromLocalInvoicesByInvoiceNumber(invoiceNumber) {
    for (var i = 0; i < invoices.length; i++) {
        if (invoices[i].invoiceNumber == invoiceNumber) {
            return invoices[i];
        }
    }
    return null;
}

function getApplicationFormFromLocalApplicationFormsByApplicationFormNumber(applicationFormNumber) {
    for (var i = 0; i < applicationForms.length; i++) {
        if (applicationForms[i].applicationFormNumber == applicationFormNumber) {
            return applicationForms[i];
        }
    }
    return null;
}

function formatInvoiceStatus(code) {
    for (var i = 0; i < invoiceStatusMapper.length; i++) {
        if (invoiceStatusMapper[i].code == code) {
            return invoiceStatusMapper[i].title;
        }
    }
    return null;
}