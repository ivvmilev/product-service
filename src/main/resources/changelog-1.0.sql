CREATE TABLE eProducts
(
    id integer NOT NULL PRIMARY KEY,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    category character varying(200) COLLATE pg_catalog."default",
    description character varying(500) COLLATE pg_catalog."default",
    quantity integer,
    created_date date NOT NULL,
    last_modified_date date
)