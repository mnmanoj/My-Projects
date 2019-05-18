/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onlinetutorialspoint.dao;

import com.onlinetutorialspoint.config.HibernateConnector;
import com.onlinetutorialspoint.pojo.Customer;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author chandu
 */
public class StudentDAO {

    public List<Customer> listStudent() {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Query query = session.createQuery("from Student s");

            List queryList = query.list();
            if (queryList != null && queryList.isEmpty()) {
                return null;
            } else {
                System.out.println("list " + queryList);
                return (List<Customer>) queryList;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public Customer findStudentById(int id) {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Query query = session.createQuery("from Student s where s.id = :id");
            query.setParameter("id", id);

            List queryList = query.list();
            if (queryList != null && queryList.isEmpty()) {
                return null;
            } else {
                System.out.println("list " + queryList);
                return (Customer) queryList.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public void updateStudent(Customer student) {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            session.saveOrUpdate(student);
            session.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Customer addStudent(Customer student) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            transaction = session.beginTransaction();
            session.save(student);
            transaction.commit();
            return student;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public void deleteStudent(int id) {
        Session session = null;
        try {
            session = HibernateConnector.getInstance().getSession();
            Transaction beginTransaction = session.beginTransaction();
            Query createQuery = session.createQuery("delete from Student s where s.id =:id");
            createQuery.setParameter("id", id);
            int executeUpdate = createQuery.executeUpdate();
            System.out.println("deleted status : " + executeUpdate);
            beginTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void main(String[] args) {
        StudentDAO studentDAO = new StudentDAO();
//        List<Student> listStudent = studentDAO.listStudent();
//        if (listStudent != null) {
//            Iterator<Student> iterator = listStudent.iterator();
//            while (iterator.hasNext()) {
//                Customer student = iterator.next();
//                System.out.println("Customer Name : " + student.getName());
//            }
//        }
//        Customer student = studentDAO.findStudentById(1);
//        System.out.println("Customer Name : " + student);
//        student.setName("chandrashekhar Goka");
//        studentDAO.updateStudent(student);
//        studentDAO.deleteStudent(1);

        Customer s = new Customer();
//        s.setGender(new Byte("1"));
//        s.setName("Kranthi");
//        s.setRoolnumber(140);
        studentDAO.addStudent(s);

    }
}
