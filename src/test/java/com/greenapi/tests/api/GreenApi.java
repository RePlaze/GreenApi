package com.greenapi.tests.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.http.ContentType;
import org.junit.Assert;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GreenApi {
    private final String baseUrl;
    private final String apiToken;

    public GreenApi(String baseUrl, String apiToken) {
        this.baseUrl = baseUrl;
        this.apiToken = apiToken;
        RestAssured.baseURI = baseUrl;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    public Response sendMessage(String chatId, String message) {
        Map<String, Object> payload = new HashMap<>() {{
            put("chatId", chatId);
            put("message", message);
        }};
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(payload)
            .post(baseUrl + "/sendMessage/" + apiToken);
    }

    public Response sendMessageWithoutPreview(String chatId, String url) {
        Map<String, Object> payload = new HashMap<>() {{
            put("chatId", chatId);
            put("message", url);
            put("linkPreview", false);
        }};
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(payload)
            .post(baseUrl + "/sendMessage/" + apiToken);
    }

    public Response getChatHistory(String chatId, int limit) {
        Map<String, Object> payload = new HashMap<>() {{
            put("chatId", chatId);
            put("count", limit);
        }};
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .body(payload)
            .post(baseUrl + "/getChatHistory/" + apiToken);
    }

    public Response getAccountState() {
        return RestAssured.given()
            .contentType(ContentType.JSON)
            .get(baseUrl + "/getStateInstance/" + apiToken);
    }

    public void validateResponse(Response response) {
        Assert.assertEquals("Статус ответа должен быть 200", 200, response.getStatusCode());
        Assert.assertNotNull("Тело ответа не должно быть пустым", response.getBody());
    }

    public void validateMessage(Map<String, Object> message) {
        Assert.assertTrue("Отсутствует поле type", message.containsKey("type"));
        Assert.assertTrue("Отсутствует поле timestamp", message.containsKey("timestamp"));
        Assert.assertTrue("Отсутствует поле idMessage", message.containsKey("idMessage"));

        long timestamp = Long.parseLong(message.get("timestamp").toString());
        Instant messageTime = Instant.ofEpochSecond(timestamp);
        Assert.assertTrue(
                "Временная метка сообщения должна быть в пределах последних 30 дней",
                messageTime.isAfter(Instant.now().minus(30, ChronoUnit.DAYS))
        );
    }
} 