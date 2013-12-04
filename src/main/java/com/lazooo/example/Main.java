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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;

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
        s.setPort(8080);

        server.addConnector(s);

        WebAppContext context = new WebAppContext();

        context.setDefaultsDescriptor( "./webapp/WEB-INF/web.xml");

        context.setResourceBase("./webapp/");
        context.setContextPath("/");

        server.setHandler(context);

        server.start();

    }
}
