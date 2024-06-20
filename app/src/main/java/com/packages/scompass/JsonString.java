package com.packages.scompass;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonString {


//    String jsonString = "{\"key\": \"value\"}";
//    JSONObject jsonObject = JsonUtils.parseJsonString(jsonString);
//if (jsonObject != null) {
//        // Successfully parsed JSON string
//        System.out.println(jsonObject.toString());
//    } else {
//        // Failed to parse JSON string
//        System.out.println("Failed to parse JSON string.");
//    }
    public static JSONObject parseJsonString(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


//    JSONObject jsonObject = new JSONObject();
//try {
//        jsonObject.put("key", "value");
//    } catch (JSONException e) {
//        e.printStackTrace();
//    }
//
//    String jsonString = JsonUtils.convertJsonToString(jsonObject);
//if (jsonString != null) {
//        // Successfully converted JSONObject to JSON string
//        System.out.println(jsonString);
//    } else {
//        // Failed to convert JSONObject to JSON string
//        System.out.println("Failed to convert JSONObject to JSON string.");
//    }



    public static String convertJsonToString(JSONObject jsonObject) {
        try {
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
