select * from information_schema.tables where table_schema='demo';

drop table if exists valueset;
create table valueset (
	id serial primary key,
	vs_category character varying(20),
	vs_code character varying(64),
	vs_display character varying(512)
);

drop table if exists city;
create table city (
	id serial primary key,
	state_code character varying(20),
	state_name character varying(64),
	city_name character varying(64),
	zip_code_list character varying(256)
);

drop table if exists helix_provider;
create table helix_provider (
	id serial primary key,
	full_url character varying(512) not null,
	addr_city character varying(128),
	addr_line character varying(512),
	addr_postal_code character varying(20),
	addr_state character varying(10),
	name character varying(128),
	status character varying(20),
	position_latitude numeric(10,10),
	position_longitude numeric(10,10),
	resource_identifier character varying(256) not null,
	created_by character varying(20),
	created_dt timestamp,
	updated_by character varying(20),
	updated_dt timestamp
);

drop table if exists provider_hours_of_opn;
create table provider_hours_of_opn (
	id serial primary key,
	full_url character varying(512) not null,
	all_day character varying(10),
	opening_time character varying(20),
	closing_time character varying(20),
	daysOfWeek character varying(128),
	created_by character varying(20),
	created_dt timestamp,
	updated_by character varying(20),
	updated_dt timestamp
);

drop table if exists provider_telecom;
create table provider_telecom (
	id serial primary key,
	full_url character varying(512) not null,
	comm_system character varying(10),
	comm_use character varying(20),
	comm_value character varying(128),
	created_by character varying(20),
	created_dt timestamp,
	updated_by character varying(20),
	updated_dt timestamp
);
