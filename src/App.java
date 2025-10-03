import java.util.NoSuchElementException;
import java.util.Scanner;

public class App {



    // =========== CAC SERVICE===========
    private final MenuService menuService = new MenuService();
    private final TableService tableService = new TableService();
    private final OrderService orderService = new OrderService(menuService, tableService);
    private final Scanner sc = new Scanner(System.in);



    // =============Ham Chinh==================
    public void run() {
        while (true) {
            System.out.println("\n== CAFE LITE ==");
            System.out.println("1) Thêm món");
            System.out.println("2) Danh sách món");
            System.out.println("3) Sửa món");
            System.out.println("4) Xoá món");
            System.out.println("5) Thêm bàn");              // NEW
            System.out.println("6) Danh sách bàn");          // NEW
            System.out.println("7) Sửa bàn");                // NEW
            System.out.println("8) Xoá bàn");                // NEW
            System.out.println("9) Đổi trạng thái bàn");     // NEW (AVAILABLE/OCCUPIED)
            System.out.println("10) Tạo/Mở order cho bàn"); // NEW
            System.out.println("11) Thêm món vào order");   // NEW
            System.out.println("12) Xem order của bàn");    // NEW
            System.out.println("13) Checkout order của bàn");// NEW
            System.out.println("14) Danh sách orders (today/week)"); // optional
            System.out.println("15) Đổi số lượng món trong order");
            System.out.println("16) Xoá món trong order");
            System.out.println("0) Thoát");
            System.out.print("Chọn: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addMenuItem();
                    case "2" -> listMenu();
                    case "3" -> editMenuItem();
                    case "4" -> deleteMenuItem();
                    case "5" -> addTable();           // NEW
                    case "6" -> listTables();         // NEW
                    case "7" -> editTable();          // NEW
                    case "8" -> deleteTable();        // NEW
                    case "9" -> toggleTableStatus();  // NEW
                    case "10" -> openOrderForTable();
                    case "11" -> addItemToOrder();
                    case "12" -> viewOrderOfTable();
                    case "13" -> checkoutOrderOfTable();
                    case "14" -> listOrdersByRange(); // optional
                    case "15" -> updateItemInOrder();
                    case "16" -> removeItemFromOrder();
                    case "0" -> { System.out.println("Bye!"); return; }
                    default -> System.out.println("Không hợp lệ.");
                }
            } catch (Exception ex) {
                System.out.println("Lỗi: " + ex.getMessage());
            }
        }
    }



    //===========QUAN LY MON================
    private void addMenuItem() {
        System.out.print("Tên món: ");
        String name = sc.nextLine().trim();
        int price = readInt("Giá (VND, số nguyên): ");
        Category cat = readCategory("Loại (COFFEE/TEA/OTHER): ");

        MenuItem mi = menuService.create(name, price, cat);
        System.out.println("Đã thêm: " + mi);
    }

    private void listMenu() {
        var all = menuService.findAll();
        if (all.isEmpty()) {
            System.out.println("(Chưa có món)");
        } else {
            all.forEach(System.out::println);
        }
    }

    // ===== NEW =====
    private void editMenuItem() {
        listMenu();
        int id = readInt("Nhập ID món cần sửa: ");
        var current = menuService.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy món"));

        System.out.println("Đang sửa: " + current);
        System.out.println("Bỏ trống để giữ nguyên giá trị hiện tại.");

        System.out.print("Tên mới: ");
        String name = sc.nextLine().trim();
        if (name.isBlank()) name = null;

        Integer price = null;
        System.out.print("Giá mới (số nguyên): ");
        String priceStr = sc.nextLine().trim();
        if (!priceStr.isBlank()) price = Integer.parseInt(priceStr);

        Category cat = null;
        System.out.print("Loại mới (COFFEE/TEA/OTHER): ");
        String catStr = sc.nextLine().trim();
        if (!catStr.isBlank()) cat = Category.valueOf(catStr.toUpperCase());

        Boolean active = null;
        System.out.print("Kích hoạt? (true/false): ");
        String actStr = sc.nextLine().trim();
        if (!actStr.isBlank()) active = Boolean.parseBoolean(actStr);

        var updated = menuService.update(id, name, price, cat, active);
        System.out.println("Đã cập nhật: " + updated);
    }

    private void deleteMenuItem() {
        listMenu();
        int id = readInt("Nhập ID món cần xoá: ");
        boolean ok = menuService.delete(id);
        System.out.println(ok ? "Đã xoá." : "Không tìm thấy món để xoá.");
    }





    //==================QUAN LY BAN====================
    private void addTable() {
        System.out.print("Tên bàn: ");
        String name = sc.nextLine().trim();
        var t = tableService.create(name);
        System.out.println("Đã thêm: " + t);
    }

    private void listTables() {
        var all = tableService.findAll();
        if (all.isEmpty()) System.out.println("(Chưa có bàn)");
        else all.forEach(System.out::println);
    }

    private void editTable() {
        listTables();
        int id = readInt("Nhập ID bàn cần sửa: ");
        System.out.println("Bỏ trống tên/note để giữ nguyên.");
        System.out.print("Tên mới: ");
        String newName = sc.nextLine();
        if (newName.isBlank()) newName = null;

        System.out.print("Note mới (có thể để trống để xoá): ");
        String newNote = sc.nextLine(); // cho phép rỗng

        var updated = tableService.update(id, newName, newNote);
        System.out.println("Đã cập nhật: " + updated);
    }

    private void deleteTable() {
        listTables();
        int id = readInt("Nhập ID bàn cần xoá: ");
        boolean ok = tableService.delete(id);
        System.out.println(ok ? "Đã xoá." : "Không tìm thấy bàn để xoá.");
    }

    private void toggleTableStatus() {
        listTables();
        int id = readInt("Nhập ID bàn cần đổi trạng thái: ");
        var t = tableService.toggleStatus(id);
        System.out.println("Trạng thái mới: " + t);
    }




    // ======       ORDER     ===============================================
    private void openOrderForTable() {
        listTables();
        int tableId = readInt("Nhập ID bàn để mở order: ");
        var o = orderService.openOrderForTable(tableId);
        System.out.println("Order hiện tại: " + o);
    }

    private void addItemToOrder() {
        listTables();
        int tableId = readInt("Nhập ID bàn có order OPEN: ");
        var o = orderService.findOpenOrderByTableId(tableId)
                .orElseThrow(() -> new NoSuchElementException("Bàn chưa có order OPEN (hãy chọn 10 để mở)"));

        listMenu();
        int menuId = readInt("Nhập ID món: ");
        int qty = readInt("Số lượng: ");
        o = orderService.addItem(o.getId(), menuId, qty);

        System.out.println("Đã thêm. Order: " + o);
        o.getItems().forEach(System.out::println);
        System.out.println("Subtotal: " + o.getSubtotal() + " VND");
    }

    private void updateItemInOrder() {
        listTables();
        int tableId = readInt("Nhập ID bàn: ");
        var o = orderService.findOpenOrderByTableId(tableId)
                .orElseThrow(() -> new NoSuchElementException("Không có order OPEN cho bàn này"));

        o.getItems().forEach(System.out::println);
        int itemId = readInt("Nhập ID món trong order: ");
        int newQty = readInt("Số lượng mới: ");

        o = orderService.updateItemQuantity(o.getId(), itemId, newQty);
        System.out.println("Đã cập nhật. Subtotal: " + o.getSubtotal() + " VND");
    }

    private void removeItemFromOrder() {
        listTables();
        int tableId = readInt("Nhập ID bàn: ");
        var o = orderService.findOpenOrderByTableId(tableId)
                .orElseThrow(() -> new NoSuchElementException("Không có order OPEN cho bàn này"));

        o.getItems().forEach(System.out::println);
        int itemId = readInt("Nhập ID món trong order cần xoá: ");
        boolean ok = orderService.removeItem(o.getId(), itemId);
        System.out.println(ok ? "Đã xoá món khỏi order." : "Không tìm thấy item.");
        System.out.println("Subtotal: " + o.getSubtotal() + " VND");
    }


    private void viewOrderOfTable() {
        listTables();
        int tableId = readInt("Nhập ID bàn: ");
        var o = orderService.findOpenOrderByTableId(tableId)
                .orElseThrow(() -> new NoSuchElementException("Không có order OPEN cho bàn này"));
        System.out.println(o);
        if (o.getItems().isEmpty()) System.out.println("(Order trống)");
        else o.getItems().forEach(System.out::println);
        System.out.println("Subtotal: " + o.getSubtotal() + " VND");
    }

    private void checkoutOrderOfTable() {
        listTables();
        int tableId = readInt("Nhập ID bàn: ");
        var o = orderService.findOpenOrderByTableId(tableId)
                .orElseThrow(() -> new NoSuchElementException("Không có order OPEN cho bàn này"));
        o = orderService.checkout(o.getId());
        System.out.println("Đã checkout. " + o);
    }

    private void listOrdersByRange() {
        System.out.print("Nhập phạm vi (today/week/any): ");
        String r = sc.nextLine().trim();
        var list = orderService.listPaidByRange(r);
        if (list.isEmpty()) System.out.println("(Chưa có order PAID phù hợp)");
        else list.forEach(System.out::println);
    }



    // ===== Helpers an toàn =====
    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = sc.nextLine().trim();
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số nguyên hợp lệ.");
            }
        }
    }

    private Category readCategory(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String s = sc.nextLine().trim().toUpperCase();
                return Category.valueOf(s);
            } catch (IllegalArgumentException e) {
                System.out.println("Chỉ nhận: COFFEE, TEA, OTHER.");
            }
        }
    }
}
