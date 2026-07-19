ALTER TABLE admins
ADD CONSTRAINT fk_admin_role
FOREIGN KEY (role_id)
REFERENCES roles(id);