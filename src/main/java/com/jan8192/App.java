package com.jan8192;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import com.opencsv.CSVReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Hello world!
 *
 */
public class App {

    public static final String MY_WORD_MORE_LIKE_SENTENCE = "Bildungsdefizienteresbreitbandproletariat";

    public static final String URL = "https://www.surveymonkey.com/r/7JZRVLJ?embedded=1";

    private static List<String> getUserAgents() throws IOException {
        List<String> userAgents = new ArrayList<>();
        var filePath = "/home/jan/Desktop/repo/langenscheidt-bot/user_agents.csv";
        var reader = new CSVReader(new FileReader(filePath));

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            userAgents.add(nextLine[0]);
        }

        return userAgents;

    }

    private static List<Proxy> getProxies() throws IOException, InterruptedException {

        var doc1 = Jsoup
                .connect("https://raw.githubusercontent.com/Agantor/viewerbot/master/Proxies_txt/good_proxy.txt").get();

        var doc2 = Jsoup.connect("https://raw.githubusercontent.com/clarketm/proxy-list/master/proxy-list-raw.txt")
                .get();

        List<Document> docs = new ArrayList<>();
            docs.add(doc1);
            docs.add(doc2);

        List<String> proxyStrings = new ArrayList<>();

        for (var document : docs) {
            // oh well....
            Collection<String> proxies = Arrays
                    .asList(document.body().toString().replace("<body>", "").replace("</body>", "").split(" "));
            proxyStrings.addAll(proxies);
        }

        System.out.println("[INFO] Fetched proxies succesfully");

        List<Proxy> proxies = new ArrayList<>();

        for (var proxy : proxyStrings) {

            if (proxy.isBlank())
                continue;

            var prox = proxy.split(":");

            var proxyNew = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(prox[0], Integer.parseInt(prox[1])));

            proxies.add(proxyNew);
        }

        return proxies;

    }

    public static void main(String... args) throws IOException, InterruptedException {

        var userAgents = getUserAgents();
        Random rng = new Random();

        var proxies = getProxies();

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
