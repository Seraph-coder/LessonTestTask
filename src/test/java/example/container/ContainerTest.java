package example.container;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тесты для контейнера {@link Container}
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
     * Тест методов add и size
     * <br>
     * Входные данные: два элемента с номерами 1 и 2
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>После добавления первого элемента, get(0) возвращает его, size() возвращает 1</li>
     *     <li>После добавления второго элемента, get(1) возвращает его, size() возвращает 2</li>
     * </ul>
     */
    @Test
    void testAddMethods() {
        Item item1 = new Item(1);
        Item item2 = new Item(2);
        container.add(item1);
        Assertions.assertEquals(item1, container.get(0));
        Assertions.assertEquals(1, container.size());
        container.add(item2);
        Assertions.assertEquals(item2, container.get(1));
        Assertions.assertEquals(2, container.size());
    }

    /**
     * Тест методов remove и contains
     * <br>
     * Входные данные: два элемента с номерами 1 и 2
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>После добавления обоих элементов, contains возвращает true для каждого</li>
     *     <li>После удаления первого элемента, contains возвращает false для первого и true для второго</li>
     *     <li>Размер контейнера после удаления первого элемента равен 1</li>
     * </ul>
     */
    @Test
    void testRemoveAndContainsMethods() {
        Item item1 = new Item(1);
        Item item2 = new Item(2);
        container.add(item1);
        container.add(item2);
        Assertions.assertTrue(container.contains(item1));
        Assertions.assertTrue(container.contains(item2));
        container.remove(item1);
        Assertions.assertFalse(container.contains(item1));
        Assertions.assertTrue(container.contains(item2));
        Assertions.assertEquals(1, container.size());
    }

    /**
     * Тест удаления несуществующего элемента
     * <br>
     * Входные данные: элемент с номером 1 в контейнере, попытка удалить элемент с номером 2
     * <br>
     * Ожидаемый результат:
     * <ul>
     *     <li>Метод remove возвращает false</li>
     *     <li>Размер контейнера остается равным 1</li>
     * </ul>
     */
    @Test
    void removeNonExistentItem() {
        Item item1 = new Item(1);
        container.add(item1);
        Assertions.assertFalse(container.remove(new Item(2)));
        Assertions.assertEquals(1, container.size());
    }
}
