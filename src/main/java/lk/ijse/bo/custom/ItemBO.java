package lk.ijse.bo.custom;

import lk.ijse.bo.SuperBo;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.dto.ItemDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface ItemBO extends SuperBo {
    List<ItemDTO> getAllItem(Connection connection) throws SQLException, ClassNotFoundException;

    boolean saveItem(ItemDTO itemDTO, Connection connection) throws SQLException, ClassNotFoundException;

    String generateNewItemID(Connection connection) throws SQLException, ClassNotFoundException;

    boolean updateItem(String id, ItemDTO itemDTO, Connection connection) throws SQLException, ClassNotFoundException;

    boolean deleteItem(String id, Connection connection) throws SQLException, ClassNotFoundException;

    ItemDTO searchItem(String itemID, Connection connection) throws SQLException, ClassNotFoundException;
}
