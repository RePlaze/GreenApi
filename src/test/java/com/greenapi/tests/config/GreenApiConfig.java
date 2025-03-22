package com.greenapi.tests.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GreenApiConfig {
    private final Properties properties;
    private final String baseUrl;
    private final String instanceId;
    private final String apiToken;
    private final String chatId;
    private final String testMessage;
    private final int chatHistoryLimit;

    private GreenApiConfig(Properties props) {
        properties = props;
        instanceId = props.getProperty("instance.id");
        apiToken = props.getProperty("api.token");
        chatId = props.getProperty("chat.id");
        testMessage = props.getProperty("test.message");
        chatHistoryLimit = Integer.parseInt(props.getProperty("chat.history.limit"));
        baseUrl = String.format(props.getProperty("base.url"), instanceId);
    }

    public static GreenApiConfig load() throws IOException {
        Properties props = new Properties();
        try (InputStream input = GreenApiConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) throw new IOException("config.properties not found");
            props.load(input);
            return new GreenApiConfig(props);
        }
    }

    public String getBaseUrl() { return baseUrl; }
    public String getApiToken() { return apiToken; }
    public String getChatId() { return chatId; }
    public String getTestMessage() { return testMessage; }
    public int getChatHistoryLimit() { return chatHistoryLimit; }
} 