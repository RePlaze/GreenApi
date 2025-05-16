require('dotenv').config();
const express = require('express');
const GreenAPIService = require('./services/GreenAPIService');

const app = express();
app.use(express.json());

// Инициализация сервиса GreenAPI
const greenAPI = new GreenAPIService(
  process.env.ID_INSTANCE,
  process.env.API_TOKEN_INSTANCE,
);

// Маршрут для отправки сообщений
app.post('/send-message', async (req, res) => {
  try {
    const { chatId, message } = req.body;
    
    if (!chatId || !message) {
      return res.status(400).json({
        success: false,
        error: 'chatId и message являются обязательными параметрами',
      });
    }

    const result = await greenAPI.sendMessage(chatId, message);
    return res.json({ success: true, data: result });
  } catch (error) {
    console.error('Error in /send-message:', error);
    return res.status(500).json({
      success: false,
      error: 'Внутренняя ошибка сервера',
    });
  }
});

// Маршрут для получения уведомлений
app.get('/notifications', async (req, res) => {
  try {
    const notification = await greenAPI.receiveNotification();
    return res.json({ success: true, data: notification });
  } catch (error) {
    console.error('Error in /notifications:', error);
    return res.status(500).json({
      success: false,
      error: 'Внутренняя ошибка сервера',
    });
  }
});

// Маршрут для удаления уведомления
app.delete('/notifications/:receiptId', async (req, res) => {
  try {
    const { receiptId } = req.params;
    const result = await greenAPI.deleteNotification(receiptId);
    return res.json({ success: true, data: result });
  } catch (error) {
    console.error('Error in DELETE /notifications:', error);
    return res.status(500).json({
      success: false,
      error: 'Внутренняя ошибка сервера',
    });
  }
});

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`Server is running on port ${PORT}`);
}); 