package id.co.octolink.ilm.bkopmart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by e_er_de on 31/05/2017.
 */

public class Billing {

    @SerializedName("json_transaction")
    @Expose
    private String jsonTransaction;
    @SerializedName("customer_id")
    @Expose
    private String customerId;

    public String getJsonTransaction() {
        return jsonTransaction;
    }

    public void setJsonTransaction(String jsonTransaction) {
        this.jsonTransaction = jsonTransaction;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
