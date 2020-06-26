
DROP TABLE IF EXISTS `jagent_config`;

CREATE TABLE `jagent_config` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT,
  `class_name` varchar(255) NOT NULL DEFAULT '' COMMENT '类全名',
  `method_name` varchar(25) NOT NULL DEFAULT '' COMMENT '方法名',
  `new_method_name` varchar(25) NOT NULL DEFAULT '' COMMENT '新方法名',
  `add_type` tinyint(4) NOT NULL COMMENT '1:before 3:after 5:modify',
  `method_content` varchar(255) DEFAULT NULL COMMENT 'add_type = 5时的方法体',
  `default_value` tinyint(11) unsigned DEFAULT '0' COMMENT '0:使用默认 1 使用编写内容',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1被删除0有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

LOCK TABLES `jagent_config` WRITE;
/*!40000 ALTER TABLE `jagent_config` DISABLE KEYS */;

INSERT INTO `jagent_config` (`id`, `class_name`, `method_name`, `new_method_name`, `add_type`, `method_content`, `default_value`, `deleted`)
VALUES
	(1,'com.sb.test.ConcreteClass','operation','operation$1',5,'{org.slf4j.MDC.put(\"traceId\",\"1\");try{System.out.println(org.slf4j.MDC.get(\"traceId\"));operation$1($$);}catch(Exception e){throw  new Exception (e.getMessage());}finally{org.slf4j.MDC.remove(\"traceId\");}}',0,1),
	(2,'com.sb.test.ConcreteClass','operation','operation$1',1,'System.out.println($$);',0,1),
	(3,'com.sb.test.ConcreteClass','operation','operation$1',1,'System.out.println($1);',0,1),
	(4,'com.sb.test.ConcreteClass','operation','operation$1',3,'System.out.println(com.alibaba.fastjson.JSON.toJSONString($_));',0,1),
	(5,'com.sb.test.ConcreteClass','operation','operation$1',3,'logger.info(\"方法返回值:{}\",com.alibaba.fastjson.JSON.toJSONString($_));',1,0);

/*!40000 ALTER TABLE `jagent_config` ENABLE KEYS */;
UNLOCK TABLES;