package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.entity.Customer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public List<Customer> getAll(Connection connection) throws SQLException {
        var ps = connection.prepareStatement("SELECT * FROM customer");
        var resultSet = ps.executeQuery();
        List<Customer> customerList = new ArrayList<>();
        while (resultSet.next()) {
            Customer customers = new Customer(
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("address"),
                    resultSet.getString("phone")
            );
            customerList.add(customers);
        }
        return customerList;
    }

    @Override
    public boolean save(Customer entity, Connection connection) throws SQLException {
        String sql = "INSERT INTO customer VALUES (?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getId());
            stmt.setString(2, entity.getName());
            stmt.setString(3, entity.getAddress());
            stmt.setString(4, entity.getPhone());

            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public String generateNewId(Connection connection) throws SQLException {
        String sql = "SELECT id FROM customer ORDER BY id DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                String lastId = rs.getString("id");

                // Extract the numeric part from the ID
                int idNum = Integer.parseInt(lastId.substring(4));

                // Increment the numeric part
                idNum++;

                // Format the new ID
                String newId = String.format("CID-%03d", idNum);


                return newId;
            } else {
                System.out.println("Generated ID: " + "CID-001");
                return "CID-001";
            }
        }
    }

    @Override
    public boolean update(String custid, Customer entity, Connection connection) throws SQLException {

        String sql = "UPDATE customer SET name=?,phone=?,address=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getName());
            stmt.setString(2, entity.getPhone());
            stmt.setString(3, entity.getAddress());
            stmt.setString(4, custid);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(String custId, Connection connection) throws SQLException {
        String sql = "DELETE FROM customer WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, custId);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Customer search(String custId, Connection connection) throws SQLException {
        Customer customer = null;
        String sql = "SELECT * FROM customer WHERE id=?";
        var ps = connection.prepareStatement(sql);
        ps.setString(1, custId);
        var rs = ps.executeQuery();
        if (rs.next()) {  // Use if instead of while
            String id = rs.getString("id");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            String address = rs.getString("address");

            customer = new Customer(id, name, phone, address);
        }
        return customer;
    }

}
