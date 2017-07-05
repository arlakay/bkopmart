package id.co.octolink.ilm.bkopmart.realm;

import io.realm.RealmObject;

/**
 * Created by e_er_de on 28/05/2017.
 */

public class TransactionItem extends RealmObject {
    private String transactionMasterID;
    private String transactionItemCustomerID;
    private String transactionItemID;
    private String transactionItemName;
    private String transactionItemQuantity;
    private int transactionItemPrice;
    private int transactionItemSubTotal;
    private String transactionItemPicture;

    public String getTransactionMasterID() {
        return transactionMasterID;
    }

    public void setTransactionMasterID(String transactionMasterID) {
        this.transactionMasterID = transactionMasterID;
    }

    public String getTransactionItemCustomerID() {
        return transactionItemCustomerID;
    }

    public void setTransactionItemCustomerID(String transactionItemCustomerID) {
        this.transactionItemCustomerID = transactionItemCustomerID;
    }

    public String getTransactionItemID() {
        return transactionItemID;
    }

    public void setTransactionItemID(String transactionItemID) {
        this.transactionItemID = transactionItemID;
    }

    public String getTransactionItemName() {
        return transactionItemName;
    }

    public void setTransactionItemName(String transactionItemName) {
        this.transactionItemName = transactionItemName;
    }

    public String getTransactionItemQuantity() {
        return transactionItemQuantity;
    }

    public void setTransactionItemQuantity(String transactionItemQuantity) {
        this.transactionItemQuantity = transactionItemQuantity;
    }

    public int getTransactionItemPrice() {
        return transactionItemPrice;
    }

    public void setTransactionItemPrice(int transactionItemPrice) {
        this.transactionItemPrice = transactionItemPrice;
    }

    public int getTransactionItemSubTotal() {
        return transactionItemSubTotal;
    }

    public void setTransactionItemSubTotal(int transactionItemSubTotal) {
        this.transactionItemSubTotal = transactionItemSubTotal;
    }

    public String getTransactionItemPicture() {
        return transactionItemPicture;
    }

    public void setTransactionItemPicture(String transactionItemPicture) {
        this.transactionItemPicture = transactionItemPicture;
    }

}
