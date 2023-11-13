package com.mostafawahied.mastermindwebapp.util;

import javax.servlet.http.HttpServletRequest;

public class Utility {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    public static String mapColorToEmoji(String color) {
        return switch (color) {
            case "red" -> "🟥";
            case "blue" -> "🟦";
            case "green" -> "🟩";
            case "yellow" -> "🟨";
            case "orange" -> "🟧";
            case "purple" -> "🟪";
            case "brown" -> "🟫";
            default -> "⬛";
        };
    }

    public static String mapNumberToEmoji(String number) {
        return switch (number) {
            case "0" -> "0️⃣";
            case "1" -> "1️⃣";
            case "2" -> "2️⃣";
            case "3" -> "3️⃣";
            case "4" -> "4️⃣";
            case "5" -> "5️⃣";
            case "6" -> "6️⃣";
            case "7" -> "7️⃣";
            default -> "";
        };

    }
}
