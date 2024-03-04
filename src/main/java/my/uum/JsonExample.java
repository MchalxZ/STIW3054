package my.uum;

import com.google.gson.JsonParseException;

public class JsonExample {
    static String extractAndParseJsonArray(String responseText) throws JsonParseException {
        responseText = responseText.replace('\u2006', ' ');
        String regex = "\\[([^\\]]*)\\]";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(responseText);
        if (matcher.find()) {
            String jsonArrayString = matcher.group(1);
            jsonArrayString = jsonArrayString.replaceAll(",(?=\\S)", "\",\"");
            return jsonArrayString;
        } else {
            throw new JsonParseException("No JSON array found in the response");
        }
}}