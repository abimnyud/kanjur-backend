CREATE TABLE users (
  student_id    bigint NOT NULL,
  name          varchar(50) NOT NULL,
  password      varchar(255) NOT NULL,
  revenue       decimal(12, 2) DEFAULT 0,
  deposit       decimal(12, 2) DEFAULT 0,
  withdraw      decimal(12, 2) DEFAULT 0,
  debt          decimal(12, 2) DEFAULT 0,
  flag          boolean DEFAULT false,
  created_at    timestamp DEFAULT NOW(),
  updated_at    timestamp DEFAULT NOW(),
  deleted_at    timestamp NULL,
  PRIMARY KEY (student_id)
);

CREATE TABLE transactions (
    id            bigint NOT NULL,
    student_id    bigint NOT NULL,
    total_price   decimal(12, 2) NULL DEFAULT 0,
    deposit       decimal(12, 2) NULL DEFAULT 0,
    withdraw      decimal(12, 2) NULL DEFAULT 0,
    balance       decimal(12, 2) NULL DEFAULT 0,
    flag          boolean NOT NULL DEFAULT false,
    created_at    timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES users(student_id)
);

CREATE TABLE products (
    id                  bigint NOT NULL,
    name                varchar(255) NOT NULL,
    image               varchar(500) NOT NULL,
    description         varchar(500) NOT NULL,
    price               decimal(12, 2) NOT NULL,
    seller_id           bigint NOT NULL,
    transaction_id      bigint NULL,
    created_at          timestamp DEFAULT NOW(),
    updated_at          timestamp DEFAULT NOW(),
    deleted_at          timestamp NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (seller_id) REFERENCES users(student_id),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);

CREATE TABLE cart (
  user_id bigint NOT NULL,
  product_id bigint NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(student_id) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE ON UPDATE CASCADE,
  PRIMARY KEY (user_id,product_id)
);