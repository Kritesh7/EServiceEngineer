package in.co.cfcs.eserviceengineer.model;

public class EngineerWorkStatusDataModel {
    private String EngineerName;

    private String EngWorkStatus;

    private String EngStatusID;

    private String EngWorkStatusID;

    private String ComplainNo;

    private String AddDateText;

    private String IsEditDelete;

    private String SpareNo;

    private String Remark;

    public EngineerWorkStatusDataModel(String ComplainNo, String EngWorkStatus, String Remark, String AddDateText, String EngineerName,
                                       String SpareNo, String IsEditDelete) {
    }

    public String getEngineerName() {
        return EngineerName;
    }

    public void setEngineerName(String EngineerName) {
        this.EngineerName = EngineerName;
    }

    public String getEngWorkStatus() {
        return EngWorkStatus;
    }

    public void setEngWorkStatus(String EngWorkStatus) {
        this.EngWorkStatus = EngWorkStatus;
    }

    public String getEngStatusID() {
        return EngStatusID;
    }

    public void setEngStatusID(String EngStatusID) {
        this.EngStatusID = EngStatusID;
    }

    public String getEngWorkStatusID() {
        return EngWorkStatusID;
    }

    public void setEngWorkStatusID(String EngWorkStatusID) {
        this.EngWorkStatusID = EngWorkStatusID;
    }

    public String getComplainNo() {
        return ComplainNo;
    }

    public void setComplainNo(String ComplainNo) {
        this.ComplainNo = ComplainNo;
    }

    public String getAddDateText() {
        return AddDateText;
    }

    public void setAddDateText(String AddDateText) {
        this.AddDateText = AddDateText;
    }

    public String getIsEditDelete() {
        return IsEditDelete;
    }

    public void setIsEditDelete(String IsEditDelete) {
        this.IsEditDelete = IsEditDelete;
    }

    public String getSpareNo() {
        return SpareNo;
    }

    public void setSpareNo(String SpareNo) {
        this.SpareNo = SpareNo;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [EngineerName = "+EngineerName+", EngWorkStatus = "+EngWorkStatus+", EngStatusID = "+EngStatusID+", EngWorkStatusID = "+EngWorkStatusID+", ComplainNo = "+ComplainNo+", AddDateText = "+AddDateText+", IsEditDelete = "+IsEditDelete+", SpareNo = "+SpareNo+", Remark = "+Remark+"]";
//    }
}
