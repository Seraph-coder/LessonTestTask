package example.note;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для NoteLogic {@link NoteLogic}
 *
 * @author Seraph-coder
 * @since 11.11.2025
 */
public class NoteLogicTest {
    private NoteLogic noteLogic;

    /**
     * Создание экземпляра NoteLogic перед каждым тестом, чтобы история заметок не
     * сохранялась между тестами
     */
    @BeforeEach
    void setUp() {
        noteLogic = new NoteLogic();
    }

    /**
     * Тестирование команд добавления и просмотра заметок
     * <br>
     * Входные данные:
     * <ul>
     *     <li>/add "Купить молоко"</li>
     *     <li>/add "Купить хлеб"</li>
     *     <li>/notes</li>
     * </ul>
     * Ожидаемый результат:
     * <ul>
     *     <li>Note "Купить молоко" added!</li>
     *     <li>Note "Купить хлеб" added!</li>
     *     <li>Your notes:
     *     <br>
     *     1) Купить молоко
     *     <br>
     *     2) Купить хлеб</li>
     * </ul>
     */
    @Test
    void testAddAndNotesCommand() {
        String addResponse1 = noteLogic.handleMessage("/add Купить молоко");
        String addResponse2 = noteLogic.handleMessage("/add Купить хлеб");
        String notesResponse = noteLogic.handleMessage("/notes");
        Assertions.assertEquals(
                "Note \"Купить молоко\" added!", addResponse1);
        Assertions.assertEquals(
                "Note \"Купить хлеб\" added!", addResponse2);
        Assertions.assertEquals("""
                        Your notes:
                        1) Купить молоко
                        2) Купить хлеб""", notesResponse);
    }

    /**
     * Тестирование команды редактирования заметки
     * <br>
     * Подготовка:
     * <br>
     * <ul>
     *     <li>/add "Купить молоко"</li>
     * </ul>
     * Входные данные:
     * <ul>
     *     <li>/edit 1 Купить хлеб</li>
     *     <li>/notes</li>
     * </ul>
     * Ожидаемый результат:
     * <ul>
     *     <li>Note 1 edited!</li>
     *     <li>Your notes:
     *     <br>
     *     1) Купить хлеб</li>
     * </ul>
     */
    @Test
    void testEditCommand() {
        noteLogic.handleMessage("/add Купить молоко");
        String editResponse = noteLogic.handleMessage("/edit 1 Купить хлеб");
        String notesResponse = noteLogic.handleMessage("/notes");
        Assertions.assertEquals("Note 1 edited!", editResponse);
        Assertions.assertEquals("""
                        Your notes:
                        1) Купить хлеб""", notesResponse);
    }

    /**
     * Тестирование команды удаления заметки
     * <br>
     * Подготовка:
     * <br>
     * <ul>
     *     <li>/add "Купить молоко"</li>
     * </ul>
     * Входные данные:
     * <ul>
     *     <li>/del 1</li>
     *     <li>/notes</li>
     * </ul>
     * Ожидаемый результат:
     * <ul>
     *     <li>Note "Купить молоко" deleted!</li>
     *     <li>Your notes:</li>
     * </ul>
     */
    @Test
    void testDeleteCommand() {
        noteLogic.handleMessage("/add Купить молоко");
        noteLogic.handleMessage("/add Купить хлеб");
        String delResponse = noteLogic.handleMessage("/del 1");
        String notesResponse = noteLogic.handleMessage("/notes");
        Assertions.assertEquals(
                "Note \"Купить молоко\" deleted!", delResponse);
        Assertions.assertEquals("Your notes:", notesResponse);
    }
}
