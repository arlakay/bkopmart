package id.co.octolink.ilm.bkopmart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by e_er_de on 25/05/2017.
 */

public class ViewDetilTransaksi {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("item_id")
    @Expose
    private String itemId;
    @SerializedName("varian_name")
    @Expose
    private String varianName;
    @SerializedName("item_price")
    @Expose
    private String itemPrice;
    @SerializedName("item_qty")
    @Expose
    private String itemQty;
    @SerializedName("item_tax")
    @Expose
    private String itemTax;
    @SerializedName("item_service")
    @Expose
    private String itemService;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getVarianName() {
        return varianName;
    }

    public void setVarianName(String varianName) {
        this.varianName = varianName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemQty() {
        return itemQty;
    }

    public void setItemQty(String itemQty) {
        this.itemQty = itemQty;
    }

    public String getItemTax() {
        return itemTax;
    }

    public void setItemTax(String itemTax) {
        this.itemTax = itemTax;
    }

    public String getItemService() {
        return itemService;
    }

    public void setItemService(String itemService) {
        this.itemService = itemService;
    }

}
