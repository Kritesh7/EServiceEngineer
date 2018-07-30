package in.co.cfcs.eserviceengineer.model;

public class SparePartListDataModel {
    private String SparePartNo;

    private String SpareDesc;

    private String SpareID;

    private String SpareQuantity;

    public String getSparePartNo() {
        return SparePartNo;
    }

    public void setSparePartNo(String SparePartNo) {
        this.SparePartNo = SparePartNo;
    }

    public String getSpareDesc() {
        return SpareDesc;
    }

    public void setSpareDesc(String SpareDesc) {
        this.SpareDesc = SpareDesc;
    }

    public String getSpareID() {
        return SpareID;
    }

    public void setSpareID(String SpareID) {
        this.SpareID = SpareID;
    }

    public String getQuantity() {
        return SpareQuantity;
    }

    public void setQuantity(String SpareQuantity) {
        this.SpareQuantity = SpareQuantity;
    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [SparePartNo = "+SparePartNo+", SpareDesc = "+SpareDesc+", SpareID = "+SpareID+"]";
//    }
}
