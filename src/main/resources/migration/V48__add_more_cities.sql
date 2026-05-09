-- Add more comprehensive cities data for India

INSERT INTO cities (
    name,
    state_id,
    admin_id,
    is_active,
    created_at,
    updated_at
) VALUES
-- Maharashtra
('Mumbai', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Pune', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Nagpur', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Thane', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Nashik', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Aurangabad', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Solapur', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Amravati', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Navi Mumbai', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Kolhapur', (SELECT id FROM states WHERE name='Maharashtra'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Gujarat
('Ahmedabad', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Surat', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Vadodara', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Rajkot', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Gandhinagar', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Bhavnagar', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Jamnagar', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Junagadh', (SELECT id FROM states WHERE name='Gujarat'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Karnataka
('Bangalore', (SELECT id FROM states WHERE name='Karnataka'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Mysore', (SELECT id FROM states WHERE name='Karnataka'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Hubli', (SELECT id FROM states WHERE name='Karnataka'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Mangalore', (SELECT id FROM states WHERE name='Karnataka'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Belgaum', (SELECT id FROM states WHERE name='Karnataka'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Gulbarga', (SELECT id FROM states WHERE name='Karnataka'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Davanagere', (SELECT id FROM states WHERE name='Karnataka'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Tamil Nadu
('Chennai', (SELECT id FROM states WHERE name='Tamil Nadu'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Coimbatore', (SELECT id FROM states WHERE name='Tamil Nadu'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Madurai', (SELECT id FROM states WHERE name='Tamil Nadu'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Tiruchirappalli', (SELECT id FROM states WHERE name='Tamil Nadu'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Salem', (SELECT id FROM states WHERE name='Tamil Nadu'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Tirunelveli', (SELECT id FROM states WHERE name='Tamil Nadu'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Vellore', (SELECT id FROM states WHERE name='Tamil Nadu'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Andhra Pradesh
('Visakhapatnam', (SELECT id FROM states WHERE name='Andhra Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Vijayawada', (SELECT id FROM states WHERE name='Andhra Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Guntur', (SELECT id FROM states WHERE name='Andhra Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Nellore', (SELECT id FROM states WHERE name='Andhra Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Kurnool', (SELECT id FROM states WHERE name='Andhra Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Rajahmundry', (SELECT id FROM states WHERE name='Andhra Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Telangana
('Hyderabad', (SELECT id FROM states WHERE name='Telangana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Warangal', (SELECT id FROM states WHERE name='Telangana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Nizamabad', (SELECT id FROM states WHERE name='Telangana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Karimnagar', (SELECT id FROM states WHERE name='Telangana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- West Bengal
('Kolkata', (SELECT id FROM states WHERE name='West Bengal'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Howrah', (SELECT id FROM states WHERE name='West Bengal'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Durgapur', (SELECT id FROM states WHERE name='West Bengal'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Siliguri', (SELECT id FROM states WHERE name='West Bengal'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Asansol', (SELECT id FROM states WHERE name='West Bengal'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Rajasthan
('Jaipur', (SELECT id FROM states WHERE name='Rajasthan'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Jodhpur', (SELECT id FROM states WHERE name='Rajasthan'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Udaipur', (SELECT id FROM states WHERE name='Rajasthan'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Kota', (SELECT id FROM states WHERE name='Rajasthan'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Bikaner', (SELECT id FROM states WHERE name='Rajasthan'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Ajmer', (SELECT id FROM states WHERE name='Rajasthan'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Uttar Pradesh
('Lucknow', (SELECT id FROM states WHERE name='Uttar Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Kanpur', (SELECT id FROM states WHERE name='Uttar Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Ghaziabad', (SELECT id FROM states WHERE name='Uttar Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Agra', (SELECT id FROM states WHERE name='Uttar Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Varanasi', (SELECT id FROM states WHERE name='Uttar Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Meerut', (SELECT id FROM states WHERE name='Uttar Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Allahabad', (SELECT id FROM states WHERE name='Uttar Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Bareilly', (SELECT id FROM states WHERE name='Uttar Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Delhi
('New Delhi', (SELECT id FROM states WHERE name='Delhi'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('North Delhi', (SELECT id FROM states WHERE name='Delhi'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('South Delhi', (SELECT id FROM states WHERE name='Delhi'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('East Delhi', (SELECT id FROM states WHERE name='Delhi'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('West Delhi', (SELECT id FROM states WHERE name='Delhi'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Central Delhi', (SELECT id FROM states WHERE name='Delhi'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Punjab
('Chandigarh', (SELECT id FROM states WHERE name='Punjab'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Ludhiana', (SELECT id FROM states WHERE name='Punjab'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Amritsar', (SELECT id FROM states WHERE name='Punjab'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Jalandhar', (SELECT id FROM states WHERE name='Punjab'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Patiala', (SELECT id FROM states WHERE name='Punjab'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Haryana
('Gurgaon', (SELECT id FROM states WHERE name='Haryana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Faridabad', (SELECT id FROM states WHERE name='Haryana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Panipat', (SELECT id FROM states WHERE name='Haryana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Ambala', (SELECT id FROM states WHERE name='Haryana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Karnal', (SELECT id FROM states WHERE name='Haryana'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Bihar
('Patna', (SELECT id FROM states WHERE name='Bihar'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Gaya', (SELECT id FROM states WHERE name='Bihar'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Bhagalpur', (SELECT id FROM states WHERE name='Bihar'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Muzaffarpur', (SELECT id FROM states WHERE name='Bihar'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Purnia', (SELECT id FROM states WHERE name='Bihar'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Odisha
('Bhubaneswar', (SELECT id FROM states WHERE name='Odisha'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Cuttack', (SELECT id FROM states WHERE name='Odisha'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Rourkela', (SELECT id FROM states WHERE name='Odisha'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Puri', (SELECT id FROM states WHERE name='Odisha'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Sambalpur', (SELECT id FROM states WHERE name='Odisha'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Kerala
('Thiruvananthapuram', (SELECT id FROM states WHERE name='Kerala'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Kochi', (SELECT id FROM states WHERE name='Kerala'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Kozhikode', (SELECT id FROM states WHERE name='Kerala'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Thrissur', (SELECT id FROM states WHERE name='Kerala'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Kollam', (SELECT id FROM states WHERE name='Kerala'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- Madhya Pradesh
('Bhopal', (SELECT id FROM states WHERE name='Madhya Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Indore', (SELECT id FROM states WHERE name='Madhya Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Gwalior', (SELECT id FROM states WHERE name='Madhya Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Jabalpur', (SELECT id FROM states WHERE name='Madhya Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),
('Ujjain', (SELECT id FROM states WHERE name='Madhya Pradesh'), NULL, TRUE, CURRENT_TIMESTAMP, NULL),

-- General
('Other', (SELECT id FROM states WHERE name='Other'), NULL, TRUE, CURRENT_TIMESTAMP, NULL)
ON CONFLICT (name, state_id) DO NOTHING;
