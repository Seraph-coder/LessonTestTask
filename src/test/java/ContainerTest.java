import example.container.Container;
import example.container.Item;
import example.container.ItemBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для контейнера {@link Container}
 * <br>
 * Тестирование через Assertions.assertAll, чтобы не останавливать тест при первом провале,
 * а сразу увидеть все ошибки
 *
 * @author Seraph-coder
 * @since 11.11.2025
 */
public class ContainerTest {
    private Container container;

    /**
     * Создание нового контейнера перед каждым тестом,
     * тк контейнер хранит состояние
     */
    @BeforeEach
     void setUp() {
        container = new Container();
    }

    /**
     * Тест методов add, size и get
     * <br>
     * Входные данные: два элемента с номерами 1 и 2
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>size() возвращает 2</li>
     *     <li>get(0) возвращает первый элемент</li>
     *     <li>get(1) возвращает второй элемент</li>
     * </ul>
     */
    @Test
    void addGetMethods() {
        Item item1 = new ItemBuilder(1).build();
        Item item2 = new ItemBuilder(2).build();
        container.add(item1);
        container.add(item2);
        Assertions.assertEquals(item1, container.get(0));
        Assertions.assertEquals(item2, container.get(1));
    }

    @Test
    void removeContainsSizeMethods() {
        Item item1 = new ItemBuilder(1).build();
        Item item2 = new ItemBuilder(2).build();
        container.add(item1);
        container.add(item2);
        Assertions.assertAll(
                () -> Assertions.assertTrue(container.contains(item1)),
                () -> Assertions.assertTrue(container.contains(item2)),
                () -> Assertions.assertEquals(2, container.size())
        );
        container.remove(item1);
        Assertions.assertAll(
                () -> Assertions.assertFalse(container.contains(item1)),
                () -> Assertions.assertTrue(container.contains(item2)),
                () -> Assertions.assertEquals(1, container.size())
        );
    }
}
