package dao;

import modelo.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO {

    public List<Product> listFiltered(String type, String text) throws SQLException {
        boolean all = type == null || type.isEmpty() || type.equals("(todas)");
        String sql = "SELECT id, product_name, brand, product_type, model, stock_quantity "
                + "FROM product WHERE product_name LIKE ? "
                + (all ? "" : "AND product_type = ? ")
                + "ORDER BY product_name";
        List<Product> result = new ArrayList<>();
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + text + "%");
            if (!all) {
                ps.setString(2, type);
            }
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

    public List<String> listTypes() throws SQLException {
        String sql = "SELECT DISTINCT product_type FROM product ORDER BY product_type";
        List<String> result = new ArrayList<>();
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString("product_type"));
            }
        }
        return result;
    }

    public Map<String, Integer> countByType() throws SQLException {
        String sql = "SELECT product_type, COUNT(*) AS total FROM product "
                + "GROUP BY product_type ORDER BY product_type";
        Map<String, Integer> totals = new LinkedHashMap<>();
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                totals.put(rs.getString("product_type"), rs.getInt("total"));
            }
        }
        return totals;
    }

    public byte[] findPhoto(int id) throws SQLException {
        String sql = "SELECT photo FROM product WHERE id = ?";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBytes("photo");
                }
                return null;
            }
        }
    }

    public void insert(Product p) throws SQLException {
        String sql = "INSERT INTO product (product_name, brand, product_type, model, stock_quantity, photo) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getProductName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getProductType());
            ps.setString(4, p.getModel());
            ps.setInt(5, p.getStockQuantity());
            if (p.hasPhoto()) {
                ps.setBytes(6, p.getPhoto());
            } else {
                ps.setNull(6, Types.BLOB);
            }
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
                + "model = ?, stock_quantity = ?, photo = ? WHERE id = ?";
        try (Connection con = DBConnection.open();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getProductName());
            ps.setString(2, p.getBrand());
            ps.setString(3, p.getProductType());
            ps.setString(4, p.getModel());
            ps.setInt(5, p.getStockQuantity());
            if (p.hasPhoto()) {
                ps.setBytes(6, p.getPhoto());
            } else {
                ps.setNull(6, Types.BLOB);
            }
            ps.setInt(7, p.getId());

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
