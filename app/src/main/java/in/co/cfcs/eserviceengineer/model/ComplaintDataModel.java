package in.co.cfcs.eserviceengineer.model;

public class ComplaintDataModel {
    private String SiteAddress;
    private String PriorityName;
    private String StatusText;
    private String ComplainNo;
    private String IsEditDelete;
    private String IsServiceReportFill;
    private String ComplainByName;
    private String CustomerName;
    private String TransactionTypeName;
    private String EscalationShortCode;
    private String WorkStatusName;
    private String ModelName;
    private String LoggedBy;
    private String ShortCode;
    private String ComplaintTitle;
    private String ComplainDateTimeText;

    public ComplaintDataModel(String SiteAddress, String PriorityName, String StatusText, String ComplainNo, String IsEditDelete, String IsServiceReportFill,
                              String ComplainByName, String CustomerName, String TransactionTypeName, String EscalationShortCode, String WorkStatusName,
                              String ModelName, String ComplaintTitle, String ComplainDateTimeText) {
    }


    public String getSiteAddress() {
        return SiteAddress;
    }

    public void setSiteAddress(String SiteAddress) {
        this.SiteAddress = SiteAddress;
    }

    public String getPriorityName() {
        return PriorityName;
    }

    public void setPriorityName(String PriorityName) {
        this.PriorityName = PriorityName;
    }

    public String getStatusText() {
        return StatusText;
    }

    public void setStatusText(String StatusText) {
        this.StatusText = StatusText;
    }

    public String getComplainNo() {
        return ComplainNo;
    }

    public void setComplainNo(String ComplainNo) {
        this.ComplainNo = ComplainNo;
    }

    public String getIsEditDelete() {
        return IsEditDelete;
    }

    public void setIsEditDelete(String IsEditDelete) {
        this.IsEditDelete = IsEditDelete;
    }

    public String getIsServiceReportFill() {
        return IsServiceReportFill;
    }

    public void setIsServiceReportFill(String IsServiceReportFill) {
        this.IsServiceReportFill = IsServiceReportFill;
    }

    public String getComplainByName() {
        return ComplainByName;
    }

    public void setComplainByName(String ComplainByName) {
        this.ComplainByName = ComplainByName;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String CustomerName) {
        this.CustomerName = CustomerName;
    }

    public String getTransactionTypeName() {
        return TransactionTypeName;
    }

    public void setTransactionTypeName(String TransactionTypeName) {
        this.TransactionTypeName = TransactionTypeName;
    }

    public String getEscalationShortCode() {
        return EscalationShortCode;
    }

    public void setEscalationShortCode(String EscalationShortCode) {
        this.EscalationShortCode = EscalationShortCode;
    }

    public String getWorkStatusName() {
        return WorkStatusName;
    }

    public void setWorkStatusName(String WorkStatusName) {
        this.WorkStatusName = WorkStatusName;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public String getLoggedBy() {
        return LoggedBy;
    }

    public void setLoggedBy(String LoggedBy) {
        this.LoggedBy = LoggedBy;
    }

    public String getShortCode() {
        return ShortCode;
    }

    public void setShortCode(String ShortCode) {
        this.ShortCode = ShortCode;
    }

    public String getComplaintTitle() {
        return ComplaintTitle;
    }

    public void setComplaintTitle(String ComplaintTitle) {
        this.ComplaintTitle = ComplaintTitle;
    }

    public String getComplainDateTimeText() {
        return ComplainDateTimeText;
    }

    public void setComplainDateTimeText(String ComplainDateTimeText) {
        this.ComplainDateTimeText = ComplainDateTimeText;
    }

//    @Override
//    public String toString() {
//        return "ClassPojo [SiteAddress = " + SiteAddress + ", PriorityName = " + PriorityName + ", StatusText = " + StatusText + ", ComplainNo = " + ComplainNo + ", IsEditDelete = " + IsEditDelete + ", IsServiceReportFill = " + IsServiceReportFill + ", ComplainByName = " + ComplainByName + ", CustomerName = " + CustomerName + ", TransactionTypeName = " + TransactionTypeName + ", EscalationShortCode = " + EscalationShortCode + ", WorkStatusName = " + WorkStatusName + ", ModelName = " + ModelName + ", LoggedBy = " + LoggedBy + ", ShortCode = " + ShortCode + ", ComplaintTitle = " + ComplaintTitle + ", ComplainDateTimeText = " + ComplainDateTimeText + "]";
//    }
}

