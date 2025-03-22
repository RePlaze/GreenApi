package com.greenapi.tests.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class GreenApiTestUtils {

    public static Map<String, Object> createMessagePayload(String chatId, String message) {
        return new HashMap<String, Object>() {{
            put("chatId", chatId);
            put("message", message);
        }};
    }

    public static Response sendMessage(String baseUrl, String apiToken, Map<String, Object> payload) {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(payload)
            .post(baseUrl + "/sendMessage/" + apiToken);
    }

    public static Response getChatHistory(String baseUrl, String apiToken, String chatId, int count) {
        Map<String, Object> payload = new HashMap<String, Object>() {{
            put("chatId", chatId);
            put("count", count);
        }};
        
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(payload)
            .post(baseUrl + "/getChatHistory/" + apiToken);
    }

    public static Response getAccountState(String baseUrl, String apiToken) {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .get(baseUrl + "/getStateInstance/" + apiToken);
    }

    public static void validateSuccessResponse(Response response) {
        Assert.assertEquals("Статус ответа должен быть 200", 200, response.getStatusCode());
        Assert.assertNotNull("Тело ответа не должно быть пустым", response.getBody());
    }

    public static void validateMessageStructure(Map<String, Object> message) {
        Assert.assertTrue("type is null!", message.containsKey("type"));
        Assert.assertTrue("timestamp is null!", message.containsKey("timestamp"));
        Assert.assertTrue("idMessage is null!", message.containsKey("idMessage"));
        
        validateMessageTimestamp(message);
    }

    public static void validateMessageTimestamp(Map<String, Object> message) {
        long timestamp = Long.parseLong(message.get("timestamp").toString());
        Instant messageTime = Instant.ofEpochSecond(timestamp);
        Assert.assertTrue(
            "Временная метка сообщения должна быть в пределах последних 30 дней",
            messageTime.isAfter(Instant.now().minus(30, ChronoUnit.DAYS))
        );
    }

    public static String extractMessageId(Response response) {
        return response.jsonPath().getString("idMessage");
    }

    public static void logResponseDetails(Response response) {
        System.out.println("=== Response Details ===");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Content Type: " + response.getContentType());
        System.out.println("Body: " + response.getBody().asString());
        System.out.println("=====================");
    }
} 