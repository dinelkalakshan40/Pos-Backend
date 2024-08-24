package lk.ijse.dao.custom;

import lk.ijse.dao.CrudDAO;
import lk.ijse.dao.SuperDAO;
import lk.ijse.entity.Customer;

import javax.sql.DataSource;

public interface CustomerDAO extends CrudDAO<Customer,  DataSource> {
}
