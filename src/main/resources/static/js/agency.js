// 退税方式数组 - 便于未来扩展
const refundMethods = [
    { id: 'cash', name: 'Cash', icon: 'money-bill-wave' },
    { id: 'bankcard', name: 'Bank Card', icon: 'credit-card' }
];

// 模拟申请单数据
const applicationData = [
    { appNumber: 'APP-2023-001', customsNumber: 'CUS-001', amount: 250.00 },
    { appNumber: 'APP-2023-002', customsNumber: 'CUS-002', amount: 175.50 },
    { appNumber: 'APP-2023-003', customsNumber: 'CUS-003', amount: 320.75 },
    { appNumber: 'APP-2023-004', customsNumber: 'CUS-004', amount: 89.99 }
];

// 使用jQuery实现功能
$(document).ready(function() {
    let currentStep = 1;
    let selectedApplications = [];
    let selectedRefundMethod = '';
    let totalRefundAmount = 0;

    // 初始化申请单表格
    function initializeApplicationsTable() {
        const tableBody = $('#applicationsTable');
        tableBody.empty();

        applicationData.forEach(app => {
            const row = `
                <tr data-app="${app.appNumber}">
                    <td>${app.appNumber}</td>
                    <td>${app.customsNumber}</td>
                    <td class="checkbox-container">
                        <input type="checkbox" class="select-checkbox" data-app="${app.appNumber}" data-amount="${app.amount}">
                    </td>
                </tr>
            `;
            tableBody.append(row);
        });
    }

    // 初始化退税方式选项
    function initializeRefundOptions() {
        const optionsContainer = $('#refundOptions');
        optionsContainer.empty();

        refundMethods.forEach(method => {
            const option = `
                <div class="refund-option" data-method="${method.id}">
                    <div class="option-icon">
                        <i class="fas fa-${method.icon}"></i>
                    </div>
                    <h3>${method.name}</h3>
                </div>
            `;
            optionsContainer.append(option);
        });
    }

    // 更新进度条
    function updateProgressBar(step) {
        $('.progress-step').removeClass('active');
        $(`.progress-step[data-step="${step}"]`).addClass('active');

        // 更新进度线
        const progressBar = $('#progressBar');
        const progressSteps = $('.progress-step');
        const progressPercent = ((step - 1) / (progressSteps.length - 1)) * 100;

        progressBar.css('--progress', `${progressPercent}%`);

        // 使用CSS变量更新进度线
        progressBar[0].style.setProperty('--progress', `${progressPercent}%`);
    }

    // 显示步骤
    function showStep(step) {
        $('.step-content').removeClass('active');
        $(`#step${step}`).addClass('active');
        currentStep = step;
        updateProgressBar(step);
    }

    // 验证步骤1
    function validateStep1() {
        const name = $('#applicantName').val().trim();
        const id = $('#applicantId').val().trim();
        const country = $('#applicantCountry').val();

        if (!name || !id || !country) {
            alert('Please fill in all personal information fields.');
            return false;
        }

        return true;
    }

    // 验证步骤2
    function validateStep2() {
        if (selectedApplications.length === 0) {
            alert('Please select at least one application form.');
            return false;
        }

        return true;
    }

    // 验证步骤4
    function validateStep4() {
        if (!selectedRefundMethod) {
            alert('Please select a refund method.');
            return false;
        }

        if (selectedRefundMethod === 'bankcard') {
            const cardNumber = $('#bankCardNumber').val().trim();
            const cardHolder = $('#bankCardHolder').val().trim();
            const bankName = $('#bankName').val().trim();

            if (!cardNumber || !cardHolder || !bankName) {
                alert('Please fill in all bank card details.');
                return false;
            }
        }

        return true;
    }

    // 更新收据信息
    function updateReceipt() {
        $('#receiptName').text($('#applicantName').val());
        $('#receiptId').text($('#applicantId').val());
        $('#receiptCountry').text($('#applicantCountry').val());

        // 设置退款方式显示文本
        const methodText = refundMethods.find(m => m.id === selectedRefundMethod)?.name || '';
        $('#receiptMethod').text(methodText);

        // 计算总退款金额
        totalRefundAmount = 0;
        selectedApplications.forEach(app => {
            const appData = applicationData.find(a => a.appNumber === app);
            if (appData) {
                totalRefundAmount += appData.amount;
            }
        });

        $('#receiptAmount').text(`$${totalRefundAmount.toFixed(2)}`);

        // 显示选中的申请单
        $('#receiptApplications').text(selectedApplications.join(', '));

        // 设置当前日期
        const today = new Date();
        const formattedDate = today.toISOString().split('T')[0];
        $('#receiptDate').text(formattedDate);
    }

    // 初始化
    initializeApplicationsTable();
    initializeRefundOptions();
    updateProgressBar(1);

    // 事件绑定

    // 模拟护照扫描
    $('#passportScan').on('click', function() {
        alert('Passport scanning simulated. Personal information populated.');
        $('#applicantName').val('John Smith');
        $('#applicantId').val('PASS123456');
        $('#applicantCountry').val('United States');
    });

    // 步骤1下一步
    $('#step1Next').on('click', function() {
        if (validateStep1()) {
            showStep(2);
        }
    });

    // 步骤2上一步
    $('#step2Prev').on('click', function() {
        showStep(1);
    });

    // 步骤2下一步
    $('#step2Next').on('click', function() {
        if (validateStep2()) {
            showStep(3);
        }
    });

    // 申请单选择
    $(document).on('change', '.select-checkbox', function() {
        const appNumber = $(this).data('app');
        const isChecked = $(this).is(':checked');

        if (isChecked) {
            if (!selectedApplications.includes(appNumber)) {
                selectedApplications.push(appNumber);
            }
        } else {
            selectedApplications = selectedApplications.filter(app => app !== appNumber);
        }

        console.log('Selected applications:', selectedApplications);
    });

    // 步骤3提交
    $('#step3Submit').on('click', function() {
        alert('Application forms submitted successfully!');
        $('#step3Next').show();
    });

    // 步骤3下一步
    $('#step3Next').on('click', function() {
        showStep(4);
    });

    // 步骤3上一步
    $('#step3Prev').on('click', function() {
        showStep(2);
    });

    // 步骤4上一步
    $('#step4Prev').on('click', function() {
        showStep(3);
    });

    // 步骤4下一步
    $('#step4Next').on('click', function() {
        if (validateStep4()) {
            updateReceipt();
            showStep(5);
        }
    });

    // 退税方式选择
    $(document).on('click', '.refund-option', function() {
        $('.refund-option').removeClass('selected');
        $(this).addClass('selected');

        selectedRefundMethod = $(this).data('method');

        // 显示或隐藏银行信息
        if (selectedRefundMethod === 'bankcard') {
            $('#bankDetails').removeClass('hidden');
        } else {
            $('#bankDetails').addClass('hidden');
        }
    });

    // 步骤5上一步
    $('#step5Prev').on('click', function() {
        showStep(4);
    });

    // 打印收据
    $('#step5Print').on('click', function() {
        alert('Receipt printed successfully!');
        // 在实际应用中，这里会调用打印功能
    });

    // 登出按钮
    $('.logout-button').on('click', function() {
        if (confirm('Are you sure you want to logout?')) {
            alert('Logging out...');
            // 在实际应用中，这里会执行登出逻辑
        }
    });
});