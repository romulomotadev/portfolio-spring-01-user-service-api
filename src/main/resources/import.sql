-- person
INSERT INTO tb_person (document, type) VALUES ('10.246.571/0001-16', 'LEGAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('28.246.279/0001-37', 'LEGAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('507.332.198-66', 'NATURAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('618.905.774-21', 'NATURAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('729.184.360-08', 'NATURAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('830.517.942-55', 'NATURAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('941.226.703-19', 'NATURAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('052.398.611-77', 'NATURAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('163.904.285-40', 'NATURAL_PERSON');
INSERT INTO tb_person (document, type) VALUES ('274.518.996-02', 'NATURAL_PERSON');

-- user
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Pedro Henrique', 'pedro@gmail.com', '1990-06-01', '123456', 1);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Ana Paula', 'ana.paula@gmail.com', '1988-03-15', '123456', 2);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Mariana Silva', 'mariana.silva@gmail.com', '1995-08-10', '123456', 3);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Rafael Costa', 'rafael.costa@gmail.com', '1987-01-05', '123456', 4);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Juliana Mendes', 'juliana.m@gmail.com', '1993-04-18', '123456', 5);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Lucas Pereira', 'lucas.p@gmail.com', '1998-09-27', '123456', 6);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Fernanda Rocha', 'fernanda.rocha@gmail.com', '1991-12-30', '123456', 7);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Bruno Alves', 'bruno.alves@gmail.com', '1989-07-14', '123456', 8);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Camila Nogueira', 'camila.n@gmail.com', '1996-05-02', '123456', 9);
INSERT INTO tb_user (name, email, birth_date, password, person_id) VALUES ('Diego Martins', 'diego.m@gmail.com', '1985-10-09', '123456', 10);

-- address
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Rua Cananeia', '65', 'Canaã', 'Apto 04', 'Sete Lagoas', '35700303', 1);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Rua das Flores', '120', 'Centro', 'Casa', 'Belo Horizonte', '30140071', 2);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Av. Brasil', '845', 'Funcionários', 'Apto 302', 'Belo Horizonte', '30140002', 3);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Rua Amazonas', '58', 'Centro', 'Sala 01', 'Contagem', '32010000', 4);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Rua Goiás', '910', 'Savassi', 'Apto 1101', 'Belo Horizonte', '30140080', 5);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Rua Bahia', '77', 'Centro', 'Fundos', 'Sete Lagoas', '35700102', 6);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Av. Padre Tarcísio', '340', 'Boa Vista', 'Casa', 'Betim', '32670000', 7);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Rua Rio de Janeiro', '1500', 'Lourdes', 'Apto 901', 'Belo Horizonte', '30160042', 8);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Rua São Paulo', '25', 'Centro', 'Apto 02', 'Divinópolis', '35500004', 9);
INSERT INTO tb_address (road, number, neighborhood, complement, city, zip_code, user_id) VALUES ('Av. Afonso Pena', '4000', 'Mangabeiras', 'Cobertura', 'Belo Horizonte', '30210000', 10);

-- ROLES
INSERT INTO tb_role (authority) VALUES ('ROLE_USER');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');