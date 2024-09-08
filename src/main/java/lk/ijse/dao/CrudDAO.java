package lk.ijse.dao;

import lk.ijse.entity.Customer;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO {
    public List<Customer> getAll(Connection connection) throws SQLException, ClassNotFoundException;
    boolean save(T entity,Connection connection) throws SQLException, ClassNotFoundException;
    String generateNewId(Connection connection) throws SQLException, ClassNotFoundException;
    boolean update(String id, T entity,Connection connection) throws SQLException, ClassNotFoundException;
    boolean delete(String custId,Connection connection) throws SQLException, ClassNotFoundException;
    T search(String custId,Connection connection) throws SQLException, ClassNotFoundException;
}
