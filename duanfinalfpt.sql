-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 23, 2020 lúc 04:45 PM
-- Phiên bản máy phục vụ: 10.4.13-MariaDB
-- Phiên bản PHP: 7.2.32

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `duanfinalfpt`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `account`
--

CREATE TABLE `account` (
  `stt` int(11) NOT NULL,
  `email` varchar(20) CHARACTER SET utf8 NOT NULL,
  `pass` varchar(20) CHARACTER SET utf8 NOT NULL,
  `roles` varchar(20) CHARACTER SET utf8 NOT NULL,
  `magiaovien` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `mahocsinh` varchar(10) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `account`
--

INSERT INTO `account` (`stt`, `email`, `pass`, `roles`, `magiaovien`, `mahocsinh`) VALUES
(1, 'a@gmail.com', '123', 'BGH', NULL, NULL),
(2, 'b', '123', 'HC', NULL, NULL),
(3, 'c@gmail.com', '123', 'GV', NULL, NULL),
(4, 'h', '123', 'GV', 'gv01', NULL),
(5, 'f', '123', 'GV', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `danhgia`
--

CREATE TABLE `danhgia` (
  `stt` int(11) NOT NULL,
  `ngay` varchar(10) CHARACTER SET utf8 NOT NULL,
  `magiaovien` varchar(10) CHARACTER SET utf8 NOT NULL,
  `mahocsinh` varchar(10) CHARACTER SET utf8 NOT NULL,
  `noidung` varchar(100) CHARACTER SET utf8 NOT NULL,
  `hocki` bit(1) DEFAULT NULL,
  `manamhoc` varchar(10) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `diem`
--

CREATE TABLE `diem` (
  `stt` int(11) NOT NULL,
  `ngay` varchar(10) CHARACTER SET utf8 NOT NULL,
  `mahocsinh` varchar(10) CHARACTER SET utf8 NOT NULL,
  `diemMieng1` int(11) DEFAULT 0,
  `diemMieng2` int(11) DEFAULT 0,
  `diemMieng3` int(11) DEFAULT 0,
  `diem15phut1` int(11) DEFAULT 0,
  `diem15phut2` int(11) DEFAULT 0,
  `diem15phut3` int(11) DEFAULT 0,
  `diem1Tiet1` decimal(3,1) DEFAULT 0.0,
  `diem1Tiet2` decimal(3,1) DEFAULT 0.0,
  `diemthi` decimal(3,1) DEFAULT 0.0,
  `diemTBM` decimal(3,2) DEFAULT 0.00,
  `mapc` varchar(10) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `diem`
--

INSERT INTO `diem` (`stt`, `ngay`, `mahocsinh`, `diemMieng1`, `diemMieng2`, `diemMieng3`, `diem15phut1`, `diem15phut2`, `diem15phut3`, `diem1Tiet1`, `diem1Tiet2`, `diemthi`, `diemTBM`, `mapc`) VALUES
(16, '2020-10-23', 'hs01', 5, 5, 5, 5, 5, 5, '8.5', '8.5', '8.5', '7.00', 'pc01'),
(17, '2020-10-23', 'hs02', 0, 0, 0, 0, 0, 0, '0.0', '0.0', '0.0', '0.00', 'pc01'),
(18, '2020-10-23', 'hs03', 0, 0, 0, 0, 0, 0, '0.0', '0.0', '0.0', '0.00', 'pc01'),
(19, '2020-10-23', 'hs04', 0, 0, 0, 0, 0, 0, '0.0', '0.0', '0.0', '0.00', 'pc01'),
(20, '2020-10-23', 'hs01', 6, 6, 6, 6, 6, 6, '6.6', '6.6', '6.6', '6.00', 'pc02'),
(21, '2020-10-23', 'hs02', 5, 5, 5, 5, 5, 5, '5.0', '5.0', '5.0', '5.00', 'pc02'),
(22, '2020-10-23', 'hs03', 0, 0, 0, 0, 0, 0, '0.0', '0.0', '0.0', '0.00', 'pc02'),
(23, '2020-10-23', 'hs04', 0, 0, 0, 0, 0, 0, '0.0', '0.0', '0.0', '0.00', 'pc02');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `diemdanh`
--

CREATE TABLE `diemdanh` (
  `stt` int(11) NOT NULL,
  `ngay` varchar(10) CHARACTER SET utf8 NOT NULL,
  `magiaovien` varchar(10) CHARACTER SET utf8 NOT NULL,
  `mahocsinh` varchar(10) CHARACTER SET utf8 NOT NULL,
  `trangthai` bit(1) NOT NULL,
  `hocki` bit(1) DEFAULT NULL,
  `manamhoc` varchar(10) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `giaovien`
--

CREATE TABLE `giaovien` (
  `id` int(11) NOT NULL,
  `magiaovien` varchar(10) CHARACTER SET utf8 NOT NULL,
  `hoten` varchar(50) CHARACTER SET utf8 NOT NULL,
  `gioitinh` bit(1) NOT NULL,
  `ngaysinh` date DEFAULT NULL,
  `diachi` varchar(50) CHARACTER SET utf8 NOT NULL,
  `dienthoai` varchar(12) CHARACTER SET utf8 NOT NULL,
  `cmnd` varchar(12) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `giaovien`
--

INSERT INTO `giaovien` (`id`, `magiaovien`, `hoten`, `gioitinh`, `ngaysinh`, `diachi`, `dienthoai`, `cmnd`) VALUES
(1, 'gv01', 'Nguyễn Thế Hanh', b'1', '1996-12-05', 'Nam Định', '0911527745', '163448363'),
(2, 'gv02', 'Đõ Thị Thúy HÀ', b'0', '1996-12-05', 'Nam Định', '0912132187', '163448363');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hocsinh`
--

CREATE TABLE `hocsinh` (
  `id` int(11) NOT NULL,
  `mahocsinh` varchar(10) CHARACTER SET utf8 NOT NULL,
  `hoten` varchar(50) CHARACTER SET utf8 NOT NULL,
  `gioitinh` bit(1) NOT NULL,
  `ngaysinh` date DEFAULT NULL,
  `diachi` varchar(50) CHARACTER SET utf8 NOT NULL,
  `dienthoai` varchar(20) CHARACTER SET utf8 NOT NULL,
  `dantoc` varchar(20) CHARACTER SET utf8 NOT NULL,
  `tongiao` varchar(20) CHARACTER SET utf8 NOT NULL,
  `ngayvaodoan` date DEFAULT NULL,
  `noisinh` varchar(50) CHARACTER SET utf8 NOT NULL,
  `cmnd` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `lop` varchar(10) CHARACTER SET utf8 NOT NULL,
  `hotenBo` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `hotenMe` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `dienthoaiBo` varchar(12) CHARACTER SET utf8 DEFAULT NULL,
  `dienthoaiMe` varchar(12) CHARACTER SET utf8 DEFAULT NULL,
  `dvCongTacBo` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `dvCongTacMe` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `nguoidamho` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `trangthai` bit(1) NOT NULL DEFAULT b'1',
  `anh` varchar(100) CHARACTER SET utf8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `hocsinh`
--

INSERT INTO `hocsinh` (`id`, `mahocsinh`, `hoten`, `gioitinh`, `ngaysinh`, `diachi`, `dienthoai`, `dantoc`, `tongiao`, `ngayvaodoan`, `noisinh`, `cmnd`, `lop`, `hotenBo`, `hotenMe`, `dienthoaiBo`, `dienthoaiMe`, `dvCongTacBo`, `dvCongTacMe`, `nguoidamho`, `trangthai`, `anh`) VALUES
(1, 'hs01', 'Nguyễn Tuấn Anh', b'1', '1998-07-21', 'TT.Cát Thành', '03503944187', 'Kinh', 'Không', NULL, 'Nam Đinh', NULL, 'lh01', NULL, 'Đỗ Thị Thúy Hà', NULL, NULL, NULL, NULL, NULL, b'1', NULL),
(2, 'hs02', 'Nguyễn Phương Thảo', b'1', '1998-07-22', 'TT.Cát Thành', '03503944187', 'Kinh', 'Không', NULL, 'Hưng Yên', NULL, 'lh01', NULL, 'Đỗ Thị Thúy ', NULL, NULL, NULL, NULL, NULL, b'1', NULL),
(3, 'hs03', 'Nguyễn Phương ', b'1', '1999-08-26', 'TT.Bac Ninh', '03503944187', 'Kinh', 'Không', NULL, 'Bac Ninh', NULL, 'lh01', NULL, 'Đỗ Thị Thúy ', NULL, NULL, NULL, NULL, NULL, b'1', NULL),
(4, 'hs04', 'Nguyễn Thanh Phong ', b'1', '2000-08-26', 'TT.Bac ', '03503944187', 'Kinh', 'Không', NULL, 'Thai Bình', NULL, 'lh01', NULL, 'Nguyễn Thị Thu Huyền', NULL, NULL, NULL, NULL, NULL, b'1', NULL),
(5, 'hs05', 'Trần Bảo Châm ', b'0', '1999-08-07', 'TT.Bac Ninh', '03503944187', 'Kinh', 'Không', NULL, 'Bac Ninh', NULL, 'lh02', NULL, 'Đỗ Thị Hảo ', NULL, NULL, NULL, NULL, NULL, b'1', NULL),
(6, 'hs06', 'Ninh Thị Hạnh ', b'0', '1999-08-07', 'TT.Bac Ninh', '03503944187', 'Kinh', 'Không', NULL, 'Bac Ninh', NULL, 'lh02', NULL, 'Đỗ Thị Hâm ', NULL, NULL, NULL, NULL, NULL, b'1', NULL),
(7, 'hs07', 'Vũ Hải Đămg ', b'1', '1999-08-07', 'TT.Bac Ninh', '03503944187', 'Kinh', 'Không', NULL, 'Bac Ninh', NULL, 'lh02', NULL, 'Đỗ Thị Len ', NULL, NULL, NULL, NULL, NULL, b'1', NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khoi`
--

CREATE TABLE `khoi` (
  `makhoi` varchar(10) CHARACTER SET utf8 NOT NULL,
  `tenkhoi` varchar(50) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `khoi`
--

INSERT INTO `khoi` (`makhoi`, `tenkhoi`) VALUES
('khoi01', 'Khối 6'),
('khoi02', 'Khối 7'),
('khoi03', 'Khối 8'),
('khoi04', 'Khối 9');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `lophoc`
--

CREATE TABLE `lophoc` (
  `id` int(11) NOT NULL,
  `malop` varchar(10) CHARACTER SET utf8 NOT NULL,
  `tenlop` varchar(50) CHARACTER SET utf8 NOT NULL,
  `manamhoc` varchar(10) CHARACTER SET utf8 NOT NULL,
  `makhoi` varchar(10) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `lophoc`
--

INSERT INTO `lophoc` (`id`, `malop`, `tenlop`, `manamhoc`, `makhoi`) VALUES
(1, 'lh01', '6A', 'nh01', 'khoi01'),
(2, 'lh02', '6B', 'nh01', 'khoi01');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `mon`
--

CREATE TABLE `mon` (
  `mamon` varchar(10) CHARACTER SET utf8 NOT NULL,
  `tenmon` varchar(50) CHARACTER SET utf8 NOT NULL,
  `hinhthucdanhgia` bit(1) NOT NULL,
  `makhoi` varchar(10) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `mon`
--

INSERT INTO `mon` (`mamon`, `tenmon`, `hinhthucdanhgia`, `makhoi`) VALUES
('mh01', 'Toán', b'1', 'khoi01'),
('mh02', 'Văn', b'1', 'khoi01'),
('mh03', 'Tiếng Anh', b'1', 'khoi01'),
('mh04', 'Vật lý', b'1', 'khoi01'),
('mh05', 'Toán', b'1', 'khoi02');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `namhoc`
--

CREATE TABLE `namhoc` (
  `manamhoc` varchar(10) CHARACTER SET utf8 NOT NULL,
  `nienhoc` varchar(10) CHARACTER SET utf8 NOT NULL,
  `ngaybatdau` date DEFAULT NULL,
  `ngayketthuc` date DEFAULT NULL,
  `trangthai` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `xoa` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `namhoc`
--

INSERT INTO `namhoc` (`manamhoc`, `nienhoc`, `ngaybatdau`, `ngayketthuc`, `trangthai`, `xoa`) VALUES
('nh01', '2018-2019', '2020-09-01', '2020-09-21', NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phancong`
--

CREATE TABLE `phancong` (
  `id` int(11) NOT NULL,
  `mapc` varchar(10) CHARACTER SET utf8 NOT NULL,
  `malop` varchar(10) CHARACTER SET utf8 NOT NULL,
  `magiaovien` varchar(10) CHARACTER SET utf8 NOT NULL,
  `vaitro` bit(1) NOT NULL,
  `mamon` varchar(10) CHARACTER SET utf8 NOT NULL,
  `hocki` bit(1) DEFAULT NULL,
  `manamhoc` varchar(10) CHARACTER SET utf8 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `phancong`
--

INSERT INTO `phancong` (`id`, `mapc`, `malop`, `magiaovien`, `vaitro`, `mamon`, `hocki`, `manamhoc`) VALUES
(1, 'pc01', 'lh01', 'gv01', b'1', 'mh01', b'1', 'nh01'),
(2, 'pc02', 'lh01', 'gv01', b'1', 'mh01', b'0', 'nh01'),
(5, 'pc03', 'lh01', 'gv01', b'0', 'mh01', NULL, 'nh01'),
(6, 'pc04', 'lh02', 'gv02', b'1', 'mh01', b'1', 'nh01');

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`stt`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `fk_giaovien_account` (`magiaovien`),
  ADD KEY `fk_hocsinh_account` (`mahocsinh`);

--
-- Chỉ mục cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  ADD PRIMARY KEY (`stt`),
  ADD KEY `fk_hocsinh_danhgia` (`mahocsinh`),
  ADD KEY `fk_giaovien_danhgia` (`magiaovien`),
  ADD KEY `fk_namhoc_danhgia` (`manamhoc`);

--
-- Chỉ mục cho bảng `diem`
--
ALTER TABLE `diem`
  ADD PRIMARY KEY (`stt`),
  ADD KEY `fk_hocsinh_diem` (`mahocsinh`),
  ADD KEY `fk_phancong_diem` (`mapc`);

--
-- Chỉ mục cho bảng `diemdanh`
--
ALTER TABLE `diemdanh`
  ADD PRIMARY KEY (`stt`),
  ADD KEY `fk_hocsinh_diemdanh` (`mahocsinh`),
  ADD KEY `fk_giaovien_diemdanh` (`magiaovien`),
  ADD KEY `fk_namhoc_diemdanh` (`manamhoc`);

--
-- Chỉ mục cho bảng `giaovien`
--
ALTER TABLE `giaovien`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `magiaovien` (`magiaovien`);

--
-- Chỉ mục cho bảng `hocsinh`
--
ALTER TABLE `hocsinh`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `mahocsinh` (`mahocsinh`),
  ADD KEY `fk_lophoc_hocsinh` (`lop`);

--
-- Chỉ mục cho bảng `khoi`
--
ALTER TABLE `khoi`
  ADD PRIMARY KEY (`makhoi`);

--
-- Chỉ mục cho bảng `lophoc`
--
ALTER TABLE `lophoc`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `malop` (`malop`),
  ADD KEY `fk_namhoc_lophoc` (`manamhoc`),
  ADD KEY `fk_khoi_lophoc` (`makhoi`);

--
-- Chỉ mục cho bảng `mon`
--
ALTER TABLE `mon`
  ADD PRIMARY KEY (`mamon`),
  ADD KEY `fk_khoi_mon` (`makhoi`);

--
-- Chỉ mục cho bảng `namhoc`
--
ALTER TABLE `namhoc`
  ADD PRIMARY KEY (`manamhoc`),
  ADD UNIQUE KEY `nienhoc` (`nienhoc`);

--
-- Chỉ mục cho bảng `phancong`
--
ALTER TABLE `phancong`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `mapc` (`mapc`),
  ADD KEY `fk_giaovien_phancong` (`magiaovien`),
  ADD KEY `fk_namhoc_phancong` (`manamhoc`),
  ADD KEY `fk_lophoc_phancong` (`malop`),
  ADD KEY `fk_mon_phancong` (`mamon`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `account`
--
ALTER TABLE `account`
  MODIFY `stt` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  MODIFY `stt` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `diem`
--
ALTER TABLE `diem`
  MODIFY `stt` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT cho bảng `diemdanh`
--
ALTER TABLE `diemdanh`
  MODIFY `stt` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `giaovien`
--
ALTER TABLE `giaovien`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `hocsinh`
--
ALTER TABLE `hocsinh`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `lophoc`
--
ALTER TABLE `lophoc`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT cho bảng `phancong`
--
ALTER TABLE `phancong`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `fk_giaovien_account` FOREIGN KEY (`magiaovien`) REFERENCES `giaovien` (`magiaovien`) ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_hocsinh_account` FOREIGN KEY (`mahocsinh`) REFERENCES `hocsinh` (`mahocsinh`) ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `danhgia`
--
ALTER TABLE `danhgia`
  ADD CONSTRAINT `fk_giaovien_danhgia` FOREIGN KEY (`magiaovien`) REFERENCES `giaovien` (`magiaovien`) ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_hocsinh_danhgia` FOREIGN KEY (`mahocsinh`) REFERENCES `hocsinh` (`mahocsinh`) ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_namhoc_danhgia` FOREIGN KEY (`manamhoc`) REFERENCES `namhoc` (`manamhoc`) ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `diem`
--
ALTER TABLE `diem`
  ADD CONSTRAINT `fk_hocsinh_diem` FOREIGN KEY (`mahocsinh`) REFERENCES `hocsinh` (`mahocsinh`) ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_phancong_diem` FOREIGN KEY (`mapc`) REFERENCES `phancong` (`mapc`) ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `diemdanh`
--
ALTER TABLE `diemdanh`
  ADD CONSTRAINT `fk_giaovien_diemdanh` FOREIGN KEY (`magiaovien`) REFERENCES `giaovien` (`magiaovien`) ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_hocsinh_diemdanh` FOREIGN KEY (`mahocsinh`) REFERENCES `hocsinh` (`mahocsinh`) ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_namhoc_diemdanh` FOREIGN KEY (`manamhoc`) REFERENCES `namhoc` (`manamhoc`) ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `hocsinh`
--
ALTER TABLE `hocsinh`
  ADD CONSTRAINT `fk_lophoc_hocsinh` FOREIGN KEY (`lop`) REFERENCES `lophoc` (`malop`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `lophoc`
--
ALTER TABLE `lophoc`
  ADD CONSTRAINT `fk_khoi_lophoc` FOREIGN KEY (`makhoi`) REFERENCES `khoi` (`makhoi`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_namhoc_lophoc` FOREIGN KEY (`manamhoc`) REFERENCES `namhoc` (`manamhoc`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `mon`
--
ALTER TABLE `mon`
  ADD CONSTRAINT `fk_khoi_mon` FOREIGN KEY (`makhoi`) REFERENCES `khoi` (`makhoi`) ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `phancong`
--
ALTER TABLE `phancong`
  ADD CONSTRAINT `fk_giaovien_phancong` FOREIGN KEY (`magiaovien`) REFERENCES `giaovien` (`magiaovien`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_lophoc_phancong` FOREIGN KEY (`malop`) REFERENCES `lophoc` (`malop`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_mon_phancong` FOREIGN KEY (`mamon`) REFERENCES `mon` (`mamon`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_namhoc_phancong` FOREIGN KEY (`manamhoc`) REFERENCES `namhoc` (`manamhoc`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
