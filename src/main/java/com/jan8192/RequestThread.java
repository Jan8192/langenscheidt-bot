package com.jan8192;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.swing.text.Style;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.FormElement;

public class RequestThread extends Thread {

    private CountDownLatch latch;

    private String userAgent;
   
    private Proxy proxy;

    public static Integer connections = 0;   

    public RequestThread(Proxy proxy, String userAgent, CountDownLatch latch) {
        super();
        this.latch = latch;
        this.proxy = proxy;
        this.userAgent = userAgent; 
    }


    @Override
    public void run() {

        Connection.Response resp;
        try {

            resp = Jsoup //
                    .connect("https://www.surveymonkey.com/r/7JZRVLJ?embedded=1") //
                    .proxy(proxy) //
                    .userAgent(userAgent).method(Method.GET).execute();
                    
                    System.out.println("[INFO] Connection to page successfull!");
                    connections++;

                    var respDoc = resp.parse();

                    var potForm = respDoc.select("#patas > main > article > section > form").first();

                    FormElement form = (FormElement) potForm; 

                    var myWord = form.select("#open-ended-single_463803684 > div > 34 63803684");
                    myWord.val("Umwertung aller Werte, das ist meine Formel für einen Akt höchster Selbstbesinnung der Menschheit, der in mir Fleisch und Genie geworden ist"); 
                    

                    // obviously randomizable
                    var ageSelect = form.select("#\34 63803414 > option:nth-child(3)"); 

                    System.out.println("ag3");

                    

                    var acceptButton = form.select("");

                    var filleOutForm = form.submit().cookies(resp.cookies()).post(); 

                    System.out.println("age");


                    System.out.println(filleOutForm);

        } catch (IOException e) {
        } //

       

        latch.countDown();



    }

}