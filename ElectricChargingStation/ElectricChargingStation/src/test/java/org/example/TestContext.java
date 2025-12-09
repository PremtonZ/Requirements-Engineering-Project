package org.example;

import java.util.List;

public class TestContext {
    public static final ECSManager network = new ECSManager();

    public static boolean isLoggedIn = false;
    public static String currentUser = null;
    public static boolean canAccessDashboard = false;
    public static boolean isCustomerLoggedIn = false;
    public static String currentCustomer = null;
    public static boolean canAccessCustomerAccount = false;
    public static Site selectedLocation;
    public static Charger selectedCharger;
    public static List<InvoiceItem> balanceHistory;
}

