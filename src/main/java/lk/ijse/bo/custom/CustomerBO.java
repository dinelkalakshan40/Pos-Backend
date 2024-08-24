package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBo;
import lk.ijse.dto.CustomerDTO;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerBO extends SuperBo {
    ArrayList<CustomerDTO> getAllCustomer(DataSource pool) throws SQLException, ClassNotFoundException;

    boolean saveCustomer(CustomerDTO customerDTO, DataSource pool) throws SQLException, ClassNotFoundException;
}
