package com.jan8192;

import java.io.IOException;

import java.net.Proxy;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;

public class RequestThread extends Thread {

    private CountDownLatch latch;

    private String userAgent;

    private Proxy proxy;

    public static Integer connections = 0;

    public static Integer successfullRequests = 0;

    public Random rng;

    public RequestThread(Proxy proxy, String userAgent, CountDownLatch latch, Random rng) {
        super();
        this.latch = latch;
        this.proxy = proxy;
        this.userAgent = userAgent;
        this.rng = rng;
    }

    @Override
    public void run() {

        try {

            var connection = Jsoup // https://www.langenscheidt.com/jugendwort-des-jahres
                    .connect(App.URL).proxy(proxy) //
                    .userAgent(userAgent).method(Method.GET);

            var sleepTime = rng.nextInt(5000);

            System.out.println("[INFO] Established connection with proxy: " + proxy.toString() + " and useragent: "
                    + userAgent + " successfully! Sleeping thread for " + sleepTime + " milliseconds...\n");
            connections++;

            super.sleep(sleepTime);

            String[] ages = { 
                "3067519627", 
                "3067519628", 
                "3067519629",
                // who considers 30+ youth? probably a Freudian slip. anyway...
                "3067519630", };

            var ageIndex = rng.nextInt(ages.length - 1);

            var doc = connection
                    .data("463803414", ages[ageIndex])
                    .data("463803684", App.MY_WORD_MORE_LIKE_SENTENCE)
                    .data("483089934[]", "3189794655")
                    .data("survey_data",
                            "4FoTYerhlqpl9PSGmuD5IQ4KOrG6PtkT0QQFIBIijog69_2F9RdsUcotipXcUgpxzrnRb3gFwT9Fo41Ud9JJuINDjyNvf6For4P8H3xuiHjJ8K7JjPAy_2Ff_2Bi1Nhwj_2BEpAWWWRPRUFIPv1Wt7Bhy0cxRNqqcOPzw5QmQ_2F_2FMFPnqAFa1aC8rVox9Q54qTb1IFCrgyzBW0iSMDPWFVV_2FWcUJ6VNTrRW3puAhlK7kQsY9NRriyf14gKeUFyRQE5zYeS1uOeKZsE5_2B4Mt7X_2FqzCsCUNSUnBmHujJpYXu7SdBu_2FFWXMea1qIdBbQIjnbMkvYn1Fg_2FpmsKUGAytKrYlt8ozSyfxh8DcWGAb2KxoCHu5swwGC5pNqJ7cXxVfKWwnxCe2TjNJItL516N6J25dlY6gISL3NmHvtKhymJlf6B7N5KwcReVHmKpLLje_2B45aNyEiCS_2FLDSpQ9O9Yjfd3bMv5IJTnSnzQ2GaJA5Pg57MYDgV90sbxJWVNa9ey_2BTNl_2B896XN48r_2FOoip3NY7p0mIIDfBk3lp8GGJa2NVl5Ki_2FNswiiA7n3ZhQYEiv_2BFyxzf5dw1aj")
                    .data("response_quality_data", "")
                    .data("is_previous", "false")
                    .post();

            if (doc != null && doc.body().toString().contains("Dein Jugendwort ist jetzt bei uns aufgenommen")) {
                successfullRequests++;
                System.out.println("[INFO] POST request to " + App.URL + " with proxy: " + proxy.toString()
                        + " and useragent: " + userAgent + " was apparently successfull! Counting down latch...\n");
            }

        } catch (IOException e) {
        } catch (NoSuchElementException ne) {

        } catch (InterruptedException e) {
        }

        latch.countDown();

    }

}