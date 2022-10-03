create sequence hibernate_sequence start 1 increment 1;

create table article (
    id_article  bigserial not null,
    description varchar(255),
    image_url varchar(255),
    title varchar(255),
    url varchar(255),
    category_id int8,
    primary key (id_article)
);

create table category (
    id  bigserial not null,
    name varchar(255),
    primary key (id)
);

create table gender (
    id  bigserial not null,
    gender_name varchar(20) not null,
    primary key (id)
);

create table likes_stuff (
    user_id int8 not null,
    stuff_id int8 not null,
    primary key (user_id, stuff_id)
);

create table password_reset_token (
    id int8 not null,
    expiry_date timestamp,
    token varchar(255),
    user_id int8 not null,
    primary key (id)
);

create table quotes (
    id  bigserial not null,
    author varchar(255),
    value varchar(255),
    primary key (id)
);

create table roles (
    id  bigserial not null,
    name varchar(20),
    primary key (id)
);

create table shopsStuff (
    id int8 not null,
    article_type varchar(50) not null,
    base_color varchar(50) not null,
    image_url varchar(255) not null,
    master_category varchar(255) not null,
    product_display_name varchar(255) not null,
    season varchar(20) not null,
    sub_category varchar(50) not null,
    usage varchar(20) not null,
    gender_id int8 not null,
    primary key (id)
);

create table user_roles (
    user_id int8 not null,
    role_id int8 not null,
    primary key (user_id, role_id)
);

create table users (
    id  bigserial not null,
    code_number1 varchar(4),
    code_number2 varchar(4),
    country varchar(120),
    email varchar(255) not null,
    full_number varchar(15),
    number varchar(7),
    password varchar(255) not null,
    photo_url varchar(255),
    username varchar(35) not null,
    gender_id int8,
    primary key (id)
);

create table wardrobe (
    user_id int8 not null,
    stuff_id int8 not null,
    primary key (user_id, stuff_id)
);

alter table gender add constraint UK5frpwtblh7i5h9l59lvqqfch0 unique (gender_name);

alter table users add constraint UKr43af9ap4edm43mmtq01oddj6 unique (username);

alter table users add constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email);

alter table article add constraint FKy5kkohbk00g0w88fi05k2hcw foreign key (category_id) references category;

alter table likes_stuff add constraint FKjb61wqqg38fdhuksv8rerdta8 foreign key (stuff_id) references shopsStuff;

alter table likes_stuff add constraint FKeck8xmlt6tg7gyn0eoacu93s8 foreign key (user_id) references users;

alter table password_reset_token add constraint FK83nsrttkwkb6ym0anu051mtxn foreign key (user_id) references users;

alter table shopsStuff add constraint FK3flocpy21qm9orl3d7fbaywqs foreign key (gender_id) references gender;

alter table user_roles add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles;

alter table user_roles add constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users;

alter table users add constraint FKo0h29eo9e4y4wvd5yh0sn03rp foreign key (gender_id) references gender;

alter table wardrobe add constraint FKog09vtv2yxg2jyisxhlai4rub foreign key (stuff_id) references shopsStuff;

alter table wardrobe add constraint FKqia3hkywicy3yrcq223fyfmif foreign key (user_id) references users;