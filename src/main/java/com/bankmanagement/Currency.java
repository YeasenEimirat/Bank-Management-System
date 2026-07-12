package com.bankmanagement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Currency {
    private String country;
    private String currencyCode;
    private String currencyName;
    private double rate;

    public Currency(String country, String currencyCode, String currencyName, double rate) {
        this.country = country;
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.rate = rate;
    }

    public static Currency emptyCurrency() {
        return new Currency("", "", "", 0);
    }

    public boolean isEmpty() {
        return currencyCode.isEmpty();
    }

    private static Currency fromLine(String line) {
        String[] parts = line.split(java.util.regex.Pattern.quote(Constants.SEPARATOR), -1);
        if (parts.length < 4) return emptyCurrency();
        return new Currency(parts[0], parts[1], parts[2], Double.parseDouble(parts[3].trim()));
    }

    private String toLine() {
        return country + Constants.SEPARATOR + currencyCode + Constants.SEPARATOR +
                currencyName + Constants.SEPARATOR + Util.fileDouble(rate);
    }

    public static List<Currency> loadAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        for (String line : FileStorage.readAllLines(Constants.CURRENCIES_FILE)) {
            if (!line.isBlank()) currencies.add(fromLine(line));
        }
        return currencies;
    }

    private static void saveAllCurrencies(List<Currency> currencies) {
        List<String> lines = currencies.stream().map(Currency::toLine).collect(Collectors.toList());
        FileStorage.writeAllLines(Constants.CURRENCIES_FILE, lines);
    }

    public static Currency findByCode(String code) {
        for (Currency currency : loadAllCurrencies()) {
            if (currency.currencyCode.equalsIgnoreCase(code)) return currency;
        }
        return emptyCurrency();
    }

    public static Currency findByCountry(String country) {
        for (Currency currency : loadAllCurrencies()) {
            if (currency.country.equalsIgnoreCase(country)) return currency;
        }
        return emptyCurrency();
    }

    public boolean updateRate(double newRate) {
        if (isEmpty() || newRate <= 0) return false;
        List<Currency> currencies = loadAllCurrencies();
        boolean updated = false;
        for (Currency currency : currencies) {
            if (currency.currencyCode.equalsIgnoreCase(this.currencyCode)
                    && currency.country.equalsIgnoreCase(this.country)) {
                currency.rate = newRate;
                this.rate = newRate;
                updated = true;
                break;
            }
        }
        saveAllCurrencies(currencies);
        return updated;
    }

    public double convertToUSD(double amount) {
        return amount / rate;
    }

    public double convertFromUSD(double amountInUSD) {
        return amountInUSD * rate;
    }

    public double convertToOtherCurrency(double amount, Currency targetCurrency) {
        double amountInUSD = convertToUSD(amount);
        return targetCurrency.convertFromUSD(amountInUSD);
    }

    public String getCountry() { return country; }
    public String getCurrencyCode() { return currencyCode; }
    public String getCurrencyName() { return currencyName; }
    public double getRate() { return rate; }
}
