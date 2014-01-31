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

/**
 * @author giok57
 * @email gioelemeoni@gmail.com
 * @modifiedBy giok57
 * <p/>
 * Date: 30/01/14
 * Time: 12:57
 */
public class UploadResponse {

    private String message;
    private int code;
    private int newMeasureEvery;
    private int newUploadEvery;
    private int newReadyAfter;

    public UploadResponse(String message, int code, int newMeasureEvery, int newUploadEvery, int newReadyAfter) {
        this.message = message;
        this.code = code;
        this.newMeasureEvery = newMeasureEvery;
        this.newUploadEvery = newUploadEvery;
        this.newReadyAfter = newReadyAfter;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNewMeasureEvery() {
        return newMeasureEvery;
    }

    public void setNewMeasureEvery(int newMeasureEvery) {
        this.newMeasureEvery = newMeasureEvery;
    }

    public int getNewUploadEvery() {
        return newUploadEvery;
    }

    public void setNewUploadEvery(int newUploadEvery) {
        this.newUploadEvery = newUploadEvery;
    }

    public int getNewReadyAfter() {
        return newReadyAfter;
    }

    public void setNewReadyAfter(int newReadyAfter) {
        this.newReadyAfter = newReadyAfter;
    }
}
