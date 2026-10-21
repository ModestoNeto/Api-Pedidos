-- Esquema inicial. Os nomes de tabelas e colunas espelham as entidades JPA
-- (adapter.out.persistence), porque o perfil dev roda com ddl-auto=validate.

CREATE TABLE tb_user (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(255),
    email    VARCHAR(255),
    phone    VARCHAR(255),
    password VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE tb_category (
    id   BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE tb_product (
    id          BIGINT NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255),
    description VARCHAR(255),
    img_url     VARCHAR(255),
    price       DOUBLE,
    PRIMARY KEY (id)
);

CREATE TABLE tb_product_category (
    product_id  BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (category_id, product_id),
    CONSTRAINT fk_product_category_product  FOREIGN KEY (product_id)  REFERENCES tb_product (id),
    CONSTRAINT fk_product_category_category FOREIGN KEY (category_id) REFERENCES tb_category (id)
);

CREATE TABLE tb_order (
    id           BIGINT      NOT NULL AUTO_INCREMENT,
    moment       DATETIME(6),
    order_status INT,
    client_id    BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_client FOREIGN KEY (client_id) REFERENCES tb_user (id)
);

CREATE TABLE tb_order_item (
    order_id   BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT,
    price      DOUBLE,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order_item_order   FOREIGN KEY (order_id)   REFERENCES tb_order (id),
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES tb_product (id)
);

CREATE TABLE tb_payment (
    order_id BIGINT NOT NULL,
    moment   DATETIME(6),
    PRIMARY KEY (order_id),
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES tb_order (id)
);
