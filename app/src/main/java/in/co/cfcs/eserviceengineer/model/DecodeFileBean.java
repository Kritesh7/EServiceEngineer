package in.co.cfcs.eserviceengineer.model;

import com.google.gson.annotations.SerializedName;

public class DecodeFileBean {

    @SerializedName("MIMEType")
    String MIMEType;

    @SerializedName("FileExt")
    String FileExt;

    @SerializedName("FileContent")
    String FileContent;

    public String getMIMEType() {
        return MIMEType;
    }

    public void setMIMEType(String MIMEType) {
        this.MIMEType = MIMEType;
    }

    public String getFileExt() {
        return FileExt;
    }

    public void setFileExt(String fileExt) {
        FileExt = fileExt;
    }

    public String getFileContent() {
        return FileContent;
    }

    public void setFileContent(String fileContent) {
        FileContent = fileContent;
    }


}
