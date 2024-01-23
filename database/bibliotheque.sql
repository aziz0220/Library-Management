-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 23, 2024 at 11:55 AM
-- Server version: 10.4.27-MariaDB
-- PHP Version: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bibliotheque`
--

-- --------------------------------------------------------

--
-- Table structure for table `detailemprunt`
--

CREATE TABLE `detailemprunt` (
  `id` int(11) NOT NULL,
  `livre_code` bigint(20) DEFAULT NULL,
  `lecteur_CIN` bigint(20) DEFAULT NULL,
  `dateEmprunt` date DEFAULT NULL,
  `dateRetour` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `detailemprunt`
--

INSERT INTO `detailemprunt` (`id`, `livre_code`, `lecteur_CIN`, `dateEmprunt`, `dateRetour`) VALUES
(1, 1, 254567899, '2023-01-01', '2023-01-08'),
(2, 2, 123456789, '2023-02-15', '2023-02-22'),
(3, 3, 987654321, '2023-03-10', '2023-03-17'),
(4, 4, 111222333, '2023-04-05', '2023-04-12'),
(5, 5, 444555666, '2023-05-20', '2023-05-27'),
(6, 6, 777888999, '2023-06-15', '2023-06-22'),
(7, 7, 254567899, '2023-07-01', '2023-07-08'),
(8, 8, 123456789, '2023-08-10', '2023-08-17'),
(9, 9, 987654321, '2023-09-05', '2023-09-12'),
(10, 10, 111222333, '2023-10-20', '2023-10-27'),
(11, 1, 123456789, '2024-01-19', '2024-01-26'),
(12, 2, 777777777, '2024-01-19', '2024-01-26'),
(13, 8, 777777777, '2024-01-19', '2024-01-26'),
(14, 9, 777777777, '2024-01-19', '2024-01-19');

-- --------------------------------------------------------

--
-- Table structure for table `lecteur`
--

CREATE TABLE `lecteur` (
  `CIN` bigint(20) NOT NULL,
  `nom` varchar(255) DEFAULT NULL,
  `prenom` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `lecteur`
--

INSERT INTO `lecteur` (`CIN`, `nom`, `prenom`) VALUES
(111222333, 'Mabrouk', 'Salma'),
(123456789, 'Doe', 'John'),
(254567899, 'Ben Salah', 'Aymen'),
(333444555, 'Guediche', 'Nour'),
(444555666, 'Gharbi', 'Hichem'),
(555666777, 'Baccar', 'Amina'),
(666777888, 'Haddad', 'Sami'),
(777888999, 'Hamdi', 'Lina'),
(888999000, 'Touati', 'Mohamed'),
(987654321, 'Bouazizi', 'Manel');

-- --------------------------------------------------------

--
-- Table structure for table `livre`
--

CREATE TABLE `livre` (
  `code` bigint(20) NOT NULL,
  `titre` varchar(255) DEFAULT NULL,
  `auteur` varchar(255) DEFAULT NULL,
  `ISBN` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `livre`
--

INSERT INTO `livre` (`code`, `titre`, `auteur`, `ISBN`) VALUES
(1, 'Le Petit Prince', 'Antoine de Saint-Exupéry', 9780156013987),
(2, 'Harry Potter and the Sorcerer\'s Stone', 'J.K. Rowling', 9780590353427),
(3, 'The Catcher in the Rye', 'J.D. Salinger', 9780241950425),
(4, 'To Kill a Mockingbird', 'Harper Lee', 9780061120084),
(5, 'The Great Gatsby', 'F. Scott Fitzgerald', 9780743273565),
(6, 'Pride and Prejudice', 'Jane Austen', 9780141439518),
(7, 'The Hobbit', 'J.R.R. Tolkien', 9780261102217),
(8, 'One Hundred Years of Solitude', 'Gabriel García Márquez', 9780061120084),
(9, 'Brave New World', 'Aldous Huxley', 9780060850524),
(10, 'The Alchemist', 'Paulo Coelho', 9780061120084),
(14, 'Test Book', 'Test Author', 9876543210123),
(15, 'Harry Potter and the Sorcerer\'s Stone', 'J.K. Rowling', 9780590353427),
(16, 'One Hundred Years of Solitude', 'Gabriel García Márquez', 9780061120084),
(17, 'Brave New World', 'Aldous Huxley', 9780060850524);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `detailemprunt`
--
ALTER TABLE `detailemprunt`
  ADD PRIMARY KEY (`id`),
  ADD KEY `livre_code` (`livre_code`),
  ADD KEY `lecteur_CIN` (`lecteur_CIN`);

--
-- Indexes for table `lecteur`
--
ALTER TABLE `lecteur`
  ADD PRIMARY KEY (`CIN`);

--
-- Indexes for table `livre`
--
ALTER TABLE `livre`
  ADD PRIMARY KEY (`code`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `detailemprunt`
--
ALTER TABLE `detailemprunt`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `livre`
--
ALTER TABLE `livre`
  MODIFY `code` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `detailemprunt`
--
ALTER TABLE `detailemprunt`
  ADD CONSTRAINT `detailemprunt_ibfk_1` FOREIGN KEY (`livre_code`) REFERENCES `livre` (`code`),
  ADD CONSTRAINT `detailemprunt_ibfk_2` FOREIGN KEY (`lecteur_CIN`) REFERENCES `lecteur` (`CIN`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
