INSERT INTO roles (name, description) VALUES
('ADMIN', 'Full system control: users, departments, roles, all data'),
('ASSET_MANAGER', 'Owns asset lifecycle, allocations, bookings, maintenance, audits'),
('DEPT_HEAD', 'Manages their own department\'s people and approves their dept\'s requests'),
('EMPLOYEE', 'Requests things for themselves; views their own data')
ON CONFLICT (name) DO NOTHING;
