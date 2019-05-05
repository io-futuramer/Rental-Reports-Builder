CREATE DATABASE `rental_reports` /*!40100 DEFAULT CHARACTER SET utf8 */;

CREATE TABLE `message_recipients` (
  `message_recipient_id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL,
  `email` varchar(500) NOT NULL,
  PRIMARY KEY (`message_recipient_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `notes` (
  `note_id` int(10) NOT NULL AUTO_INCREMENT,
  `note` varchar(500) NOT NULL,
  PRIMARY KEY (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `run_table` (
  `run_id` int(10) NOT NULL AUTO_INCREMENT,
  `run_time` datetime NOT NULL,
  PRIMARY KEY (`run_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `rental_units` (
  `rental_unit_id` int(10) NOT NULL AUTO_INCREMENT,
  `run_id` int(10) NOT NULL,
  `created_on` datetime NOT NULL,
  `created_by` varchar(500) NOT NULL,
  `rental_unit` varchar(500) NOT NULL,
  `location` varchar(500) DEFAULT NULL,
  `property_status` varchar(500) DEFAULT NULL,
  `application_status` varchar(500) DEFAULT NULL,
  `applicant_name` varchar(500) DEFAULT NULL,
  `rent_pcm_amount` int(10) DEFAULT NULL,
  `rent_pcm_currency` varchar(500) DEFAULT NULL,
  `deposit_amount` int(10) DEFAULT NULL,
  `deposit_currency` varchar(500) DEFAULT NULL,
  `check_in_fee_amount` int(10) DEFAULT NULL,
  `check_in_fee_currency` varchar(500) DEFAULT NULL,
  `available_start` datetime DEFAULT NULL,
  `available_end` datetime DEFAULT NULL,
  `gl_id` varchar(500) DEFAULT NULL,
  `short_description` varchar(500) DEFAULT NULL,
  `description` varchar(500) DEFAULT NULL,
  `viewing_notification` varchar(500) DEFAULT NULL,
  `key_numbers` varchar(500) DEFAULT NULL,
  `listing` varchar(500) DEFAULT NULL,
  `tags` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`rental_unit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
