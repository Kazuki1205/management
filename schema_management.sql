-- DROP処理 --
DROP TABLE IF EXISTS employees CASCADE;
DROP TABLE IF EXISTS departments CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS items_history CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS customers_history CASCADE;
DROP TABLE IF EXISTS productions CASCADE;
DROP TABLE IF EXISTS employees_history CASCADE;
DROP TABLE IF EXISTS reports CASCADE;
DROP TABLE IF EXISTS stocks CASCADE;
DROP TABLE IF EXISTS storings CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS orders_details CASCADE;
DROP TABLE IF EXISTS shippings CASCADE;
DROP FUNCTION IF EXISTS process_employees_history CASCADE; 
DROP FUNCTION IF EXISTS process_items_history CASCADE; 
DROP FUNCTION IF EXISTS process_customers_history CASCADE; 
DROP FUNCTION IF EXISTS process_stocks CASCADE; 

---- テーブル作成 ----
-- 顧客テーブル --
CREATE TABLE IF NOT EXISTS customers (
	id BIGSERIAL NOT NULL, 
	code VARCHAR(4) UNIQUE NOT NULL, 
	name VARCHAR(128) NOT NULL, 
	postal_code VARCHAR(8) NOT NULL, 
	first_address VARCHAR(8) NOT NULL, 
	second_address VARCHAR(128) NOT NULL, 
	third_address VARCHAR(255) NOT NULL, 
	phone_number VARCHAR(16) NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (id)
);

-- 顧客履歴テーブル --
CREATE TABLE IF NOT EXISTS customers_history (
	id BIGSERIAL NOT NULL, 
	customer_id BIGINT NOT NULL, 
	code VARCHAR(4) NOT NULL, 
	name VARCHAR(128) NOT NULL, 
	postal_code VARCHAR(8) NOT NULL, 
	first_address VARCHAR(8) NOT NULL, 
	second_address VARCHAR(128) NOT NULL, 
	third_address VARCHAR(255) NOT NULL, 
	phone_number VARCHAR(16) NOT NULL, 
	updated_at TIMESTAMP NOT NULL, 
	PRIMARY KEY (id)
);

ALTER TABLE customers_history ADD CONSTRAINT FK_customers_history_customers FOREIGN KEY (customer_id) REFERENCES customers;

-- 商品テーブル --
CREATE TABLE IF NOT EXISTS items (
	id BIGSERIAL NOT NULL, 
	code VARCHAR(8) UNIQUE NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	unit_price BIGINT NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (id)
);

-- 商品履歴テーブル --
CREATE TABLE IF NOT EXISTS items_history (
	id BIGSERIAL NOT NULL, 
	item_id BIGINT NOT NULL, 
	code VARCHAR(8) NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	unit_price BIGINT NOT NULL, 
	updated_at TIMESTAMP NOT NULL, 
	PRIMARY KEY (id)
);

ALTER TABLE items_history ADD CONSTRAINT FK_items_history_items FOREIGN KEY (item_id) REFERENCES items;

-- 部署テーブル --
CREATE TABLE IF NOT EXISTS departments (
	id BIGSERIAL NOT NULL, 
	code VARCHAR(4) UNIQUE NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (id)
);

-- 社員テーブル --
CREATE TABLE IF NOT EXISTS employees (
	id BIGSERIAL NOT NULL, 
	username VARCHAR(4) UNIQUE NOT NULL,
	password VARCHAR(255) NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	department_id BIGINT NOT NULL, 
	authority VARCHAR(32) NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (id)
);

ALTER TABLE employees ADD CONSTRAINT FK_employees_departments FOREIGN KEY (department_id) REFERENCES departments;
   	
-- 社員履歴テーブル --
CREATE TABLE IF NOT EXISTS employees_history (
	id BIGSERIAL NOT NULL, 
	employee_id BIGINT NOT NULL, 
	username VARCHAR(4) NOT NULL, 
	name VARCHAR(32) NOT NULL, 
	department_id BIGINT NOT NULL, 
	updated_at TIMESTAMP NOT NULL,
	PRIMARY KEY (id)
);

ALTER TABLE employees_history ADD CONSTRAINT FK_employees_history_employees FOREIGN KEY (employee_id) REFERENCES employees;
ALTER TABLE employees_history ADD CONSTRAINT FK_employees_history_departments FOREIGN KEY (department_id) REFERENCES departments;
   
-- 製作テーブル --
CREATE TABLE IF NOT EXISTS productions (
	id BIGSERIAL NOT NULL, 
	item_id BIGINT NOT NULL, 
	lot_number VARCHAR(16) UNIQUE NOT NULL, 
	lot_quantity INT NOT NULL, 
	scheduled_completion_date DATE NOT NULL, 
	completion_date DATE DEFAULT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (id)
);

ALTER TABLE productions ADD CONSTRAINT FK_productions_items FOREIGN KEY (item_id) REFERENCES items;

-- 日報テーブル --
CREATE TABLE IF NOT EXISTS reports (
	id BIGSERIAL NOT NULL, 
	production_id BIGINT NOT NULL, 
	employee_history_id BIGINT NOT NULL, 
	completion_quantity INT NOT NULL, 
	failure_quantity INT NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (id)
);

ALTER TABLE reports ADD CONSTRAINT FK_reports_productions FOREIGN KEY (production_id) REFERENCES productions;
ALTER TABLE reports ADD CONSTRAINT FK_reports_employees_history FOREIGN KEY (employee_history_id) REFERENCES employees_history;

-- 在庫テーブル --
CREATE TABLE IF NOT EXISTS stocks (
	id BIGSERIAL NOT NULL, 
	item_id BIGINT NOT NULL, 
	actual_quantity INT DEFAULT ROUND(( RANDOM() * (1 - 100) )::NUMERIC, 0) + 100, 
	created_at TIMESTAMP NOT NULL, 
	updated_at TIMESTAMP NOT NULL, 
	PRIMARY KEY (id), 
	CONSTRAINT quantity_check CHECK(actual_quantity >= 0)
);

ALTER TABLE stocks ADD CONSTRAINT FK_stocks_items FOREIGN KEY (item_id) REFERENCES items;

-- 入庫テーブル --
CREATE TABLE IF NOT EXISTS storings (
	id BIGSERIAL NOT NULL, 
	production_id BIGINT NOT NULL, 
	storing_quantity INT NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	PRIMARY KEY (id)
);

ALTER TABLE storings ADD CONSTRAINT FK_storings_productions FOREIGN KEY (production_id) REFERENCES productions;

-- 受注テーブル --
CREATE TABLE IF NOT EXISTS orders (
	id BIGSERIAL NOT NULL, 
	order_number VARCHAR(8) UNIQUE NOT NULL, 
	customer_history_id BIGINT NOT NULL, 
	employee_history_id BIGINT NOT NULL, 
	completion_date DATE DEFAULT NULL,  
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (order_number)
);

ALTER TABLE orders ADD CONSTRAINT FK_orders_customers_history FOREIGN KEY (customer_history_id) REFERENCES customers_history;
ALTER TABLE orders ADD CONSTRAINT FK_orders_employees_history FOREIGN KEY (employee_history_id) REFERENCES employees_history;

-- 受注明細テーブル --
CREATE TABLE IF NOT EXISTS orders_details (
	order_number VARCHAR(8) NOT NULL, 
	detail_id INT NOT NULL, 
	item_history_id BIGINT NOT NULL, 
	order_quantity INT NOT NULL, 
	completion_date DATE DEFAULT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (order_number, detail_id)
);

ALTER TABLE orders_details ADD CONSTRAINT FK_orders_details_orders FOREIGN KEY (order_number) REFERENCES orders;
ALTER TABLE orders_details ADD CONSTRAINT FK_orders_details_items_history FOREIGN KEY (item_history_id) REFERENCES items_history;

-- 出荷テーブル --
CREATE TABLE IF NOT EXISTS shippings (
	id BIGSERIAL NOT NULL, 
	order_number VARCHAR(8) NOT NULL, 
	detail_id INT NOT NULL, 
	shipping_quantity INT NOT NULL, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	PRIMARY KEY (id)
);

ALTER TABLE shippings ADD CONSTRAINT FK_shippings_orders_details FOREIGN KEY (order_number, detail_id) REFERENCES orders_details;

---- 関数・トリガー設定 ----
-- 社員履歴テーブル作成関数 --
CREATE FUNCTION process_employees_history() RETURNS TRIGGER AS $process_employees_history$
BEGIN
	INSERT INTO 
		employees_history (
			employee_id, 
			username, 
			name, 
			department_id, 
			updated_at
		) 
	SELECT
		emp.id, 
		emp.username, 
		emp.name, 
		dep.id, 
		emp.updated_at
	FROM
		employees emp
	INNER JOIN
		departments dep
	ON
		emp.department_id = dep.id
	WHERE
		NEW.id = emp.id;
		
	RETURN NULL;
END;
$process_employees_history$
LANGUAGE plpgsql;

-- 社員履歴テーブル作成トリガー --
CREATE TRIGGER trigger_employees_history AFTER INSERT OR UPDATE OF name, department_id ON employees
	FOR EACH ROW EXECUTE FUNCTION process_employees_history();

-- 商品履歴テーブル作成関数 --
CREATE FUNCTION process_items_history() RETURNS TRIGGER AS $process_items_history$
BEGIN
	INSERT INTO 
		items_history (
			item_id, 
			code, 
			name, 
			unit_price, 
			updated_at
		)
	VALUES (
		NEW.id, 
		NEW.code, 
		NEW.name, 
		NEW.unit_price, 
		NEW.updated_at
	);
	
	RETURN NULL;
END;
$process_items_history$
LANGUAGE plpgsql;

-- 商品履歴テーブル作成トリガー --
CREATE TRIGGER trigger_items_history AFTER INSERT OR UPDATE OF name, unit_price ON items
	FOR EACH ROW EXECUTE FUNCTION process_items_history();
	
-- 顧客履歴テーブル作成関数 --
CREATE FUNCTION process_customers_history() RETURNS TRIGGER AS $process_customers_history$
BEGIN
	INSERT INTO
		customers_history (
			customer_id, 
			code, 
			name, 
			postal_code, 
			first_address, 
			second_address, 
			third_address, 
			phone_number, 
			updated_at
		)
	VALUES (
		NEW.id, 
		NEW.code, 
		NEW.name, 
		NEW.postal_code, 
		NEW.first_address, 
		NEW.second_address, 
		NEW.third_address, 
		NEW.phone_number, 
		NEW.updated_at
	);
	
	RETURN NULL;
END;
$process_customers_history$
LANGUAGE plpgsql;

-- 顧客履歴テーブル作成トリガー --
CREATE TRIGGER trigger_customers_history AFTER INSERT OR UPDATE OF name, postal_code, first_address, second_address, third_address, phone_number ON customers
	FOR EACH ROW EXECUTE FUNCTION process_customers_history();
	
-- 在庫テーブル作成関数 --
CREATE FUNCTION process_stocks() RETURNS TRIGGER AS $process_stocks$
BEGIN
	INSERT INTO
		stocks (
			item_id, 
			created_at, 
			updated_at
		)
	VALUES (
		NEW.id, 
		NEW.created_at, 
		NEW.updated_at
	);
	
	RETURN NULL;
END;
$process_stocks$
LANGUAGE plpgsql;

-- 在庫テーブル作成トリガー --
CREATE TRIGGER trigger_stocks AFTER INSERT ON items
	FOR EACH ROW EXECUTE FUNCTION process_stocks();

---- 事前データ準備 ----
-- 顧客データ --		
INSERT INTO customers (code, name, postal_code, first_address, second_address, third_address, phone_number) VALUES
	('0001', 'SAMPLE会社01','028-7644','岩手県','八幡平市雀長根','8-12-2 雀長根ヒルズ 13F','0195-30-3691'), 
	('0002', 'SAMPLE会社02','501-4602','岐阜県','郡上市大和町小間見','6-14-8','0575-18-9506'), 
	('0003', 'SAMPLE会社03','202-0002','東京都','西東京市ひばりが丘北','5-11-8','042-212-7605'), 
	('0004', 'SAMPLE会社04','162-0856','東京都','新宿区市谷甲良町','3-8-7 ベルピア・市谷甲良町','03-3652-0357'), 
	('0005', 'SAMPLE会社05','135-0023','東京都','江東区平野','1-2-5','03-3709-5552'), 
	('0006', 'SAMPLE会社06','675-2202','兵庫県','加西市野条町','8-11-1-2F','0790-39-2093'), 
	('0007', 'SAMPLE会社07','666-0233','兵庫県','川辺郡猪名川町紫合','3-11-3-9階','072-189-2372'), 
	('0008', 'SAMPLE会社08','072-0001','北海道','美唄市大通東一条北','2-12-3','0126-65-2672'), 
	('0009', 'SAMPLE会社09','013-0544','秋田県','横手市大森町長助巻','3-2-2 ＴＯＰ・大森町長助巻 11階','0182-00-5096'), 
	('0010', 'SAMPLE会社10','740-0041','山口県','岩国市黒磯町','5-5-6-101号室','0827-82-3874'), 
	('0011', 'SAMPLE会社11','501-2303','岐阜県','山県市片原','8-12-6','0581-04-5797'), 
	('0012', 'SAMPLE会社12','185-0004','東京都','国分寺市新町','1-15-8','042-378-8121'), 
	('0013', 'SAMPLE会社13','502-0847','岐阜県','岐阜市早田栄町','6-9-1','058-761-0074'), 
	('0014', 'SAMPLE会社14','308-0076','茨城県','筑西市泉','2-9-2 ピソブランコ・泉','0296-14-3721'), 
	('0015', 'SAMPLE会社15','115-0053','東京都','北区赤羽台','3-10-1 Kコーポ赤羽台 102号室','03-3626-2510'), 
	('0016', 'SAMPLE会社16','865-0073','熊本県','玉名市横島町共栄','5-7-8','0968-16-4270'), 
	('0017', 'SAMPLE会社17','198-0023','東京都','青梅市今井','2-13-9 メゾン今井 1515','0428-40-9081'), 
	('0018', 'SAMPLE会社18','923-0183','石川県','小松市瀬領町','9-8-5','0761-00-0762'), 
	('0019', 'SAMPLE会社19','879-6426','大分県','豊後大野市大野町郡山','9-10-8 コスモ・イオ大野町郡山','0974-80-5181'), 
	('0020', 'SAMPLE会社20','500-8001','岐阜県','岐阜市鏡岩','6-13-7','058-622-9954'), 
	('0021', 'SAMPLE会社21','993-0014','山形県','長井市小出','7-14-5','0238-56-5728'), 
	('0022', 'SAMPLE会社22','954-0165','新潟県','長岡市福原','6476','0258-18-1595'), 
	('0023', 'SAMPLE会社23','692-0002','島根県','安来市上坂田町','8-12-8','0854-82-3245'), 
	('0024', 'SAMPLE会社24','949-2301','新潟県','上越市中郷区板橋','8-14-7 ピソブランコ・中郷区板橋 501','025-276-3449'), 
	('0025', 'SAMPLE会社25','441-1334','愛知県','新城市中宇利','6-3-2','05362-8-3746'), 
	('0026', 'SAMPLE会社26','311-1134','茨城県','水戸市百合が丘町','4-6-5','029-855-7698'), 
	('0027', 'SAMPLE会社27','507-0841','岐阜県','多治見市明治町','9-6-4','0572-06-5048'), 
	('0028', 'SAMPLE会社28','731-3166','広島県','広島市安佐南区大塚東','6-4-9','082-538-7743'), 
	('0029', 'SAMPLE会社29','063-0012','北海道','札幌市西区福井','3-3-7 アーバンライフ福井','011-146-5366'), 
	('0030', 'SAMPLE会社30','671-2131','兵庫県','姫路市夢前町戸倉','6-12-1','07933-7-4354');
	
-- 商品データ --
INSERT INTO items (code, name, unit_price) VALUES 
	('00000001','A001',28400), 
	('00000002','A002',28700), 
	('00000003','A003',29000), 
	('00000004','A004',29300), 
	('00000005','A005',29600), 
	('00000006','A006',29900), 
	('00000007','A007',30200), 
	('00000008','A008',30500), 
	('00000009','A009',30800), 
	('00000010','A010',31100), 
	('00000011','B001',25400), 
	('00000012','B002',25800), 
	('00000013','B003',26200), 
	('00000014','B004',26600), 
	('00000015','B005',27000), 
	('00000016','B006',27400), 
	('00000017','B007',27800), 
	('00000018','B008',28200), 
	('00000019','B009',28600), 
	('00000020','B010',29000), 
	('00000021','C001',29300), 
	('00000022','C002',29500), 
	('00000023','C003',29700), 
	('00000024','C004',29900), 
	('00000025','C005',30100), 
	('00000026','C006',30300), 
	('00000027','C007',30500), 
	('00000028','C008',30700), 
	('00000029','C009',30900), 
	('00000030','C010',31100), 
	('00000031','D001',45100), 
	('00000032','D002',45600), 
	('00000033','D003',46100), 
	('00000034','D004',46600), 
	('00000035','D005',47100), 
	('00000036','D006',47600), 
	('00000037','D007',48100), 
	('00000038','D008',48600), 
	('00000039','D009',49100), 
	('00000040','D010',49600), 
	('00000041','E001',19800), 
	('00000042','E002',20000), 
	('00000043','E003',20200), 
	('00000044','E004',20400), 
	('00000045','E005',20600), 
	('00000046','E006',20800), 
	('00000047','E007',21000), 
	('00000048','E008',21200), 
	('00000049','E009',21400), 
	('00000050','E010',21600);
	
-- 部署データ --
INSERT INTO departments (code, name) VALUES 
	('0001', '間接'), 
	('0002', '旋盤'), 
	('0003', '切削'), 
	('0004', '焼入'), 
	('0005', '研磨'), 
	('0006', '検査'), 
	('0007', '組立'), 
	('0008', '梱包'),
	('0009', 'A営業所'), 
	('0010', 'B営業所'), 
	('0011', 'C営業所');
	
-- 社員データ --
INSERT INTO employees (username, password, name, department_id, authority) VALUES 
	('0001', '$2a$10$slx68sgVm43rRXdGd0bUV.sZGV5RaNtoH4MFw.jZW0s0wc3Ax3Zba', 'admin', 2, 'ROLE_ADMIN'),
	('0002', '$2a$10$4q2IFjpQhI/fCJlVdTXtVO/dirmor6FajB2h1CteSAXrJ.diNucoC', '社員A', 1, 'ROLE_OFFICE'),
   	('0003', '$2a$10$aOd3FYnQGWgzDvfNHZimxOURCpYyYK/9lkFUoyqGsiA60xQ8cmVvu', '社員B', 2, 'ROLE_FIELD'),
    ('0004', '$2a$10$FgS1bDHplMGNG2VWtibej.yg6l.HHZrgmY6N1ZLK0zVU9EA5koq6i', '社員C', 3, 'ROLE_FIELD'),
   	('0005', '$2a$10$rwRry5KvwYPhw6sHa8HNjuaKS.TDyCEcJbwGM7twElLW02cc5r.O.', '社員D', 4, 'ROLE_FIELD'),
    ('0006', '$2a$10$AalEW50Z92JaZ0GmxX3bj.O2PYmb7s12Vq.xF.0YUKbO9Zvizh4uO', '社員E', 5, 'ROLE_FIELD'),
    ('0007', '$2a$10$iLAyKBgfnFrtjW2KA/AQCeusjR8ip.KsjlSH6BAiorDyPnseKQQ3u', '社員F', 6, 'ROLE_FIELD'),
   	('0008', '$2a$10$HXeIExG1cLFAJiZFotRSIeihIu/w6JV1BLw9ncg51VuggsqDEMUUK', '社員G', 7, 'ROLE_FIELD'),
   	('0009', '$2a$10$G7DX1chDEd58wtPwq95tuuWhJdrO0fKEAEooQes8xox5T1NLHSi.a', '社員H', 8, 'ROLE_FIELD'),
   	('0010', '$2a$10$zJoCSWp.oDKvXBhLiDfhQO01W6z/VzarQAqhRbRwR6rMU.BuuxbEG', '社員I', 9, 'ROLE_SALE'),
   	('0011', '$2a$10$o4YyGGF0cospwLxq2s4yLe./aduDsvgaWlWgeyZxhQPtHyVcFZoji', '社員J', 10, 'ROLE_SALE'),
   	('0012', '$2a$10$IcVOaiPkuVkqzisPg71Re./xDVgqcWHRWF39Iiz6afWzoN2Jn33d2', '社員K', 11, 'ROLE_SALE');
   	
-- 製作データ --
INSERT INTO productions (item_id, lot_number, lot_quantity, scheduled_completion_date, created_at, updated_at) VALUES
	(7,'2022-03-0001',5,'2022-05-02','2022-03-04','2022-03-04'), 
	(26,'2022-03-0002',90,'2022-05-03','2022-03-05','2022-03-05'), 
	(33,'2022-03-0003',25,'2022-05-04','2022-03-06','2022-03-06'), 
	(17,'2022-03-0004',40,'2022-05-05','2022-03-07','2022-03-07'), 
	(31,'2022-03-0005',90,'2022-05-07','2022-03-09','2022-03-09'), 
	(7,'2022-03-0006',95,'2022-05-07','2022-03-09','2022-03-09'), 
	(10,'2022-03-0007',35,'2022-05-10','2022-03-12','2022-03-12'), 
	(5,'2022-03-0008',30,'2022-05-12','2022-03-14','2022-03-14'), 
	(49,'2022-03-0009',80,'2022-05-15','2022-03-17','2022-03-17'), 
	(18,'2022-03-0010',55,'2022-05-19','2022-03-21','2022-03-21'), 
	(29,'2022-03-0011',80,'2022-05-20','2022-03-22','2022-03-22'), 
	(41,'2022-03-0012',65,'2022-05-20','2022-03-22','2022-03-22'), 
	(7,'2022-03-0013',95,'2022-05-21','2022-03-23','2022-03-23'), 
	(10,'2022-03-0014',60,'2022-05-21','2022-03-23','2022-03-23'), 
	(19,'2022-03-0015',85,'2022-05-22','2022-03-24','2022-03-24'), 
	(19,'2022-03-0016',5,'2022-05-22','2022-03-24','2022-03-24'), 
	(47,'2022-03-0017',75,'2022-05-22','2022-03-24','2022-03-24'), 
	(11,'2022-03-0018',5,'2022-05-23','2022-03-25','2022-03-25'), 
	(12,'2022-03-0019',20,'2022-05-23','2022-03-25','2022-03-25'), 
	(49,'2022-03-0020',20,'2022-05-24','2022-03-26','2022-03-26'), 
	(33,'2022-03-0021',15,'2022-05-26','2022-03-28','2022-03-28'), 
	(48,'2022-03-0022',40,'2022-05-27','2022-03-29','2022-03-29'), 
	(29,'2022-03-0023',100,'2022-05-28','2022-03-30','2022-03-30'), 
	(49,'2022-03-0024',15,'2022-05-28','2022-03-30','2022-03-30'), 
	(13,'2022-03-0025',50,'2022-05-28','2022-03-30','2022-03-30'), 
	(14,'2022-04-0001',25,'2022-06-03','2022-04-05','2022-04-05'), 
	(9,'2022-04-0002',70,'2022-06-03','2022-04-05','2022-04-05'), 
	(7,'2022-04-0003',10,'2022-06-03','2022-04-05','2022-04-05'), 
	(41,'2022-04-0004',75,'2022-06-05','2022-04-07','2022-04-07'), 
	(32,'2022-04-0005',50,'2022-06-07','2022-04-09','2022-04-09'), 
	(10,'2022-04-0006',55,'2022-06-09','2022-04-11','2022-04-11'), 
	(27,'2022-04-0007',50,'2022-06-11','2022-04-13','2022-04-13'), 
	(5,'2022-04-0008',35,'2022-06-11','2022-04-13','2022-04-13'), 
	(32,'2022-04-0009',15,'2022-06-13','2022-04-15','2022-04-15'), 
	(6,'2022-04-0010',80,'2022-06-14','2022-04-16','2022-04-16'), 
	(34,'2022-04-0011',10,'2022-06-15','2022-04-17','2022-04-17'), 
	(24,'2022-04-0012',50,'2022-06-17','2022-04-19','2022-04-19'), 
	(14,'2022-04-0013',30,'2022-06-17','2022-04-19','2022-04-19'), 
	(21,'2022-04-0014',90,'2022-06-17','2022-04-19','2022-04-19'), 
	(27,'2022-04-0015',10,'2022-06-18','2022-04-20','2022-04-20'), 
	(28,'2022-04-0016',45,'2022-06-19','2022-04-21','2022-04-21'), 
	(40,'2022-04-0017',100,'2022-06-22','2022-04-24','2022-04-24'), 
	(24,'2022-04-0018',10,'2022-06-22','2022-04-24','2022-04-24'), 
	(29,'2022-04-0019',30,'2022-06-22','2022-04-24','2022-04-24'), 
	(28,'2022-04-0020',85,'2022-06-24','2022-04-26','2022-04-26'), 
	(49,'2022-04-0021',100,'2022-06-24','2022-04-26','2022-04-26'), 
	(33,'2022-04-0022',65,'2022-06-29','2022-05-01','2022-05-01'), 
	(13,'2022-04-0023',45,'2022-06-30','2022-05-02','2022-05-02'), 
	(10,'2022-04-0024',80,'2022-06-30','2022-05-02','2022-05-02'), 
	(21,'2022-04-0025',50,'2022-06-30','2022-05-02','2022-05-02'); 

-- 日報データ --
INSERT INTO reports (production_id, employee_history_id, completion_quantity, failure_quantity, created_at, updated_at) VALUES
	(1,3,5,0,'2022-03-14','2022-03-14'),  
	(2,3,88,2,'2022-03-15','2022-03-15'),  
	(3,3,25,0,'2022-03-16','2022-03-16'),  
	(4,3,38,2,'2022-03-17','2022-03-17'),  
	(5,3,97,3,'2022-03-19','2022-03-19'),  
	(6,3,93,2,'2022-03-19','2022-03-19'),  
	(7,3,35,0,'2022-03-22','2022-03-22'),  
	(8,3,30,0,'2022-03-24','2022-03-24'),  
	(9,3,80,0,'2022-03-27','2022-03-27'),  
	(10,3,53,2,'2022-03-31','2022-03-31'),  
	(11,3,79,1,'2022-04-01','2022-04-01'),  
	(12,3,65,0,'2022-04-01','2022-04-01'),  
	(13,3,95,0,'2022-04-02','2022-04-02'),  
	(14,3,60,0,'2022-04-02','2022-04-02'),  
	(15,3,81,4,'2022-04-03','2022-04-03'),  
	(16,3,5,0,'2022-04-03','2022-04-03'),  
	(17,3,75,0,'2022-04-03','2022-04-03'),  
	(18,3,5,0,'2022-04-04','2022-04-04'),  
	(19,3,20,0,'2022-04-04','2022-04-04'),  
	(20,3,20,0,'2022-04-05','2022-04-05'),  
	(21,3,15,0,'2022-04-07','2022-04-07'),  
	(22,3,39,1,'2022-04-08','2022-04-08'),  
	(23,3,97,3,'2022-04-09','2022-04-09'),  
	(24,3,15,0,'2022-04-09','2022-04-09'),  
	(25,3,48,2,'2022-04-09','2022-04-09'),  
	(26,3,24,1,'2022-04-15','2022-04-15'),  
	(27,3,70,0,'2022-04-15','2022-04-15'),  
	(28,3,10,0,'2022-04-15','2022-04-15'),  
	(29,3,72,3,'2022-04-17','2022-04-17'),  
	(30,3,48,2,'2022-04-19','2022-04-19'),  
	(31,3,53,2,'2022-04-21','2022-04-21'),  
	(32,3,48,2,'2022-04-23','2022-04-23'),  
	(33,3,33,2,'2022-04-23','2022-04-23'),  
	(34,3,15,0,'2022-04-25','2022-04-25'),  
	(35,3,76,4,'2022-04-26','2022-04-26'),  
	(36,3,10,0,'2022-04-27','2022-04-27'),  
	(37,3,48,2,'2022-04-29','2022-04-29'),  
	(38,3,30,0,'2022-04-29','2022-04-29'),  
	(39,3,90,0,'2022-04-29','2022-04-29'),  
	(40,3,10,0,'2022-04-30','2022-04-30'), 
	(1,4,5,0,'2022-03-19','2022-03-19'),  
	(2,4,88,0,'2022-03-20','2022-03-20'),  
	(3,4,23,2,'2022-03-21','2022-03-21'),  
	(4,4,37,1,'2022-03-22','2022-03-22'),  
	(5,4,97,0,'2022-03-24','2022-03-24'),  
	(6,4,91,2,'2022-03-24','2022-03-24'),  
	(7,4,35,0,'2022-03-27','2022-03-27'),  
	(8,4,28,2,'2022-03-29','2022-03-29'),  
	(9,4,79,1,'2022-04-01','2022-04-01'),  
	(10,4,51,2,'2022-04-05','2022-04-05'),  
	(11,4,79,0,'2022-04-06','2022-04-06'),  
	(12,4,63,2,'2022-04-06','2022-04-06'),  
	(13,4,93,2,'2022-04-07','2022-04-07'),  
	(14,4,60,0,'2022-04-07','2022-04-07'),  
	(15,4,81,0,'2022-04-08','2022-04-08'),  
	(16,4,5,0,'2022-04-08','2022-04-08'),  
	(17,4,75,0,'2022-04-08','2022-04-08'),  
	(18,4,5,0,'2022-04-09','2022-04-09'),  
	(19,4,18,2,'2022-04-09','2022-04-09'),  
	(20,4,20,0,'2022-04-10','2022-04-10'),  
	(21,4,13,2,'2022-04-12','2022-04-12'),  
	(22,4,39,0,'2022-04-13','2022-04-13'),  
	(23,4,95,2,'2022-04-14','2022-04-14'),  
	(24,4,13,2,'2022-04-14','2022-04-14'),  
	(25,4,46,2,'2022-04-14','2022-04-14'),  
	(26,4,24,0,'2022-04-20','2022-04-20'),  
	(27,4,68,2,'2022-04-20','2022-04-20'),  
	(28,4,10,0,'2022-04-20','2022-04-20'),  
	(29,4,71,1,'2022-04-22','2022-04-22'),  
	(30,4,47,1,'2022-04-24','2022-04-24'),  
	(31,4,51,2,'2022-04-26','2022-04-26'),  
	(32,4,47,1,'2022-04-28','2022-04-28'),  
	(33,4,32,1,'2022-04-28','2022-04-28'),  
	(34,4,13,2,'2022-04-30','2022-04-30'),  
	(35,4,75,1,'2022-05-01','2022-05-01'),  
	(36,4,10,0,'2022-05-02','2022-05-02'),  
	(1,5,5,0,'2022-03-24','2022-03-24'),  
	(2,5,88,0,'2022-03-25','2022-03-25'),  
	(3,5,22,1,'2022-03-26','2022-03-26'),  
	(4,5,35,2,'2022-03-27','2022-03-27'),  
	(5,5,97,0,'2022-03-29','2022-03-29'),  
	(6,5,90,1,'2022-03-29','2022-03-29'),  
	(7,5,33,2,'2022-04-01','2022-04-01'),  
	(8,5,27,1,'2022-04-03','2022-04-03'),  
	(9,5,78,1,'2022-04-06','2022-04-06'),  
	(10,5,50,1,'2022-04-10','2022-04-10'),  
	(11,5,77,2,'2022-04-11','2022-04-11'),  
	(12,5,62,1,'2022-04-11','2022-04-11'),  
	(13,5,91,2,'2022-04-12','2022-04-12'),  
	(14,5,58,2,'2022-04-12','2022-04-12'),  
	(15,5,81,0,'2022-04-13','2022-04-13'),  
	(16,5,5,0,'2022-04-13','2022-04-13'),  
	(17,5,74,1,'2022-04-13','2022-04-13'),  
	(18,5,5,0,'2022-04-14','2022-04-14'),  
	(19,5,16,2,'2022-04-14','2022-04-14'),  
	(20,5,19,1,'2022-04-15','2022-04-15'),  
	(21,5,12,1,'2022-04-17','2022-04-17'),  
	(22,5,39,0,'2022-04-18','2022-04-18'),  
	(23,5,93,2,'2022-04-19','2022-04-19'),  
	(24,5,12,1,'2022-04-19','2022-04-19'),  
	(25,5,44,2,'2022-04-19','2022-04-19'),  
	(26,5,24,0,'2022-04-25','2022-04-25'),  
	(27,5,66,2,'2022-04-25','2022-04-25'),  
	(28,5,10,0,'2022-04-25','2022-04-25'),  
	(29,5,69,2,'2022-04-27','2022-04-27'),  
	(30,5,45,2,'2022-04-29','2022-04-29'),  
	(31,5,50,1,'2022-05-01','2022-05-01'),  
	(1,6,5,0,'2022-03-29','2022-03-29'),  
	(2,6,87,1,'2022-03-30','2022-03-30'),  
	(3,6,22,0,'2022-03-31','2022-03-31'),  
	(4,6,35,0,'2022-04-01','2022-04-01'),  
	(5,6,95,2,'2022-04-03','2022-04-03'),  
	(6,6,89,1,'2022-04-03','2022-04-03'),  
	(7,6,33,0,'2022-04-06','2022-04-06'),  
	(8,6,27,0,'2022-04-08','2022-04-08'),  
	(9,6,77,1,'2022-04-11','2022-04-11'),  
	(10,6,49,1,'2022-04-15','2022-04-15'),  
	(11,6,77,0,'2022-04-16','2022-04-16'),  
	(12,6,60,2,'2022-04-16','2022-04-16'),  
	(13,6,89,2,'2022-04-17','2022-04-17'),  
	(14,6,58,0,'2022-04-17','2022-04-17'),  
	(15,6,81,0,'2022-04-18','2022-04-18'),  
	(16,6,5,0,'2022-04-18','2022-04-18'),  
	(17,6,74,0,'2022-04-18','2022-04-18'),  
	(18,6,4,1,'2022-04-19','2022-04-19'),  
	(19,6,15,1,'2022-04-19','2022-04-19'),  
	(20,6,19,0,'2022-04-20','2022-04-20'),  
	(21,6,11,1,'2022-04-22','2022-04-22'),  
	(22,6,37,2,'2022-04-23','2022-04-23'),  
	(23,6,93,0,'2022-04-24','2022-04-24'),  
	(24,6,11,1,'2022-04-24','2022-04-24'),  
	(25,6,44,0,'2022-04-24','2022-04-24'),  
	(26,6,24,0,'2022-04-30','2022-04-30'),  
	(27,6,66,0,'2022-04-30','2022-04-30'),  
	(28,6,10,0,'2022-04-30','2022-04-30'),  
	(1,7,5,0,'2022-04-03','2022-04-03'),  
	(2,7,85,2,'2022-04-04','2022-04-04'),  
	(3,7,22,0,'2022-04-05','2022-04-05'),  
	(4,7,34,1,'2022-04-06','2022-04-06'),  
	(5,7,93,2,'2022-04-08','2022-04-08'),  
	(6,7,89,0,'2022-04-08','2022-04-08'),  
	(7,7,31,2,'2022-04-11','2022-04-11'),  
	(8,7,26,1,'2022-04-13','2022-04-13'),  
	(9,7,77,0,'2022-04-16','2022-04-16'),  
	(10,7,47,2,'2022-04-20','2022-04-20'),  
	(11,7,77,0,'2022-04-21','2022-04-21'),  
	(12,7,60,0,'2022-04-21','2022-04-21'),  
	(13,7,87,2,'2022-04-22','2022-04-22'),  
	(14,7,56,2,'2022-04-22','2022-04-22'),  
	(15,7,80,1,'2022-04-23','2022-04-23'),  
	(16,7,4,1,'2022-04-23','2022-04-23'),  
	(17,7,74,0,'2022-04-23','2022-04-23'),  
	(18,7,4,0,'2022-04-24','2022-04-24'),  
	(19,7,15,0,'2022-04-24','2022-04-24'),  
	(20,7,19,0,'2022-04-25','2022-04-25'),  
	(21,7,10,1,'2022-04-27','2022-04-27'),  
	(22,7,36,1,'2022-04-28','2022-04-28'),  
	(23,7,91,2,'2022-04-29','2022-04-29'),  
	(24,7,9,2,'2022-04-29','2022-04-29'),  
	(25,7,43,1,'2022-04-29','2022-04-29'), 
	(1,8,5,0,'2022-04-08','2022-04-08'),  
	(2,8,85,0,'2022-04-09','2022-04-09'),  
	(3,8,22,0,'2022-04-10','2022-04-10'),  
	(4,8,34,0,'2022-04-11','2022-04-11'),  
	(5,8,92,1,'2022-04-13','2022-04-13'),  
	(6,8,88,1,'2022-04-13','2022-04-13'),  
	(7,8,31,0,'2022-04-16','2022-04-16'),  
	(8,8,24,2,'2022-04-18','2022-04-18'),  
	(9,8,77,0,'2022-04-21','2022-04-21'),  
	(10,8,45,2,'2022-04-25','2022-04-25'),  
	(11,8,75,2,'2022-04-26','2022-04-26'),  
	(12,8,59,1,'2022-04-26','2022-04-26'),  
	(13,8,86,1,'2022-04-27','2022-04-27'),  
	(14,8,54,2,'2022-04-27','2022-04-27'),  
	(15,8,80,0,'2022-04-28','2022-04-28'),  
	(16,8,3,1,'2022-04-28','2022-04-28'),  
	(17,8,73,1,'2022-04-28','2022-04-28'),  
	(18,8,4,0,'2022-04-29','2022-04-29'),  
	(19,8,15,0,'2022-04-29','2022-04-29'),  
	(20,8,18,1,'2022-04-30','2022-04-30'),  
	(1,9,5,0,'2022-04-13','2022-04-13'),  
	(2,9,85,0,'2022-04-14','2022-04-14'),  
	(3,9,22,0,'2022-04-15','2022-04-15'),  
	(4,9,34,0,'2022-04-16','2022-04-16'),  
	(5,9,92,0,'2022-04-18','2022-04-18'),  
	(6,9,88,0,'2022-04-18','2022-04-18'),  
	(7,9,31,0,'2022-04-21','2022-04-21'),  
	(8,9,24,0,'2022-04-23','2022-04-23'),  
	(9,9,77,0,'2022-04-26','2022-04-26'),  
	(10,9,45,0,'2022-04-30','2022-04-30'),  
	(11,9,75,0,'2022-05-01','2022-05-01'),  
	(12,9,59,0,'2022-05-01','2022-05-01');
	
-- 受注データ --
INSERT INTO orders (order_number, customer_history_id, employee_history_id) VALUES
	('00000001',19,12), 
	('00000002',10,12), 
	('00000003',17,10), 
	('00000004',2,12), 
	('00000005',30,10), 
	('00000006',16,11), 
	('00000007',16,10), 
	('00000008',14,12), 
	('00000009',27,10), 
	('00000010',22,11), 
	('00000011',14,11), 
	('00000012',17,11), 
	('00000013',8,10), 
	('00000014',4,12), 
	('00000015',24,12);

-- 受注明細データ --
INSERT INTO orders_details (order_number, detail_id, item_history_id, order_quantity) VALUES
	('00000001',1,41,18), 
	('00000001',2,19,13), 
	('00000001',3,20,20), 
	('00000001',4,22,13), 
	('00000002',1,2,27), 
	('00000002',2,6,25), 
	('00000003',1,2,24), 
	('00000003',2,49,1), 
	('00000003',3,10,20), 
	('00000004',1,10,5), 
	('00000004',2,18,5), 
	('00000004',3,22,12), 
	('00000004',4,1,9), 
	('00000005',1,1,14), 
	('00000006',1,49,28), 
	('00000006',2,21,13), 
	('00000006',3,38,3), 
	('00000006',4,48,26), 
	('00000006',5,19,5), 
	('00000006',6,3,17), 
	('00000006',7,5,28), 
	('00000007',1,19,25), 
	('00000007',2,43,4), 
	('00000007',3,17,7), 
	('00000007',4,27,24), 
	('00000008',1,20,7), 
	('00000008',2,26,29), 
	('00000009',1,33,14), 
	('00000009',2,6,27), 
	('00000009',3,18,29), 
	('00000010',1,4,2), 
	('00000010',2,41,29), 
	('00000010',3,38,16), 
	('00000010',4,37,19), 
	('00000011',1,32,12), 
	('00000011',2,20,14), 
	('00000011',3,37,7), 
	('00000011',4,37,28), 
	('00000012',1,21,25), 
	('00000012',2,14,19), 
	('00000013',1,4,6), 
	('00000014',1,13,20), 
	('00000015',1,43,20), 
	('00000015',2,5,18), 
	('00000015',3,4,7), 
	('00000015',4,13,13), 
	('00000015',5,27,4);

-- 権限付与 --
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO management;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO management;



