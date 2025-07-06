-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 06, 2025 at 09:20 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sk_calendar`
--

-- --------------------------------------------------------

--
-- Table structure for table `events`
--

CREATE TABLE `events` (
  `id` int(11) NOT NULL,
  `title` varchar(255) NOT NULL,
  `event_date` date NOT NULL,
  `event_time` time DEFAULT NULL,
  `location` varchar(255) DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `attending_officials_count` int(11) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `events`
--

INSERT INTO `events` (`id`, `title`, `event_date`, `event_time`, `location`, `created_by`, `created_at`, `attending_officials_count`) VALUES
(11, 'meeting2', '2025-07-06', '12:00:00', 'anywhere', 1, '2025-07-06 05:32:17', 10);

-- --------------------------------------------------------

--
-- Table structure for table `event_assignments`
--

CREATE TABLE `event_assignments` (
  `id` int(11) NOT NULL,
  `event_id` int(11) DEFAULT NULL,
  `sk_profile_id` int(11) DEFAULT NULL,
  `position_number` int(11) DEFAULT NULL,
  `assigned_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `event_assignments`
--

INSERT INTO `event_assignments` (`id`, `event_id`, `sk_profile_id`, `position_number`, `assigned_at`) VALUES
(19, 11, 1, 1, '2025-07-06 05:32:59'),
(20, 11, 2, 2, '2025-07-06 05:32:59'),
(21, 11, 3, 3, '2025-07-06 05:32:59'),
(22, 11, 4, 4, '2025-07-06 05:32:59'),
(23, 11, 5, 5, '2025-07-06 05:32:59'),
(24, 11, 6, 6, '2025-07-06 05:32:59'),
(25, 11, 7, 7, '2025-07-06 05:32:59'),
(26, 11, 8, 8, '2025-07-06 05:32:59'),
(27, 11, 9, 9, '2025-07-06 05:32:59'),
(28, 11, 10, 10, '2025-07-06 05:32:59');

-- --------------------------------------------------------

--
-- Table structure for table `event_attendance`
--

CREATE TABLE `event_attendance` (
  `id` int(11) NOT NULL,
  `event_id` int(11) DEFAULT NULL,
  `sk_profile_id` int(11) DEFAULT NULL,
  `attendance_status` enum('present','absent','excused') DEFAULT 'present',
  `attendance_time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `notes` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `sk_profiles`
--

CREATE TABLE `sk_profiles` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `age` int(11) NOT NULL,
  `sex` enum('Male','Female') NOT NULL,
  `position` varchar(50) NOT NULL,
  `position_number` int(11) NOT NULL,
  `committee` varchar(100) DEFAULT NULL,
  `sector` varchar(100) DEFAULT NULL,
  `photo_path` varchar(255) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sk_profiles`
--

INSERT INTO `sk_profiles` (`id`, `name`, `age`, `sex`, `position`, `position_number`, `committee`, `sector`, `photo_path`, `created_at`, `updated_at`) VALUES
(1, 'Juan Dela Cruz', 22, 'Male', 'SK Chairman', 1, 'Education Committee', 'Out-of-School Youth', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(2, 'Maria Santos', 20, 'Female', 'SK Kagawad 1', 2, 'Health Committee', 'Working Youth', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(3, 'Carlos Mendoza', 21, 'Male', 'SK Kagawad 2', 3, 'Sports Committee', 'Students', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(4, 'Ana Garcia', 19, 'Female', 'SK Kagawad 3', 4, 'Environment Committee', 'Students', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(5, 'Roberto Silva', 23, 'Male', 'SK Kagawad 4', 5, 'Livelihood Committee', 'Working Youth', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(6, 'Carmen Lopez', 20, 'Female', 'SK Kagawad 5', 6, 'Youth Development Committee', 'Students', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(7, 'Miguel Torres', 22, 'Male', 'SK Kagawad 6', 7, 'Peace and Order Committee', 'Working Youth', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(8, 'Isabella Reyes', 21, 'Female', 'SK Kagawad 7', 8, 'Cultural Affairs Committee', 'Students', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(9, 'Diego Morales', 19, 'Male', 'Secretary', 9, 'Information Committee', 'Students', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00'),
(10, 'Sofia Hernandez', 20, 'Female', 'Treasurer', 10, 'Finance Committee', 'Out-of-School Youth', NULL, '2025-07-06 03:57:00', '2025-07-06 03:57:00');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `full_name` varchar(100) NOT NULL,
  `position` varchar(50) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `full_name`, `position`, `created_at`) VALUES
(1, 'admin', 'admin', 'System Administrator', 'Administrator', '2025-07-06 03:57:00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `events`
--
ALTER TABLE `events`
  ADD PRIMARY KEY (`id`),
  ADD KEY `created_by` (`created_by`);

--
-- Indexes for table `event_assignments`
--
ALTER TABLE `event_assignments`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_event_profile` (`event_id`,`sk_profile_id`),
  ADD KEY `sk_profile_id` (`sk_profile_id`);

--
-- Indexes for table `event_attendance`
--
ALTER TABLE `event_attendance`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_event_attendance` (`event_id`,`sk_profile_id`),
  ADD KEY `sk_profile_id` (`sk_profile_id`);

--
-- Indexes for table `sk_profiles`
--
ALTER TABLE `sk_profiles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `position_number` (`position_number`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `events`
--
ALTER TABLE `events`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `event_assignments`
--
ALTER TABLE `event_assignments`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `event_attendance`
--
ALTER TABLE `event_attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `sk_profiles`
--
ALTER TABLE `sk_profiles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `events`
--
ALTER TABLE `events`
  ADD CONSTRAINT `events_ibfk_1` FOREIGN KEY (`created_by`) REFERENCES `users` (`id`);

--
-- Constraints for table `event_assignments`
--
ALTER TABLE `event_assignments`
  ADD CONSTRAINT `event_assignments_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `event_assignments_ibfk_2` FOREIGN KEY (`sk_profile_id`) REFERENCES `sk_profiles` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `event_attendance`
--
ALTER TABLE `event_attendance`
  ADD CONSTRAINT `event_attendance_ibfk_1` FOREIGN KEY (`event_id`) REFERENCES `events` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `event_attendance_ibfk_2` FOREIGN KEY (`sk_profile_id`) REFERENCES `sk_profiles` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
