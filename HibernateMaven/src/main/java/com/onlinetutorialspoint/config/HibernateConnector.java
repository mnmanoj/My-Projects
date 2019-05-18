/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onlinetutorialspoint.config;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author chandu
 */
public class HibernateConnector {

    private static HibernateConnector me;
    private Configuration cfg;
    private SessionFactory factory;

    private HibernateConnector(Configuration preConfig) throws HibernateException {

        // build the config
        if (preConfig == null) {
            cfg = new Configuration();
        } else {
            cfg = preConfig;
        }

        /**
         * Connection Information..
         */
        cfg.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        cfg.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/onlinetutorialspoint");
        cfg.setProperty("hibernate.connection.username", "chandu");
        cfg.setProperty("hibernate.connection.password", "123456");
        cfg.setProperty("hibernate.show_sql", "true");

        /**
         * Mapping Resources..
         */
        cfg.addResource("com/onlinetutorialspoint/pojo/Login.hbm.xml");
        cfg.addResource("com/onlinetutorialspoint/pojo/Student.hbm.xml");

        factory = cfg.buildSessionFactory();
    }

    public static synchronized HibernateConnector getInstance() throws HibernateException {
        if (me == null) {
            me = new HibernateConnector(null);
        }

        return me;
    }
    
    public Session getSession() throws HibernateException {
        Session session = factory.openSession();
        if (!session.isConnected()) {
            this.reconnect();
        }
        return session;
    }
    
    private void reconnect() throws HibernateException {
        this.factory = cfg.buildSessionFactory();
    }
}
