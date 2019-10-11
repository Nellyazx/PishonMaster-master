package alien.com.model;

public class Category
{

    public String Id;
    public String categoryName;
    public String categoryImage;

    public Category(String categoryName,String Id,String categoryImage)
    {
        this.Id = Id;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public String getId() {
        return Id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }
}
