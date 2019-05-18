/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.onlinetutorialspoint.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author chandu
 */
public class LoginDAO {

    public String mapCsProductsWithPullChannelProducts(String objectId, String ratePlanId, String marketCode) {

        try {
            StringBuilder requestXML = null;

            requestXML = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>"
                    + "<OTA_HotelRatePlanNotifRQ "
                    + "xmlns='http://www.opentravel.org/OTA/2003/05' "
                    + "TimeStamp='" + currentTime() + "' "
                    + "Target='Production' Version='3.30' PrimaryLangID='en'>"
                    + "<POS>");
            requestXML.append("<Source AgentSine='41554' AgentDutyCode='b1390ee011e89eba'>" + "    <RequestorID Type='10' ID='" + objectId + "' ID_Context='CLT' />" + "<BookingChannel Type='" + 4 + "' />"
                    + "</Source>");
            requestXML.append("</POS>" + "<RatePlans>");

            if (ratePlanId != null) {
                requestXML.append("<RatePlan RatePlanType=\"11\"  RatePlanID=\"").append(ratePlanId).append("\" ");
            }
            requestXML.append("RatePlanNotifType=\"Overlay\" ");
            if (marketCode != null && marketCode.trim().length() > 0) {
                requestXML.append("MarketCode=\"").append(marketCode).append("\" ");
            }
            System.err.println("marketCode = " + marketCode);
            requestXML.append(" RestrictedDisplayIndicator=\"false\" ");

            requestXML.append(" InventoryAllocatedInd=\"true\">");

            requestXML.append("</RatePlan>");

            requestXML.append("</RatePlans>");
            requestXML.append("</OTA_HotelRatePlanNotifRQ>");

            //System.out.println("Request String :: " + requestXML.toString());
            return requestXML.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String makeRequestForObjectDisributorMapAndDeMap(String objectId, String distributorId, String currencyCode, String langCode) {
        try {
            StringBuilder requestXML = new StringBuilder("<?xml version='1.0' encoding='UTF-8'?>"
                    + "<OTA_HotelDescriptiveContentNotifRQ "
                    + " xmlns='http://www.opentravel.org/OTA/2003/05' "
                    + "              TimeStamp='" + currentTime() + "' "
                    + "              Target='Production' Version='3.14' PrimaryLangID='en'>"
                    + "	<POS "
                    + "CultSwitchUUID='dc4f5e9f-a494-479b-9370-acc549a31182-1519366263'>");

            requestXML.append(" <Source AgentSine='").append("41554").append("' AgentDutyCode='").append("b1390ee011e89eba").append("'>  <RequestorID Type='10' ID='").append(objectId).append("'/>" + " <BookingChannel Type='").append("").append("'/>"
                    + " </Source>");

            requestXML.append("<Source>"
                    + "<RequestorID ID='").append(objectId).append("' Type='1' URL='urn:cultuzz:cultswitch:auth:username'/>");
            requestXML.append("<BookingChannel Type='" + 4 + "'/>"
                    + "</Source>"
                    + "</POS>");

            requestXML.append("<HotelDescriptiveContents HotelCode='" + objectId + "'>");
            requestXML.append("<HotelDescriptiveContent CurrencyCode='" + currencyCode + "' LanguageCode='" + langCode + "'>");
            requestXML.append("<TPA_Extensions>");
            requestXML.append("<ChannelInfos>");
            requestXML.append("<ChannelInfo CodeContext='DistributorsConnection' DistributorID='" + distributorId + "'");

            requestXML.append(" Status='2'>");

            requestXML.append("</ChannelInfo></ChannelInfos></TPA_Extensions></HotelDescriptiveContent>");
            requestXML.append("</HotelDescriptiveContents>");

            requestXML.append("</OTA_HotelDescriptiveContentNotifRQ>");
            return requestXML.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String currentTime() {

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formatdate = sdf.format(d);
        return formatdate;
    }

    public static void main(String[] args) {
        LineNumberReader reader = null;
        String line = "";
        String FILE_PATH = "/home/chandu/Desktop/data.txt";
        String[] split = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(new File(FILE_PATH))));
            while ((line = reader.readLine()) != null) {
                split = line.split(",");
            }
            LoginDAO loginDAO = new LoginDAO();
            for (int i = 0; i < split.length; i++) {
                System.out.println("ObjectIds : " + split[i]);
                String req = loginDAO.makeRequestForObjectDisributorMapAndDeMap(split[i], "4561", "1", "2");
                System.out.println("req : " + req);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
