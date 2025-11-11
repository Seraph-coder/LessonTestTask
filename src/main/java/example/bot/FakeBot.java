package example.bot;

import java.util.ArrayList;
import java.util.List;

/**
 * Фейковый бот для тестов
 *
 * @author Seraph-coder
 * @since 11.11.2025
 */
public class FakeBot implements Bot{
    private final List<String> messages;

    public FakeBot() {
        messages = new ArrayList<>();
    }

    @Override
    public void sendMessage(Long chatId, String message) {
        messages.add(message);
    }

    /**
     * Получить сообщение по индексу
     */
    public String getMessageAt(int index) {
        return messages.get(index);
    }

    /**
     * Получить последнее отправленное сообщение
     */
    public String getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.getLast();
    }

    /**
     * Очистить все отправленные сообщения
     */
    public void clearMessages() {
        messages.clear();
    }
}
