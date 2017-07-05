package id.co.octolink.ilm.bkopmart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by e_er_de on 25/05/2017.
 */

public class Pembelian {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("transaction_id")
    @Expose
    private String transactionId;
    @SerializedName("cashier_id")
    @Expose
    private String cashierId;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("outlet_id")
    @Expose
    private String outletId;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("transaction_date")
    @Expose
    private String transactionDate;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("cust_tendered")
    @Expose
    private String custTendered;
    @SerializedName("cust_change")
    @Expose
    private String custChange;
    @SerializedName("cust_email")
    @Expose
    private String custEmail;
    @SerializedName("record_status")
    @Expose
    private String recordStatus;

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

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOutletId() {
        return outletId;
    }

    public void setOutletId(String outletId) {
        this.outletId = outletId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCustTendered() {
        return custTendered;
    }

    public void setCustTendered(String custTendered) {
        this.custTendered = custTendered;
    }

    public String getCustChange() {
        return custChange;
    }

    public void setCustChange(String custChange) {
        this.custChange = custChange;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

}
