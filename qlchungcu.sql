-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: localhost:3307
-- Thời gian đã tạo: Th12 14, 2024 lúc 03:14 PM
-- Phiên bản máy phục vụ: 8.0.30
-- Phiên bản PHP: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `qlchungcu`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `apartment`
--

CREATE TABLE `apartment` (
  `id` bigint NOT NULL,
  `acreage` float DEFAULT NULL,
  `floor` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `is_sold` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `apartment`
--

INSERT INTO `apartment` (`id`, `acreage`, `floor`, `name`, `price`, `is_sold`) VALUES
(4, 75, 1, '101', 1500000, b'1'),
(5, 80, 1, '102', 17000000, b'1'),
(6, 80, 1, '103', 17000000, b'0'),
(7, 80, 1, '104', 17000000, b'1'),
(8, 80, 1, '105', 17000000, b'0'),
(9, 85, 2, '201', 18000000, b'0'),
(10, 85, 2, '202', 18000000, b'0'),
(11, 85, 2, '203', 18000000, b'0'),
(12, 85, 2, '204', 18000000, b'0'),
(13, 85, 2, '205', 18000000, b'0'),
(14, 80, 3, '301', 17000000, b'1'),
(15, 80, 3, '302', 17000000, b'0'),
(16, 80, 3, '303', 17000000, b'0'),
(17, 80, 3, '304', 17000000, b'0'),
(19, 85, 3, '305', 560000000, b'0'),
(20, 60, 4, '401', 250000000, b'0'),
(21, 67, 4, '402', 450000000, b'0'),
(22, 66, 4, '403', 45000000, b'0'),
(23, 78, 4, '404', 670000000, b'0'),
(24, 79, 4, '405', 78000000, b'1');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `maintenance`
--

CREATE TABLE `maintenance` (
  `id` bigint NOT NULL,
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `created_date` datetime DEFAULT NULL,
  `expected_completion_date` date DEFAULT NULL,
  `maintenance_date` date DEFAULT NULL,
  `title` varchar(5000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `created_by_id` bigint DEFAULT NULL,
  `completed` bit(1) DEFAULT NULL,
  `expected_completion_time` varchar(8) DEFAULT NULL,
  `maintenance_time` varchar(8) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `maintenance`
--

INSERT INTO `maintenance` (`id`, `content`, `created_date`, `expected_completion_date`, `maintenance_date`, `title`, `created_by_id`, `completed`, `expected_completion_time`, `maintenance_time`) VALUES
(2, '<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">Chúng tôi rất xin lỗi phải gửi thông báo này đến các cư dân</span></p><p><span style=\"font-family: &quot;&quot;;\">thang máy tầng 3 đã bị hỏng, chúng tôi cần sửa chữa</span></p><p><span style=\"font-family: &quot;&quot;; font-weight: bold;\">các bạn hãy đi thang bộ nhé</span></p></body></html>', '2024-12-01 09:41:25', '2024-12-02', '2024-12-02', 'Thông báo sửa thang máy', NULL, b'0', '19:40', '09:40'),
(3, '<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">oke đấy b</span></p><p><span style=\"font-family: &quot;&quot;;\">fwegifwef</span></p><p><span style=\"font-family: &quot;&quot;;\">fwefwef</span></p></body></html>', '2024-12-01 10:55:40', '2024-12-06', '2024-12-13', 'heheh', NULL, b'0', '10:55', '10:55'),
(4, '<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">tôi xin chân trọng thông báo</span></p><p><span style=\"font-family: &quot;&quot;;\">sửa cổng chính nhà để xe</span></p><p><span style=\"font-family: &quot;&quot;; font-weight: bold;\">không đi cổng đó</span></p></body></html>', '2024-12-02 08:17:12', '2024-12-12', '2024-12-04', 'Thông báo sửa cổng', NULL, b'0', '08:16', '08:16'),
(6, '<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p><span style=\"font-family: &quot;&quot;;\">đây là nội dung gửi utf-8</span></p></body></html>', '2024-12-10 10:41:36', '2024-12-10', '2024-12-10', 'Test fix người tạo bảo trì', 1, b'0', '13:40', '12:40');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `notification`
--

CREATE TABLE `notification` (
  `id` bigint NOT NULL,
  `content` varchar(255) DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `is_read` bit(1) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `repair_request`
--

CREATE TABLE `repair_request` (
  `id` bigint NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `fee` double DEFAULT NULL,
  `fixed` bit(1) DEFAULT NULL,
  `paid` bit(1) DEFAULT NULL,
  `request_date` datetime DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL,
  `cancel_date` datetime DEFAULT NULL,
  `canceled` bit(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `repair_request`
--

INSERT INTO `repair_request` (`id`, `description`, `fee`, `fixed`, `paid`, `request_date`, `apartment_id`, `cancel_date`, `canceled`) VALUES
(2, '<p>t&ocirc;i cần sửa bồn vệ sinh</p>\r\n<p>nhanh gi&uacute;p t&ocirc;i update</p>', 120000, b'0', b'0', '2024-12-02 21:25:24', 24, NULL, NULL),
(3, '<p>T&ocirc;i muốn thay b&oacute;ng điẹn</p>\r\n<p>trước 3h chiều nay</p>', 300000, b'0', b'0', '2024-12-02 23:44:11', 24, NULL, NULL),
(4, '<p>cần sửa điện</p>', 0, b'0', b'0', '2024-12-10 11:22:48', 4, '2024-12-10 11:27:39', b'1');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `resident`
--

CREATE TABLE `resident` (
  `id` bigint NOT NULL,
  `bod` date DEFAULT NULL,
  `cic` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `is_household_head` bit(1) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `resident`
--

INSERT INTO `resident` (`id`, `bod`, `cic`, `email`, `full_name`, `image`, `is_household_head`, `password`, `phone`, `username`, `apartment_id`) VALUES
(1, '2024-11-07', '324235345435345', 'hieutran2102000@gmail.com', 'Hoàng hùng', 'http://res.cloudinary.com/dxqh3xpza/image/upload/v1732940495/olryfwilhajwkzismogh.jpg', b'1', '12345', '093478234', 'hieutran2102000@gmail.com', 24),
(2, '2023-12-06', '9000032400000', 'doanlongvu2404@gmail.com', 'Doan Long Vu', 'http://res.cloudinary.com/dxqh3xpza/image/upload/v1733157534/zn5klnrwo1n3kgvteade.jpg', b'0', '12345', '098334234', 'doanlongvu2404@gmail.com', 24),
(3, '2024-12-04', '0374235345435', 'hieutran02102804@gmail.com', 'Hoàng minh hiếu', 'http://res.cloudinary.com/dxqh3xpza/image/upload/v1733363586/sghshnqkdwwbmo42tcja.jpg', b'1', '12345', '097732423', 'hieutran02102804@gmail.com', 4),
(4, '2024-12-03', '93947385345', 'hieuvnua3@gmail.com', 'Lê tùng hoàng', 'http://res.cloudinary.com/dxqh3xpza/image/upload/v1733363656/huevg0lkhswilir4jq5x.jpg', b'1', '12345', '0978645353', 'hieuvnua3@gmail.com', 5),
(5, '2024-12-04', '213472348234', 'dev002102@gmail.com', 'Hoàng minh nghĩa', 'http://res.cloudinary.com/dxqh3xpza/image/upload/v1733363744/gcxrdwixdkadfzi1mlr6.jpg', b'1', '12345', '0978342343', 'dev002102@gmail.com', 7),
(6, '2024-12-04', '937483534534', 'mailfake@gmail.com', 'Ha na', 'http://res.cloudinary.com/dxqh3xpza/image/upload/v1733397237/o6gblpxdbpv6ynpuk6pc.jpg', b'1', '12345', '0932478235', 'mailfake@gmail.com', 14);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `service_fee`
--

CREATE TABLE `service_fee` (
  `id` bigint NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `fee` double DEFAULT NULL,
  `month` int DEFAULT NULL,
  `paid_status` bit(1) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `service_fee`
--

INSERT INTO `service_fee` (`id`, `created_date`, `fee`, `month`, `paid_status`, `year`, `apartment_id`) VALUES
(1, '2024-12-05 10:02:53', 975000, 12, b'1', 2024, 4),
(2, '2024-12-05 10:02:53', 1040000, 12, b'0', 2024, 5),
(4, '2024-12-05 10:02:53', 1027000, 12, b'1', 2024, 24),
(5, '2024-12-05 15:38:06', 1040000, 12, b'0', 2024, 7),
(6, '2024-12-05 18:17:18', 1040000, 12, b'1', 2024, 14);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `id` bigint NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `users`
--

INSERT INTO `users` (`id`, `password`, `full_name`, `username`) VALUES
(1, 'admin', 'Admin', 'admin');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `utility_bill`
--

CREATE TABLE `utility_bill` (
  `id` bigint NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `electricity_amount` float DEFAULT NULL,
  `electricity_index` float DEFAULT NULL,
  `electricity_index_pre_month` float DEFAULT NULL,
  `fee` double DEFAULT NULL,
  `month` int DEFAULT NULL,
  `num_electricity` float DEFAULT NULL,
  `num_water` float DEFAULT NULL,
  `paid_status` bit(1) DEFAULT NULL,
  `water_amount` float DEFAULT NULL,
  `water_index` float DEFAULT NULL,
  `water_index_pre_month` float DEFAULT NULL,
  `year` int DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `utility_bill`
--

INSERT INTO `utility_bill` (`id`, `created_date`, `electricity_amount`, `electricity_index`, `electricity_index_pre_month`, `fee`, `month`, `num_electricity`, `num_water`, `paid_status`, `water_amount`, `water_index`, `water_index_pre_month`, `year`, `apartment_id`) VALUES
(1, '2024-12-05 10:02:53', NULL, 127, NULL, 356809, 12, 127, 13, b'1', NULL, 12, NULL, 2024, 4),
(2, '2024-12-05 10:02:53', NULL, 250, NULL, 1015750, 12, 250, 35, b'0', NULL, 35, NULL, 2024, 5),
(4, '2024-12-05 10:02:53', NULL, 206, NULL, 1030674, 12, 206, 40, b'1', NULL, 40, NULL, 2024, 24),
(5, '2024-12-05 15:38:06', NULL, 175, NULL, 960125, 12, 175, 40, b'0', NULL, 40, NULL, 2024, 7),
(6, '2024-12-05 18:17:18', NULL, 200, NULL, 584300, 12, 200, 20, b'0', NULL, 20, NULL, 2024, 14);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vehicle`
--

CREATE TABLE `vehicle` (
  `id` bigint NOT NULL,
  `license_plate` varchar(255) DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `vehicle_type` int DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `update_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `vehicle`
--

INSERT INTO `vehicle` (`id`, `license_plate`, `quantity`, `vehicle_type`, `apartment_id`, `created_date`, `update_date`) VALUES
(1, '30H-11 941145', NULL, 1, 24, '2024-12-02', NULL),
(3, '30H-11 94114554', NULL, 0, 24, '2024-12-02', '2024-12-02'),
(4, '30H-11 941145', NULL, 2, 24, '2024-12-02', NULL),
(7, '90AC 67891', NULL, 1, 24, '2024-12-02', NULL),
(9, '30H-11 941145', NULL, 2, 4, '2024-12-11', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vehicle_fee`
--

CREATE TABLE `vehicle_fee` (
  `id` bigint NOT NULL,
  `created_date` datetime DEFAULT NULL,
  `fee` double DEFAULT NULL,
  `month` int DEFAULT NULL,
  `paid_status` bit(1) DEFAULT NULL,
  `year` int DEFAULT NULL,
  `apartment_id` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `vehicle_fee`
--

INSERT INTO `vehicle_fee` (`id`, `created_date`, `fee`, `month`, `paid_status`, `year`, `apartment_id`) VALUES
(5, '2024-12-05 10:02:53', 2300000, 12, b'0', 2024, 24),
(6, '2024-12-05 10:47:05', 0, 12, b'0', 2024, 4),
(7, '2024-12-05 10:47:05', 0, 12, b'0', 2024, 5),
(9, '2024-12-05 15:38:06', 0, 12, b'0', 2024, 7),
(10, '2024-12-05 18:17:18', 0, 12, b'0', 2024, 14);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `vehicle_service_fee`
--

CREATE TABLE `vehicle_service_fee` (
  `id` bigint NOT NULL,
  `fee` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Đang đổ dữ liệu cho bảng `vehicle_service_fee`
--

INSERT INTO `vehicle_service_fee` (`id`, `fee`, `name`) VALUES
(1, 2200000, 'Xe ô tô'),
(2, 150000, 'Xe máy'),
(3, 0, 'Xe đạp');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `apartment`
--
ALTER TABLE `apartment`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `maintenance`
--
ALTER TABLE `maintenance`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKs16o462y9usppp7rauu41mxua` (`created_by_id`);

--
-- Chỉ mục cho bảng `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKnk4ftb5am9ubmkv1661h15ds9` (`user_id`);

--
-- Chỉ mục cho bảng `repair_request`
--
ALTER TABLE `repair_request`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKkx3200jgoh5ekjjiil4e7yqfx` (`apartment_id`);

--
-- Chỉ mục cho bảng `resident`
--
ALTER TABLE `resident`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK26401pp7xa5ab4a4l62l5llnq` (`apartment_id`);

--
-- Chỉ mục cho bảng `service_fee`
--
ALTER TABLE `service_fee`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK903y1s59yi03ait9eiq2j1d9c` (`apartment_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `utility_bill`
--
ALTER TABLE `utility_bill`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKdigwi7koc5hdbc9t7lyp4dj0p` (`apartment_id`);

--
-- Chỉ mục cho bảng `vehicle`
--
ALTER TABLE `vehicle`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKm6y4s31aoyjr0aej4detratnh` (`apartment_id`);

--
-- Chỉ mục cho bảng `vehicle_fee`
--
ALTER TABLE `vehicle_fee`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK52fgf5miwe3dst0osysta3ejh` (`apartment_id`);

--
-- Chỉ mục cho bảng `vehicle_service_fee`
--
ALTER TABLE `vehicle_service_fee`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `apartment`
--
ALTER TABLE `apartment`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT cho bảng `maintenance`
--
ALTER TABLE `maintenance`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `notification`
--
ALTER TABLE `notification`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `repair_request`
--
ALTER TABLE `repair_request`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT cho bảng `resident`
--
ALTER TABLE `resident`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `service_fee`
--
ALTER TABLE `service_fee`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT cho bảng `utility_bill`
--
ALTER TABLE `utility_bill`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `vehicle`
--
ALTER TABLE `vehicle`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `vehicle_fee`
--
ALTER TABLE `vehicle_fee`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `vehicle_service_fee`
--
ALTER TABLE `vehicle_service_fee`
  MODIFY `id` bigint NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `maintenance`
--
ALTER TABLE `maintenance`
  ADD CONSTRAINT `FKs16o462y9usppp7rauu41mxua` FOREIGN KEY (`created_by_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `FKnk4ftb5am9ubmkv1661h15ds9` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Các ràng buộc cho bảng `repair_request`
--
ALTER TABLE `repair_request`
  ADD CONSTRAINT `FKkx3200jgoh5ekjjiil4e7yqfx` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`);

--
-- Các ràng buộc cho bảng `resident`
--
ALTER TABLE `resident`
  ADD CONSTRAINT `FK26401pp7xa5ab4a4l62l5llnq` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`);

--
-- Các ràng buộc cho bảng `service_fee`
--
ALTER TABLE `service_fee`
  ADD CONSTRAINT `FK903y1s59yi03ait9eiq2j1d9c` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`);

--
-- Các ràng buộc cho bảng `utility_bill`
--
ALTER TABLE `utility_bill`
  ADD CONSTRAINT `FKdigwi7koc5hdbc9t7lyp4dj0p` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`);

--
-- Các ràng buộc cho bảng `vehicle`
--
ALTER TABLE `vehicle`
  ADD CONSTRAINT `FKm6y4s31aoyjr0aej4detratnh` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`);

--
-- Các ràng buộc cho bảng `vehicle_fee`
--
ALTER TABLE `vehicle_fee`
  ADD CONSTRAINT `FK52fgf5miwe3dst0osysta3ejh` FOREIGN KEY (`apartment_id`) REFERENCES `apartment` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
