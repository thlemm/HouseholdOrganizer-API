-- liquibase formatted sql
-- changeset thlemm:1 add:initial
CREATE TABLE `thlemmde_household`.`items` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `mark` BIGINT NOT NULL,
    `image` VARCHAR(255) NOT NULL,
    `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated` TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    `transaction_id` BIGINT,
    UNIQUE (`mark`),
    PRIMARY KEY (`id`)
);

CREATE TABLE `thlemmde_household`.`tags` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `item_id` BIGINT NOT NULL,
    `tag` VARCHAR(50) NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `thlemmde_household`.`rooms` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(60),
    PRIMARY KEY (`id`)
);
INSERT IGNORE INTO `rooms` (`id`, `name`) VALUES
    (1, 'ROOM_DINING'),
    (2, 'ROOM_LIVING'),
    (3, 'ROOM_KITCHEN'),
    (4, 'ROOM_GARAGE'),
    (5, 'ROOM_OFFICE'),
    (6, 'ROOM_BEDROOM'),
    (7, 'ROOM_GREEN'),
    (8, 'ROOM_LOTTO'),
    (9, 'ROOM_LIBORI_ONE'),
    (10, 'ROOM_LIBORI_TWO'),
    (11, 'ROOM_BATH'),
    (12, 'ROOM_TOILET_PINK'),
    (13, 'ROOM_TOILET_BEIGE'),
    (14, 'ROOM_PANTRY'),
    (15, 'ROOM_HALL_GROUND_FLOOR'),
    (16, 'ROOM_HALL_FIRST_FLOOR'),
    (17, 'ROOM_HALL_SECOND_FLOOR'),
    (18, 'ROOM_CENTRAL_CELLAR'),
    (19, 'ROOM_LAUNDRY_CELLAR'),
    (20, 'ROOM_STORAGE_CELLAR'),
    (21, 'ROOM_WORKSHOP'),
    (22, 'ROOM_SOUTH_BALCONY'),
    (23, 'ROOM_NORTH_BALCONY'),
    (24, 'ROOM_REFER_TO_IMAGE');

CREATE TABLE `items_original_rooms` (
    `item_id` BIGINT NOT NULL,
    `room_id` BIGINT NOT NULL,
    PRIMARY KEY (`item_id`, `room_id`)
);

-- changeset thlemm:2 add:user
CREATE TABLE `user_roles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(60) UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `user_status` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(60) UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `user_types` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(60) UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    PRIMARY KEY (`id`)
);

CREATE TABLE `users_roles` (
    `user_id` BIGINT NOT NULL,
    `user_role_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `user_role_id`)
);

CREATE TABLE `users_status` (
    `user_id` BIGINT NOT NULL,
    `user_status_id` BIGINT NOT NULL,
    PRIMARY KEY (`user_id`, `user_status_id`)
);

INSERT IGNORE INTO `user_roles` (`id`, `name`) VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN'),
    (3, 'ROLE_FAMILY'),
    (4, 'ROLE_FRIEND');

INSERT IGNORE INTO `user_status` (`id`, `name`) VALUES
    (1, 'USER_STATUS_ACTIVE'),
    (2, 'USER_STATUS_BANNED');

-- changeset thlemm:3 add:type
CREATE TABLE `item_types` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(60) UNIQUE,
    PRIMARY KEY (`id`)
);

INSERT IGNORE INTO `item_types` (`id`, `name`) VALUES
    (1, 'TYPE_DECORATION'),
    (2, 'TYPE_FURNITURE'),
    (3, 'TYPE_UTILITY_ITEM'),
    (4, 'TYPE_TECHNICAL_DEVICE'),
    (5, 'TYPE_FURNISHING');

CREATE TABLE `items_types` (
    `item_id` BIGINT NOT NULL,
    `item_type_id` BIGINT NOT NULL,
    PRIMARY KEY (`item_id`, `item_type_id`)
);

-- changeset thlemm:4 add:interest
CREATE TABLE `interests` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `item_id` BIGINT NOT NULL,
    `interested` TINYINT(1) NOT NULL,
    PRIMARY KEY (`id`)
);

-- changeset thlemm:5 add:locationsrooms
CREATE TABLE `locations` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `mark` BIGINT UNIQUE NOT NULL,
    `box` TINYINT(1),
    PRIMARY KEY (`id`)
);

CREATE TABLE `locations_rooms` (
    `location_id` BIGINT NOT NULL,
    `room_id` BIGINT NOT NULL,
    PRIMARY KEY (`location_id`, `room_id`)
);

CREATE TABLE `items_locations` (
    `item_id` BIGINT NOT NULL,
    `location_id` BIGINT NOT NULL,
    PRIMARY KEY (`item_id`, `location_id`)
);

-- changeset thlemm:6 add:transactions
CREATE TABLE `transactions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `price_min` BIGINT,
    `price_max` BIGINT,
    `price_sold` BIGINT,
    `updated` TIMESTAMP NULL,
    `transaction_status_id` BIGINT,
    `user_id` BIGINT,
    PRIMARY KEY (`id`)
);

CREATE TABLE `transaction_status` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(60) UNIQUE,
    PRIMARY KEY (`id`)
);

INSERT IGNORE INTO `transaction_status` (`id`, `name`) VALUES
    (1, 'TRANSACTION_STATUS_AVAILABLE'),
    (2, 'TRANSACTION_STATUS_SOLD'),
    (3, 'TRANSACTION_STATUS_TAKEN'),
    (4, 'TRANSACTION_STATUS_DISPOSED'),
    (5, 'TRANSACTION_STATUS_GIVEN_AWAY'),
    (6, 'TRANSACTION_STATUS_NOT_ASSESSED'),
    (7, 'TRANSACTION_STATUS_RESERVED');

-- changeset thlemm:7 add:casinocodes
CREATE TABLE `casino_codes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `code` VARCHAR(6) UNIQUE,
    PRIMARY KEY (`id`)
);