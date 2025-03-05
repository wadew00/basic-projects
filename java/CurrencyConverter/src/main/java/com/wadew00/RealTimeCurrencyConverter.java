package com.wadew00;

import java.util.Scanner;

import org.json.JSONObject;

import com.wadew00.utils.ApiClient;

public class RealTimeCurrencyConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
                System.out.println("Welcome to the Real-Time Currency Converter!");
                System.out.print("Enter the amount(type q if you want to exit):  ");
                String sAmount = scanner.next();
                if (sAmount.equals("q")) {
                    scanner.close();
                    System.exit(0);
                }
                double amount = Double.parseDouble(sAmount);
                scanner.nextLine(); 
                System.out.print("Enter the source currency (e.g., USD): ");
                String fromCurrency = scanner.nextLine().toUpperCase();
                System.out.print("Enter the target currency (e.g., EUR): ");
                String toCurrency = scanner.nextLine().toUpperCase();
                try {
                    JSONObject exchangeRates = ApiClient.fetchExchangeRates();
                    if (exchangeRates != null) {
                        JSONObject rates = exchangeRates.getJSONObject("rates");
                        
                        // Check if the currency codes exist in the rates
                        if (!rates.has(fromCurrency) || !rates.has(toCurrency)) {
                            System.out.println("Invalid currency codes entered. Please try again.");
                            return;
                        }
                        
                        double fromRate = rates.getDouble(fromCurrency);
                        double toRate = rates.getDouble(toCurrency);
                        
                        // Convert the amount
                        double convertedAmount = (amount / fromRate) * toRate;
                        System.out.printf("%.2f %s = %.2f %s%n", amount, fromCurrency, convertedAmount, toCurrency);
                    } else {
                        System.out.println("Failed to fetch exchange rates. Please check your API key and internet connection.");
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                }
                }
        
        }
}

