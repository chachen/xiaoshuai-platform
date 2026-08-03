CREATE TABLE IF NOT EXISTS xs_operation_log (
    id BIGINT NOT NULL PRIMARY KEY,
    title VARCHAR(100),
    path VARCHAR(255),
    http_method VARCHAR(20),
    user_id BIGINT,
    username VARCHAR(50),
    ip VARCHAR(64),
    trace_id VARCHAR(64),
    duration_ms BIGINT,
    success SMALLINT NOT NULL DEFAULT 1,
    error_message VARCHAR(500),
    request_params TEXT,
    create_time TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS xs_login_log (
    id BIGINT NOT NULL PRIMARY KEY,
    username VARCHAR(50),
    status VARCHAR(2),
    message VARCHAR(200),
    ip VARCHAR(64),
    user_agent VARCHAR(500),
    trace_id VARCHAR(64),
    create_time TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS xs_sys_dict_type (
    id BIGINT NOT NULL PRIMARY KEY,
    dict_type VARCHAR(100) NOT NULL UNIQUE,
    dict_name VARCHAR(100) NOT NULL,
    status SMALLINT NOT NULL DEFAULT 1,
    create_time TIMESTAMP NULL,
    update_time TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS xs_sys_dict_data (
    id BIGINT NOT NULL PRIMARY KEY,
    dict_type VARCHAR(100) NOT NULL,
    dict_label VARCHAR(100) NOT NULL,
    dict_value VARCHAR(100) NOT NULL,
    sort INTEGER NOT NULL DEFAULT 0,
    status SMALLINT NOT NULL DEFAULT 1,
    create_time TIMESTAMP NULL,
    update_time TIMESTAMP NULL
);
