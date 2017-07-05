package id.co.octolink.ilm.bkopmart.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by e_er_de on 23/05/2017.
 */

public class Slider {

    @SerializedName("sliderapp_id")
    @Expose
    private String sliderappId;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("create_at")
    @Expose
    private String createAt;

    public String getSliderappId() {
        return sliderappId;
    }

    public void setSliderappId(String sliderappId) {
        this.sliderappId = sliderappId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

}
