-- Table: country
CREATE TABLE country (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL
);

-- Table: airline
CREATE TABLE airline (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    code TEXT NOT NULL
);

-- Table: role
CREATE TABLE role (
    id INTEGER PRIMARY KEY,
    role_name TEXT NOT NULL,
    description TEXT
);

-- Table: airport
CREATE TABLE airport (
    id INTEGER PRIMARY KEY,
    country_id INTEGER NOT NULL,
    code TEXT NOT NULL,
    name TEXT NOT NULL,
    address TEXT,
    status TEXT,
    FOREIGN KEY (country_id) REFERENCES country(id)
);

-- Table: user
CREATE TABLE user (
    id INTEGER PRIMARY KEY,
    username TEXT NOT NULL,
    email TEXT NOT NULL,
    phone TEXT,
    password TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    age INTEGER,
    role_id INTEGER NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Table: weeklySchedule
CREATE TABLE weeklySchedule (
    id INTEGER PRIMARY KEY,
    dayOfWeek TEXT NOT NULL,
    departure_time TIME NOT NULL,
    customDate DATE
);

-- Table: aircraft
CREATE TABLE aircraft (
    id INTEGER PRIMARY KEY,
    airline_id INTEGER NOT NULL,
    modal TEXT NOT NULL,
    manufacturing_year INTEGER,
    FOREIGN KEY (airline_id) REFERENCES airline(id)
);

-- Table: seat
CREATE TABLE seat (
    id INTEGER PRIMARY KEY,
    aircraft_id INTEGER NOT NULL,
    class TEXT NOT NULL,
    FOREIGN KEY (aircraft_id) REFERENCES aircraft(id)
);

-- Table: flight
CREATE TABLE flight (
    id INTEGER PRIMARY KEY,
    arrival_airport_id INTEGER NOT NULL,
    departure_airport_id INTEGER NOT NULL,
    gate TEXT,
    duration INTEGER,
    flight_schedule_id INTEGER NOT NULL,
    aircraft_id INTEGER NOT NULL,
    FOREIGN KEY (arrival_airport_id) REFERENCES airport(id),
    FOREIGN KEY (departure_airport_id) REFERENCES airport(id),
    FOREIGN KEY (flight_schedule_id) REFERENCES weeklySchedule(id),
    FOREIGN KEY (aircraft_id) REFERENCES aircraft(id)
);

-- Table: payment
CREATE TABLE payment (
    id INTEGER PRIMARY KEY,
    user_id INTEGER NOT NULL,
    payment_amount DECIMAL NOT NULL,
    payment_state TEXT NOT NULL,
    payment_method TEXT NOT NULL,
    payment_date DATE NOT NULL,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Table: customSchedule
CREATE TABLE customSchedule (
    id INTEGER PRIMARY KEY,
    flight_id INTEGER NOT NULL,
    departure_time TIME NOT NULL,
    customDate DATE,
    FOREIGN KEY (flight_id) REFERENCES flight(id)
);

-- Table: flightReservation
CREATE TABLE flightReservation (
    id INTEGER PRIMARY KEY,
    flight_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    qr_code TEXT,
    booking_date DATE,
    status TEXT,
    FOREIGN KEY (flight_id) REFERENCES flight(id),
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Table: passenger
CREATE TABLE passenger (
    id INTEGER PRIMARY KEY,
    user_id INTEGER, -- Can be NULL
    name TEXT NOT NULL,
    passport TEXT,
    flightReservation_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (flightReservation_id) REFERENCES flightReservation(id)
);

-- Junction Table: flight_payment (flight ↔ payment)
CREATE TABLE flight_payment (
    flight_id INTEGER,
    payment_id INTEGER,
    PRIMARY KEY (flight_id, payment_id),
    FOREIGN KEY (flight_id) REFERENCES flight(id),
    FOREIGN KEY (payment_id) REFERENCES payment(id)
);

-- Junction Table: passenger_seat (passenger ↔ seat)
CREATE TABLE passenger_seat (
    passenger_id INTEGER,
    seat_id INTEGER,
    flightReservation_id INTEGER NOT NULL,
    PRIMARY KEY (passenger_id, seat_id),
    FOREIGN KEY (passenger_id) REFERENCES passenger(id),
    FOREIGN KEY (seat_id) REFERENCES seat(id),
    FOREIGN KEY (flightReservation_id) REFERENCES flightReservation(id)
);