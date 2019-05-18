/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onlinetutorialspoint.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;

/**
 *
 * @author chandu
 */
public class MessageDigestCheck {

    public String calMsgDigest(String xmlString, String check_server) {
        MessageDigest md = null;
        try {

            String msgSignatrueKey = "e9e02fad91072dcc1f381e854f5a3cc1";

//            if(check_server.equals("CSI"))
//            {
//                 msgSignatrueKey = "54e1fd3f0f6f3d18ae5a9a55abc55f5f";
//            }
//            else if(check_server.equals("CHE"))
//            {
//                 msgSignatrueKey = "d2d83c805e2a9fecd6b7ed1f1ab52639";
//            }
//            else if(check_server.equals("CRC"))
//            {
//                 msgSignatrueKey = "97caf8a2536179830090e1030f4b9829";
//            }
            System.out.println("msgSignatrueKey from CheConfigurationsDAO: " + msgSignatrueKey);
            md = MessageDigest.getInstance("MD5");
            /* read message */
            md.update(xmlString.replaceAll("[ \t\r\n]", "").getBytes());
            char[] x = Hex.encodeHex(md.digest());
            System.out.println("Stripped XML: "
                    + xmlString.replaceAll("[ \t\r\n]", ""));
            System.out.println("Generated XML MD5: "
                    + String.valueOf(x));
            md.reset();
            /* generate message signature key */
            md.update(msgSignatrueKey.getBytes());
            char[] y = Hex.encodeHex(md.digest());
            System.out.println("Generated Message Signature MD5: "
                    + String.valueOf(y));
            md.reset();
            /* generate SHA Digest */
            String tmp = String.valueOf(y) + String.valueOf(x)
                    + String.valueOf(y);
            System.out.println("Combined Hashes: " + tmp);
            md = MessageDigest.getInstance("SHA");
            md.update(tmp.getBytes());
            char[] secureKey2 = Hex.encodeHex(md.digest());
            /* generate output */
            System.out.println("Generated SecureKey: "
                    + String.valueOf(secureKey2));
            md.reset();

            return String.valueOf(secureKey2);

        } catch (NoSuchAlgorithmException ex) {

            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
