package in.co.cfcs.eserviceengineer.model;

import com.google.gson.annotations.SerializedName;

public class DecodeImageBean {

    @SerializedName("MIMEType")
    String MIMEType;

    @SerializedName("ImageExtension")
    String ImageExtension;

    @SerializedName("ImageString")
    String ImageString;

    public String getMIMEType() {
        return MIMEType;
    }

    public void setMIMEType(String MIMEType) {
        this.MIMEType = MIMEType;
    }

    public String getImageExtension() {
        return ImageExtension;
    }

    public void setImageExtension(String imageExtension) {
        ImageExtension = imageExtension;
    }

    public String getImageString() {
        return ImageString;
    }

    public void setImageString(String imageString) {
        ImageString = imageString;
    }

}
