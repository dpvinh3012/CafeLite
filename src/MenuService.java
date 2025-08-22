import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MenuService {
    private final List<MenuItem> items = new ArrayList<>();
    private final AtomicInteger seq = new AtomicInteger(1);

    public MenuItem create(String name, int price, Category category) {
        MenuItem mi = new MenuItem(seq.getAndIncrement(), name, price, category);
        items.add(mi);
        return mi;
    }

    public List<MenuItem> findAll() {
        return Collections.unmodifiableList(items);
    }

    // (sau sẽ thêm: findById, update, delete, filter theo category/active)
}
