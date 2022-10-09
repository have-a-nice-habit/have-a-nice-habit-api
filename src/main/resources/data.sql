insert into user (id, nickname)
values (1, 'ALOE');
insert into user (id, nickname)
values (2, 'Zenol');
insert into user (id, nickname)
values (3, 'TRUFFLE');
insert into user (id, nickname)
values (4, 'Nuddy');
insert into user (id, nickname)
values (5, 'Alprazolam');
insert into user (id, nickname)
values (6, 'Diabetes');
insert into user (id, nickname)
values (7, 'SPF');
insert into user (id, nickname)
values (8, 'Clonidine');
insert into user (id, nickname)
values (9, 'Varibar');
insert into user (id, nickname)
values (10, 'Povidone');

insert into habit (id, user_id, count, title, week_count) values (1, 1, 1, '물 마시기', 2);
insert into habit (id, user_id, count, title) values (2, 2, 7, '22시 이전에 자기');
insert into habit (id, user_id, count, title) values (3, 3, 5, '영단어 외우기');

insert into habit_display_date_list(habit_id, display_date_list) values(1,'2022-10-03');
insert into habit_display_date_list(habit_id, display_date_list) values(2,'2022-10-08');
insert into habit_display_date_list(habit_id, display_date_list) values(3,'2022-10-07');
