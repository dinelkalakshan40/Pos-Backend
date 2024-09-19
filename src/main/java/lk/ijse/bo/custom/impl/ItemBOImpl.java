package lk.ijse.bo.custom.impl;

import lk.ijse.bo.custom.ItemBO;
import lk.ijse.dao.DAOFactory;
import lk.ijse.dao.custom.ItemDAO;
import lk.ijse.dto.CustomerDTO;
import lk.ijse.dto.ItemDTO;
import lk.ijse.entity.Customer;
import lk.ijse.entity.Item;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {
    ItemDAO itemDAO = (ItemDAO) DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.Item);


    @Override
    public List<ItemDTO> getAllItem(Connection connection) throws SQLException, ClassNotFoundException {
        List<ItemDTO> itemDTOS = new ArrayList<>();
        List<Item> items = itemDAO.getAll(connection);
        for (Item item : items) {
            itemDTOS.add(new ItemDTO(item.getItemID(),item.getItemName(), item.getItemPrice(), item.getItemQty()));
        }
        return itemDTOS;

    }

    @Override
    public boolean saveItem(ItemDTO itemDTO, Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.save(new Item(itemDTO.getItemID(),itemDTO.getItemName(),itemDTO.getItemPrice(),itemDTO.getItemQty()),connection);
    }

    @Override
    public String generateNewItemID(Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.generateNewId(connection);
    }

    @Override
    public boolean updateItem(String id, ItemDTO itemDTO, Connection connection) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean deleteItem(String id, Connection connection) throws SQLException, ClassNotFoundException {
        return itemDAO.delete(id,connection);
    }

    @Override
    public ItemDTO searchItem(String itemID, Connection connection) throws SQLException, ClassNotFoundException {
        Item item =itemDAO.search(itemID, connection);
        if (item != null) {
            return new ItemDTO(item.getItemID(),item.getItemName(), item.getItemPrice(), item.getItemQty());
        } else {
            return null;
        }
    }
}
