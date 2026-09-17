package dao;

import modelo.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> list() throws SQLException {
        String sql = "SELECT id, product_name, brand, product_type, model, stock_quantity "
                + "FROM product ORDER BY product_name";
        List<Product> result = new ArrayList<>();
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(new Product(
                        rs.getInt("id"),
                        rs.getString("product_name"),
                        rs.getString("brand"),
                        rs.getString("product_type"),
                        rs.getString("model"),
                        rs.getInt("stock_quantity")));
            }
        }
        return result;
    }

    public List<Product> searchByName(String text) throws SQLException {
        String sql = "SELECT id, product_name, brand, product_type, model, stock_quantity "
                + "FROM product WHERE product_name LIKE ? ORDER BY product_name";
        List<Product> result = new ArrayList<>();
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + text + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new Product(
                            rs.getInt("id"),
                            rs.getString("product_name"),
                            rs.getString("brand"),
                            rs.getString("product_type"),
                            rs.getString("model"),
                            rs.getInt("stock_quantity")));
                }
            }
        }
        return result;
    }

    public void insert(Product p) throws SQLException {
        String sql = "INSERT INTO product (product_name, brand, product_type, model, stock_quantity) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getProductName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getProductType());
            ps.setString(4, p.getModel());
            ps.setInt(5, p.getStockQuantity());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt(1));
                }
            }
        }
    }

    public boolean update(Product p) throws SQLException {
        String sql = "UPDATE product SET product_name = ?, brand = ?, product_type = ?, "
                + "model = ?, stock_quantity = ? WHERE id = ?";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getProductName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getProductType());
            ps.setString(4, p.getModel());
            ps.setInt(5, p.getStockQuantity());
            ps.setInt(6, p.getId()); // o sexto ? e o do WHERE

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM product WHERE id = ?";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
