public class OrderItem {
    private final int id;
    private final int menuItemId;   // tham chiếu món
    private final String menuName;  // snapshot tên món tại thời điểm thêm
    private int quantity;           // số lượng
    private final int unitPrice;    // VND - snapshot giá tại thời điểm thêm
    private int lineTotal;          // = quantity * unitPrice

    public OrderItem(int id, int menuItemId, String menuName, int quantity, int unitPrice) {
        if (quantity <= 0) throw new IllegalArgumentException("Số lượng phải > 0");
        this.id = id;
        this.menuItemId = menuItemId;
        this.menuName = menuName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        recalc();
    }

    public void changeQuantity(int newQty) {
        if (newQty <= 0) throw new IllegalArgumentException("Số lượng phải > 0");
        this.quantity = newQty;
        recalc();
    }

    public void recalc() {
        this.lineTotal = quantity * unitPrice;
    }

    public int getId() { return id; }
    public int getMenuItemId() { return menuItemId; }
    public String getMenuName() { return menuName; }
    public int getQuantity() { return quantity; }
    public int getUnitPrice() { return unitPrice; }
    public int getLineTotal() { return lineTotal; }

    @Override
    public String toString() {
        return "#"+id+" | "+menuName+" x"+quantity+" | "+unitPrice+" VND => "+lineTotal+" VND";
    }
}
