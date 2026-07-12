package com.assetflow.util;

public final class ApiPaths {
    public static final String API_VERSION = "/api/v1";
    
    public static final String AUTH = API_VERSION + "/auth";
    public static final String USERS = API_VERSION + "/users";
    public static final String DEPARTMENTS = API_VERSION + "/departments";
    public static final String ASSET_CATEGORIES = API_VERSION + "/asset-categories";
    public static final String ASSETS = API_VERSION + "/assets";
    public static final String ALLOCATIONS = API_VERSION + "/allocations";
    public static final String TRANSFERS = API_VERSION + "/transfers";
    public static final String RETURNS = API_VERSION + "/returns";
    public static final String RESOURCES = API_VERSION + "/resources";
    public static final String BOOKINGS = API_VERSION + "/bookings";
    public static final String MAINTENANCE = API_VERSION + "/maintenance";
    public static final String AUDITS = API_VERSION + "/audits";
    
    private ApiPaths() {
        throw new UnsupportedOperationException("Utility class");
    }
}
