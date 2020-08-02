package com.jan8192;

import java.io.IOException;

import java.net.Proxy;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * Hello world!
 *
 */
public class App {

    public static final String MY_WORD_MORE_LIKE_SENTENCE = "Bildungsdefizienteresbreitbandproletariat";

    public static final String URL = "https://www.surveymonkey.com/r/7JZRVLJ?embedded=1";

    public static void main(String... args) throws IOException, InterruptedException {

        var utils = new AppUtils().get();

        List<Proxy> proxies = (List<Proxy>) utils.get(0);
        List<String> userAgents = (List<String>) utils.get(1);

        Random rng = new Random();

        var latch = new CountDownLatch(proxies.size() - 1);

        System.out.println("[INFO] Proxy amount:" + Integer.toString(proxies.size() - 1));
        System.out.println("[INFO] Useragents: " + Integer.toString(userAgents.size() - 1) + "\n");

        for (var i = 0; i < proxies.size() - 1; i++) {

            var proxy = proxies.get(rng.nextInt(proxies.size() - 1));
            var userAgent = userAgents.get(rng.nextInt(userAgents.size() - 1));

            try {
                RequestThread requestThread = new RequestThread(proxy, userAgent, latch, rng);
                requestThread.start();

            } catch (Exception e) {
            }

        }

        latch.await();
        System.out.println("[INFO] Sent " + RequestThread.connections + " requests. " + "Succesfull ones: "
                + RequestThread.successfullRequests);
    }
}
