package lk.ijse.dao.custom.impl;

import lk.ijse.dao.SQLUtil;
import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.entity.Customer;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public ArrayList<Customer> getAll(DataSource pool) throws SQLException {

        ResultSet rst = SQLUtil.execute(pool, "SELECT * FROM customer");
        ArrayList<Customer> customerArrayList = new ArrayList<>();

        while (rst.next()) {
            Customer entity = new Customer(
                    rst.getString("cus_id"),
                    rst.getString("cus_name"),
                    rst.getString("cus_address"),
                    rst.getString("cus_mobile"));
            customerArrayList.add(entity);
        }
        return customerArrayList;
    }
    @Override
    public boolean save(Customer entity, DataSource pool) throws SQLException {
        return SQLUtil.execute(pool, "INSERT INTO customer VALUES (?,?,?,?)",
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getPhone());
    }

}
