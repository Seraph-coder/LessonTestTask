package example.bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для логики бота {@link BotLogic}
 *
 * @author Seraph-coder
 * @since 11.11.2025
 */
public class BotLogicTest {
    private final FakeBot fakeBot = new FakeBot();
    private final BotLogic botLogic = new BotLogic(fakeBot);

    private User user;

    /**
     * Перед каждым тестом создаём нового пользователя, и очищаем сообщения фейкового бота
     * Не пересоздаем FakeBot, так как он хранит историю сообщений, которую можно просто очистить,
     * в то время как BotLogic не хранит состояние между вызовами processCommand
     */
    @BeforeEach
    void setup() {
        user = new User(1L);
        fakeBot.clearMessages();
    }


    /**
     * Тест обработки несуществующей команды
     * <br>
     *
     * Входные данные: команда "/invalid"
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот отправляет сообщение об ошибке</li>
     * </ul>
     */
    @Test
    void invalidCommand() {
        botLogic.processCommand(user, "/invalid");
        Assertions.assertEquals("Такой команды пока не существует, " +
                "или Вы допустили ошибку в написании. " +
                "Воспользуйтесь командой /help, чтобы прочитать инструкцию.", fakeBot.getLastMessage());
    }

    /**
     * Тест команды напоминания с корректной задержкой
     * <br>
     * Входные данные:
     * <ul>
     *     <li>Команда "/notify"</li>
     *     <li>Текст напоминания "Напомни мне сделать домашку"</li>
     *     <li>Задержка "1" (1 секунда)</li>
     * </ul>
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот отправляет сообщение с напоминанием через 1 секунду</li>
     * </ul>
     */
    @Test
    void notifyWithDelay() throws  InterruptedException {
        botLogic.processCommand(user, "/notify");
        Assertions.assertEquals("Введите текст напоминания", fakeBot.getLastMessage());
        botLogic.processCommand(user, "Напомни мне сделать домашку");
        Assertions.assertEquals("Через сколько секунд напомнить?", fakeBot.getLastMessage());
        botLogic.processCommand(user, "1");
        Assertions.assertEquals("Напоминание установлено", fakeBot.getLastMessage());
        Assertions.assertEquals(3, fakeBot.getSize());
        Thread.sleep(1010);
        Assertions.assertEquals("Сработало напоминание: " +
                "'Напомни мне сделать домашку'", fakeBot.getLastMessage());
    }

    /**
     * Тест команды напоминания с некорректной задержкой
     * <br>
     * Входные данные:
     * <ul>
     *     <li>Команда "/notify"</li>
     *     <li>Текст напоминания "Напомни мне сделать домашку"</li>
     *     <li>Задержка "-5" (отрицательное число)</li>
     *     <li>Задержка "abc" (не число)</li>
     * </ul>
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот выбрасывает IllegalArgumentException при отрицательной задержке</li>
     *     <li>Бот отправляет сообщение с просьбой ввести целое число при нечисловой задержке</li>
     * </ul>
     */
    @Test
    void notifyWithInvalidDelay() {
        botLogic.processCommand(user, "/notify");
        botLogic.processCommand(user, "Напомни мне сделать домашку");
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () ->
            botLogic.processCommand(user, "-5")
        );
        Assertions.assertEquals("Negative delay.", exception.getMessage());
        botLogic.processCommand(user, "abc");
        Assertions.assertEquals("Пожалуйста, введите целое число", fakeBot.getLastMessage());
    }

    /**
     * Тест команды тестирования с правильными ответами
     * <br>
     * Входные данные:
     * <ul>
     *     <li>Команда "/test"</li>
     *     <li>Ответ "100" на вопрос "Вычислите степень: 10^2"</li>
     *     <li>Ответ "6" на вопрос "Сколько будет 2 + 2 * 2"</li>
     * </ul>
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот подтверждает правильность каждого ответа</li>
     * </ul>
     */
    @Test
    void testCommandWithCorrectAnswers() {
        botLogic.processCommand(user, "/test");
        Assertions.assertEquals("Вычислите степень: 10^2", fakeBot.getMessageAt(0));
        botLogic.processCommand(user, "100");
        Assertions.assertEquals("Правильный ответ!", fakeBot.getMessageAt(1));
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", fakeBot.getMessageAt(2));
        botLogic.processCommand(user, "6");
        Assertions.assertEquals("Правильный ответ!", fakeBot.getMessageAt(3));
    }

    /**
     * Тест команды тестирования с неправильными ответами
     * <br>
     * Входные данные:
     * <ul>
     *     <li>Команда "/test"</li>
     *     <li>Ответ "0" на вопрос "Вычислите степень: 10^2"</li>
     *     <li>Ответ "0" на вопрос "Сколько будет 2 + 2 * 2"</li>
     * </ul>
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот сообщает о неправильности каждого ответа и предоставляет верный ответ</li>
     * </ul>
     */
    @Test
    void testCommandWithWrongAnswers() {
        botLogic.processCommand(user, "/test");
        Assertions.assertEquals("Вычислите степень: 10^2", fakeBot.getMessageAt(0));
        botLogic.processCommand(user, "0");
        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", fakeBot.getMessageAt(1));
        Assertions.assertEquals("Сколько будет 2 + 2 * 2", fakeBot.getMessageAt(2));
        botLogic.processCommand(user, "0");
        Assertions.assertEquals("Вы ошиблись, верный ответ: 6", fakeBot.getMessageAt(3));
    }

    /**
     * Тест команды повторения без неправильных ответов
     * <br>
     * Входные данные: команда "/repeat"
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот сообщает, что нет вопросов для повторения</li>
     *     <li>Состояние пользователя остаётся INIT</li>
     * </ul>
     */
    @Test
    void testRepeatWithoutWrongAnswers() {
        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", fakeBot.getLastMessage());
        Assertions.assertEquals(State.INIT, user.getState());
    }

    /**
     * Тест команды повторения после правильных ответов на тест
     * <br>
     * Входные данные:
     * <ul>
     *     <li>Команда "/test"</li>
     *     <li>Ответ "100" на вопрос "Вычислите степень: 10^2"</li>
     *     <li>Ответ "6" на вопрос "Сколько будет 2 + 2 * 2"</li>
     *     <li>Команда "/repeat"</li>
     * </ul>
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот сообщает, что нет вопросов для повторения</li>
     *     <li>Состояние пользователя остаётся INIT</li>
     * </ul>
     */
    @Test
    void TestRepeatWithRightAnswersOnly() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "100");
        botLogic.processCommand(user, "6");
        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", fakeBot.getLastMessage());
        Assertions.assertEquals(State.INIT, user.getState());
    }

    /**
     * Тест команды повторения с неправильным ответом, затем правильным ответом
     * <br>
     * Входные данные:
     * <ul>
     *     <li>Команда "/test"</li>
     *     <li>Ответ "0" на вопрос "Вычислите степень: 10^2"</li>
     *     <li>Команда "/repeat"</li>
     *     <li>Ответ "100" на повторный вопрос "Вычислите степень: 10^2"</li>
     * </ul>
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот подтверждает правильность повторного ответа</li>
     *     <li>После правильного ответа на повторный вопрос бот сообщает, что нет вопросов для повторения</li>
     * </ul>
     */
    @Test
    void shouldWrongAnswerRemovesAfterCorrectRepeatAnswer() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "0");
        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Вычислите степень: 10^2", fakeBot.getMessageAt(3));
        botLogic.processCommand(user, "100");
        Assertions.assertEquals("Правильный ответ!", fakeBot.getMessageAt(4));
        Assertions.assertEquals("Тест завершен", fakeBot.getMessageAt(5));

        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Нет вопросов для повторения", fakeBot.getMessageAt(6));
    }

    /**
     * Тест команды повторения с неправильным ответом 2 раза
     * <br>
     * Входные данные:
     * <ul>
     *     <li>Команда "/test"</li>
     *     <li>Ответ "0" на вопрос "Вычислите степень: 10^2"</li>
     *     <li>Команда "/repeat"</li>
     *     <li>Ответ "0" на повторный вопрос "Вычислите степень: 10^2"</li>
     * </ul>
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот сообщает о неправильности повторного ответа и предоставляет верный ответ</li>
     *     <li>После неправильного ответа на повторный вопрос бот снова задаёт этот вопрос при следующей команде "/repeat"</li>
     * </ul>
     */
    @Test
    void shouldWrongAnswerStaysAfterWrongRepeatAnswer() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "0");
        botLogic.processCommand(user, "/repeat");
        Assertions.assertEquals("Вычислите степень: 10^2", fakeBot.getMessageAt(3));
        botLogic.processCommand(user, "0");
        Assertions.assertEquals("Вы ошиблись, верный ответ: 100", fakeBot.getMessageAt(4));
        Assertions.assertEquals("Тест завершен", fakeBot.getMessageAt(5));
    }
    /**
     * Тест команды остановки теста во время теста
     * <br>
     * Входные данные:
     * <ul>
     *     <li>Команда "/test"</li>
     *     <li>Команда "/stop"</li>
     * </ul>
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот подтверждает завершение теста</li>
     * </ul>
     */
    @Test
    void stopCommandDuringTest() {
        botLogic.processCommand(user, "/test");
        botLogic.processCommand(user, "/stop");
        Assertions.assertEquals("Тест завершен", fakeBot.getLastMessage());
    }

    /**
     * Тест команды остановки теста вне теста
     * <br>
     * Входные данные: команда "/stop"
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Бот сообщает, что тест не был начат</li>
     * </ul>
     */
    @Test
    void stopCommandOutsideTest() {
        botLogic.processCommand(user, "/stop");
        Assertions.assertEquals("Вы не начинали тестирование. Воспользуйтесь " +
                "командой /help, чтобы прочитать инструкцию.", fakeBot.getLastMessage());
    }
}
