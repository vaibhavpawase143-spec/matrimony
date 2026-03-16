INSERT INTO blood_groups (admin_id, type, status, created_at, updated_at)
VALUES (2,'A+',true,NOW(),NOW()) ON CONFLICT (type) DO NOTHING;

INSERT INTO blood_groups (admin_id, type, status, created_at, updated_at)
VALUES (2,'A-',true,NOW(),NOW()) ON CONFLICT (type) DO NOTHING;

INSERT INTO blood_groups (admin_id, type, status, created_at, updated_at)
VALUES (2,'B+',true,NOW(),NOW()) ON CONFLICT (type) DO NOTHING;

INSERT INTO blood_groups (admin_id, type, status, created_at, updated_at)
VALUES (2,'B-',true,NOW(),NOW()) ON CONFLICT (type) DO NOTHING;

INSERT INTO blood_groups (admin_id, type, status, created_at, updated_at)
VALUES (2,'AB+',true,NOW(),NOW()) ON CONFLICT (type) DO NOTHING;

INSERT INTO blood_groups (admin_id, type, status, created_at, updated_at)
VALUES (2,'AB-',true,NOW(),NOW()) ON CONFLICT (type) DO NOTHING;

INSERT INTO blood_groups (admin_id, type, status, created_at, updated_at)
VALUES (2,'O+',true,NOW(),NOW()) ON CONFLICT (type) DO NOTHING;

INSERT INTO blood_groups (admin_id, type, status, created_at, updated_at)
VALUES (2,'O-',true,NOW(),NOW()) ON CONFLICT (type) DO NOTHING;