CREATE TABLE vets (
	id serial,
	first_name character varying(30),
	last_name character varying(30),
	CONSTRAINT pk_vets PRIMARY KEY (id)
);
CREATE INDEX idx_vets_last_name ON vets (last_name);

CREATE TABLE specialties (
	id serial,
	name character varying(80),
	CONSTRAINT pk_specialties PRIMARY KEY (id)
);
CREATE INDEX idx_specialties_name ON specialties (name);

CREATE TABLE vet_specialties (
	vet_id integer NOT NULL,
	specialty_id integer NOT NULL,
	FOREIGN KEY (vet_id) REFERENCES vets (id), 
	FOREIGN KEY (specialty_id) REFERENCES specialties (id), 
	CONSTRAINT unique_ids UNIQUE (vet_id,specialty_id)
);

CREATE TABLE types (
	id serial,
	name character varying(80),
	CONSTRAINT pk_types PRIMARY KEY (id)
);
CREATE INDEX idx_types_name ON types (name);

CREATE TABLE owners (
	id serial,
	first_name character varying(30),
	last_name character varying(30),
	address character varying(255),
	city character varying(80),
	telephone character varying(20),
	CONSTRAINT pk_owners PRIMARY KEY (id)
);
CREATE INDEX idx_owners_last_name ON owners (last_name);

CREATE TABLE pets (
	id serial,
	name character varying(30),
	birth_date date,
	type_id integer NOT NULL,
	owner_id integer NOT NULL,
	FOREIGN KEY (type_id) REFERENCES types (id), 
	FOREIGN KEY (owner_id) REFERENCES owners (id),
	CONSTRAINT pk_pets PRIMARY KEY (id)
);
CREATE INDEX idx_pets_name ON pets (name);

CREATE TABLE visits (
	id serial,
	pet_id integer NOT NULL,
	visit_date date,
	description character varying(255),
	FOREIGN KEY (pet_id) REFERENCES pets (id),
	CONSTRAINT pk_visits PRIMARY KEY (id)
);