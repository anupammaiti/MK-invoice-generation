-- we don't know how to generate schema invoices (class Schema) :(
create table banks
(
	bank_id bigint auto_increment
		primary key,
	bank_name varchar(70) null
)
;

create table clients
(
	client_id bigint auto_increment
		primary key,
	client_name varchar(50) not null
)
;

create table company_location
(
	location_id bigint auto_increment
		primary key,
	country varchar(50) not null
)
;

create table client_companies_info
(
	company_id bigint auto_increment
		primary key,
	company_name varchar(50) null,
	vat_number varchar(20) null,
	address varchar(100) null,
	city varchar(50) null,
	postcode varchar(20) not null,
	client_id bigint not null,
	location_id bigint null,
	constraint client_companies_info_vat_number_uindex
		unique (vat_number),
	constraint client_companies_info_ibfk_1
		foreign key (location_id) references company_location (location_id)
)
;

create index location_id
	on client_companies_info (location_id)
;

create table currencies
(
	currency_id bigint auto_increment
		primary key,
	currency_code varchar(3) null,
	constraint currency_code
		unique (currency_code)
)
;

create table currency_rates
(
	rate_id bigint auto_increment
		primary key,
	exchange_rate float not null,
	from_currency bigint not null,
	to_currency bigint not null,
	constraint currency_rates_currencies_currency_id_fk
		foreign key (from_currency) references currencies (currency_id),
	constraint currency_rates_currencies_currency_id_fk_2
		foreign key (to_currency) references currencies (currency_id)
)
;

create table custody_charges
(
	custody_charge_id bigint auto_increment,
	custody_charge_vat float null,
	custody_charge_incl_vat float null,
	custody_charge_excl_vat float null,
	constraint custody_charges_id_uindex
		unique (custody_charge_id)
)
;

alter table custody_charges
	add primary key (custody_charge_id)
;

create table invoice_status
(
	status_id bigint auto_increment
		primary key,
	paid varchar(3) not null,
	sent varchar(3) not null
)
;

create table mk_bank_accounts
(
	account_id bigint auto_increment
		primary key,
	account_name varchar(75) not null,
	swift_code varchar(11) not null,
	euro_acc_num varchar(30) null,
	euro_iban varchar(40) null,
	usd_acc_num varchar(30) null,
	usd_iban varchar(40) null,
	bank_id bigint not null,
	constraint euro_acc_num
		unique (euro_acc_num),
	constraint euro_iban
		unique (euro_iban),
	constraint swift_code
		unique (swift_code),
	constraint usd_acc_num
		unique (usd_acc_num),
	constraint usd_iban
		unique (usd_iban),
	constraint mk_bank_accounts_ibfk_1
		foreign key (bank_id) references banks (bank_id)
)
;

create index bank_id
	on mk_bank_accounts (bank_id)
;

create table portfolios
(
	portfolio_id bigint auto_increment
		primary key,
	portfolio_code varchar(20) not null,
	client_id bigint not null,
	company_id bigint null,
	constraint portfolio_code
		unique (portfolio_code),
	constraint portfolios_client_companies_info_company_id_fk
		foreign key (company_id) references client_companies_info (company_id),
	constraint portfolios_ibfk_1
		foreign key (client_id) references clients (client_id)
)
;

create index client_id
	on portfolios (client_id)
;

create table services
(
	service_id bigint auto_increment
		primary key,
	service_name varchar(50) not null,
	constraint service_name
		unique (service_name)
)
;

create table vat
(
	vat_id bigint auto_increment
		primary key,
	vat_applicable varchar(3) not null,
	vat_rate float null,
	constraint vat_rate
		unique (vat_rate)
)
;

create table invoices
(
	id bigint auto_increment
		primary key,
	invoice_type varchar(10) null,
	invoice_number varchar(20) not null,
	frequency varchar(25) null,
	period varchar(25) null,
	year_issued int(4) null,
	date_issued date null,
	reverse_charge varchar(3) null,
	vat_exempt varchar(3) null,
	bank_acc_id bigint null,
	vat_id bigint null,
	portfolio_id bigint null,
	currency_rate_id bigint null,
	custody_charge_id bigint null,
	service_id bigint null,
	status_id bigint null,
	constraint invoice_number
		unique (invoice_number),
	constraint invoices_ibfk_1
		foreign key (service_id) references services (service_id),
	constraint invoices_ibfk_2
		foreign key (bank_acc_id) references mk_bank_accounts (account_id),
	constraint invoices_ibfk_5
		foreign key (vat_id) references vat (vat_id),
	constraint invoices_ibfk_6
		foreign key (portfolio_id) references portfolios (portfolio_id),
	constraint invoices_ibfk_7
		foreign key (currency_rate_id) references currency_rates (rate_id),
	constraint invoices_ibfk_8
		foreign key (custody_charge_id) references custody_charges (custody_charge_id),
	constraint invoices_ibfk_9
		foreign key (status_id) references invoice_status (status_id)
)
;

create index bank_acc_id
	on invoices (bank_acc_id)
;

create index portfolio_id
	on invoices (portfolio_id)
;

create index service_id
	on invoices (service_id)
;

create index vat_id
	on invoices (vat_id)
;

