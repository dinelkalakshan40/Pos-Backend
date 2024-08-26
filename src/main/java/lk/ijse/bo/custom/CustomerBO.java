package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBo;
import lk.ijse.dto.CustomerDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBo {
    ArrayList<CustomerDTO> getAllCustomer(Connection connection) throws SQLException, ClassNotFoundException;

    boolean saveCustomer(CustomerDTO customerDTO, Connection connection) throws SQLException, ClassNotFoundException;
}
