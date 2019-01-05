package com.eran1205.portal.controller;

import com.eran1205.portal.model.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wunderlist")
public class WunderListController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    private static String client_id = "62f057c2552ca7fc8f53";
    private static String client_secret = "bf406f62c3eac4585863f8f09edfc3d51349d72a1df1df30affb371b51c8";
    private static String access_token = "7c6db36cd90e9631c6b8974ab99b34bdcb116142af75d970f73b70799a27";


    // Get User Details
    @GetMapping("/userdetails")
    public ResponseEntity<?> getUserDetails() throws IOException {

        URL url = new URL("https://a.wunderlist.com/api/v1/user");
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.addRequestProperty("X-Access-Token", access_token);
        conn.addRequestProperty("X-Client-ID", client_id);

        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String inputLine = br.lines().collect(Collectors.joining());
        br.close();

        return new ResponseEntity<>(inputLine, HttpStatus.OK);
    }

    @GetMapping("/lists")
    public ResponseEntity<?> getActiveUser() throws IOException {

        URL url = new URL("https://a.wunderlist.com/api/v1/lists");
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
        conn.addRequestProperty("X-Access-Token", access_token);
        conn.addRequestProperty("X-Client-ID", client_id);

        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        String inputLine = br.lines().collect(Collectors.joining());
        br.close();

        return new ResponseEntity<>(inputLine, HttpStatus.OK);
    }
}
