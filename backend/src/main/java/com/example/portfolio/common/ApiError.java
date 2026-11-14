package com.example.portfolio.common;

import java.util.Map;

public record ApiError(int status, String message, Map<String, String> fieldErrors) {

    public static ApiError of(int status, String message) {
        return new ApiError(status, message, Map.of());
    }
}
