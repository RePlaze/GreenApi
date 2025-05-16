const axios = require('axios');
const GreenAPIService = require('../GreenAPIService');
require('dotenv').config();

jest.mock('axios');

describe('GreenAPI Integration Tests', () => {
  let greenAPI;
  const chatId = process.env.CHAT_ID;
  const testMessage = 'Test message';
  const historyLimit = 100;

  beforeAll(() => {
    greenAPI = new GreenAPIService(
      process.env.ID_INSTANCE,
      process.env.API_TOKEN_INSTANCE
    );
  });

  test('проверка авторизации', async () => {
    const response = await greenAPI.getStateInstance();
    expect(response.stateInstance).toBe('authorized');
  });

  test('отправка сообщения', async () => {
    const response = await greenAPI.sendMessage(chatId, testMessage);
    expect(response.idMessage).toBeDefined();
  });

  test('получение истории чата', async () => {
    const response = await greenAPI.getChatHistory(chatId, historyLimit);
    expect(Array.isArray(response)).toBe(true);
    expect(response.length).toBeLessThanOrEqual(historyLimit);
  });

  test('получение информации о контакте', async () => {
    const response = await greenAPI.getContactInfo(chatId);
    expect(response.existsWhatsapp).toBe(true);
  });
});

describe('GreenAPIService', () => {
  let greenAPI;
  const mockIdInstance = 'test123';
  const mockApiToken = 'token123';

  beforeEach(() => {
    greenAPI = new GreenAPIService(mockIdInstance, mockApiToken);
    jest.clearAllMocks();
  });

  describe('sendMessage', () => {
    it('должен успешно отправлять сообщение', async () => {
      const mockResponse = { data: { idMessage: '123' } };
      axios.post.mockResolvedValueOnce(mockResponse);

      const chatId = '79001234567@c.us';
      const message = 'Test message';

      const result = await greenAPI.sendMessage(chatId, message);

      expect(axios.post).toHaveBeenCalledWith(
        `https://api.green-api.com/waInstance${mockIdInstance}/SendMessage/${mockApiToken}`,
        { chatId, message }
      );
      expect(result).toEqual(mockResponse.data);
    });

    it('должен обрабатывать ошибки при отправке сообщения', async () => {
      const errorMessage = 'Network error';
      axios.post.mockRejectedValueOnce(new Error(errorMessage));

      await expect(
        greenAPI.sendMessage('79001234567@c.us', 'Test message')
      ).rejects.toThrow(errorMessage);
    });
  });

  describe('receiveNotification', () => {
    it('должен успешно получать уведомления', async () => {
      const mockNotification = { data: { receiptId: '123', body: {} } };
      axios.get.mockResolvedValueOnce(mockNotification);

      const result = await greenAPI.receiveNotification();

      expect(axios.get).toHaveBeenCalledWith(
        `https://api.green-api.com/waInstance${mockIdInstance}/ReceiveNotification/${mockApiToken}`
      );
      expect(result).toEqual(mockNotification.data);
    });
  });

  describe('deleteNotification', () => {
    it('должен успешно удалять уведомление', async () => {
      const mockResponse = { data: { result: true } };
      axios.delete.mockResolvedValueOnce(mockResponse);

      const receiptId = '123';
      const result = await greenAPI.deleteNotification(receiptId);

      expect(axios.delete).toHaveBeenCalledWith(
        `https://api.green-api.com/waInstance${mockIdInstance}/DeleteNotification/${mockApiToken}/${receiptId}`
      );
      expect(result).toEqual(mockResponse.data);
    });
  });
}); 