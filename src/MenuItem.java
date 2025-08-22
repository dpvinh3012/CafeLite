public class MenuItem {
    private final int id;
    private String name;
    private int price; // VND, dùng int cho đơn giản
    private Category category;
    private boolean active = true;

    public MenuItem(int id, String name, int price, Category category) {
        if (name == null || name.length() < 2) {
            throw new IllegalArgumentException("Tên món tối thiểu 2 ký tự");
        }
        if (price < 0) throw new IllegalArgumentException("Giá không âm");
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public Category getCategory() { return category; }
    public boolean isActive() { return active; }

    public void setName(String name) { this.name = name; }
    public void setPrice(int price) { this.price = price; }
    public void setCategory(Category category) { this.category = category; }
    public void setActive(boolean active) { this.active = active; }

    @Override public String toString() {
        return "#" + id + " | " + name + " | " + price + " VND | " + category + (active ? "" : " (inactive)");
    }
}
