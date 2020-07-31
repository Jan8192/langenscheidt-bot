package com.jan8192;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import javax.swing.text.Document;
import javax.xml.crypto.Data;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.FormElement;

public class RequestThread extends Thread {

    private CountDownLatch latch;

    private String userAgent;

    private Proxy proxy;

    public static Integer connections = 0;

    public static Random rng = new Random();

    public RequestThread(Proxy proxy, String userAgent, CountDownLatch latch) {
        super();
        this.latch = latch;
        this.proxy = proxy;
        this.userAgent = userAgent;
    }

    @Override
    public void run() {

        try {

            Connection.Response resp = Jsoup // https://www.langenscheidt.com/jugendwort-des-jahres
                    .connect(App.URL).proxy(proxy) //
                    .userAgent(userAgent).method(Method.GET).execute();

            // System.out.println("[INFO] Connection to page with proxy: " + proxy.toString() + " and useragent: "
            //         + userAgent + " successfull!");
            connections++;

            String[] ages = { "3067519627", "3067519628"

            };

            var doc = Jsoup.connect(App.URL).proxy(proxy).userAgent(userAgent).method(Method.POST)
                    .data("463803414", "3067519627").data("463803684", App.MY_WORD_MORE_LIKE_SENTENCE)
                    .data("483089934[]", "3189794655")
                    .data("survey_data",
                            "4FoTYerhlqpl9PSGmuD5IQ4KOrG6PtkT0QQFIBIijog69_2F9RdsUcotipXcUgpxzrnRb3gFwT9Fo41Ud9JJuINDjyNvf6For4P8H3xuiHjJ8K7JjPAy_2Ff_2Bi1Nhwj_2BEpAWWWRPRUFIPv1Wt7Bhy0cxRNqqcOPzw5QmQ_2F_2FMFPnqAFa1aC8rVox9Q54qTb1IFCrgyzBW0iSMDPWFVV_2FWcUJ6VNTrRW3puAhlK7kQsY9NRriyf14gKeUFyRQE5zYeS1uOeKZsE5_2B4Mt7X_2FqzCsCUNSUnBmHujJpYXu7SdBu_2FFWXMea1qIdBbQIjnbMkvYn1Fg_2FpmsKUGAytKrYlt8ozSyfxh8DcWGAb2KxoCHu5swwGC5pNqJ7cXxVfKWwnxCe2TjNJItL516N6J25dlY6gISL3NmHvtKhymJlf6B7N5KwcReVHmKpLLje_2B45aNyEiCS_2FLDSpQ9O9Yjfd3bMv5IJTnSnzQ2GaJA5Pg57MYDgV90sbxJWVNa9ey_2BTNl_2B896XN48r_2FOoip3NY7p0mIIDfBk3lp8GGJa2NVl5Ki_2FNswiiA7n3ZhQYEiv_2BFyxzf5dw1aj")
                    .data("response_quality_data", "").data("is_previous", "false").post();

            System.out.println("[INFO] POST request to " + App.URL + " with proxy: "+ proxy.toString() + " and useragent: "
            + userAgent + " was apparently successfull! Counting down latch...");

        } catch (IOException e) {
        } catch (NoSuchElementException ne) {

        }

        latch.countDown();

    }

}