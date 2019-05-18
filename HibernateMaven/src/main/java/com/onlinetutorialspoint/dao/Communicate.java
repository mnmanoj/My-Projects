package com.onlinetutorialspoint.dao;

import java.awt.List;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.HttpURLConnection;
import java.net.URLEncoder;

public class Communicate {

    private String serverPath = "";
    private String hostName = "";
    private int port = 80;
    public static final Integer CULTSWITCH = 1;
    public static final Integer CULTSWITCH_SERVICE = 2;
    public static final Integer ROOMTYPES = 10;
    public static final Integer DISTRIBUTOR_CREDENTIALS = 11;
    public static final Integer GDS_CODES = 12;
    private String postData = "";
    private String responseData = "";
    private boolean noHeaders = true;
    private String httpUser = "";
    private String httpPass = "";
    private boolean connectionProblems = false;
    public static int inc = 0;
    public static final int POST = 1;
    public static final int GET = 2;
    private int postMethod = POST;
    List l;
    private String authUser;
    private String authPassword;
    private boolean foundErrors = false;
    private String errorMessage;

    public boolean isFoundErrors() {
        return foundErrors;
    }

    public void setFoundErrors(boolean foundErrors) {
        this.foundErrors = foundErrors;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Communicate() {
    }

    private Communicate(String host, int port, String serverPath, String postData, String authUser, String authPassword) {
        setHostName(host);
        setPort(port);
        setServerPath(serverPath);
        setAuthUser(authUser);
        setAuthPassword(authPassword);
        if (postData != null && postData.length() > 0) {
            setPostData(postData);
        }
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    public void setAuthPassword(String authPassword) {
        this.authPassword = authPassword;
    }

    private Communicate(String host, int port, String serverPath, String postData) {
        setHostName(host);
        setPort(port);
        setServerPath(serverPath);
        if (postData != null && postData.length() > 0) {
            setPostData(postData);
        }
    }

    public Communicate getInstance(String projectType, Communicate communicate, String request, Boolean isHttpAuth, Boolean isSecureKey) {
        try {
//            CheConfigurations configurationsByProjetcName = null;
//            configurationsByProjetcName = cheConfigurationsDAO.getConfigurationsByProjetcName(projectType);

            communicate.setHostName("backups.cultuzz.de");
            communicate.setPort(8080);
            MessageDigestCheck mv = new MessageDigestCheck();

            if (isSecureKey) {
                String MD5Message1 = mv.calMsgDigest(request, projectType);
                communicate.setPostData("otaRQ=" + request + "&secure_key=" + MD5Message1);
            } else {
                communicate.setPostData("otaRQ=" + java.net.URLEncoder.encode(request, "UTF-8"));
            }
            if (isHttpAuth) {
                communicate.setServerPath("/cultswitch/basic");
                communicate.doHandShake("544107", "NTQ0MTA3**MDQ=**ZTgxMTY5MDg4OWNiYWU2");

            } else {
                communicate.setServerPath("/cultswitch/processOTA");
                communicate.doHandShake();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return communicate;

    }

    public void setServerPath(String path) {
        serverPath = path;
    }

    public String getServerPath() {
        return serverPath;
    }

    public void setHostName(String name) {
        hostName = name;
    }

    public String getHostName() {
        return hostName;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPostData(String postData) {
        this.postData = postData;
    }

    public String getPostData() {
        return postData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public String getResponseData() {
        return responseData;
    }

    public boolean isNoHeaders() {
        return noHeaders;
    }

    public void setNoHeaders(boolean headoff) {
        noHeaders = headoff;
    }

    public void setPostMethod(int method) {
        postMethod = method;
    }

    public void setUserData(String username, String password) {
        httpUser = username;
        httpPass = password;
    }

    private void authenticate(String username, String password) {
        setUserData(username, password);
        authenticate();
    }

    private void authenticate() {
        java.io.InputStream ins = null;
        StringBuffer response = null;
        try {
            setFoundErrors(false);
            System.out.println("i am in authentication..........");
            System.out.println("httpuser...." + httpUser + " .........httpPass....." + httpPass + "  port...." + port);
//            java.net.Authenticator.setDefault(new Communicate.CDAuthenticator(httpUser, httpPass));

            java.net.URL url = new java.net.URL("http://" + hostName + ":" + port + serverPath);
            System.out.println("url connection ........" + url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            System.out.println("connection......" + conn);
            String userpass = httpUser + ":" + httpPass;
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
            System.out.println("basic auth......" + basicAuth);
            conn.setRequestProperty("Authorization", basicAuth);

            //URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(postData);
            out.close();
            ins = conn.getInputStream();
            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(ins));
            java.lang.String str;

            response = new StringBuffer("");
            while ((str = reader.readLine()) != null) {
                response.append(str).append("\r\n");
            }
            responseData = response.toString();
            System.out.println("done");
        } catch (IOException ex) {
            System.out.println("i am in IOException...." + ex);
            setFoundErrors(true);
            setErrorMessage("Failed making connection to Server!(" + hostName + ")");
            setConnectionProblems(true);
        } finally {
            try {
                response = null;
                ins.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                setConnectionProblems(true);
            }
        }
    }

    public void doHandShake(String username, String password) {
        authenticate(username, password);
    }

    public void doHandShake() {
        //System.out.println("iam in dohandshake start");
        StringBuffer response = null;
        try {
            setFoundErrors(false);
            if (serverPath.equalsIgnoreCase("") || hostName.equalsIgnoreCase("")) {
                //System.out.println("serverPath : "+serverPath + "  Host Name :: "+hostName);
                return;
            }

            // Create a socket to the host
            InetAddress addr = InetAddress.getByName(hostName);
            Socket socket = new Socket(addr, port);

            // Send header
            BufferedWriter bWr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));

            bWr.write("POST " + serverPath + " HTTP/1.0\r\n");
            bWr.write("Host: " + hostName + "\r\n");
            bWr.write("Content-Length: " + postData.length() + "\r\n");
            bWr.write("Content-Type: application/x-www-form-urlencoded\r\n");
            bWr.write("Accept-Encoding : gzip, deflate\r\n");
            // bWr.write("Content-Type: text/html\r\n");
            bWr.write("User-Agent: Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.8.1.3) Gecko/20070417 Fedora/2.0.0.3-4.fc7 Firefox/2.0.0.3\r\n");
            bWr.write("Connection: close\r\n");
            bWr.write("\r\n");
            bWr.write(postData);
            bWr.flush();

            BufferedReader bRd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = "";

            response = new StringBuffer("");
            while ((line = bRd.readLine()) != null) {
                response.append(line).append("\r\n");
            }
            responseData = response.toString();

            // without headers
            if (noHeaders) {
                responseData = responseData.substring(responseData.indexOf("\r\n\r\n") + 4);
            }

            bWr.close();
            bRd.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            setConnectionProblems(true);
            setFoundErrors(true);
            setErrorMessage("Failed making connection to Server! (" + hostName + ")");
        }
    }

    public boolean isConnectionProblems() {
        return connectionProblems;
    }

    public void setConnectionProblems(boolean connectionProblems) {
        this.connectionProblems = connectionProblems;
    }

    public static String encode(String str) {

        try {

            return java.net.URLEncoder.encode(str, "UTF-8");

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public static String executeCultService(String webHost, int port, String webURL, String data) {
        ByteArrayInputStream arrayInputStream = null;
        StringBuilder xmlRes = new StringBuilder();
        try {
            Socket s = new Socket(webHost, port);
            BufferedReader rd = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), "UTF8"));
            String encodeData = URLEncoder.encode("otaRQ", "UTF-8") + "=" + URLEncoder.encode(data.trim(), "UTF-8");
            wr.write("POST " + "/" + webURL + " HTTP/1.0\n");
            wr.write("Host: " + webHost + "\r\n");
            wr.write("Content-Length: " + encodeData.trim().length() + "\n");
            wr.write("Content-Type: application/x-www-form-urlencoded\n");
            wr.write("\n");
            wr.write(encodeData.trim());
            wr.flush();
            String line;
            while ((line = rd.readLine()) != null) {
                xmlRes.append(line + "\n");
            }
            xmlRes.delete(0, xmlRes.indexOf("<?xml"));
            xmlRes.delete(xmlRes.indexOf("<?xml"), xmlRes.indexOf("?>") + 2);
            arrayInputStream = new ByteArrayInputStream(xmlRes.toString().getBytes());
            wr.close();
            rd.close();

        } catch (java.net.NoRouteToHostException ne) {

            return "noInternet";
        } catch (Exception e) {

            e.printStackTrace();

        }
        return xmlRes.toString();
    }
}
