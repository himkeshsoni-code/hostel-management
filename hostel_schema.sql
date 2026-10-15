-- Hostel Management System | MySQL 8.0+
DROP DATABASE IF EXISTS hostel_db;
CREATE DATABASE hostel_db;
USE hostel_db;

CREATE TABLE hostel (
    hostel_id   INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    type        ENUM('Boys','Girls') NOT NULL,
    total_rooms INT NOT NULL CHECK (total_rooms > 0)
);

CREATE TABLE warden (
    warden_id INT AUTO_INCREMENT PRIMARY KEY,
    hostel_id INT NOT NULL,
    name      VARCHAR(80) NOT NULL,
    phone     VARCHAR(15),
    username  VARCHAR(30) NOT NULL UNIQUE,
    password  VARCHAR(100) NOT NULL,
    FOREIGN KEY (hostel_id) REFERENCES hostel(hostel_id)
);

CREATE TABLE room (
    room_id   INT AUTO_INCREMENT PRIMARY KEY,
    hostel_id INT NOT NULL,
    room_no   VARCHAR(10) NOT NULL,
    floor     INT NOT NULL,
    capacity  INT NOT NULL CHECK (capacity BETWEEN 1 AND 6),
    occupied  INT NOT NULL DEFAULT 0,
    status    ENUM('Vacant','Full') NOT NULL DEFAULT 'Vacant',
    UNIQUE (hostel_id, room_no),
    CHECK (occupied <= capacity),
    FOREIGN KEY (hostel_id) REFERENCES hostel(hostel_id)
);

CREATE TABLE student (
    student_id     INT AUTO_INCREMENT PRIMARY KEY,
    roll_no        VARCHAR(20) NOT NULL UNIQUE,
    name           VARCHAR(80) NOT NULL,
    gender         ENUM('M','F') NOT NULL,
    dept           VARCHAR(40),
    year           INT CHECK (year BETWEEN 1 AND 5),
    phone          VARCHAR(15),
    email          VARCHAR(80) UNIQUE,
    guardian_name  VARCHAR(80),
    guardian_phone VARCHAR(15)
);

CREATE TABLE allocation (
    alloc_id    INT AUTO_INCREMENT PRIMARY KEY,
    student_id  INT NOT NULL,
    room_id     INT NOT NULL,
    alloc_date  DATE NOT NULL,
    vacate_date DATE NULL,
    status      ENUM('Active','Vacated') NOT NULL DEFAULT 'Active',
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (room_id)    REFERENCES room(room_id)
);

CREATE TABLE fee_payment (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    semester   VARCHAR(10) NOT NULL,
    amount_due DECIMAL(10,2) NOT NULL,
    amount_paid DECIMAL(10,2) NOT NULL DEFAULT 0,
    pay_date   DATE NULL,
    mode       ENUM('Cash','UPI','Card','Bank') NULL,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    CHECK (amount_paid <= amount_due)
);

CREATE TABLE complaint (
    complaint_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id   INT NOT NULL,
    category     ENUM('Electrical','Plumbing','Cleaning','Food','Other') NOT NULL,
    description  VARCHAR(255) NOT NULL,
    status       ENUM('Open','In Progress','Resolved') NOT NULL DEFAULT 'Open',
    raised_on    DATE NOT NULL,
    resolved_on  DATE NULL,
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);

CREATE TABLE visitor_log (
    visit_id     INT AUTO_INCREMENT PRIMARY KEY,
    student_id   INT NOT NULL,
    visitor_name VARCHAR(80) NOT NULL,
    relation     VARCHAR(30),
    in_time      DATETIME NOT NULL,
    out_time     DATETIME NULL,
    FOREIGN KEY (student_id) REFERENCES student(student_id)
);

-- ---------- Sample data ----------
INSERT INTO hostel (name, type, total_rooms) VALUES
('Aryabhatta Hostel','Boys',4),
('Sarojini Hostel','Girls',3);

INSERT INTO warden (hostel_id, name, phone, username, password) VALUES
(1,'Mr. Sharma','9876500001','warden1','admin123'),
(2,'Mrs. Iyer','9876500002','warden2','admin123');

INSERT INTO room (hostel_id, room_no, floor, capacity) VALUES
(1,'101',1,3),(1,'102',1,2),(1,'201',2,3),(1,'202',2,2),
(2,'G01',0,2),(2,'G02',0,3),(2,'F01',1,2);

INSERT INTO student (roll_no,name,gender,dept,year,phone,email,guardian_name,guardian_phone) VALUES
('CS101','Rahul Verma','M','CSE',2,'9000000001','rahul@college.edu','Mr. Verma','9100000001'),
('CS102','Amit Singh','M','CSE',2,'9000000002','amit@college.edu','Mr. Singh','9100000002'),
('EC201','Priya Nair','F','ECE',1,'9000000003','priya@college.edu','Mr. Nair','9100000003'),
('ME301','Sneha Rao','F','MECH',3,'9000000004','sneha@college.edu','Mr. Rao','9100000004');

INSERT INTO allocation (student_id, room_id, alloc_date) VALUES
(1,1,'2026-08-01'),(2,1,'2026-08-01'),(3,5,'2026-08-02');

-- Occupancy for sample allocations (a trigger will automate this later)
UPDATE room SET occupied = 2 WHERE room_id = 1;
UPDATE room SET occupied = 1 WHERE room_id = 5;

INSERT INTO fee_payment (student_id, semester, amount_due, amount_paid, pay_date, mode) VALUES
(1,'Sem 3',25000,25000,'2026-08-05','UPI'),
(2,'Sem 3',25000,10000,'2026-08-06','Cash'),
(3,'Sem 1',25000,0,NULL,NULL);

INSERT INTO complaint (student_id, category, description, raised_on) VALUES
(1,'Electrical','Fan not working in room 101','2026-09-10');

INSERT INTO visitor_log (student_id, visitor_name, relation, in_time, out_time) VALUES
(1,'Mr. Verma','Father','2026-09-12 16:00:00','2026-09-12 17:30:00');

-- Quick check
SELECT s.name, r.room_no, h.name AS hostel
FROM allocation a
JOIN student s ON s.student_id = a.student_id
JOIN room r    ON r.room_id    = a.room_id
JOIN hostel h  ON h.hostel_id  = r.hostel_id
WHERE a.status = 'Active';
