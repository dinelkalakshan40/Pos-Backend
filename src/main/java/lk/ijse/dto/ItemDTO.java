package lk.ijse.dto;


import java.io.Serializable;


public class ItemDTO implements Serializable {
    private String itemID;
    private String itemName;
    private double itemPrice;
    private int itemQty;

    public ItemDTO() {
    }

    public ItemDTO(String itemID, String itemName, double itemPrice, int itemQty) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQty = itemQty;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemQty() {
        return itemQty;
    }

    public void setItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    @Override
    public String toString() {
        return "ItemDTO{" +
                "itemID='" + itemID + '\'' +
                ", itemName='" + itemName + '\'' +
                ", itemPrice=" + itemPrice +
                ", itemQty=" + itemQty +
                '}';
    }

}
