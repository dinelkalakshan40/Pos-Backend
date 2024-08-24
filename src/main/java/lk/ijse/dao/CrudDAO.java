package lk.ijse.dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CrudDAO<T, P> extends SuperDAO {
    ArrayList<T> getAll(P pool) throws SQLException, ClassNotFoundException;
    boolean save(T entity,P pool) throws SQLException, ClassNotFoundException;
}
