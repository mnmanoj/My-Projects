/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onlinetutorialspoint.dao;

import com.onlinetutorialspoint.config.HibernateConnector;
import com.onlinetutorialspoint.pojo.Customer;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author chandu
 */
public class StudentCriteria {

    public void getStudents() {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Criteria createCriteria = session.createCriteria(Customer.class);
            List list = createCriteria.list();
            System.out.println("count : " + list.size());
        } catch (Exception e) {
            session.close();
        }
    }

    public void getLimitedStudents() {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Criteria createCriteria = session.createCriteria(Customer.class);
            createCriteria.setMaxResults(1);
            List list = createCriteria.list();
            System.out.println("count : " + list.size());
        } catch (Exception e) {
            session.close();
        }
    }

    public static void main(String[] args) {
        StudentCriteria sc = new StudentCriteria();
        sc.getStudents();
        sc.getLimitedStudents();
    }
}
