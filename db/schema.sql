-- ==========================================================
-- DISASTER RELIEF RESOURCE COORDINATOR - DATABASE SCHEMA
-- Compatible with MySQL 8.0+ and H2 Database
-- ==========================================================

CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(30) NOT NULL, -- COORDINATOR, SHELTER, ORGANIZATION, VOLUNTEER
    contact VARCHAR(30),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS disasters (
    disaster_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    disaster_type VARCHAR(40) NOT NULL, -- FLOOD, EARTHQUAKE, CYCLONE, LANDSLIDE
    location VARCHAR(150) NOT NULL,
    severity VARCHAR(20) NOT NULL,      -- CRITICAL, HIGH, MEDIUM, LOW
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, CONTAINED, RESOLVED
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS shelters (
    shelter_id INT AUTO_INCREMENT PRIMARY KEY,
    disaster_id INT,
    user_id INT,
    name VARCHAR(120) NOT NULL,
    location VARCHAR(150) NOT NULL,
    capacity INT NOT NULL DEFAULT 100,
    current_population INT NOT NULL DEFAULT 0,
    contact VARCHAR(30),
    status VARCHAR(30) DEFAULT 'OPERATIONAL', -- OPERATIONAL, AT_CAPACITY, EVACUATING, CLOSED
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (disaster_id) REFERENCES disasters(disaster_id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS organizations (
    organization_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    org_name VARCHAR(150) NOT NULL,
    org_type VARCHAR(50) NOT NULL, -- NGO, GOVT, RED_CROSS, PRIVATE, COMMUNITY
    contact_person VARCHAR(100),
    phone VARCHAR(30),
    email VARCHAR(120),
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS resources (
    resource_id INT AUTO_INCREMENT PRIMARY KEY,
    organization_id INT,
    resource_name VARCHAR(120) NOT NULL,
    resource_type VARCHAR(50) NOT NULL, -- FOOD, WATER, MEDICINE, SHELTER_KIT, CLOTHING, RESCUE_GEAR
    quantity INT NOT NULL DEFAULT 0,
    unit VARCHAR(30) NOT NULL,          -- Bottles, Kits, Kg, Boxes, Packets
    expiry_date VARCHAR(30),
    availability_status VARCHAR(30) DEFAULT 'AVAILABLE', -- AVAILABLE, LOW_STOCK, DEPLETED, EXPIRED
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (organization_id) REFERENCES organizations(organization_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS resource_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    shelter_id INT NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    quantity INT NOT NULL,
    unit VARCHAR(30) NOT NULL,
    urgency VARCHAR(20) NOT NULL,      -- CRITICAL, HIGH, MEDIUM, LOW
    people_affected INT DEFAULT 0,
    priority_score DOUBLE DEFAULT 0.0,
    status VARCHAR(30) DEFAULT 'PENDING', -- PENDING, ALLOCATED, DISPATCHED, DELIVERED, REJECTED
    notes TEXT,
    requested_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    required_date VARCHAR(30),
    FOREIGN KEY (shelter_id) REFERENCES shelters(shelter_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_number VARCHAR(40) NOT NULL UNIQUE,
    vehicle_type VARCHAR(30) NOT NULL, -- TRUCK, VAN, BOAT, HELICOPTER, AMBULANCE
    capacity_kg INT NOT NULL DEFAULT 1000,
    driver_name VARCHAR(100),
    contact VARCHAR(30),
    availability_status VARCHAR(30) DEFAULT 'AVAILABLE' -- AVAILABLE, IN_TRANSIT, MAINTENANCE
);

CREATE TABLE IF NOT EXISTS volunteers (
    volunteer_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    name VARCHAR(100),
    skill VARCHAR(50) NOT NULL,         -- FIRST_AID, RESCUE, DRIVING, LOGISTICS, MEDICAL
    experience_years INT DEFAULT 1,
    availability_status VARCHAR(30) DEFAULT 'AVAILABLE', -- AVAILABLE, ASSIGNED, OFF_DUTY
    contact VARCHAR(30),
    location VARCHAR(120),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS allocations (
    allocation_id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    resource_id INT NOT NULL,
    allocated_quantity INT NOT NULL,
    volunteer_id INT,
    vehicle_id INT,
    allocation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dispatch_date TIMESTAMP NULL,
    delivery_date TIMESTAMP NULL,
    status VARCHAR(30) DEFAULT 'PREPARING', -- PREPARING, DISPATCHED, IN_TRANSIT, DELIVERED, CANCELLED
    tracking_notes TEXT,
    FOREIGN KEY (request_id) REFERENCES resource_requests(request_id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES resources(resource_id) ON DELETE CASCADE,
    FOREIGN KEY (volunteer_id) REFERENCES volunteers(volunteer_id) ON DELETE SET NULL,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS system_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    log_level VARCHAR(20) DEFAULT 'INFO',
    module VARCHAR(50),
    message TEXT
);
