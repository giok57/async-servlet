package com.lazooo.example.bean;
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

import java.util.Set;

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 30/01/14
 * Time: 12:54
 */
public class WifiHour {

    boolean ready;
    boolean userMoving;
    String connId;
    WifiBean wifiBean;
    long timeSince;
    long timeLast;
    //
    WifiHour(WifiBean wifiBean, long time, String connId){

        this.ready = false;
        this.wifiBean = wifiBean;
        this.timeSince = time;
        this.timeLast = time;
        this.connId = connId;
        this.userMoving = false;
    }

    @Override
    public String toString() {
        return "WifiHour\n" +
                "    ready=" + ready + "\n" +
                "    userMoving=" + userMoving + "\n" +
                "    connId='" + connId + '\'' + "\n\n" +
                "    wifiBean=" + wifiBean + "\n" +
                "    timeLast=" + timeLast + "\n" +
                "    timeSince=" + timeSince + "\n" +
                '}' + "\n";
    }


    public class WifiBean {
        private String security;
        private boolean isRedirect;
        private Set<String> wifiAround;
        private String wifiId;
        private String ssid;
        private Set<String> macs;
        private String redirectPage;
        private Set<Location> locations;
        private int connTime;
        private int internetConnTime;
        private float mbDown;
        private boolean isInternet;
        private Avg connSpeed;
        private int signStr;

        public WifiBean(String wifiId, String security, boolean isRedirect, String ssid,
                        Set<String> mac, Set<String> wifiAround, String redirectPage, int internetConnTime, float mbDown,
                        int connTime, float connSpeed, int signStr, boolean isInternet, Set<Location> locations) {
            this.wifiId = wifiId;
            this.security = security;
            this.redirectPage = redirectPage;
            this.isRedirect = isRedirect;
            this.ssid = ssid;
            this.macs = mac;
            this.wifiAround = wifiAround;
            this.isInternet = isInternet;
            this.mbDown = mbDown;
            this.connTime = connTime;
            this.connSpeed = new Avg(connSpeed);
            this.signStr = signStr;
            this.locations = locations;
            this.internetConnTime = internetConnTime;
        }

        public String getSecurity() {
            return security;
        }

        public void setSecurity(String security) {
            this.security = security;
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public int getConnTime() {
            return connTime;
        }

        public void setConnTime(int connTime) {
            this.connTime = connTime;
        }

        public float getConnSpeed() {
            return connSpeed.avg;
        }

        public void setConnSpeed(float connSpeed) {
            this.connSpeed.addVal(connSpeed);
        }

        public int getSignStr() {
            return signStr;
        }

        public void setSignStr(int signStr) {
            this.signStr = signStr;
        }

        public boolean isInternet() {
            return isInternet;
        }

        public void setInternet(boolean isInternet) {
            this.isInternet = isInternet;
        }

        public String getWifiId() {
            return wifiId;
        }

        public void setWifiId(String wifiId) {
            this.wifiId = wifiId;
        }

        public boolean isRedirect() {
            return isRedirect;
        }

        public void setRedirect(boolean isRedirect) {
            this.isRedirect = isRedirect;
        }

        public String getRedirectPage() {
            return redirectPage;
        }

        public void setRedirectPage(String redirectPage) {
            this.redirectPage = redirectPage;
        }

        public Set<String> getWifiAround() {
            return wifiAround;
        }

        public void setWifiAround(Set<String> wifiAround) {
            this.wifiAround = wifiAround;
        }

        public Set<String> getMacs() {
            return macs;
        }

        public void setMacs(Set<String> macs) {
            this.macs = macs;
        }

        public Set<Location> getLocations() {
            return locations;
        }

        public void setLocations(Set<Location> locations) {
            this.locations = locations;
        }

        public int getInternetConnTime() {
            return internetConnTime;
        }

        public void setInternetConnTime(int internetConnTime) {
            this.internetConnTime = internetConnTime;
        }

        public float getMbDown() {
            return mbDown;
        }

        public void setMbDown(float mbDown) {
            this.mbDown = mbDown;
        }

        @Override
        public String toString() {
            String redirectShort = "";
            if(redirectPage != null && redirectPage.length() > 10){
                redirectShort = redirectPage.substring(0, 10);
            }
            return "    WifiBean {" +
                    "       security='" + security + '\'' +
                    "       isRedirect=" + isRedirect +
                    "       wifiAround=" + wifiAround +
                    "       wifiId='" + wifiId + '\'' +
                    "       ssid='" + ssid + '\'' +
                    "       macs=" + macs +
                    "       redirectPage='" + redirectPage + '\'' +
                    "       locations=" + locations +
                    "       connTime=" + connTime +
                    "       internetConnTime=" + internetConnTime +
                    "       mbDown=" + mbDown +
                    "       isInternet=" + isInternet +
                    "       connSpeed=" + connSpeed +
                    "       signStr=" + signStr +
                    "   }";
        }
    }

    public static class Avg{
        float avg;
        float sum;
        int nr;
        Avg(float val) {
            this.avg = val;
            this.sum = val;
            this.nr = 1;
        }
        Avg() {
            avg = 0;
            sum = 0;
            nr = 0;
        }

        void addVal(float val){
            sum += val;
            nr++;
            avg = sum / ((float)nr);
        }

        float getAvg(){
            return avg;
        }

        @Override
        public String toString(){
            return String.valueOf(avg);
        }
    }

    public static class Location{
        float accurancy;
        double lat;
        double lon;
        Location(double lat, double lon, float accurancy){
            this.lat = lat;
            this.lon = lon;
            this.accurancy = accurancy;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "accurancy=" + accurancy +
                    ", lat=" + lat +
                    ", lon=" + lon +
                    '}';
        }
    }

}
