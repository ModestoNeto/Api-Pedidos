-- Dados de exemplo do perfil dev. São os mesmos do TestConfig (perfil test),
-- todos fictícios. Os ids são explícitos e as datas estão em UTC.

INSERT INTO tb_user (id, name, email, phone, password) VALUES
    (1, 'Maria Exemplo', 'maria@exemplo.com', '(00) 00000-0001', 'senha-ficticia'),
    (2, 'Alex Exemplo',  'alex@exemplo.com',  '(00) 00000-0002', 'senha-ficticia'),
    (3, 'Bruno Exemplo', 'bruno@exemplo.com', '(00) 00000-0003', 'senha-ficticia');

INSERT INTO tb_category (id, name) VALUES
    (1, 'Eletrônicos'),
    (2, 'Livros'),
    (3, 'Computadores');

INSERT INTO tb_product (id, name, description, price, img_url) VALUES
    (1, 'Livro de Exemplo',    'Descrição de um livro fictício.',    90.5,   ''),
    (2, 'Smart TV de Exemplo', 'Descrição de uma TV fictícia.',      2190.0, ''),
    (3, 'Notebook de Exemplo', 'Descrição de um notebook fictício.', 1250.0, ''),
    (4, 'PC Gamer de Exemplo', 'Descrição de um PC fictício.',       1200.0, '');

INSERT INTO tb_product_category (product_id, category_id) VALUES
    (1, 2),
    (2, 1),
    (3, 3),
    (3, 1),
    (4, 3);

-- order_status: 1 = WAITING_PAYMENT, 2 = PAID, 3 = SHIPPED
INSERT INTO tb_order (id, moment, order_status, client_id) VALUES
    (1, '2026-06-20 19:53:07', 2, 1),
    (2, '2026-07-21 03:42:10', 1, 2),
    (3, '2026-07-22 15:21:22', 3, 1);

INSERT INTO tb_order_item (order_id, product_id, quantity, price) VALUES
    (1, 1, 2, 90.5),
    (1, 3, 1, 1250.0),
    (2, 3, 2, 1250.0),
    (3, 2, 1, 2190.0),
    (3, 4, 1, 1200.0);

INSERT INTO tb_payment (order_id, moment) VALUES
    (1, '2026-06-20 21:53:07'),
    (3, '2026-07-22 16:00:00');
