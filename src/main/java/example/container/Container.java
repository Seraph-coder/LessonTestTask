package example.container;

import java.util.ArrayList;
import java.util.List;

/**
 * Контейнер
 */
public class Container {
    /**
     * Элементы контейнера
     */
    private final List<Item> items = new ArrayList<>();

    /**
     * Добавить элемент
     */
    public boolean add(Item item) {
        return items.add(item);
    }

    /**
     * Удалить элемент
     */
    public boolean remove(Item item) {
        return items.remove(item);
    }

    /**
     * Получить элемент по индексу
     */
    public Item get(int index) {
        return items.get(index);
    }

    /**
     * Размер контейнера
     */
    public int size() {
        return items.size();
    }

    /**
     * Содержится ли элемент в контейнере
     */
    public boolean contains(Item item) {
        return items.contains(item);
    }
}
