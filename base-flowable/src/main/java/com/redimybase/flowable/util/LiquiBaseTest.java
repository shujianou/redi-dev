package com.redimybase.flowable.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class LiquiBaseTest {
	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext=
				new ClassPathXmlApplicationContext("sql/dataBase.xml");
		
		DruidDataSource ds = (DruidDataSource) applicationContext.getBean("dataSource");
		
		DruidPooledConnection connection = ds.getConnection();
		
		
		DatabaseConnection databaseConnection=new JdbcConnection(connection);
		Database db = DatabaseFactory.getInstance()
		.findCorrectDatabaseImplementation(databaseConnection);
		
		
		
		Liquibase liquibase=new Liquibase("sql/1.xml", new ClassLoaderResourceAccessor(),
				
				
				db);
		
		liquibase.update("flowable");
		
	}
}
