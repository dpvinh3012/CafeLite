import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private final int id;
    private final int tableId;
    private OrderStatus status = OrderStatus.OPEN;
    private final LocalDateTime openedAt = LocalDateTime.now();
    private LocalDateTime paidAt;
    private final List<OrderItem> items = new ArrayList<>();
    private int subtotal; // tổng VND

    public Order(int id, int tableId) {
        this.id = id;
        this.tableId = tableId;
    }

    public int getId() { return id; }
    public int getTableId() { return tableId; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOpenedAt() { return openedAt; }
    public LocalDateTime getPaidAt() { return paidAt; }
    public List<OrderItem> getItems() { return items; }
    public int getSubtotal() { return subtotal; }

    public void addItem(OrderItem item) {
        items.add(item);
        recalcSubtotal();
    }

    public boolean removeItem(int orderItemId) {
        boolean ok = items.removeIf(oi -> oi.getId() == orderItemId);
        if (ok) recalcSubtotal();
        return ok;
    }

    public void recalcSubtotal() {
        subtotal = items.stream().mapToInt(OrderItem::getLineTotal).sum();
    }

    public void setPaid() {
        this.status = OrderStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "ORDER#"+id+" | table="+tableId+" | "+status+" | subtotal="+subtotal+" VND";
    }
}
