-- maria & mysql: User & authority


create table user
(
    user_id  bigint auto_increment
        primary key comment '유저 아이디',
    email    varchar(255) null comment '이메일',
    name     varchar(255) null comment '이름',
    login_id varchar(255) null comment '로그인 아이디',
    password varchar(255) null comment '비밀번호'
)
    comment '사용자';

insert into user (email, name, login_id, password) values ('test@test.com', 'test', 'test1234', '1234');


-- authority
create table authority
(
    id   bigint auto_increment comment '권한 아이디',
    auth_code varchar(255) null comment '권한 코드',
    user_key bigint      null comment '사용자 KEY',

        constraint authority_constraints_pk
        primary key (id),
        foreign key (user_key) references user (user_id)
)
    comment '사용자별 권한';

insert into authority (auth_code, user_key) values ('ROLE_ROOT', 1);
insert into authority (auth_code, user_key) values ('ROLE_ADMIN', 1);
insert into authority (auth_code, user_key) values ('ROLE_USER', 1);