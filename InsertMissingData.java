import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsertMissingData {
    public static void main(String[] args) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/centerpoint_db?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh", "root", "abc123!");
        Statement stmt = conn.createStatement();
        
        // Insert Brands
        String[] brands = {"MSI", "E-Dra", "Ziyou", "Leobog", "Logitech"};
        for(String b : brands) {
            String slug = b.toLowerCase().replace(" ", "-");
            stmt.executeUpdate("INSERT INTO brands (name, slug) SELECT '" + b + "', '" + slug + "' WHERE NOT EXISTS (SELECT 1 FROM brands WHERE name = '" + b + "')");
        }
        
        // Insert Category
        stmt.executeUpdate("INSERT INTO categories (name, slug) SELECT 'Phụ kiện', 'phu-kien' WHERE NOT EXISTS (SELECT 1 FROM categories WHERE name = 'Phụ kiện')");
        
        System.out.println("Brands:");
        ResultSet rs = stmt.executeQuery("SELECT * FROM brands");
        while(rs.next()){
            System.out.println(rs.getInt("id") + " : " + rs.getString("name"));
        }
        
        System.out.println("Categories:");
        ResultSet rc = stmt.executeQuery("SELECT * FROM categories");
        while(rc.next()){
            System.out.println(rc.getInt("id") + " : " + rc.getString("name"));
        }
        conn.close();
    }
}
