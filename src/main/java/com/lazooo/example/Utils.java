package com.lazooo.example;
/**
 The MIT License (MIT)

 Copyright (c) 2013 Lazooo

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.

 LazoooTeam
 */

import com.ecwid.mailchimp.MailChimpClient;
import com.ecwid.mailchimp.MailChimpException;
import com.ecwid.mailchimp.MailChimpObject;
import com.ecwid.mailchimp.method.v2_0.lists.Email;
import com.ecwid.mailchimp.method.v2_0.lists.SubscribeMethod;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.AsyncResponse;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 30/11/13
 * Time: 01:43
 */
public class Utils {

    //todo set token, same in AP cookie
    private static String AP_TOKEN = "123";
    //todo set your mailchimp api key
    public static String MAILCHIMP_APIKEY = "xxx";
    //todo set your mailchimp api list id
    public static String MAILCHIMP_LISTID = "xxx";


    private File file = new File("email-mac.txt");
    private File log = new File("logs.txt");

    private static Utils utils = null;

    private static Integer success = 0;
    private List<Pair> queue = new ArrayList<Pair>();

    private static AsyncResponse waiter = null;


    // +++START private classes+++
    private static class MergeVars extends MailChimpObject {

        @Field
        public String EMAIL, FNAME, LNAME;

        public MergeVars() {
        }

        public MergeVars(String email, String fname, String lname) {
            this.EMAIL = email;
            this.FNAME = fname;
            this.LNAME = lname;
        }
    }

    private class Pair {

        private AsyncResponse asyncResponse;
        private String mac;

        public Pair(AsyncResponse asyncResponse, String mac) {

            this.asyncResponse = asyncResponse;
            this.mac = mac;
        }

        public AsyncResponse getAsyncResponse() {
            return asyncResponse;
        }

        public void setAsyncResponse(AsyncResponse asyncResponse) {
            this.asyncResponse = asyncResponse;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }
    }
    // +++END+++


    /**
     *
     * private empty constructor
     *
     */
    private Utils(){
    }


    /**
     *
     * Class singleton getter
     *
     * @return
     */
    public static Utils getUtils(){
        if(utils == null){
            utils = new Utils();
        }
        return utils;
    }

    /**
     *
     * Subscribe an email to your mailchimp list.
     *
     * @param email
     * @param mac
     */
    public void subscribeEmail(String email, String mac){

        log2File(file, email + " - " + mac + "\n");

        // reuse the same MailChimpClient object whenever possible
        MailChimpClient mailChimpClient = new MailChimpClient();

        // Subscribe a person
        SubscribeMethod subscribeMethod = new SubscribeMethod();
        subscribeMethod.apikey = MAILCHIMP_APIKEY;
        subscribeMethod.id = MAILCHIMP_LISTID;
        subscribeMethod.email = new Email();
        subscribeMethod.email.email = email;
        subscribeMethod.double_optin = true;
        subscribeMethod.update_existing = true;
        subscribeMethod.merge_vars = new MergeVars(email, mac, "");
        try {
            mailChimpClient.execute(subscribeMethod);
            System.out.println(email+" added...");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (MailChimpException em) {
            em.printStackTrace();
        }
    }

    /**
     *
     * Simple log file
     *
     * @param file name
     * @param s string to append
     */
    private void log2File(File file, String s){

        try {
            if(!file.exists()){
                file.createNewFile();
            }

            //true = append file
            FileWriter fileWritter = new FileWriter(file.getName(),true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(s);
            bufferWritter.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public synchronized void push(AsyncResponse clientResponse, String mac){
        if(waiter != null){
            waiter.resume(mac);
            waiter = null;
            clientResponse.resume("OK");
        }else {
            queue.add(new Pair(clientResponse, mac));
        }

    }

    public synchronized void pop(AsyncResponse apResponse){

        if(queue.size() == 0){
            waiter = apResponse;
        }else {
            log2File(log, success.toString() + "-");
            Pair p = queue.remove(0);
            p.getAsyncResponse().resume("OK");
            apResponse.resume(p.getMac());
        }
    }


    /**
     *
     * this method authenticates your wifi access point that has the cookie router-cookie setted with AP_TOKEN
     *
     * @param request
     * @return
     */
    public Boolean requiredToken(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String sessionToken = null;
        for (Integer index = 0; index < cookies.length; index++){
            if(cookies[index].getName().equals("ap-cookie"))
                sessionToken = cookies[index].getValue();
        }
        if(sessionToken != null && sessionToken.equals(AP_TOKEN)){
            return true;
        }
        return false;

    }

    /**
     *
     * @param args parameters required by the endpoint
     * @return
     */
    public static boolean paramsRequired(Object... args){
        for(Object arg : args){
            if(arg == null){
                return false;
            }
        }
        return true;
    }
}


