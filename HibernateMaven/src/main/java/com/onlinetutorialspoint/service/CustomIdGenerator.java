/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onlinetutorialspoint.service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

import org.hibernate.id.IdentifierGenerator;

/**
 *
 * @author chandrashekhar
 */
public class CustomIdGenerator implements IdentifierGenerator {

    public int generateCustId() {
        Random random = new Random();
        return random.nextInt(100);
    }

    @Override
    public Serializable generate(SessionImplementor si, Object o) throws HibernateException {

        Date date = new Date();
        
        Calendar calendar = Calendar.getInstance();
        return "Cust_" + this.generateCustId() + "_" + calendar.get(Calendar.YEAR);

    }
}
