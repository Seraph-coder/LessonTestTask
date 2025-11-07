package example.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Пользователь
 */
public class User {

    /**
     * id чата пользователя
     */
    private final Long chatId;

    /**
     * Состояние пользователя
     */
    private State state = State.INIT;

    /**
     * Настройка напоминания
     */
    private Notification notification = null;

    /**
     * Индекс текущего вопроса
     */
    private int currentQuestionIndex = 0;

    /**
     * Вопросы, на которые дан неправильный ответ
     */
    private final List<Question> wrongAnswerQuestions = new ArrayList<>();

    /**
     * Номер текущего вопроса из списка вопросов, на которые дан неправильный ответ
     */
    private int currentWrongAnswerQuestionIndex = 0;

    /**
     * Конструктор - создание нового пользователя по id чата
     *
     * @param chatId - id чата нового пользователя
     */
    public User(Long chatId) {
        this.chatId = chatId;
    }

    public Long getChatId() {
        return chatId;
    }

    /**
     * Получить состояние в котором находится пользователь
     */
    public State getState() {
        return state;
    }

    /**
     * Установить состояние
     */
    public void setState(State state) {
        this.state = Objects.requireNonNull(state);
        switch (state) {
            case INIT, TEST, REPEAT -> {
                notification = null;
                currentQuestionIndex = 0;
                currentWrongAnswerQuestionIndex = 0;
            }
        }
    }

    /**
     * Настроенное напоминание
     */
    public Optional<Notification> getNotification() {
        return Optional.ofNullable(notification);
    }

    /**
     * Создать напоминание
     *
     * @param text текст напоминания
     */
    @SuppressWarnings("UnusedReturnValue")
    public Notification createNotification(String text) {
        this.notification = new Notification(text, chatId);
        return notification;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    /**
     * Переключится на следующий вопрос
     */
    public int nextQuestionIndex() {
        return ++currentQuestionIndex;
    }

    public List<Question> getWrongAnswerQuestions() {
        return wrongAnswerQuestions;
    }

    /**
     * Добавить вопрос, на который дан неправильный ответ
     */
    public void addWrongAnswerQuestion(Question question) {
        if (wrongAnswerQuestions.contains(question)) {
            return;
        }
        wrongAnswerQuestions.add(question);
    }

    /**
     * Удалить вопрос из списка вопросов, на которые дан неправильный ответ
     */
    public void removeWrongAnswerQuestion(Question question) {
        if (wrongAnswerQuestions.remove(question)) {
            currentWrongAnswerQuestionIndex--;
        }
    }

    /**
     * Переключится на следующий вопрос, на который дан неправильный ответ
     */
    public Optional<Question> nextWrongAnswerQuestion() {
        currentWrongAnswerQuestionIndex++;
        return getCurrentWrongAnswerQuestion();
    }

    /**
     * Получить текущий вопрос из списка вопросов, на который дан неправильный ответ
     */
    public Optional<Question> getCurrentWrongAnswerQuestion() {
        if (currentWrongAnswerQuestionIndex >= wrongAnswerQuestions.size()) {
            return Optional.empty();
        }
        return Optional.of(wrongAnswerQuestions.get(currentWrongAnswerQuestionIndex));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User)o;
        return chatId.equals(user.chatId);
    }

    @Override
    public int hashCode() {
        return chatId.hashCode();
    }
}