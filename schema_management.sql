DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS departments CASCADE;
DROP TABLE IF EXISTS items CASCADE;

CREATE TABLE IF NOT EXISTS items (
	id SERIAL NOT NULL, 
	item_code VARCHAR(8) UNIQUE NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	unit_price INT NOT NULL, 
	carry_over_stock INT DEFAULT 0 NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid INT DEFAULT 0, 
	PRIMARY KEY (id)
);

INSERT INTO items (item_code, name, unit_price, carry_over_stock) VALUES 
	('00000001','A001',28400,14), 
	('00000002','A002',28700,14), 
	('00000003','A003',29000,6), 
	('00000004','A004',29300,6), 
	('00000005','A005',29600,6), 
	('00000006','A006',29900,11), 
	('00000007','A007',30200,2), 
	('00000008','A008',30500,6), 
	('00000009','A009',30800,7), 
	('00000010','A010',31100,4), 
	('00000011','B001',25400,14), 
	('00000012','B002',25800,18), 
	('00000013','B003',26200,19), 
	('00000014','B004',26600,14), 
	('00000015','B005',27000,3), 
	('00000016','B006',27400,12), 
	('00000017','B007',27800,20), 
	('00000018','B008',28200,16), 
	('00000019','B009',28600,12), 
	('00000020','B010',29000,6), 
	('00000021','C001',29300,1), 
	('00000022','C002',29500,0), 
	('00000023','C003',29700,19), 
	('00000024','C004',29900,17), 
	('00000025','C005',30100,17), 
	('00000026','C006',30300,15), 
	('00000027','C007',30500,16), 
	('00000028','C008',30700,12), 
	('00000029','C009',30900,1), 
	('00000030','C010',31100,7), 
	('00000031','D001',45100,13), 
	('00000032','D002',45600,5), 
	('00000033','D003',46100,3), 
	('00000034','D004',46600,18), 
	('00000035','D005',47100,9), 
	('00000036','D006',47600,14), 
	('00000037','D007',48100,4), 
	('00000038','D008',48600,11), 
	('00000039','D009',49100,1), 
	('00000040','D010',49600,20), 
	('00000041','E001',19800,3), 
	('00000042','E002',20000,16), 
	('00000043','E003',20200,13), 
	('00000044','E004',20400,18), 
	('00000045','E005',20600,15), 
	('00000046','E006',20800,9), 
	('00000047','E007',21000,3), 
	('00000048','E008',21200,10), 
	('00000049','E009',21400,14), 
	('00000050','E010',21600,20), 
	('00000051','F001',30000,16), 
	('00000052','F002',31000,2), 
	('00000053','F003',32000,2), 
	('00000054','F004',33000,13), 
	('00000055','F005',34000,20), 
	('00000056','F006',35000,14), 
	('00000057','F007',36000,20), 
	('00000058','F008',37000,18), 
	('00000059','F009',38000,17), 
	('00000060','F010',39000,9), 
	('00000061','G001',27000,9), 
	('00000062','G002',28500,17), 
	('00000063','G003',30000,7), 
	('00000064','G004',31500,20), 
	('00000065','G005',33000,6), 
	('00000066','G006',34500,15), 
	('00000067','G007',36000,3), 
	('00000068','G008',37500,17), 
	('00000069','G009',39000,10), 
	('00000070','G010',40500,0), 
	('00000071','H001',26000,17), 
	('00000072','H002',26400,1), 
	('00000073','H003',26800,7), 
	('00000074','H004',27200,9), 
	('00000075','H005',27600,4), 
	('00000076','H006',28000,7), 
	('00000077','H007',28400,3), 
	('00000078','H008',28800,11), 
	('00000079','H009',29200,20), 
	('00000080','H010',29600,17), 
	('00000081','I001',35100,6), 
	('00000082','I002',35400,0), 
	('00000083','I003',35700,15), 
	('00000084','I004',36000,10), 
	('00000085','I005',36300,2), 
	('00000086','I006',36600,15), 
	('00000087','I007',36900,1), 
	('00000088','I008',37200,18), 
	('00000089','I009',37500,13), 
	('00000090','I010',37800,9), 
	('00000091','J001',28900,4), 
	('00000092','J002',29400,11), 
	('00000093','J003',29900,3), 
	('00000094','J004',30400,11), 
	('00000095','J005',30900,0), 
	('00000096','J006',31400,16), 
	('00000097','J007',31900,1), 
	('00000098','J008',32400,16), 
	('00000099','J009',32900,1), 
	('00000100','J010',33400,10);

CREATE TABLE IF NOT EXISTS departments (
	id SERIAL NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	PRIMARY KEY (id)
);

INSERT INTO departments (name) VALUES 
	('間接'), 
	('営業'), 
	('旋盤'), 
	('切削'), 
	('焼入'), 
	('研磨'), 
	('検査'), 
	('組立'), 
	('梱包');

CREATE TABLE IF NOT EXISTS employees (
	id SERIAL NOT NULL, 
	username VARCHAR(32) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	department_id INT NOT NULL, 
	authority VARCHAR(32) DEFAULT 'ROLE_USER' NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid INT DEFAULT 0, 
	PRIMARY KEY (id)
);

ALTER TABLE employees ADD CONSTRAINT FK_employee_department FOREIGN KEY (department_id) REFERENCES departments;

INSERT INTO employees (username, password, name, department_id, authority) VALUES 
	('0001', '$2a$10$FTvFiXdJvVsngfi5gdwjZ.NRo9vz.FZ61LcDHUOogQddwJWwgGdJK', 'admin', 1, 'ROLE_ADMIN'); 

INSERT INTO employees (username, password, name, department_id) VALUES 
	('0002', '$2a$10$4q2IFjpQhI/fCJlVdTXtVO/dirmor6FajB2h1CteSAXrJ.diNucoC', '社員A', 1),
   	('0003', '$2a$10$aOd3FYnQGWgzDvfNHZimxOURCpYyYK/9lkFUoyqGsiA60xQ8cmVvu', '社員B', 2),
    ('0004', '$2a$10$FgS1bDHplMGNG2VWtibej.yg6l.HHZrgmY6N1ZLK0zVU9EA5koq6i', '社員C', 3),
   	('0005', '$2a$10$rwRry5KvwYPhw6sHa8HNjuaKS.TDyCEcJbwGM7twElLW02cc5r.O.', '社員D', 4),
    ('0006', '$2a$10$AalEW50Z92JaZ0GmxX3bj.O2PYmb7s12Vq.xF.0YUKbO9Zvizh4uO', '社員E', 5),
    ('0007', '$2a$10$iLAyKBgfnFrtjW2KA/AQCeusjR8ip.KsjlSH6BAiorDyPnseKQQ3u', '社員F', 6),
   	('0008', '$2a$10$HXeIExG1cLFAJiZFotRSIeihIu/w6JV1BLw9ncg51VuggsqDEMUUK', '社員G', 7),
   	('0009', '$2a$10$G7DX1chDEd58wtPwq95tuuWhJdrO0fKEAEooQes8xox5T1NLHSi.a', '社員H', 8),
   	('0010', '$2a$10$zJoCSWp.oDKvXBhLiDfhQO01W6z/VzarQAqhRbRwR6rMU.BuuxbEG', '社員I', 9);
   
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO management;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO management;

