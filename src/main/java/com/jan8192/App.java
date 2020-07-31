package com.jan8192;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

import com.opencsv.CSVReader;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.Connection;
import java.net.Proxy.Type;

/**
 * Hello world!
 *
 */
public class App {



    private static List<String> getUserAgents() throws IOException {
        List<String> userAgents = new ArrayList<>();
        var filePath = "/home/jan/Desktop/repo/langenscheidt-bot/user_agents.csv";
        var reader = new CSVReader(new FileReader(filePath));

        String[] nextLine;
        int lineNum = 0;
        while ((nextLine = reader.readNext()) != null) {
            lineNum++;

            userAgents.add(nextLine[0]);
        }

        return userAgents;

    }

    private static List<Proxy> getProxies() throws IOException, InterruptedException {

        var filePath = "/home/jan/Desktop/repo/langenscheidt-bot/proxies.csv";

        Document doc = Jsoup.connect("https://raw.githubusercontent.com/Agantor/viewerbot/master/Proxies_txt/good_proxy.txt").get();

        // oh well....
        var proxyStrings = doc.body().toString().replace("<body>", "").replace("</body>", "").split(" "); 

        System.out.println("Fetched proxies succesfully");


        List<Proxy> proxies = new ArrayList<>(); 


        for (var proxy : proxyStrings) {

            if(proxy.isBlank())
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

        var latch = new CountDownLatch(proxies.size() -1);

        for (var i = 0; i < proxies.size() -1 ; i++) {

            var proxy = proxies.get(rng.nextInt(proxies.size() -1 )); 
            var userAgent = userAgents.get(rng.nextInt(userAgents.size() -1)); 

            RequestThread requestThread = new RequestThread(proxy, userAgent, latch);
            requestThread.start();
        }

        latch.await(); 

    }
}
