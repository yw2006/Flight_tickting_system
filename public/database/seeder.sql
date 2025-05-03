-- MySQL Database Seeder Script
-- This script populates the airport management database with sample data

-- Clear existing data (if needed)
-- Disable foreign key checks for easier deletion
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE passenger_seat;
TRUNCATE TABLE flight_payment;
TRUNCATE TABLE flightReservation;
TRUNCATE TABLE flight;
TRUNCATE TABLE customSchedule;
TRUNCATE TABLE weeklySchedule;
TRUNCATE TABLE seat;
TRUNCATE TABLE aircraft;
TRUNCATE TABLE airline;
TRUNCATE TABLE passenger;
TRUNCATE TABLE payment;
TRUNCATE TABLE user;
TRUNCATE TABLE role;
TRUNCATE TABLE airport;
TRUNCATE TABLE country;

-- Enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Seed country table
INSERT INTO country (id, name) VALUES
(1, 'United States'),
(2, 'United Kingdom'),
(3, 'Japan'),
(4, 'Germany'),
(5, 'France'),
(6, 'Canada'),
(7, 'Australia'),
(8, 'China'),
(9, 'Brazil'),
(10, 'India');

-- Seed airport table
INSERT INTO airport (id, country_id, code, name, address, status) VALUES
(1, 1, 'JFK', 'John F Kennedy International Airport', 'Queens, NY 11430, USA', 'Active'),
(2, 1, 'LAX', 'Los Angeles International Airport', '1 World Way, Los Angeles, CA 90045, USA', 'Active'),
(3, 1, 'ORD', 'O''Hare International Airport', '10000 W O''Hare Ave, Chicago, IL 60666, USA', 'Active'),
(4, 2, 'LHR', 'London Heathrow Airport', 'Longford TW6, UK', 'Active'),
(5, 2, 'LGW', 'London Gatwick Airport', 'Horley, Gatwick RH6 0NP, UK', 'Active'),
(6, 3, 'HND', 'Tokyo Haneda Airport', 'Hanedakuko, Ota City, Tokyo 144-0041, Japan', 'Active'),
(7, 3, 'NRT', 'Narita International Airport', '1-1 Furugome, Narita, Chiba 282-0004, Japan', 'Active'),
(8, 4, 'FRA', 'Frankfurt Airport', '60547 Frankfurt, Germany', 'Active'),
(9, 5, 'CDG', 'Charles de Gaulle Airport', '95700 Roissy-en-France, France', 'Active'),
(10, 6, 'YYZ', 'Toronto Pearson International Airport', '6301 Silver Dart Dr, Mississauga, ON L5P 1B2, Canada', 'Active'),
(11, 7, 'SYD', 'Sydney Airport', 'Sydney NSW 2020, Australia', 'Active'),
(12, 8, 'PEK', 'Beijing Capital International Airport', 'Shunyi, Beijing, China', 'Active'),
(13, 9, 'GRU', 'São Paulo–Guarulhos International Airport', 'Rod. Hélio Smidt, s/nº - Aeroporto, Guarulhos - SP, Brazil', 'Active'),
(14, 10, 'DEL', 'Indira Gandhi International Airport', 'New Delhi, Delhi 110037, India', 'Active'),
(15, 1, 'DFW', 'Dallas/Fort Worth International Airport', '2400 Aviation Dr, DFW Airport, TX 75261, USA', 'Active');

-- Seed role table
INSERT INTO role (id, role_name, description) VALUES
(1, 'Admin', 'System administrator with full access'),
(2, 'Customer', 'Regular customer user'),
(3, 'Airline Staff', 'Airline employee with access to flight management'),
(4, 'Airport Staff', 'Airport employee with access to airport operations'),
(5, 'Guest', 'Limited access user');

-- Seed user table
INSERT INTO user (id, username, email, phone, password, created_at, updated_at, age, role_id) VALUES
(1, 'admin', 'admin@airport.com', '+1-555-123-4567', '$2a$12$J8Zt1Z5g5QH0UEX9jO.PY.XGNMbGLOEIBnW9UGVDQ8rvsKo2LtmCu', '2023-01-01 00:00:00', '2023-01-01 00:00:00', 35, 1),
(2, 'john_doe', 'john.doe@example.com', '+1-555-234-5678', '$2a$12$yTJOKzuPTrD7F6N7.FVXPOr5ywb8hWjYzNzeSJ5m0vdUdYwDVNy1G', '2023-01-02 10:30:00', '2023-04-12 14:45:00', 28, 2),
(3, 'jane_smith', 'jane.smith@example.com', '+1-555-345-6789', '$2a$12$LmxAlfVFNx0Zs1XH4Vwz5eSmYB5tGtxs0SY9ZyTZ6UGS3GzJlVNLG', '2023-01-03 14:15:00', '2023-05-20 09:30:00', 34, 2),
(4, 'airline_staff1', 'staff1@airline.com', '+1-555-456-7890', '$2a$12$8nEwG6xH2B7Yt1hfTY7cXuWvWY9KgQhIfgaS.jq0OXqNy5Bx1VTe2', '2023-01-04 09:00:00', '2023-01-04 09:00:00', 42, 3),
(5, 'airport_staff1', 'staff1@airport.com', '+1-555-567-8901', '$2a$12$l1TlOFIffXPBVdX4uLJQ5.FYEDTJVGJZVLw3KmTZjg9nGJjzfJEbS', '2023-01-05 11:20:00', '2023-03-15 16:30:00', 39, 4),
(6, 'guest_user', 'guest@example.com', NULL, '$2a$12$H2nMEEzD0X5TpFmk5X59n.aCrU3fXWwcOg2QUx5kF9hKpP5UXEqBm', '2023-01-06 16:45:00', '2023-01-06 16:45:00', 25, 5),
(7, 'emma_wilson', 'emma.wilson@example.com', '+1-555-678-9012', '$2a$12$iN6SbJ1SyNQUJDSN3xmTn.dv3JOBbIVwk1UXbN2C3qUH2p8nYRpiq', '2023-01-07 13:10:00', '2023-06-10 10:15:00', 31, 2),
(8, 'michael_brown', 'michael.brown@example.com', '+1-555-789-0123', '$2a$12$KmM4vySyL8KxfHQ9F6WZYeHvW6YaPAyGj6r5UY2YKPSK2lmS2SrAq', '2023-01-08 08:30:00', '2023-02-18 12:40:00', 45, 2),
(9, 'sarah_johnson', 'sarah.johnson@example.com', '+1-555-890-1234', '$2a$12$JNq3CPKD8PyS0k2Cv7cSWeKhRUfljTNzRZQnVlABHoVHtE4oeEVli', '2023-01-09 15:20:00', '2023-07-22 14:30:00', 29, 2),
(10, 'david_lee', 'david.lee@example.com', '+1-555-901-2345', '$2a$12$tqQGAOXs8TnO23AYz3gDn.qP3hNWQKFDwpWz6CJxg2QlQpeWMexgC', '2023-01-10 12:00:00', '2023-05-05 11:10:00', 37, 2);

-- Seed passenger table (with NULL flightReservation_id as they'll be set later)
INSERT INTO passenger (id, user_id, name, passport, flightReservation_id) VALUES
(1, 2, 'John Doe', 'US123456', NULL),
(2, 3, 'Jane Smith', 'US234567', NULL),
(3, 7, 'Emma Wilson', 'US345678', NULL),
(4, 8, 'Michael Brown', 'US456789', NULL),
(5, 9, 'Sarah Johnson', 'US567890', NULL),
(6, 10, 'David Lee', 'US678901', NULL),
(7, NULL, 'Robert Davis', 'UK123456', NULL),
(8, NULL, 'Emily Chen', 'CN234567', NULL),
(9, NULL, 'James Wilson', 'AU345678', NULL),
(10, NULL, 'Olivia Martinez', 'BR456789', NULL);

-- Seed payment table
INSERT INTO payment (id, user_id, payment_amount, payment_state, payment_method, payment_date, updated_at) VALUES
(1, 2, 450.00, 'Completed', 'Credit Card', '2023-02-15', '2023-02-15 14:30:00'),
(2, 3, 550.00, 'Completed', 'PayPal', '2023-02-16', '2023-02-16 10:15:00'),
(3, 7, 325.00, 'Completed', 'Credit Card', '2023-02-17', '2023-02-17 16:45:00'),
(4, 8, 750.00, 'Completed', 'Debit Card', '2023-02-18', '2023-02-18 09:30:00'),
(5, 9, 420.00, 'Completed', 'Credit Card', '2023-02-19', '2023-02-19 11:20:00'),
(6, 10, 580.00, 'Completed', 'PayPal', '2023-02-20', '2023-02-20 13:10:00'),
(7, 2, 610.00, 'Refunded', 'Credit Card', '2023-02-21', '2023-03-05 14:25:00'),
(8, 3, 490.00, 'Pending', 'Credit Card', '2023-02-22', '2023-02-22 15:40:00'),
(9, 7, 800.00, 'Completed', 'Bank Transfer', '2023-02-23', '2023-02-23 17:30:00'),
(10, 8, 375.00, 'Failed', 'Credit Card', '2023-02-24', '2023-02-24 08:50:00');

-- Seed weeklySchedule table
INSERT INTO weeklySchedule (id, dayOfWeek, departure_time, customDate) VALUES
(1, 'Monday', '08:00:00', NULL),
(2, 'Monday', '14:30:00', NULL),
(3, 'Tuesday', '09:15:00', NULL),
(4, 'Wednesday', '11:45:00', NULL),
(5, 'Thursday', '16:30:00', NULL),
(6, 'Friday', '07:00:00', NULL),
(7, 'Friday', '19:20:00', NULL),
(8, 'Saturday', '10:00:00', NULL),
(9, 'Sunday', '13:45:00', NULL),
(10, 'Sunday', '22:30:00', NULL);

-- Seed airline table
INSERT INTO airline (id, name, code) VALUES
(1, 'American Airlines', 'AA'),
(2, 'Delta Air Lines', 'DL'),
(3, 'United Airlines', 'UA'),
(4, 'British Airways', 'BA'),
(5, 'Lufthansa', 'LH'),
(6, 'Air France', 'AF'),
(7, 'Japan Airlines', 'JL'),
(8, 'Qantas', 'QF'),
(9, 'Emirates', 'EK'),
(10, 'Singapore Airlines', 'SQ');

-- Seed aircraft table
INSERT INTO aircraft (id, airline_id, modal, manufacturing_year) VALUES
(1, 1, 'Boeing 737-800', 2015),
(2, 1, 'Boeing 777-300ER', 2018),
(3, 2, 'Airbus A320', 2016),
(4, 2, 'Boeing 767-300', 2012),
(5, 3, 'Boeing 787-9', 2019),
(6, 4, 'Airbus A380', 2014),
(7, 5, 'Airbus A350-900', 2020),
(8, 6, 'Boeing 777-200', 2013),
(9, 7, 'Boeing 787-8', 2017),
(10, 8, 'Airbus A330-300', 2016),
(11, 9, 'Airbus A380-800', 2018),
(12, 10, 'Boeing 777-300', 2015);

-- Seed seat table
INSERT INTO seat (id, aircraft_id, class) VALUES
(1, 1, 'Economy'),
(2, 1, 'Economy'),
(3, 1, 'Economy'),
(4, 1, 'Business'),
(5, 1, 'Business'),
(6, 2, 'Economy'),
(7, 2, 'Economy'),
(8, 2, 'Business'),
(9, 2, 'First'),
(10, 3, 'Economy'),
(11, 3, 'Economy'),
(12, 3, 'Business'),
(13, 4, 'Economy'),
(14, 4, 'Business'),
(15, 5, 'Economy'),
(16, 5, 'Business'),
(17, 5, 'First'),
(18, 6, 'Economy'),
(19, 6, 'Business'),
(20, 6, 'First');

-- Seed flight table
INSERT INTO flight (id, arrival_airport_id, departure_airport_id, gate, duration, flight_schedule_id, aircraft_id) VALUES
(1, 2, 1, 'A1', 360, 1, 1),
(2, 4, 1, 'B3', 420, 2, 2),
(3, 1, 4, 'C5', 440, 3, 6),
(4, 6, 4, 'D2', 720, 4, 6),
(5, 9, 8, 'E4', 90, 5, 7),
(6, 11, 7, 'F6', 600, 6, 9),
(7, 12, 3, 'G1', 840, 7, 1),
(8, 5, 10, 'H3', 480, 8, 10),
(9, 13, 14, 'J2', 300, 9, 11),
(10, 8, 15, 'K4', 660, 10, 5);

-- Seed customSchedule table
INSERT INTO customSchedule (id, flight_id, departure_time, customDate) VALUES
(1, 1, '09:30:00', '2023-06-15'),
(2, 2, '15:45:00', '2023-06-16'),
(3, 3, '11:00:00', '2023-06-17'),
(4, 4, '14:15:00', '2023-06-18'),
(5, 5, '18:30:00', '2023-06-19');

-- Seed flightReservation table
INSERT INTO flightReservation (id, flight_id, user_id, qr_code, booking_date, status) VALUES
(1, 1, 2, 'QR12345678', '2023-02-15', 'Confirmed'),
(2, 2, 3, 'QR23456789', '2023-02-16', 'Confirmed'),
(3, 3, 7, 'QR34567890', '2023-02-17', 'Confirmed'),
(4, 4, 8, 'QR45678901', '2023-02-18', 'Confirmed'),
(5, 5, 9, 'QR56789012', '2023-02-19', 'Confirmed'),
(6, 6, 10, 'QR67890123', '2023-02-20', 'Confirmed'),
(7, 7, 2, 'QR78901234', '2023-02-21', 'Cancelled'),
(8, 8, 3, 'QR89012345', '2023-02-22', 'Pending'),
(9, 9, 7, 'QR90123456', '2023-02-23', 'Confirmed'),
(10, 10, 8, 'QR01234567', '2023-02-24', 'Failed');

-- Update passenger with flightReservation_id
UPDATE passenger SET flightReservation_id = 1 WHERE id = 1;
UPDATE passenger SET flightReservation_id = 2 WHERE id = 2;
UPDATE passenger SET flightReservation_id = 3 WHERE id = 3;
UPDATE passenger SET flightReservation_id = 4 WHERE id = 4;
UPDATE passenger SET flightReservation_id = 5 WHERE id = 5;
UPDATE passenger SET flightReservation_id = 6 WHERE id = 6;
UPDATE passenger SET flightReservation_id = 7 WHERE id = 7;
UPDATE passenger SET flightReservation_id = 8 WHERE id = 8;
UPDATE passenger SET flightReservation_id = 9 WHERE id = 9;
UPDATE passenger SET flightReservation_id = 10 WHERE id = 10;

-- Seed flight_payment table
INSERT INTO flight_payment (flight_id, payment_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5),
(6, 6),
(7, 7),
(8, 8),
(9, 9),
(10, 10);

-- Seed passenger_seat table
INSERT INTO passenger_seat (passenger_id, seat_id, flightReservation_id) VALUES
(1, 4, 1),
(2, 9, 2),
(3, 12, 3),
(4, 14, 4),
(5, 16, 5),
(6, 20, 6),
(7, 3, 7),
(8, 10, 8),
(9, 18, 9),
(10, 15, 10);