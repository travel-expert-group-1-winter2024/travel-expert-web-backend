package org.example.travelexpertwebbackend.utils;

import jakarta.servlet.http.HttpServletRequest;

public class RestUtil {
    public static String buildBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    public static String buildPhotoUrl(String photoPath, HttpServletRequest request) {
        return buildBaseUrl(request) + "/uploads/" + photoPath;
    }
}
