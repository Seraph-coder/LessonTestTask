package example.container;

/**
 * Билдер для элемента контейнера.
 *
 * <p>В реальных проектах этот билдер должен находиться в `src/test/java` и использоваться
 * только в тестах. Здесь он вынесён в основной код из\-за package\-private конструктора класса
 * {@link Item}, чтобы упростить создание экземпляров в тестах.

 *
 * @author Seraph-coder
 * @since 11.11.2025
 */
public class ItemBuilder {
    private final long num;

    /**
     * Конструктор билдера
     */
    public ItemBuilder(long num) {
        this.num = num;
    }

    /**
     * Построить элемент контейнера
     */
    public Item build() {
        return new Item(num);
    }
}
