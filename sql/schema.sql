create table `categories`
(
    `id`   bigint auto_increment,
    `name` varchar(50) not null,
    primary key (`id`)
);



create table `characteristics`
(
    `id`             bigint auto_increment,
    `category_id`    bigint not null,
    `characteristic` text   not null,
    primary key (`id`),
    foreign key (`category_id`) references `categories` (`id`)
);

create table `products`
(
    `id`          bigint auto_increment,
    `category_id` bigint      not null,
    `name`        varchar(50) not null,
    `description` text        not null,
    `price`       int         not null,
    primary key (`id`),
    foreign key (`category_id`) references `categories` (`id`)
);

create table `values`
(

    `id`                bigint auto_increment,
    `product_id`        bigint       not null,
    `characteristic_id` bigint       not null,
    `value`             varchar(255) not null,
    primary key (`id`),
    foreign key (`product_id`) references `products` (`id`),
    foreign key (`characteristic_id`) references `characteristics` (`id`)

);