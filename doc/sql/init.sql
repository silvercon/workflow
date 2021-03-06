-- 同步用户/用户组
insert into ACT_ID_USER(ID_, FIRST_, LAST_, EMAIL_) select id, user_account, mobile, user_email from nf_monitor.om_user;

insert into ACT_ID_GROUP(ID_, NAME_) select role_id, role_name from nf_monitor.om_role;

insert into ACT_ID_MEMBERSHIP(USER_ID_, GROUP_ID_) select user_id, role_id from sys_role_user;
