package com.lazooo.example; /**
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

import com.google.gson.Gson;
import com.lazooo.example.bean.UploadResponse;
import com.lazooo.example.bean.WifiHour;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 30/11/13
 * Time: 23:54
 */
public class Main {
    public static void main(String[] args) throws Exception {

        Server server = new Server();

        ServerConnector s = new ServerConnector(server);
        s.setPort(8000);

        server.addConnector(s);

        WebAppContext context = new WebAppContext();

        context.setDefaultsDescriptor( "./webapp/WEB-INF/web.xml");

        context.setResourceBase("./webapp/");
        context.setContextPath("/");

        server.setHandler(context);

        server.start();

        LinkedList<WifiHour> l = new LinkedList<WifiHour>();
        l.add(new WifiHour(new WifiHour.WifiBean("id","OPEN", true, "ssid", new HashSet<String>(), new HashSet<String>(), "redir", 1
                ,1,1,1,1,true, new HashSet<WifiHour.Location>()),1,"ff"));
        l.add(new WifiHour(new WifiHour.WifiBean("id","OPEN", true, "ssid", new HashSet<String>(), new HashSet<String>(), "redir", 1
                ,1,1,1,1,true, new HashSet<WifiHour.Location>()),1,"ff"));
        ///System.out.println(uploadJson(l));

    }

    public static List<WifiHour> uploadJson(List<WifiHour> list){

        HttpClient httpClient = new DefaultHttpClient();
        String json = null;
        if(list != null){

            Gson gson = new Gson();
            json = gson.toJson(list);
            try {

                HttpPost request = new HttpPost("http://localhost:8000/upload");
                StringEntity params =new StringEntity(json);
                request.addHeader("Content-type", "application/json");
                request.setEntity(params);
                request.setHeader("Cookie", "crawler-cookie=favato");
                HttpResponse response = httpClient.execute(request);
                if(response.getStatusLine().getStatusCode() == 200){

                    json = EntityUtils.toString(response.getEntity());

                    UploadResponse uploadResponse = gson.fromJson(json, UploadResponse.class);
                    if(uploadResponse.getCode() == 200){

                        return new LinkedList<WifiHour>();
                    }else {

                        System.out.println(uploadResponse.getMessage());
                    }
                }else {

                    System.out.println("http return code != 200 --> "+response.getStatusLine().getStatusCode());
                    return list;
                }
            }catch (Exception ex) {

                ex.printStackTrace();
                return list;
            } finally {

                httpClient.getConnectionManager().shutdown();
            }
        }
        return list;
    }
}
