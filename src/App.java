import java.util.Scanner;

public class App {
    private final MenuService menuService = new MenuService();
    private final Scanner sc = new Scanner(System.in);

    public void run() {
        while (true) {
            System.out.println("\n== CAFE LITE ==");
            System.out.println("1) Thêm món");
            System.out.println("2) Danh sách món");
            System.out.println("0) Thoát");
            System.out.print("Chọn: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> addMenuItem();
                case "2" -> listMenu();
                case "0" -> { System.out.println("Bye!"); return; }
                default -> System.out.println("Không hợp lệ.");
            }
        }
    }

    private void addMenuItem() {
        System.out.print("Tên món: ");
        String name = sc.nextLine().trim();
        System.out.print("Giá (VND, số nguyên): ");
        int price = Integer.parseInt(sc.nextLine().trim());
        System.out.print("Loại (COFFEE/TEA/OTHER): ");
        Category cat = Category.valueOf(sc.nextLine().trim().toUpperCase());

        MenuItem mi = menuService.create(name, price, cat);
        System.out.println("Đã thêm: " + mi);
    }

    private void listMenu() {
        if (menuService.findAll().isEmpty()) {
            System.out.println("(Chưa có món)");
        } else {
            menuService.findAll().forEach(System.out::println);
        }
    }
}
