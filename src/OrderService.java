import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderService {

    //========   Field  ===========
    private final AtomicInteger orderSeq = new AtomicInteger(1);
    private final AtomicInteger orderItemSeq = new AtomicInteger(1);

    private final List<Order> orders = new ArrayList<>();
    private final MenuService menuService;
    private final TableService tableService;



    //=========   Constructor   ==========
    public OrderService(MenuService menuService, TableService tableService) {
        this.menuService = menuService;
        this.tableService = tableService;
    }



    // ==========   ORDER CRUD   ============

    // Mở order mới cho một bàn (nếu bàn đã có order OPEN thì trả về order đó)
    public Order openOrderForTable(int tableId) {
        Optional<Order> open = findOpenOrderByTableId(tableId);
        if (open.isPresent()) return open.get();

        // set bàn sang OCCUPIED
        tableService.setOccupied(tableId);

        Order o = new Order(orderSeq.getAndIncrement(), tableId);
        orders.add(o);
        return o;
    }

    public Optional<Order> findOpenOrderByTableId(int tableId) {
        return orders.stream()
                .filter(o -> o.getTableId() == tableId && o.getStatus() == OrderStatus.OPEN)
                .findFirst();
    }

    public Optional<Order> findById(int orderId) {
        return orders.stream().filter(o -> o.getId() == orderId).findFirst();
    }



    // ===========  ITEM thao tac   =============
    public Order addItem(int orderId, int menuItemId, int qty) {
        Order o = findById(orderId).orElseThrow(() -> new NoSuchElementException("Không tìm thấy order"));
        if (o.getStatus() != OrderStatus.OPEN) throw new IllegalStateException("Order đã thanh toán");

        MenuItem mi = menuService.findById(menuItemId)
                .orElseThrow(() -> new NoSuchElementException("Không tìm thấy món id=" + menuItemId));
        // snapshot giá & tên tại thời điểm thêm
        OrderItem oi = new OrderItem(orderItemSeq.getAndIncrement(), mi.getId(), mi.getName(), qty, mi.getPrice());
        o.addItem(oi);
        return o;
    }

    public Order updateItemQuantity(int orderId, int orderItemId, int newQty) {
        Order o = findById(orderId).orElseThrow();
        if (o.getStatus() != OrderStatus.OPEN) throw new IllegalStateException("Order đã thanh toán");

        for (OrderItem item : o.getItems()) {
            if (item.getId() == orderItemId) {
                item.changeQuantity(newQty);
                o.recalcSubtotal();
                return o;
            }
        }
        throw new NoSuchElementException("Không tìm thấy orderItem id=" + orderItemId);
    }


    public boolean removeItem(int orderId, int orderItemId) {
        Order o = findById(orderId).orElseThrow();
        if (o.getStatus() != OrderStatus.OPEN) throw new IllegalStateException("Order đã thanh toán");
        return o.removeItem(orderItemId);
    }





    //========= Check out ============
    public Order checkout(int orderId) {
        Order o = findById(orderId).orElseThrow(() -> new NoSuchElementException("Không tìm thấy order"));
        if (o.getStatus() != OrderStatus.OPEN) throw new IllegalStateException("Order đã thanh toán");
        if (o.getItems().isEmpty()) throw new IllegalStateException("Order trống, không thể checkout");
        o.setPaid();
        // trả bàn về AVAILABLE
        tableService.setAvailable(o.getTableId());
        return o;
    }


    // ============ REPORT ===========
    // Lọc đơn theo ngày (today / thisWeek)
    public List<Order> listPaidByRange(String range) {
        LocalDate today = LocalDate.now();
        return orders.stream()
                .filter(o -> o.getStatus() == OrderStatus.PAID && o.getPaidAt() != null)
                .filter(o -> {
                    LocalDate paidDate = o.getPaidAt().toLocalDate();
                    return switch (range.toLowerCase()) {
                        case "today" -> paidDate.equals(today);
                        case "week"  -> {
                            LocalDate start = today.minusDays(today.getDayOfWeek().getValue() - 1);
                            LocalDate end = start.plusDays(6);
                            yield !paidDate.isBefore(start) && !paidDate.isAfter(end);
                        }
                        default -> true;
                    };
                })
                .sorted(Comparator.comparing(Order::getPaidAt).reversed())
                .collect(Collectors.toList());
    }
}
