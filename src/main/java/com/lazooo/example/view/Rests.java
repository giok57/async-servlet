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

package com.lazooo.example.view;

import org.glassfish.jersey.server.ChunkedOutput;
import com.lazooo.example.Utils;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.container.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.EOFException;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 30/11/13
 * Time: 00:35
 */


//try to go http://wifi.lazooo.com/com.lazooo.example/login
@Path("/")
public class Rests {

    private static int numberOfSuccessResponses = 0;
    private static int numberOfFailures = 0;

    /**
     *
     * Client subscription, waiting for authentication provided by the AP endpoint
     *
     * @param asyncResponse
     * @param email
     * @param mac
     */
    @GET
    @Path("/subscribe")
    @Produces({MediaType.TEXT_PLAIN})
    public void clientSubscribe(@Suspended final AsyncResponse asyncResponse,
                                @QueryParam("email") String email, @QueryParam("mac") String mac){

        if(Utils.paramsRequired(mac, email) == false){
            asyncResponse.resume("miss some shit!");
        }else {
            System.out.println("[" + new Date() + "] Email: " + email + ", Mac: " + mac + " is now connected.");

            Utils.getUtils().subscribeEmail(email, mac);
            Utils.getUtils().push(asyncResponse, mac);
        }
    }


    /**
     *
     * AP long polling fetch endpoint.
     * Requires "ap-cookie" setted with value {@link Utils#AP_TOKEN}
     *
     */
    @GET
    @Path("/supercalifragilistichespiralitoso")
    @Produces({MediaType.TEXT_PLAIN})

    public void apWaiting(@Context HttpServletRequest request, @Suspended final AsyncResponse async) {

        Boolean login = Utils.getUtils().requiredToken(request);
        if(login){
            //get first entered in client login requests queue
            Utils.getUtils().pop(async);
        }else {
            async.resume("You are not allowed here");
        }
    }


    /**
     *
     * This endpoint shows how chunck works
     *
     * @param motion string to show every second
     * @return chuncked output
     */
    @Path("/slow/{motion}")
    @GET
    public ChunkedOutput<String> ChunkedGet(@PathParam("motion")final String motion) {

        //the important thing
        final ChunkedOutput<String> output = new ChunkedOutput<String>(String.class);

        new Thread() {
            public void run() {
                try {
                    String chunk;

                    while ((chunk = getNextString(motion)) != null) {
                        //this can trow an EOFException
                        output.write(chunk);
                    }
                } catch (Exception e) {
                    System.err.println("Trunked Chunked Output!");
                    // IOException thrown when writing the
                    // chunks of response: should be handled
                } finally {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // simplified: IOException thrown from
                    // this close() should be handled here...
                }
            }
        }.start();

        // the output will be probably returned even before
        // a first chunk is written by the new thread
        return output;
    }

    private String getNextString(String motion) {
        // ... long running operation that returns
        //     next string or null if no other string is accessible
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  motion;
    }


    /**
     *
     * This endpoint shows how async works
     *
     * @param asyncResponse
     */
    @GET
    @Path("/resource")
    @Produces(MediaType.TEXT_PLAIN)
    public void asyncGet(@Suspended final AsyncResponse asyncResponse) {
        System.out.println("async request started...");
        asyncResponse.setTimeoutHandler(new TimeoutHandler() {

            @Override
            public void handleTimeout(AsyncResponse asyncResponse) {
                asyncResponse.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Operation timed out.").build());
            }
        });
        //dimostrative use only
        asyncResponse.setTimeout(10, TimeUnit.SECONDS);

        asyncResponse.register(new ConnectionCallback() {
            @Override
            //does noot work!!
            public void onDisconnect(AsyncResponse asyncResponse) {
                System.err.print("disconnected!");
            }
        });

        asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable == null) {
                    System.out.println("done!");

                    // no throwable - the processing ended successfully
                    // (response already written to the client)
                    numberOfSuccessResponses++;
                } else {
                    System.err.println("error!");
                    numberOfFailures++;
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = null;
                try {
                    result = veryExpensiveOperation();
                } catch (InterruptedException e) {

                }
                asyncResponse.resume(result);
            }

            private String veryExpensiveOperation() throws InterruptedException {
                Thread.sleep(5000);
                return "Hi everybody!";
            }
        }).start();
    }
}
