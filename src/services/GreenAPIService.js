const axios = require('axios');

class GreenAPIService {
  constructor(idInstance, apiTokenInstance) {
    this.idInstance = idInstance;
    this.apiTokenInstance = apiTokenInstance;
    this.baseUrl = `https://api.green-api.com/waInstance${this.idInstance}`;
  }

  /**
   * Отправляет сообщение в WhatsApp
   * @param {string} chatId - ID чата
   * @param {string} message - Сообщение для отправки
   * @returns {Promise<Object>} Ответ от сервера
   */
  async sendMessage(chatId, message) {
    const response = await axios.post(
      `${this.baseUrl}/SendMessage/${this.apiTokenInstance}`,
      { chatId, message }
    );
    return response.data;
  }

  /**
   * Проверяет статус авторизации
   * @returns {Promise<Object>} Статус авторизации
   */
  async getStateInstance() {
    const response = await axios.get(
      `${this.baseUrl}/getStateInstance/${this.apiTokenInstance}`
    );
    return response.data;
  }

  /**
   * Получает историю сообщений чата
   * @param {string} chatId - ID чата
   * @param {number} count - Количество сообщений
   * @returns {Promise<Object>} История сообщений
   */
  async getChatHistory(chatId, count) {
    const response = await axios.post(
      `${this.baseUrl}/GetChatHistory/${this.apiTokenInstance}`,
      { chatId, count }
    );
    return response.data;
  }

  /**
   * Получает информацию о чате
   * @param {string} chatId - ID чата
   * @returns {Promise<Object>} Информация о чате
   */
  async getContactInfo(chatId) {
    const response = await axios.post(
      `${this.baseUrl}/GetContactInfo/${this.apiTokenInstance}`,
      { chatId }
    );
    return response.data;
  }

  /**
   * Получает последнее входящее уведомление
   * @returns {Promise<Object|null>} Уведомление или null
   */
  async receiveNotification() {
    try {
      const endpoint = `${this.baseUrl}/ReceiveNotification/${this.apiTokenInstance}`;
      const response = await axios.get(endpoint);
      return response.data;
    } catch (error) {
      console.error('Error receiving notification:', error.message);
      throw error;
    }
  }

  /**
   * Удаляет уведомление по его ID
   * @param {string} receiptId - ID уведомления
   * @returns {Promise<Object>} Результат удаления
   */
  async deleteNotification(receiptId) {
    try {
      const endpoint = `${this.baseUrl}/DeleteNotification/${this.apiTokenInstance}/${receiptId}`;
      const response = await axios.delete(endpoint);
      return response.data;
    } catch (error) {
      console.error('Error deleting notification:', error.message);
      throw error;
    }
  }
}

module.exports = GreenAPIService; 