DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS department CASCADE;

CREATE TABLE IF NOT EXISTS department (
	id SERIAL NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	PRIMARY KEY (id)
);

INSERT INTO department (name) VALUES 
	('間接'), 
	('営業'), 
	('旋盤'), 
	('切削'), 
	('焼入'), 
	('研磨'), 
	('検査'), 
	('組立'), 
	('梱包');

CREATE TABLE IF NOT EXISTS employee (
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

ALTER TABLE employee ADD CONSTRAINT FK_employee_department FOREIGN KEY (department_id) REFERENCES department;

INSERT INTO employee (username, password, name, department_id, authority) 
VALUES ('0001', '$2a$10$FTvFiXdJvVsngfi5gdwjZ.NRo9vz.FZ61LcDHUOogQddwJWwgGdJK', 'admin', 1, 'ROLE_ADMIN'); 

INSERT INTO employee (username, password, name, department_id) 
VALUES ('0002', '$2a$10$4q2IFjpQhI/fCJlVdTXtVO/dirmor6FajB2h1CteSAXrJ.diNucoC', '社員A', 1),
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

