CREATE TABLE
    users (
        id bigint NOT NULL,
        name varchar(50) NOT NULL,
        password varchar(255) NOT NULL,
        revenue decimal(12, 2) DEFAULT 0,
        deposit decimal(12, 2) DEFAULT 0,
        withdraw decimal(12, 2) DEFAULT 0,
        debt decimal(12, 2) DEFAULT 0,
        flag boolean DEFAULT false,
        created_at timestamp DEFAULT NOW(),
        updated_at timestamp DEFAULT NOW(),
        is_deleted bit(1) NOT NULL DEFAULT 0,
        PRIMARY KEY (id)
    );

CREATE TABLE
    transactions (
        id bigint auto_increment,
        user_id bigint NOT NULL,
        total_price decimal(12, 2) NULL DEFAULT 0,
        deposit decimal(12, 2) NULL DEFAULT 0,
        withdraw decimal(12, 2) NULL DEFAULT 0,
        balance decimal(12, 2) NULL DEFAULT 0,
        flag boolean NOT NULL DEFAULT false,
        created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        PRIMARY KEY (id),
        FOREIGN KEY (user_id) REFERENCES users(id)
    );

CREATE TABLE
    products (
        id bigint auto_increment,
        name varchar(255) NOT NULL,
        image varchar(500) NOT NULL,
        description varchar(500) NOT NULL,
        price decimal(12, 2) NOT NULL,
        stock integer NULL,
        created_by bigint NOT NULL,
        created_at timestamp DEFAULT NOW(),
        updated_at timestamp DEFAULT NOW(),
        is_deleted bit(1) NOT NULL DEFAULT 0,
        PRIMARY KEY (id),
        FOREIGN KEY (created_by) REFERENCES users(id)
    );

CREATE TABLE
    carts (
        id bigint auto_increment,
        user_id bigint NOT NULL,
        product_id bigint NOT NULL,
        qty int NOT NULL,
        price decimal(12, 2) NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE,
        PRIMARY KEY (id)
    );

CREATE TABLE
    product_transaction (
        id bigint auto_increment,
        product_id bigint NOT NULL,
        transaction_id bigint NOT NULL,
        qty int NOT NULL,
        price decimal(12, 2) NOT NULL,
        FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE ON UPDATE CASCADE,
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE,
        PRIMARY KEY (id)
    );