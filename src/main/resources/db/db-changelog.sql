--changeset author:thlemm add:initial no:1
CREATE TABLE "household_organizer"."items" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "mark" BIGINT UNIQUE NOT NULL,
    "image" TEXT NOT NULL,
    "created" TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    "updated" TIMESTAMP WITHOUT TIME ZONE,
    "transaction_id" BIGINT,
    CONSTRAINT "items_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "household_organizer"."tags" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "item_id"  BIGINT NOT NULL,
    "tag" TEXT NOT NULL,
    CONSTRAINT "tags_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "household_organizer"."rooms" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "name" VARCHAR(60),
    CONSTRAINT "rooms_pkey" PRIMARY KEY ("id")
);

INSERT INTO "household_organizer"."rooms"
    (id, name)
VALUES
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
    (24, 'ROOM_REFER_TO_IMAGE')
ON CONFLICT DO NOTHING;

CREATE TABLE "household_organizer"."items_original_rooms" (
    "item_id" BIGINT NOT NULL,
    "room_id" BIGINT NOT NULL,
    CONSTRAINT "items_original_rooms_pkey" PRIMARY KEY ("item_id", "room_id")
);

--changeset author:thlemm add:user no:2
CREATE TABLE "household_organizer"."user_roles" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "name" VARCHAR(60) UNIQUE,
    CONSTRAINT "user_roles_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "household_organizer"."user_status" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "name" VARCHAR(60) UNIQUE,
    CONSTRAINT "user_status_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "household_organizer"."user_types" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "name" VARCHAR(60) UNIQUE,
    CONSTRAINT "user_types_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "household_organizer"."users" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "password" VARCHAR(255) NOT NULL,
    "username" VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT "users_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "household_organizer"."users_roles" (
    "user_id" BIGINT NOT NULL,
    "user_role_id" BIGINT NOT NULL,
    CONSTRAINT "users_roles_pkey" PRIMARY KEY ("user_id", "user_role_id")
);

CREATE TABLE "household_organizer"."users_status" (
    "user_id" BIGINT NOT NULL,
    "user_status_id" BIGINT NOT NULL,
    CONSTRAINT "users_status_pkey" PRIMARY KEY ("user_id", "user_status_id")
);

CREATE TABLE "household_organizer"."users_types" (
    "user_id" BIGINT NOT NULL,
    "user_type_id" BIGINT NOT NULL,
    CONSTRAINT "users_types_pkey" PRIMARY KEY ("user_id", "user_type_id")
);

INSERT INTO "household_organizer"."user_roles"
    (id, name)
VALUES
    (1, 'ROLE_USER'),
    (2, 'ROLE_ADMIN')
ON CONFLICT DO NOTHING;

INSERT INTO "household_organizer"."user_status"
    (id, name)
VALUES
    (1, 'USER_STATUS_ACTIVE'),
    (2, 'USER_STATUS_BANNED')
ON CONFLICT DO NOTHING;

INSERT INTO "household_organizer"."user_types"
    (id, name)
VALUES
    (1, 'USER_TYPE_FAMILY'),
    (2, 'USER_TYPE_FRIEND')
ON CONFLICT DO NOTHING;

--changeset author:thlemm add:type no:3
CREATE TABLE "household_organizer"."item_types" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "name" VARCHAR(60) UNIQUE,
    CONSTRAINT "item_type_pkey" PRIMARY KEY ("id")
);

INSERT INTO "household_organizer"."item_types"
    (id, name)
VALUES
    (1, 'TYPE_DECORATION'),
    (2, 'TYPE_FURNITURE'),
    (3, 'TYPE_UTILITY_ITEM'),
    (4, 'ROOM_TECHNICAL_DEVICE'),
    (5, 'ROOM_FURNISHING')
ON CONFLICT DO NOTHING;

CREATE TABLE "household_organizer"."items_types" (
    "item_id" BIGINT NOT NULL,
    "item_type_id" BIGINT NOT NULL,
    CONSTRAINT "items_types_pkey" PRIMARY KEY ("item_id", "item_type_id")
);

--changeset author:thlemm add:interest no:4
CREATE TABLE "household_organizer"."interests" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "user_id" BIGINT NOT NULL,
    "item_id" BIGINT NOT NULL,
    "interested" BOOLEAN NOT NULL,
    CONSTRAINT "interests_pkey" PRIMARY KEY ("id")
);

--changeset author:thlemm add:locationsrooms no:5
CREATE TABLE "household_organizer"."locations" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "mark" BIGINT UNIQUE NOT NULL,
    "box" BOOLEAN,
    CONSTRAINT "locations_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "household_organizer"."locations_rooms" (
    "location_id" BIGINT NOT NULL,
    "room_id" BIGINT NOT NULL,
    CONSTRAINT "locations_rooms_pkey" PRIMARY KEY ("location_id", "room_id")
);

CREATE TABLE "household_organizer"."items_locations" (
    "item_id" BIGINT NOT NULL,
    "location_id" BIGINT NOT NULL,
    CONSTRAINT "items_locations_rooms_pkey" PRIMARY KEY ("item_id", "location_id")
);

--changeset author:thlemm add:transactions no:6
CREATE TABLE "household_organizer"."transactions" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "price_min" BIGINT,
    "price_max" BIGINT,
    "price_sold" BIGINT,
    "updated" TIMESTAMP WITHOUT TIME ZONE,
    "transaction_status_id" BIGINT,
    "user_id" BIGINT,
    CONSTRAINT "transactions_pkey" PRIMARY KEY ("id")
);

CREATE TABLE "household_organizer"."transaction_status" (
    "id" BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    "name" VARCHAR(60) UNIQUE,
    CONSTRAINT "transaction_status_pkey" PRIMARY KEY ("id")
);

INSERT INTO "household_organizer"."transaction_status"
    (id, name)
VALUES
    (1, 'TRANSACTION_STATUS_AVAILABLE'),
    (2, 'TRANSACTION_STATUS_SOLD'),
    (3, 'TRANSACTION_STATUS_TAKEN'),
    (4, 'TRANSACTION_STATUS_DISPOSED'),
    (5, 'TRANSACTION_STATUS_GIVEN_AWAY'),
    (6, 'TRANSACTION_STATUS_NOT_ASSESSED'),
    (7, 'TRANSACTION_STATUS_RESERVED')
ON CONFLICT DO NOTHING;