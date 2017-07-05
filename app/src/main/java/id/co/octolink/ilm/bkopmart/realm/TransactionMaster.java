package id.co.octolink.ilm.bkopmart.realm;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by e_er_de on 28/05/2017.
 */

public class TransactionMaster extends RealmObject {
    @PrimaryKey
    private String transactionMasterID;
    private String transactionMasterDateAndTime;
    private RealmList<TransactionItem> TransactionItem;

    public String getTransactionMasterID() {
        return transactionMasterID;
    }

    public void setTransactionMasterID(String transactionMasterID) {
        this.transactionMasterID = transactionMasterID;
    }

    public String getTransactionMasterDateAndTime() {
        return transactionMasterDateAndTime;
    }

    public void setTransactionMasterDateAndTime(String transactionMasterDateAndTime) {
        this.transactionMasterDateAndTime = transactionMasterDateAndTime;
    }

    public RealmList<id.co.octolink.ilm.bkopmart.realm.TransactionItem> getTransactionItem() {
        return TransactionItem;
    }

    public void setTransactionItem(RealmList<id.co.octolink.ilm.bkopmart.realm.TransactionItem> transactionItem) {
        TransactionItem = transactionItem;
    }
}
