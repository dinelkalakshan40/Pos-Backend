package lk.ijse.dao.custom.impl;

import lk.ijse.dao.custom.ItemDAO;
import lk.ijse.dto.ItemDTO;
import lk.ijse.entity.Customer;
import lk.ijse.entity.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {


    @Override
    public List<Item> getAll(Connection connection) throws SQLException {
        var ps = connection.prepareStatement("select * from item");
        var resultSet = ps.executeQuery();
        List<Item> itemList = new ArrayList<>();
        while (resultSet.next()) {
            Item items = new Item(
                    resultSet.getString("itemID"),
                    resultSet.getString("itemName"),
                    resultSet.getDouble("itemPrice"),
                    resultSet.getInt("itemQty")
            );
            itemList.add(items);
        }
        return itemList;
    }

    @Override
    public boolean save(Item entity, Connection connection) throws SQLException {
        String sql = "INSERT INTO item VALUES(?,?,?,?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getItemID());
            stmt.setString(2, entity.getItemName());
            stmt.setDouble(3, entity.getItemPrice());
            stmt.setInt(4, entity.getItemQty());
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public String generateNewId(Connection connection) throws SQLException {
        String sql = "select itemID from item order by itemID Desc Limit 1";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String lastId = rs.getString("itemID");
                int idNUm = Integer.parseInt(lastId.substring(4));

                idNUm++;

                String newId = String.format("ID-%03d", idNUm);

                return newId;
            } else {


                return "ID-001";
            }
        }
    }

    @Override
    public boolean update(String itemid, Item entity, Connection connection) throws SQLException {
        String sql = "UPDATE item SET itemName=?,itemPrice=?,itemQty=? where itemID=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entity.getItemName());
            stmt.setDouble(2, entity.getItemPrice());
            stmt.setInt(3, entity.getItemQty());
            stmt.setString(4, entity.getItemID());
            return stmt.executeUpdate() > 0;
        }

    }


    @Override
    public boolean delete(String itemid, Connection connection) throws SQLException {
        String sql = "DELETE FROM item WHERE itemID=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, itemid);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public Item search(String itemid, Connection connection) throws SQLException, ClassNotFoundException {
        Item item = null;
        String sql = "SELECT * FROM item WHERE itemID=?";
        var ps = connection.prepareStatement(sql);
        ps.setString(1, itemid);
        System.out.println("clall");
        var rs = ps.executeQuery();
        if (rs.next()) {  // Use if instead of while
            String itemID = rs.getString("itemID");
            String itemName = rs.getString("itemName");
            double itemPrice = rs.getDouble("itemPrice");
            int itemQty = rs.getInt("itemQty");

            item = new Item(itemID, itemName, itemPrice, itemQty);
        }
        return item;
    }


}
