package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBo;
import lk.ijse.dto.CustomerDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CustomerBO extends SuperBo {
    List<CustomerDTO> getAllCustomer(Connection connection) throws SQLException, ClassNotFoundException;

    boolean saveCustomer(CustomerDTO customerDTO, Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewCustomerId(Connection connection) throws SQLException, ClassNotFoundException;

    boolean updateCustomer(String id, CustomerDTO customerDTO, Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteCustomer(String id, Connection connection) throws SQLException, ClassNotFoundException;
}
