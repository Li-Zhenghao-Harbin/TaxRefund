-- ----------------------------
-- Table structure for b_application_form
-- ----------------------------
DROP TABLE IF EXISTS `b_application_form`;
CREATE TABLE `b_application_form`  (
  `application_form_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `applicant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `applicant_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `applicant_country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `issue_date` datetime NOT NULL,
  `issue_merchant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `total_amount` decimal(16, 2) NOT NULL,
  `customs_confirm_amount` decimal(16, 2) NULL DEFAULT NULL,
  `status` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`application_form_number`) USING BTREE,
  INDEX `idx_merchant_date`(`issue_date`, `issue_merchant_name`) USING BTREE,
  INDEX `idx_applicant_info_date`(`applicant_name`, `applicant_id`, `applicant_country`, `issue_date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_application_form
-- ----------------------------
INSERT INTO `b_application_form` VALUES ('20251127000001', 'Tony', 'FW838030', 'BR', '2025-11-27 12:47:12', 'debug_merchant', 250.00, NULL, 1);
INSERT INTO `b_application_form` VALUES ('20251127000002', 'Tony', 'FW838030', 'BR', '2025-11-27 14:21:49', 'debug_merchant', 1372.00, 622.00, 3);
INSERT INTO `b_application_form` VALUES ('20251202000001', 'David', 'AB131415', 'AU', '2025-12-02 14:44:35', 'debug_merchant', 340.00, 320.00, 3);
INSERT INTO `b_application_form` VALUES ('20251202000002', 'Peter', 'DT130409', 'DE', '2025-12-02 14:51:15', 'debug_merchant', 225.00, NULL, 1);

-- ----------------------------
-- Table structure for b_bank_card
-- ----------------------------
DROP TABLE IF EXISTS `b_bank_card`;
CREATE TABLE `b_bank_card`  (
  `application_form_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `bank_card_number` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `bank_card_holder` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`application_form_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_bank_card
-- ----------------------------
INSERT INTO `b_bank_card` VALUES ('20251127000002', '1324178932120983', 'Tony', 'Bank of America');

-- ----------------------------
-- Table structure for b_invoice
-- ----------------------------
DROP TABLE IF EXISTS `b_invoice`;
CREATE TABLE `b_invoice`  (
  `invoice_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `issue_merchant_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `total_amount` decimal(16, 2) NOT NULL,
  `issue_date` datetime NOT NULL,
  `application_form_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`invoice_number`) USING BTREE,
  INDEX `idx_application_form_number`(`application_form_number`) USING BTREE,
  INDEX `idx_merchant_date`(`issue_merchant_name`, `status`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_invoice
-- ----------------------------
INSERT INTO `b_invoice` VALUES ('2025112700000001', 'debug_merchant', 250.00, '2025-11-27 12:18:37', '20251127000001', 2);
INSERT INTO `b_invoice` VALUES ('2025112700000002', 'debug_merchant', 1150.00, '2025-11-27 12:20:16', '20251127000002', 2);
INSERT INTO `b_invoice` VALUES ('2025112700000003', 'debug_merchant', 222.00, '2025-11-27 12:43:59', '20251127000002', 2);
INSERT INTO `b_invoice` VALUES ('2025120200000001', 'debug_merchant', 20.00, '2025-12-02 14:40:19', '20251202000001', 2);
INSERT INTO `b_invoice` VALUES ('2025120200000002', 'debug_merchant', 320.00, '2025-12-02 14:43:46', '20251202000001', 2);
INSERT INTO `b_invoice` VALUES ('2025120200000003', 'debug_merchant', 225.00, '2025-12-02 14:50:19', '20251202000002', 2);

-- ----------------------------
-- Table structure for b_item
-- ----------------------------
DROP TABLE IF EXISTS `b_item`;
CREATE TABLE `b_item`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `invoice_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `item_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `quantity` int NOT NULL,
  `unit_price` decimal(16, 2) NOT NULL,
  `status` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_invoice_status`(`invoice_number`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_item
-- ----------------------------
INSERT INTO `b_item` VALUES (1, '2025112700000001', 'Cup', 10, 25.00, 1);
INSERT INTO `b_item` VALUES (2, '2025112700000002', 'Nike Shoes', 1, 600.00, -1);
INSERT INTO `b_item` VALUES (3, '2025112700000002', 'Adidas Shoes', 1, 550.00, 2);
INSERT INTO `b_item` VALUES (4, '2025112700000003', 'Pen', 10, 15.00, -1);
INSERT INTO `b_item` VALUES (5, '2025112700000003', 'Pencil', 12, 6.00, 2);
INSERT INTO `b_item` VALUES (6, '2025120200000001', 'Bottle', 2, 10.00, -1);
INSERT INTO `b_item` VALUES (7, '2025120200000002', 'Bag', 4, 80.00, 2);
INSERT INTO `b_item` VALUES (8, '2025120200000003', 'Soap', 5, 45.00, 1);

-- ----------------------------
-- Table structure for b_seller
-- ----------------------------
DROP TABLE IF EXISTS `b_seller`;
CREATE TABLE `b_seller`  (
  `merchant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `seller_tax_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`merchant_name`) USING BTREE,
  INDEX `idx_merchant_name`(`merchant_name`) USING BTREE,
  CONSTRAINT `f_user` FOREIGN KEY (`merchant_name`) REFERENCES `b_user` (`name`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_seller
-- ----------------------------
INSERT INTO `b_seller` VALUES ('debug_merchant', 'XXX Group', '11429179082');

-- ----------------------------
-- Table structure for b_sequence
-- ----------------------------
DROP TABLE IF EXISTS `b_sequence`;
CREATE TABLE `b_sequence`  (
  `business` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `current` int NOT NULL DEFAULT 0,
  `step` int NOT NULL,
  PRIMARY KEY (`business`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_sequence
-- ----------------------------
INSERT INTO `b_sequence` VALUES ('application_form', 3, 1);
INSERT INTO `b_sequence` VALUES ('invoice', 4, 1);

-- ----------------------------
-- Table structure for b_tax_refund
-- ----------------------------
DROP TABLE IF EXISTS `b_tax_refund`;
CREATE TABLE `b_tax_refund`  (
  `application_form_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tax_refund_method` int NOT NULL,
  `tax_refund_date` datetime NOT NULL,
  `application_form_material` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `invoice_material` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`application_form_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_tax_refund
-- ----------------------------
INSERT INTO `b_tax_refund` VALUES ('20251127000002', 2, '2025-11-27 16:20:40', 'xxx/applicationForm/20251127000002.pdf', 'xxx/invoice/20251127000002.pdf');
INSERT INTO `b_tax_refund` VALUES ('20251202000001', 1, '2025-12-02 14:54:03', 'xxx/applicationForm/20251202000001.pdf', 'xxx/invoice/20251202000001.pdf');

-- ----------------------------
-- Table structure for b_user
-- ----------------------------
DROP TABLE IF EXISTS `b_user`;
CREATE TABLE `b_user`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role` int NOT NULL,
  `status` int NOT NULL DEFAULT 1,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of b_user
-- ----------------------------
INSERT INTO `b_user` VALUES ('333', 'MzMz', 2, 0);
INSERT INTO `b_user` VALUES ('administer', 'MTIz', 0, 1);
INSERT INTO `b_user` VALUES ('debug_agency', 'MTIz', 3, 1);
INSERT INTO `b_user` VALUES ('debug_customs', 'MTIz', 2, 1);
INSERT INTO `b_user` VALUES ('debug_merchant', 'MTIz', 1, 1);

-- ----------------------------
-- Table structure for c_role
-- ----------------------------
DROP TABLE IF EXISTS `c_role`;
CREATE TABLE `c_role`  (
  `code` int NOT NULL,
  `title` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_role
-- ----------------------------
INSERT INTO `c_role` VALUES (0, 'Manager');
INSERT INTO `c_role` VALUES (1, 'Merchant');
INSERT INTO `c_role` VALUES (2, 'Customs');
INSERT INTO `c_role` VALUES (3, 'Agency');

-- ----------------------------
-- Table structure for c_status
-- ----------------------------
DROP TABLE IF EXISTS `c_status`;
CREATE TABLE `c_status`  (
  `code` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `business` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`code`, `business`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_status
-- ----------------------------
INSERT INTO `c_status` VALUES (-1, 'Discard', 'Invoice');
INSERT INTO `c_status` VALUES (-1, 'Rejected', 'Item');
INSERT INTO `c_status` VALUES (0, 'Unavailable', 'User');
INSERT INTO `c_status` VALUES (1, 'Issued', 'Application form');
INSERT INTO `c_status` VALUES (1, 'Issued', 'Invoice');
INSERT INTO `c_status` VALUES (1, 'Not reviewed', 'Item');
INSERT INTO `c_status` VALUES (1, 'Available', 'User');
INSERT INTO `c_status` VALUES (2, 'Reviewed', 'Application form');
INSERT INTO `c_status` VALUES (2, 'Related to application form', 'Invoice');
INSERT INTO `c_status` VALUES (2, 'Approved', 'Item');
INSERT INTO `c_status` VALUES (3, 'Tax refunded', 'Application form');

-- ----------------------------
-- Table structure for c_tax_refund_method
-- ----------------------------
DROP TABLE IF EXISTS `c_tax_refund_method`;
CREATE TABLE `c_tax_refund_method`  (
  `code` int NOT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of c_tax_refund_method
-- ----------------------------
INSERT INTO `c_tax_refund_method` VALUES (1, 'Cash');
INSERT INTO `c_tax_refund_method` VALUES (2, 'Bank card');

SET FOREIGN_KEY_CHECKS = 1;
