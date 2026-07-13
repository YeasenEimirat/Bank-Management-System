package com.bankmanagement;

/** Dependency-free regression test for the JavaFX currency search behavior. */
public final class CurrencySearchTest {
    private CurrencySearchTest() {}

    public static void main(String[] args) {
        if (args.length != 1) throw new IllegalArgumentException("Expected data directory");
        System.setProperty("bank.data.dir", args[0]);

        requireCurrency("Algeria", "DZD");
        requireCurrency("algeria", "DZD");
        requireCurrency("  Algeria  ", "DZD");
        requireCurrency("DZD", "DZD");
        requireCurrency("dzd", "DZD");

        if (!Currency.find("Unknown Country").isEmpty()) {
            throw new IllegalStateException("Unknown search should return an empty currency");
        }
        System.out.println("Currency search test passed.");
    }

    private static void requireCurrency(String searchText, String expectedCode) {
        Currency currency = Currency.find(searchText);
        if (currency.isEmpty() || !currency.getCurrencyCode().equals(expectedCode)) {
            throw new IllegalStateException("Search failed for: " + searchText);
        }
    }
}
