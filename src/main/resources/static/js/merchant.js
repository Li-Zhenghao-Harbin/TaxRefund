// 模拟数据
const invoiceData = {
    1: {
        invoiceNumber: "INV-2023-001",
        totalAmount: "$1,250.00",
        issueDate: "2023-05-15",
        status: "Paid",
        company: "Tech Solutions Inc.",
        sellerTaxId: "TAX-001234",
        items: [
            { id: 1, itemName: "Laptop", quantity: 2, unitPrice: "$500.00", total: "$1,000.00" },
            { id: 2, itemName: "Mouse", quantity: 5, unitPrice: "$50.00", total: "$250.00" }
        ]
    },
    2: {
        invoiceNumber: "INV-2023-002",
        totalAmount: "$850.50",
        issueDate: "2023-05-18",
        status: "Pending",
        company: "MediaWorks LLC",
        sellerTaxId: "TAX-005678",
        items: [
            { id: 1, itemName: "Web Design Service", quantity: 1, unitPrice: "$850.50", total: "$850.50" }
        ]
    },
    3: {
        invoiceNumber: "INV-2023-003",
        totalAmount: "$2,100.75",
        issueDate: "2023-05-20",
        status: "Cancelled",
        company: "Retail Partners Co.",
        sellerTaxId: "TAX-009876",
        items: [
            { id: 1, itemName: "Tablet", quantity: 3, unitPrice: "$350.00", total: "$1,050.00" },
            { id: 2, itemName: "Keyboard", quantity: 5, unitPrice: "$80.00", total: "$400.00" },
            { id: 3, itemName: "Monitor", quantity: 2, unitPrice: "$325.37", total: "$650.75" }
        ]
    }
};

const applicationData = {
    1: {
        appNumber: "APP-2023-001",
        applicantName: "John Smith",
        applicantId: "APP-001",
        applicantCountry: "United States",
        issueDate: "2023-05-10",
        totalAmount: "$500.00",
        status: "Approved"
    },
    2: {
        appNumber: "APP-2023-002",
        applicantName: "Maria Garcia",
        applicantId: "APP-002",
        applicantCountry: "Spain",
        issueDate: "2023-05-12",
        totalAmount: "$750.00",
        status: "Pending"
    },
    3: {
        appNumber: "APP-2023-003",
        applicantName: "David Johnson",
        applicantId: "APP-003",
        applicantCountry: "Canada",
        issueDate: "2023-05-14",
        totalAmount: "$1,200.00",
        status: "Rejected"
    }
};

var token;
var invoices;
var invoiceStatusMapper;

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
        $('.tab').removeClass('active');
        $(this).addClass('active');
        $('.tab-content').removeClass('active');
        $(`#${tabId}-tab`).addClass('active');
        if (tabId == "invoices") {
            getAllInvoices();
        }
    });

    // 查看发票详情
    $('.btn-view').on('click', function() {
        const row = $(this).closest('tr');
        const id = row.data('id');
        const isInvoice = row.closest('#invoices-tab').length > 0;

        if (isInvoice) {
            const invoice = invoiceData[id];

            if (invoice) {
                $('#viewInvoiceNumber').val(invoice.invoiceNumber);
                $('#viewInvoiceAmount').val(invoice.totalAmount);
                $('#viewInvoiceDate').val(invoice.issueDate);
                $('#viewInvoiceStatus').val(invoice.status);
                $('#viewInvoiceCompany').val(invoice.company);
                $('#viewInvoiceTaxId').val(invoice.sellerTaxId);

                // 填充发票项目
                const itemsTable = $('#invoiceItemsTable');
                itemsTable.empty();

                invoice.items.forEach(item => {
                    itemsTable.append(`
                        <tr>
                            <td>${item.id}</td>
                            <td>${item.itemName}</td>
                            <td>${item.quantity}</td>
                            <td>${item.unitPrice}</td>
                            <td>${item.total}</td>
                        </tr>
                    `);
                });

                $('#invoiceViewModal').fadeIn().addClass('active');
            }
        } else {
            const application = applicationData[id];

            if (application) {
                $('#viewAppNumber').val(application.appNumber);
                $('#viewAppName').val(application.applicantName);
                $('#viewAppId').val(application.applicantId);
                $('#viewAppCountry').val(application.applicantCountry);
                $('#viewAppDate').val(application.issueDate);
                $('#viewAppAmount').val(application.totalAmount);
                $('#viewAppStatus').val(application.status);

                $('#applicationViewModal').fadeIn().addClass('active');
            }
        }
    });

    // 作废操作
    $('.btn-discard').on('click', function() {
        const row = $(this).closest('tr');
        const id = row.data('id');
        const isInvoice = row.closest('#invoices-tab').length > 0;
        const number = isInvoice ? invoiceData[id].invoiceNumber : applicationData[id].appNumber;
        const type = isInvoice ? 'invoice' : 'application form';

        if (confirm(`Are you sure you want to discard this ${type}: ${number}?`)) {
            alert(`${type.charAt(0).toUpperCase() + type.slice(1)} ${number} has been discarded.`);
            // 在实际应用中，这里会有作废逻辑
        }
    });

    // 搜索功能
    $('.search-button').on('click', function() {
        const tabContent = $(this).closest('.tab-content');
        const searchTerm = tabContent.find('.search-input').val().toLowerCase();
        const isInvoice = tabContent.attr('id') === 'invoices-tab';

        tabContent.find('.data-table tbody tr').each(function() {
            const firstCell = $(this).find('td:first-child').text().toLowerCase();
            const secondCell = $(this).find('td:nth-child(2)').text().toLowerCase();

            if (firstCell.includes(searchTerm) || secondCell.includes(searchTerm)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });

    // 分页按钮点击事件
    $('.pagination button').on('click', function() {
        $(this).closest('.pagination').find('.active').removeClass('active');
        $(this).addClass('active');
        alert(`Loading page ${$(this).text()}`);
        // 在实际应用中，这里会有加载对应页数据的逻辑
    });

    // 创建按钮
    $('.add-button').on('click', function() {
        const tabContent = $(this).closest('.tab-content');
        const isInvoice = tabContent.attr('id') === 'invoices-tab';
        const type = isInvoice ? 'invoice' : 'application form';

        alert(`Opening create ${type} form...`);
        // 在实际应用中，这里会打开创建表单
    });

    // 登出按钮
    $('.logout-button').on('click', function() {
        if (confirm('Are you sure you want to logout?')) {
            alert('Logging out...');
            // 在实际应用中，这里会执行登出逻辑
        }
    });

    // 关闭模态框
    $('.close-button, .btn-cancel').on('click', function() {
        const modalOverlay = $(this).closest('.modal-overlay');
        modalOverlay.removeClass('active');
        setTimeout(() => {
            modalOverlay.fadeOut();
        }, 300);
    });

    // 点击模态框外部关闭
    $('.modal-overlay').on('click', function(e) {
        if (e.target === this) {
            $(this).removeClass('active');
            setTimeout(() => {
                $(this).fadeOut();
            }, 300);
        }
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

function initTableOperations() {
    document.querySelectorAll('.btn-view').forEach(button => {
        button.addEventListener('click', function() {
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

function formatInvoiceStatus(code) {
    for (var i = 0; i < invoiceStatusMapper.length; i++) {
        if (invoiceStatusMapper[i].code == code) {
            return invoiceStatusMapper[i].title;
        }
    }
    return null;
}