package com.cherednik;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {

    private static String URL = "https://api.privatbank.ua/p24api/exchange_rates?json&date=";
    private static final String DATE_FORMAT = "dd.mm.yyyy";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Enter date: " + DATE_FORMAT);
        String date = scanner.nextLine();
        for (int i = 0; i < 100; i++) {
            if (!isThisDateValid(date)) {
                System.out.println("You enter invalid date format. \n Please, try again.");
                date = scanner.nextLine();
            } else {
                System.out.println("Please expect a response");
                httpRequest(date);
                //okHttp(date);
                System.out.println(date);
                break;
            }
        }
    }

    private static void httpRequest(String day) {
        String request = URL + day;
        Response response = HttpUtil.sendRequest(request);
        Gson gson = new Gson();
        Currency currency = gson.fromJson(response.body, Currency.class);
        if (response.body.contains("USD")) {
            if (response.getException() == null) {
                System.out.println("ResponseCode: " + response.responseCode);
                print(currency);
            } else {
                System.out.println("Request failed: " + response.getException());
            }
        } else {
            System.out.println("No data for this date");
        }
    }

    private static void okHttp(String date) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(URL + date)
                    .build();
            okhttp3.Response response = client.newCall(request).execute();
            Gson gson = new Gson();
            Currency currency = gson.fromJson(response.body().string(), Currency.class);
            if (currency != null && response.isSuccessful()) {
                print(currency);
            } else {
                System.out.println("Request failed: " + response.message() + " Response code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isThisDateValid(String dateToValidate) {

        if (dateToValidate == null) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setLenient(false);
        try {
            Date date = sdf.parse(dateToValidate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void print(Currency car) {
        for (Currency currency : car.exchangeRate) {
            if (currency.currency != null && currency.currency.equals("USD")) {
                System.out.println(currency);
            }
        }
    }

}
