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
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	completion_flag SMALLINT DEFAULT 0, 
	invalid SMALLINT DEFAULT 0, 
	PRIMARY KEY (id)
);

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

-- 在庫テーブル --
CREATE TABLE IF NOT EXISTS stocks (
	id BIGSERIAL NOT NULL, 
	item_id BIGINT NOT NULL, 
	actual_quantity INT DEFAULT 0, 
	created_at TIMESTAMP NOT NULL, 
	updated_at TIMESTAMP NOT NULL, 
	PRIMARY KEY (id), 
	CONSTRAINT quantity_check CHECK(actual_quantity >= 0)
);

-- 入庫テーブル --
CREATE TABLE IF NOT EXISTS storings (
	id BIGSERIAL NOT NULL, 
	production_id BIGINT NOT NULL, 
	storing_quantity INT NOT NULL, 
	completion_input SMALLINT DEFAULT 0, 
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
	PRIMARY KEY (id)
);

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
	('0001', '$2a$10$FTvFiXdJvVsngfi5gdwjZ.NRo9vz.FZ61LcDHUOogQddwJWwgGdJK', 'admin', 1, 'ROLE_ADMIN'),
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
   	
-- 権限付与 --
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO management;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO management;



