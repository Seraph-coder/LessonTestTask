package example.bot;

import java.util.Objects;

/**
 * Вопрос
 *
 * @param text Текст вопроса
 * @param correctAnswer Правильный ответ
 */
public record Question(String text, String correctAnswer) {
    public Question(String text, String correctAnswer) {
        this.text = Objects.requireNonNull(text);
        this.correctAnswer = Objects.requireNonNull(correctAnswer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Question question = (Question) o;

        return text.equals(question.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }
}
