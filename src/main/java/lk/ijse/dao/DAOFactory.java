package lk.ijse.dao;

import lk.ijse.dao.custom.impl.CustomerDAOImpl;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {
    }

    public static DAOFactory getDaoFactory() {
        return (daoFactory == null) ? daoFactory = new DAOFactory() : daoFactory;
    }
    public enum DAOTypes {
        CUSTOMER
    }

    public SuperDAO getDAO(DAOTypes daoTypes) {
        switch (daoTypes) {
            case CUSTOMER:
                return new CustomerDAOImpl();
            /*case ORDER:
                return new OrderDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case ORDERDETAIL:
                return new OrderDetailDAOImpl();*/
            default:
                return null;
        }
    }
}
