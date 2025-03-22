import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.List;
import com.greenapi.tests.config.GreenApiConfig;
import com.greenapi.tests.api.GreenApi;

import static junit.framework.TestCase.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Тесты для проверки функциональности Green API
 * @author Nazenov.R
 */
public class GreenApiTest {
    private GreenApiConfig config;
    private GreenApi api;

    @Before
    public void setup() throws Exception {
        config = GreenApiConfig.load();
        api = new GreenApi(config.getBaseUrl(), config.getApiToken());
    }

    //отправка сообщения с различными параметрами
    @Test
    public void testSendMessage() {
        Response response = api.sendMessage(config.getChatId(), config.getTestMessage());
        api.validateResponse(response);
        String messageId = response.jsonPath().getString("idMessage");
        assertNotNull("ID сообщения не должен быть null", messageId);

        response = api.sendMessageWithoutPreview(config.getChatId(), "https://green-api.com");
        api.validateResponse(response);
    }

    //получение истории чата с различными параметрами
    @Test
    public void testGetChatHistory() {
        Response response = api.getChatHistory(config.getChatId(), config.getChatHistoryLimit());
        api.validateResponse(response);
        List<Map<String, Object>> messages = response.jsonPath().getList("$");
        
        assertFalse("История чата не должна быть пустой", messages.isEmpty());
        api.validateMessage(messages.get(0));
    }

    //проверка состояния авторизации в личном кабинете
    @Test
    public void testAccountState() {
        Response response = api.getAccountState();
        api.validateResponse(response);
        
        String stateInstance = response.jsonPath().getString("stateInstance");
        assertEquals("Клиент должен быть авторизован", "authorized", stateInstance);
    }
} 