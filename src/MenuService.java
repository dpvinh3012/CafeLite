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

    // ===== NEW =====
    public Optional<MenuItem> findById(int id) {
        return items.stream().filter(it -> it.getId() == id).findFirst();
    }

    /** Update theo kiểu "tùy chọn": truyền null/empty để giữ nguyên giá trị cũ */
    public MenuItem update(int id, String name, Integer price, Category category, Boolean active) {
        MenuItem it = findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy món id=" + id));
        if (name != null && !name.isBlank()) it.setName(name);
        if (price != null) {
            if (price < 0) throw new IllegalArgumentException("Giá không âm");
            it.setPrice(price);
        }
        if (category != null) it.setCategory(category);
        if (active != null) it.setActive(active);
        return it;
    }

    /** Trả về true nếu xoá được */
    public boolean delete(int id) {
        return items.removeIf(it -> it.getId() == id);
    }
}
