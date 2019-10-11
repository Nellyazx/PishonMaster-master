package alien.com.model;

public class Product {
    String category,id,name,price,imageurl,quantity;

    public Product(String category, String id, String name, String price, String imageurl, String quantity) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageurl = imageurl;
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getQuantity() {
        return quantity;
    }
}
