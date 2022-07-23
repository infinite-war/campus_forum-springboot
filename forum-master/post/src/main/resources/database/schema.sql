# 数据库名为campus_forum

create database campus_forum;

create table user(
    user_id      bigint auto_increment comment '用户ID',
    username     varchar(20)   null comment '用户名',
    password     char(32)      null comment '密码',
    nickname     varchar(20)   null comment '昵称',
    gender       int           null comment '性别',
    college      varchar(10)   null comment '学院',
    birthday     date          null comment '生日',
    phone        varchar(20)   null comment '手机号',
    email        varchar(30)   null comment '邮箱',
    introduction varchar(200)  null comment '简介',
    level        int           null comment '论坛等级',
    points       int           null comment '论坛积分',
    published    int           null comment '累计发帖',
    visits       bigint        null comment '被访问次数',
    likes        int           null comment '被点赞数',
    role         int default 1 null comment '身份',
    constraint user2_user_id_uindex
        unique (user_id)
)comment '用户' charset = utf8;

alter table user
    add primary key (user_id);

create table post(
    post_id      bigint       not null comment '帖子ID',
    category     int          null comment '分区',
    title        varchar(30)  null comment '标题',
    content      blob null comment '内容',
    user_id      bigint       null comment '用户ID',
    views        bigint       null comment '浏览量',
    likes        int          null comment '点赞数',
    create_time  datetime     null comment '发布时间',
    update_time  datetime     null comment '最后回复时间',
    floors       int          null comment '当前楼层数',
    total_floors int          null comment '历史楼层数',
    constraint post_post_id_uindex
        unique (post_id),
    constraint post_user_user_id_fk
        foreign key (user_id) references user (user_id)
)comment '帖子' charset = utf8;

alter table post
    add primary key (post_id);

create table floor(
    floor_id       bigint       not null comment '楼层ID',
    belong_post_id bigint       null comment '所属帖子ID',
    floor_number   int          null comment '楼层编号',
    user_id        bigint       null comment '用户ID',
    likes          int          null comment '点赞数',
    content        varchar(200) null comment '内容',
    create_time    datetime     null comment '发布时间',
    comments       int          null comment '当前评论数',
    total_comments int          null comment '历史评论数',
    constraint floor_floor_id_uindex
        unique (floor_id),
    constraint floor_post_post_id_fk
        foreign key (belong_post_id) references post (post_id),
    constraint floor_user_user_id_fk
        foreign key (user_id) references user (user_id)
)comment '楼层' charset = utf8;

alter table floor
    add primary key (floor_id);

create table comment(
    comment_id      bigint       not null comment '评论ID',
    belong_floor_id bigint       null comment '所属楼层ID',
    comment_number  int          null comment '评论编号',
    user_id         bigint       null comment '用户ID',
    likes           int          null comment '点赞数',
    content         varchar(200) null comment '内容',
    create_time     datetime     null comment '发布时间',
    constraint comment_comment_id_uindex
        unique (comment_id),
    constraint comment_floor_floor_id_fk
        foreign key (belong_floor_id) references floor (floor_id),
    constraint comment_user_user_id_fk
        foreign key (user_id) references user (user_id)
)comment '评论' charset = utf8;

alter table comment
    add primary key (comment_id);

