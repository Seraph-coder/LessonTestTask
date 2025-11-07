package example.bot;

import java.util.List;
import java.util.Optional;

import static example.bot.Constants.*;

/**
 * Верхнеуровневая логика бота
 * <p>Дисклеймер: <b>Этот код не является образцом написания ООП кода,
 * он написан студентами</b></p>
 */
public class BotLogic {
    private final Bot bot;

    public BotLogic(Bot bot) {
        this.bot = bot;
    }

    private final List<Question> questions = List.of(
            new Question("Вычислите степень: 10^2", "100"),
            new Question("Сколько будет 2 + 2 * 2", "6"));

    /**
     * Функция для обработки сообщений пользователя
     *
     * @param user    - текущий пользователь
     * @param command - сообщение пользователя
     */
    public final void processCommand(User user, String command) {
        switch (command) {
            case COMMAND_START -> {
                bot.sendMessage(user.getChatId(), "Привет!");
                user.setState(State.INIT);
            }
            case COMMAND_HELP -> bot.sendMessage(user.getChatId(), HELP_INFO);
            case COMMAND_TEST -> {
                user.setState(State.TEST);
                bot.sendMessage(user.getChatId(),
                        questions.get(user.getCurrentQuestionIndex()).text());
            }
            case COMMAND_STOP -> processStop(user);
            case COMMAND_REPEAT -> user.getCurrentWrongAnswerQuestion().ifPresentOrElse(question -> {
                        user.setState(State.REPEAT);
                        bot.sendMessage(user.getChatId(),
                                question.text());
                    },
                    () -> bot.sendMessage(user.getChatId(), "Нет вопросов для повторения"));
            case COMMAND_NOTIFY -> {
                user.setState(State.SET_NOTIFY_TEXT);
                bot.sendMessage(user.getChatId(), "Введите текст напоминания");
            }
            default -> processNonCommand(user, command);
        }
    }

    /**
     * Обработать сообщение, которое не является командой
     */
    private void processNonCommand(User user, String message) {
        switch (user.getState()) {
            case TEST -> checkTestAnswer(user, message);
            case REPEAT -> checkRepeatTestAnswer(user, message);
            case SET_NOTIFY_TEXT -> {
                user.createNotification(message);
                bot.sendMessage(user.getChatId(), "Через сколько секунд напомнить?");
                user.setState(State.SET_NOTIFY_DELAY);
            }
            case SET_NOTIFY_DELAY -> {
                Notification notification = user.getNotification().orElseThrow();
                try {
                    long delay = Long.parseLong(message);
                    notification.schedule(delay, bot);
                    bot.sendMessage(user.getChatId(), "Напоминание установлено");
                    user.setState(State.INIT);
                } catch (NumberFormatException e) {
                    bot.sendMessage(user.getChatId(), "Пожалуйста, введите целое число");
                }
            }
            default -> bot.sendMessage(user.getChatId(),
                    "Такой команды пока не существует, или Вы допустили ошибку в написании. " +
                    "Воспользуйтесь командой /help, чтобы прочитать инструкцию.");
        }
    }

    /**
     * Обработка команды остановки тестирования
     */
    private void processStop(User user) {
        if (user.getState() != State.TEST
            && user.getState() != State.REPEAT) {
            bot.sendMessage(user.getChatId(),
                    "Вы не начинали тестирование. " +
                    "Воспользуйтесь командой /help, чтобы прочитать инструкцию.");
        } else {
            bot.sendMessage(user.getChatId(), "Тест завершен");
            user.setState(State.INIT);
        }
    }

    /**
     * Проверка ответов пользователя на тест
     *
     * @param user       пользователь
     * @param userAnswer ответ от пользователя
     */
    private void checkTestAnswer(User user, String userAnswer) {
        Question question = questions.get(user.getCurrentQuestionIndex());
        int nextQuestionIndex = user.nextQuestionIndex();
        Question nextQuestion = nextQuestionIndex < questions.size()
                ? questions.get(nextQuestionIndex)
                : null;
        checkAnswer(user, userAnswer, question, nextQuestion);
    }

    /**
     * Проверка ответов пользователя на тест из вопросов, на которые ранее был дан неправильный ответ
     */
    private void checkRepeatTestAnswer(User user, String userAnswer) {
        Question question = user.getCurrentWrongAnswerQuestion().orElseThrow();
        Optional<Question> nextWrongAnswerQuestion = user.nextWrongAnswerQuestion();
        checkAnswer(user, userAnswer, question, nextWrongAnswerQuestion.orElse(null));
    }

    /**
     * Проверить ответ
     */
    private void checkAnswer(User user, String userAnswer, Question question, Question nextQuestion) {
        if (userAnswer.equalsIgnoreCase(question.correctAnswer())) {
            bot.sendMessage(user.getChatId(), "Правильный ответ!");
            user.removeWrongAnswerQuestion(question);
        } else {
            bot.sendMessage(user.getChatId(),
                    "Вы ошиблись, верный ответ: " + question.correctAnswer());
            user.addWrongAnswerQuestion(question);
        }
        if (nextQuestion == null) {
            processStop(user);
            return;
        }
        bot.sendMessage(user.getChatId(), nextQuestion.text());
    }
}
