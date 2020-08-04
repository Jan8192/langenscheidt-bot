package com.jan8192;

import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.opencsv.CSVReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class AppUtils {

    public List<String> getUserAgents() throws IOException {
        List<String> userAgents = new ArrayList<>();
        var filePath = "/home/jan/Desktop/repo/langenscheidt-bot/user_agents.csv";
        var reader = new CSVReader(new FileReader(filePath));

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            userAgents.add(nextLine[0]);
        }

        return userAgents;

    }

    public List<Proxy> getProxies() throws IOException, InterruptedException {

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


}