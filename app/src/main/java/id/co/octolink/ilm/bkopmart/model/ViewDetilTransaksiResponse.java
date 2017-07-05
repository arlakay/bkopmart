package id.co.octolink.ilm.bkopmart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by e_er_de on 25/05/2017.
 */

public class ViewDetilTransaksiResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<ViewDetilTransaksi> data = null;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ViewDetilTransaksi> getData() {
        return data;
    }

    public void setData(List<ViewDetilTransaksi> data) {
        this.data = data;
    }

}
