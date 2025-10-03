import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TableService {
    private final List<CafeTable> tables = new ArrayList<>();
    private final AtomicInteger seq = new AtomicInteger(1);

    // seed vài bàn mẫu (tuỳ chọn)
    public TableService() {
        create("Bàn 1");
        create("Bàn 2");
        create("Bàn 3");
    }

    public CafeTable create(String name) {
        CafeTable t = new CafeTable(seq.getAndIncrement(), name);
        tables.add(t);
        return t;
    }

    public List<CafeTable> findAll() {
        return Collections.unmodifiableList(tables);
    }

    public Optional<CafeTable> findById(int id) {
        return tables.stream().filter(t -> t.getId() == id).findFirst();
    }

    public CafeTable update(int id, String newName, String newNote) {
        CafeTable t = findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy bàn id=" + id));
        if (newName != null && !newName.isBlank()) t.setName(newName);
        if (newNote != null) t.setNote(newNote); // cho phép rỗng để xoá note
        return t;
    }

    public boolean delete(int id) {
        return tables.removeIf(t -> t.getId() == id);
    }

    /** Đổi trạng thái: AVAILABLE <-> OCCUPIED (phục vụ demo console) */
    public CafeTable toggleStatus(int id) {
        CafeTable t = findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy bàn"));
        t.setStatus(t.getStatus() == TableStatus.AVAILABLE ? TableStatus.OCCUPIED : TableStatus.AVAILABLE);
        return t;
    }

    /** Sẽ dùng ở phần Order: set trạng thái bàn theo order */
    public void setOccupied(int id) {
        CafeTable t = findById(id).orElseThrow();
        t.setStatus(TableStatus.OCCUPIED);
    }
    public void setAvailable(int id) {
        CafeTable t = findById(id).orElseThrow();
        t.setStatus(TableStatus.AVAILABLE);
    }
}
