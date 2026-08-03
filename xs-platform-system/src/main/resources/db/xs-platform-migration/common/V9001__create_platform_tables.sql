CREATE TABLE IF NOT EXISTS xs_sys_user
(
    id BIGINT NOT NULL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(200) NOT NULL,
    nickname VARCHAR(100),
    status SMALLINT NOT NULL DEFAULT 1,
    locked SMALLINT NOT NULL DEFAULT 0,
    create_time TIMESTAMP NULL,
    update_time TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS xs_sys_role
(
    id BIGINT NOT NULL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    status SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS xs_sys_permission
(
    id BIGINT NOT NULL PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    status SMALLINT NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS xs_sys_user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE IF NOT EXISTS xs_sys_role_permission
(
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE IF NOT EXISTS xs_sys_menu
(
    id BIGINT NOT NULL PRIMARY KEY,
    parent_id BIGINT DEFAULT 0,
    name VARCHAR(100) NOT NULL,
    path VARCHAR(200),
    permission VARCHAR(100),
    status SMALLINT NOT NULL DEFAULT 1,
    sort INTEGER NOT NULL DEFAULT 0
);
