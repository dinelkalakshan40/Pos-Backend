package lk.ijse.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Item implements Serializable {
    private String itemID;
    private String itemName;
    private double itemPrice;
    private int itemQty;
}
