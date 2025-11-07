package example.container;

/**
 * Элемент контейнера
 */
public class Item {
    /**
     * Номер
     */
    private final long num;

    Item(long num) {
        this.num = num;
    }

    public long getNum() {
        return num;
    }
}
