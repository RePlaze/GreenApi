module.exports = {
  testEnvironment: 'node',
  testMatch: ['**/__tests__/**/*.test.js'],
  setupFiles: ['dotenv/config'],
  testTimeout: 30000, // 30 секунд для интеграционных тестов
}; 