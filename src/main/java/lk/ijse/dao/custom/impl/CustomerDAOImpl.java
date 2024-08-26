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

}
