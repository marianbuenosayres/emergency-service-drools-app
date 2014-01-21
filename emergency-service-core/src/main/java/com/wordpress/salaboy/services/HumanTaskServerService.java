/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wordpress.salaboy.services;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.jbpm.services.task.HumanTaskServiceFactory;
import org.jbpm.services.task.identity.JBossUserGroupCallbackImpl;
import org.kie.api.task.TaskService;

import bitronix.tm.resource.jdbc.PoolingDataSource;

/**
 *
 * @author salaboy
 */
public class HumanTaskServerService {

    private static HumanTaskServerService instance = null;
    //private MinaTaskServer server;
    private EntityManagerFactory emf;
    private TaskService taskService;
    private PoolingDataSource ds1;

    private HumanTaskServerService() {
    }

    public static HumanTaskServerService getInstance() {
        if (instance == null) {
            instance = new HumanTaskServerService();
        }
        return instance;
    }

    public void initTaskServer() {

        System.out.println(">>> Starting Human Task Server ...");

        ds1 = new PoolingDataSource();
        ds1.setUniqueName("jdbc/testDS1");

        //ds1.setClassName("com.mysql.jdbc.jdbc2.optional.MysqlXADataSource");
        ds1.setClassName("org.h2.jdbcx.JdbcDataSource");
        ds1.setMaxPoolSize(5);
        ds1.setAllowLocalTransactions(true);
        ds1.getDriverProperties().put("user", "root");
        ds1.getDriverProperties().put("password", "atcroot");
        //ds1.getDriverProperties().put("databaseName", "droolsflow");
        //ds1.getDriverProperties().put("serverName", "localhost");

        ds1.init();
        // Use persistence.xml configuration
        emf = Persistence.createEntityManagerFactory("org.jbpm.task");

        Properties userGroups = new Properties();
        userGroups.setProperty("operator", "");
        userGroups.setProperty("control", "");
        userGroups.setProperty("hospital", "");
        userGroups.setProperty("doctor", "");
        userGroups.setProperty("firefighter", "");
        userGroups.setProperty("garage_emergency_service", "");
        userGroups.setProperty("Administrator", "");
        JBossUserGroupCallbackImpl userGroupCallback = new JBossUserGroupCallbackImpl(userGroups);
        
        taskService = HumanTaskServiceFactory.newTaskServiceConfigurator().
        	entityManagerFactory(emf).
        	userGroupCallback(userGroupCallback).
        	getTaskService();
        
        System.out.println(">>> Human Task Server Started!");
    }

    public void stopTaskServer() {
    	ds1.close();
        ds1 = null;
        System.out.println(">>> Human Task Server Stopped!");
    }

	public TaskService getTaskService() {
		return taskService;
	}
}
