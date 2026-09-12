-- ==========================================================
-- DISASTER RELIEF RESOURCE COORDINATOR - DEMO SEED DATA
-- Pre-populated for full viva demonstration & instant testing
-- ==========================================================

-- 1. USERS
INSERT INTO users (user_id, name, email, password, role, contact, is_active) VALUES
(1, 'Dr. Rethish Kumar (Chief Coordinator)', 'admin@relief.org', 'admin123', 'COORDINATOR', '+91 98765 00001', TRUE),
(2, 'Hari Krishna (Shelter Manager)', 'shelter1@relief.org', 'shelter123', 'SHELTER', '+91 98765 00002', TRUE),
(3, 'Dipanshu Verma (Shelter Manager)', 'shelter2@relief.org', 'shelter123', 'SHELTER', '+91 98765 00003', TRUE),
(4, 'Red Cross Disaster Team', 'redcross@relief.org', 'org123', 'ORGANIZATION', '+91 98765 00004', TRUE),
(5, 'UNICEF Emergency Logistics', 'unicef@relief.org', 'org123', 'ORGANIZATION', '+91 98765 00005', TRUE),
(6, 'Rahul Sharma (Volunteer Leader)', 'volunteer1@relief.org', 'vol123', 'VOLUNTEER', '+91 98765 00006', TRUE),
(7, 'Priya Nair (Medical Volunteer)', 'volunteer2@relief.org', 'vol123', 'VOLUNTEER', '+91 98765 00007', TRUE);

-- 2. DISASTERS
INSERT INTO disasters (disaster_id, name, disaster_type, location, severity, status, description) VALUES
(1, 'Super Cyclone Vardah', 'CYCLONE', 'Coastal Sector 4 & Fishermen Harbor', 'CRITICAL', 'ACTIVE', 'Category 4 cyclone with wind speeds reaching 165 km/h, severe storm surge and widespread inundation.'),
(2, 'Brahmaputra Flash Floods', 'FLOOD', 'Eastern River Basin & Lowland Villages', 'HIGH', 'ACTIVE', 'Breached river embankments submerged 14 villages; road transit cut off in sectors A through F.'),
(3, 'Northern Hills Seismic Event', 'EARTHQUAKE', 'Upper Ridge District (Epicenter Depth 12km)', 'CRITICAL', 'ACTIVE', 'Magnitude 6.3 earthquake causing structural collapse, landslides blocking arterial highways.');

-- 3. SHELTERS
INSERT INTO shelters (shelter_id, disaster_id, user_id, name, location, capacity, current_population, contact, status) VALUES
(1, 1, 2, 'Coastal Community Hall Relief Center', 'Bayfront Avenue, Sector 4', 800, 680, '+91 98765 11001', 'OPERATIONAL'),
(2, 2, 3, 'Riverside Central Stadium Camp', 'Station Road, Eastern Riverfront', 1500, 1340, '+91 98765 11002', 'OPERATIONAL'),
(3, 3, NULL, 'Hillside High School Safe Haven', 'Ridge Highway Mile 14', 500, 490, '+91 98765 11003', 'AT_CAPACITY'),
(4, 1, NULL, 'Civic Auditorium Evacuation Zone', 'Harbor Boulevard, Zone B', 600, 310, '+91 98765 11004', 'OPERATIONAL');

-- 4. ORGANIZATIONS
INSERT INTO organizations (organization_id, user_id, org_name, org_type, contact_person, phone, email, address) VALUES
(1, 4, 'Red Cross International Aid', 'RED_CROSS', 'Marcus Vance', '+91 98765 22001', 'redcross@relief.org', 'Relief Logistics Complex, Zone 1'),
(2, 5, 'UNICEF Child & Family Support', 'NGO', 'Elena Rostova', '+91 98765 22002', 'unicef@relief.org', 'Humanitarian Hub, Sector 9'),
(3, NULL, 'National Disaster Relief Force Depot', 'GOVT', 'Col. S. Deshmukh', '+91 98765 22003', 'ndrf.depot@relief.gov', 'Central Military Supply Base');

-- 5. RESOURCES
INSERT INTO resources (resource_id, organization_id, resource_name, resource_type, quantity, unit, expiry_date, availability_status, description) VALUES
(1, 1, 'Purified Drinking Water Packs', 'WATER', 4500, 'Bottles (1L)', '2027-12-31', 'AVAILABLE', 'Sealed, mineralized clean drinking water for dehydrated evacuees.'),
(2, 1, 'Emergency Trauma & First Aid Kit', 'MEDICINE', 350, 'Kits', '2028-06-30', 'AVAILABLE', 'Sterile bandages, antiseptic, tourniquets, burn dressings, sutures.'),
(3, 2, 'Ready-to-Eat High Calorie Meals', 'FOOD', 3200, 'Meal Packs', '2027-09-15', 'AVAILABLE', 'Nutritionally dense, shelf-stable self-heating ration packs.'),
(4, 2, 'Thermal Fleece Blankets', 'CLOTHING', 1200, 'Pieces', 'N/A', 'AVAILABLE', 'Heavy-duty insulated emergency blankets for flood & cold weather.'),
(5, 3, 'Inflatable Heavy-Duty Rescue Rafts', 'RESCUE_GEAR', 18, 'Rafts', 'N/A', 'AVAILABLE', 'Motor-compatible rescue boats accommodating 8 passengers each.'),
(6, 3, 'Pediatric Antibiotics & ORS Sachets', 'MEDICINE', 750, 'Boxes', '2027-04-20', 'AVAILABLE', 'Essential rehydration salts and child-safe infection defense medicines.');

-- 6. VEHICLES
INSERT INTO vehicles (vehicle_id, vehicle_number, vehicle_type, capacity_kg, driver_name, contact, availability_status) VALUES
(1, 'NDRF-TRK-101', 'TRUCK', 5000, 'Baldev Singh', '+91 98765 33001', 'AVAILABLE'),
(2, 'REDX-VAN-204', 'VAN', 1200, 'Imran Qureshi', '+91 98765 33002', 'AVAILABLE'),
(3, 'COAST-BOAT-03', 'BOAT', 900, 'K. Arumugam', '+91 98765 33003', 'AVAILABLE'),
(4, 'AIR-HELI-07', 'HELICOPTER', 3500, 'Capt. R. Sengupta', '+91 98765 33004', 'AVAILABLE'),
(5, 'MED-AMB-112', 'AMBULANCE', 600, 'Vikram Mehta', '+91 98765 33005', 'IN_TRANSIT');

-- 7. VOLUNTEERS
INSERT INTO volunteers (volunteer_id, user_id, name, skill, experience_years, availability_status, contact, location) VALUES
(1, 6, 'Rahul Sharma', 'RESCUE', 5, 'AVAILABLE', '+91 98765 00006', 'Coastal Sector Basecamp'),
(2, 7, 'Priya Nair', 'MEDICAL', 4, 'AVAILABLE', '+91 98765 00007', 'Central Triage Center'),
(3, NULL, 'Amit Patel', 'DRIVING', 7, 'AVAILABLE', '+91 98765 44001', 'Depot Staging Area'),
(4, NULL, 'Sunita Rao', 'LOGISTICS', 3, 'AVAILABLE', '+91 98765 44002', 'Civic Auditorium'),
(5, NULL, 'Devendra Jha', 'FIRST_AID', 2, 'ASSIGNED', '+91 98765 44003', 'Hillside Safe Haven');

-- 8. RESOURCE REQUESTS
-- Scoring Formula: (Urgency * 40) + (People * 0.35) + (Hours * 2.0) + DisasterBonus
INSERT INTO resource_requests (request_id, shelter_id, resource_type, quantity, unit, urgency, people_affected, priority_score, status, notes, requested_date, required_date) VALUES
(1, 1, 'WATER', 600, 'Bottles (1L)', 'CRITICAL', 680, 663.0, 'PENDING', 'Drinking water contamination due to tidal surge. Evacuees experiencing severe dehydration.', CURRENT_TIMESTAMP, 'Immediate'),
(2, 3, 'MEDICINE', 50, 'Kits', 'CRITICAL', 490, 596.5, 'ALLOCATED', 'Severe lacerations and fractures from falling roof debris after tremors.', CURRENT_TIMESTAMP, 'Today 20:00'),
(3, 2, 'FOOD', 800, 'Meal Packs', 'HIGH', 1340, 764.0, 'PENDING', 'Brahmaputra embankment breach has isolated camp; rations exhausted.', CURRENT_TIMESTAMP, 'Within 6 hours'),
(4, 4, 'CLOTHING', 250, 'Pieces', 'MEDIUM', 310, 273.5, 'PENDING', 'Submerged clothing items, cold evening winds approaching coastal zone.', CURRENT_TIMESTAMP, 'Tomorrow Morning');

-- 9. ALLOCATIONS
INSERT INTO allocations (allocation_id, request_id, resource_id, allocated_quantity, volunteer_id, vehicle_id, allocation_date, dispatch_date, delivery_date, status, tracking_notes) VALUES
(1, 2, 2, 50, 5, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, NULL, 'IN_TRANSIT', 'Ambulance MED-AMB-112 dispatched with trauma kits. Driver in communication via VHF radio.');

-- 10. SYSTEM LOGS
INSERT INTO system_logs (timestamp, log_level, module, message) VALUES
(CURRENT_TIMESTAMP, 'INFO', 'SYSTEM', 'Disaster Relief Resource Coordinator initialized successfully.'),
(CURRENT_TIMESTAMP, 'INFO', 'REQUEST', 'Emergency request #1 (WATER) registered from Coastal Community Hall Relief Center.'),
(CURRENT_TIMESTAMP, 'WARN', 'DISASTER', 'Super Cyclone Vardah upgraded to CRITICAL severity by Meteorological Observatory.'),
(CURRENT_TIMESTAMP, 'INFO', 'ALLOCATION', 'Allocation #1 assigned to Ambulance MED-AMB-112 for Hillside High School.');
