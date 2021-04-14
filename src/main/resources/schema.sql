CREATE TABLE IF NOT EXISTS t_lookup_value (
    lv_id SERIAL PRIMARY KEY,
    lv_entity_type VARCHAR(64) NOT NULL,
    lv_entity_id NUMBER,
    lv_value_type VARCHAR(64) NOT NULL,
    lv_value VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS t_author (
    au_id SERIAL PRIMARY KEY,
    au_guid UUID NOT NULL,
    au_name VARCHAR(64) NOT NULL,
    au_created_by VARCHAR(64),
    au_created_on TIMESTAMP,
    au_modified_by VARCHAR(64),
    au_modified_on TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_document (
    do_id SERIAL PRIMARY KEY,
    do_guid UUID NOT NULL,
    do_type VARCHAR(64) NOT NULL,
    do_name VARCHAR(64) NOT NULL,
    do_created_by VARCHAR(64),
    do_created_on TIMESTAMP,
    do_modified_by VARCHAR(64),
    do_modified_on TIMESTAMP
);

CREATE TABLE IF NOT EXISTS t_section (
    se_id SERIAL PRIMARY KEY,
    se_guid UUID NOT NULL,
    se_type VARCHAR(64) NOT NULL,
    se_name VARCHAR(64) NOT NULL,
    se_created_by VARCHAR(64),
    se_created_on TIMESTAMP,
    se_modified_by VARCHAR(64),
    se_modified_on TIMESTAMP
);
