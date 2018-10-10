DROP TABLE IF EXISTS act_process_type;
CREATE TABLE `act_process_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code_id` varchar(10) NOT NULL COMMENT '类别id',
  `name_` varchar(20) NOT NULL COMMENT '类型名称',
  `pid` int(11) NOT NULL DEFAULT '0' COMMENT '分类的上级id',
  `state_` bigint(4) NOT NULL DEFAULT '0' COMMENT '有效性',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_` (`name_`),
  KEY `code_id` (`code_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='流程分类';

delete from act_process_type
INSERT into act_process_type(code_id,name_,state_) VALUES('leave','请假流程',0);
INSERT into act_process_type(code_id,name_,state_) VALUES('hr','人事流程',0);


INSERT into ACT_ID_USER(ID_,PWD_) VALUES ('b','b');
INSERT into ACT_ID_USER(ID_,PWD_) VALUES ('c','c');