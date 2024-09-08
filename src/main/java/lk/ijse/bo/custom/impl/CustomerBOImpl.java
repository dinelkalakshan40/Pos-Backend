package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.CustomerBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.CustomerDAO;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.entity.Customer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {
    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public List<CustomerDTO> getAllCustomer(Connection connection) throws SQLException, ClassNotFoundException {
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        List<Customer> customers = customerDAO.getAll(connection);
        for (Customer customer : customers) {
            customerDTOS.add(new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress(), customer.getPhone()));
        }
        return customerDTOS;
    }

    @Override
    public boolean saveCustomer(CustomerDTO customerDTO, Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.save(new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getAddress(), customerDTO.getPhone()), connection);
    }

    @Override
    public String generateNewCustomerId(Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.generateNewId(connection);
    }

    @Override
    public boolean updateCustomer(String id, CustomerDTO customerDTO, Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.update(id,
                new Customer(customerDTO.getId(), customerDTO.getName(), customerDTO.getPhone(), customerDTO.getAddress()), connection);
    }

    @Override
    public boolean deleteCustomer(String id, Connection connection) throws SQLException, ClassNotFoundException {
        return customerDAO.delete(id, connection);
    }
    @Override
    public CustomerDTO searchCustomer(String customerID, Connection connection) throws SQLException, ClassNotFoundException {
        Customer customer =customerDAO.search(customerID, connection);
        if (customer != null) {
            return new CustomerDTO(customer.getId(), customer.getName(), customer.getAddress(), customer.getPhone());
        } else {
            return null;
        }

    }

}
