var token;
var invoices;
var applicationForms;
var invoiceStatusMapper;
var currentTabId = "invoices";
var itemCount = 0;
var invoiceCount = 0;

$(document).ready(function() {
    token = localStorage.getItem("auth_token");
    if (!token) {
        alert("Please login");
        window.location.href = "login.html";
    }
    // prepare layout
    loadCountries($("#createApplicationFormApplicantCountry"));
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
                                    <td>` + getCountryNameByCode(item.applicantCountry) + `</td>
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
    /* create invoice */
    // open create modal
    $('.add-button').on('click', function() {
        if (currentTabId == "invoices") {
            $('#invoiceItems').empty();
            addItemRow();
            var currentUser = JSON.parse(localStorage.getItem("user"))
            $('#createInvoiceCompany').val(currentUser.company);
            $('#createInvoiceSellerTaxId').val(currentUser.sellerTaxId);
            $('#createInvoiceModal').fadeIn().addClass('active');
        } else {
            $('#ApplicationFormInvoices').empty();
            addInvoiceRow();
            $('#createApplicationFormModal').fadeIn().addClass('active');
        }
    });
    $('#addItemBtn').on('click', addItemRow);
    $('#addInvoiceBtn').on('click', addInvoiceRow);
    // delete item
    $(document).on('click', '.delete-item-button', function() {
        const rowId = $(this).data('row');
        if (currentTabId == "invoices") {
            deleteItemRow(rowId);
        } else {
            deleteInvoiceRow(rowId);
        }
    });
    // confirm create invoice
    $('#createInvoiceModal .btn-confirm').on('click', function() {
        var relatedItems = []; // items in the invoice
        // check items count
        if ($('#invoiceItems tr').length === 0) {
            alert('There are no items in the invoice!');
            return;
        }
        // check items filled
        var validItems = true;
        $('#invoiceItems tr').each(function() {
            var itemName = $(this).find('.item-name').val();
            var quantity = $(this).find('.item-quantity').val();
            var unitPrice = $(this).find('.item-unit-price').val();
            var itemObject = {
                "itemName": itemName,
                "quantity": quantity,
                "unitPrice": unitPrice
            }
            relatedItems.push(itemObject);
            if (!itemName || !quantity || !unitPrice) {
                validItems = false;
            }
        });
        if (!validItems) {
            alert('Items information not fulfilled!');
            return;
        }
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "http://localhost:8081/invoice/createInvoice",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: JSON.stringify({
                "items": relatedItems
            }),
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    alert('Invoice successfully created!');
                    getAllInvoices();
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
        $('#createInvoiceModal').removeClass('active');
        setTimeout(() => {
            $('#createInvoiceModal').fadeOut();
        }, 300);
    });
    // confirm create application form
    $('#createApplicationFormModal .btn-confirm').on('click', function() {
        var relatedInvoices = []; // invoices in the application form
        // check invoice count
        if ($('#ApplicationFormInvoices tr').length === 0) {
            alert('There are no invoices in the application form!');
            return;
        }
        // check applicant information
        var applicantName = $("#createApplicationFormApplicantName").val();
        var applicantId = $("#createApplicationFormApplicantId").val();
        var applicantCountry = $("#createApplicationFormApplicantCountry").val();
        if (applicantName == "" || applicantName == null) {
            alert("Applicant name can not be null!");
            return;
        }
        if (applicantId == "" || applicantId == null) {
            alert("Applicant id can not be null");
            return;
        }
        if (applicantCountry == "" || applicantCountry == null) {
            alert("Applicant country can not be null!");
            return;
        }
        // check invoice filled
        var validInvoices = true;
        $('#ApplicationFormInvoices tr').each(function() {
            var invoiceNumber = $(this).find('.invoice-number').val();
            if (invoiceNumber == "" || invoiceNumber == null) {
                validItems = false;
            }
            relatedInvoices.push(invoiceNumber);
        });
        if (!validInvoices) {
            alert('Invoice information not fulfilled!');
            return;
        }
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "http://localhost:8081/applicationForm/createApplicationForm",
            headers: {
                "Authorization": "Bearer " + token
            },
            data: JSON.stringify({
                "applicantName": applicantName,
                "applicantId": applicantId,
                "applicantCountry": applicantCountry,
                "invoices": relatedInvoices
            }),
            xhrFields: { withCredentials: true },
            success: function(data) {
                if (data.status == "success") {
                    alert('Application form successfully created!');
                    getAllApplicationForms();
                } else {
                    alert(data.data.errorMessage);
                }
            },
            error: function(xhr, status, error) {
                alert(xhr.responseText || error);
            }
        });
        $('#createApplicationFormModal').removeClass('active');
        setTimeout(() => {
            $('#createApplicationFormModal').fadeOut();
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
                                <td>` + getCountryNameByCode(item.applicantCountry) + `</td>
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
                $("#viewApplicantCountry").val(getCountryNameByCode(applicationForm.applicantCountry));
                $("#viewApplicationFormIssueDate").val(formatDate(applicationForm.issueDate));
                $("#viewApplicationFormTotalAmount").val(formatAmount(applicationForm.totalAmount));
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

/* add invoice */
function addItemRow() {
    itemCount++;
    const rowId = `item-${itemCount}`;
    const row = `
        <tr id="${rowId}">
            <td>
                <input type="text" class="form-input item-name" placeholder="Enter item name">
            </td>
            <td>
                <input type="number" class="form-input item-quantity" min="1" value="1" placeholder="Quantity">
            </td>
            <td>
                <input type="number" class="form-input item-unit-price" min="0" step="0.01" value="0.00" placeholder="0.00">
            </td>
            <td>
                <input type="text" class="form-input item-total" value="0.00" readonly>
            </td>
            <td>
                <button class="delete-item-button" data-row="${rowId}">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `;
    $('#invoiceItems').append(row);
    $(`#${rowId} .item-quantity, #${rowId} .item-unit-price`).on('input', calculateItemTotal);
    calculateItemTotalAmount();
}

// calculate amount of single item
function calculateItemTotal() {
    const row = $(this).closest('tr');
    const quantity = parseFloat(row.find('.item-quantity').val()) || 0;
    const unitPrice = parseFloat(row.find('.item-unit-price').val()) || 0;
    const total = (quantity * unitPrice).toFixed(2);
    row.find('.item-total').val(total);
    calculateItemTotalAmount();
}

// calculate amount of all items
function calculateItemTotalAmount() {
    let totalAmount = 0;
    $('.item-total').each(function() {
        totalAmount += parseFloat($(this).val()) || 0;
    });
    $('#totalAmount').val(totalAmount.toFixed(2));
}

function deleteItemRow(rowId) {
    $(`#${rowId}`).remove();
    calculateItemTotalAmount();
}

/* add application form */
function addInvoiceRow() {
    invoiceCount++;
    const rowId = `invoice-${invoiceCount}`;
    const row = `
        <tr id="${rowId}">
            <td>
                <input type="text" class="form-input invoice-number" placeholder="Enter invoice number">
            </td>>
            <td>
                <button class="delete-item-button" data-row="${rowId}">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        </tr>
    `;
    $('#ApplicationFormInvoices').append(row);
}

function deleteInvoiceRow(rowId) {
    $(`#${rowId}`).remove();
}