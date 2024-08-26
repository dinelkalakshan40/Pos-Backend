package lk.ijse.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T> extends SuperDAO {
    ArrayList<T> getAll(Connection connection) throws SQLException, ClassNotFoundException;
    boolean save(T entity,Connection connection) throws SQLException, ClassNotFoundException;
}
