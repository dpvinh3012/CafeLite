public class CafeTable {
    private final int id;
    private String name;          // VD: "Bàn 1" hoặc "T1"
    private TableStatus status;   // AVAILABLE/OCCUPIED
    private String note;          // ghi chú ngắn (tuỳ chọn)

    public CafeTable(int id, String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên bàn không được trống");
        }
        this.id = id;
        this.name = name;
        this.status = TableStatus.AVAILABLE; // mặc định là trống
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public TableStatus getStatus() { return status; }
    public String getNote() { return note; }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên bàn không được trống");
        }
        this.name = name;
    }
    public void setStatus(TableStatus status) { this.status = status; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        String s = "#" + id + " | " + name + " | " + status;
        if (note != null && !note.isBlank()) s += " | note: " + note;
        return s;
    }
}
