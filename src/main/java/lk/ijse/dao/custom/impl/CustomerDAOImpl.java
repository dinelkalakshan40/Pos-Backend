package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.entity.Customer;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<Customer> getAll(Connection connection) throws SQLException {

        String sql = "SELECT * FROM customer";
        ArrayList<Customer> customerArrayList = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rst = stmt.executeQuery(sql)) {

            while (rst.next()) {
                Customer entity = new Customer(
                        rst.getString("cus_id"),
                        rst.getString("cus_name"),
                        rst.getString("cus_address"),
                        rst.getString("cus_mobile"));
                customerArrayList.add(entity);
            }
        }

        return customerArrayList;
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
        System.out.println("customerDAOImpl");
        String sql = "SELECT id FROM customer ORDER BY id DESC LIMIT 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                String lastId = rs.getString("id");

                // Extract the numeric part from the ID
                int idNum = Integer.parseInt(lastId.substring(4));

                // Increment the numeric part
                idNum++;

                System.out.println("idNum " + idNum);

                // Format the new ID
                String newId = String.format("CID-%03d", idNum);

                System.out.println("Generated ID: " + newId);
                return newId;
            } else {
                System.out.println("Generated ID: " + "CID-001");
                return "CID-001";
            }
        }
    }

}
