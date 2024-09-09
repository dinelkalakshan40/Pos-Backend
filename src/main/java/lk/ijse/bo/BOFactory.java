package lk.ijse.bo;

import lk.ijse.bo.custom.impl.CustomerBOImpl;
import lk.ijse.bo.custom.impl.ItemBOImpl;

public class BOFactory {

    private static BOFactory boFactory;
    private BOFactory() {

    }
    public static BOFactory getBoFactory() {
        return (boFactory == null) ? boFactory = new BOFactory() : boFactory;
    }
    public enum BOTypes {
        CUSTOMER_BO, ORDER, ITEM_BO, ORDERDETAIL
    }

    public SuperBo getBO(BOTypes boTypes) {
        switch (boTypes) {
            case CUSTOMER_BO:
                return new CustomerBOImpl();
            case ITEM_BO:
                return new ItemBOImpl();
            default:
                return null;
        }
    }
}
