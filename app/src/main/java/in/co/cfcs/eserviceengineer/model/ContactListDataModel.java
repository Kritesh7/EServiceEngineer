package in.co.cfcs.eserviceengineer.model;

public class ContactListDataModel {
    private String Phone;

    private String PageStartIndex;

    private String SiteAddress;

    private String ApproveStatusName;

    private String IsEditDelete;

    private String ParentCustomerName;

    private String ContactPersonId;

    private String ContactPersonName;

    private String Email;

    private String CustomerID;

    private String AddDateText;

    private String ApproveStatusRemark;

    private String AddByName;

    private String Designation;

    private String Counter;

    private String LoginUserName;

    private String OtherContact;

    public ContactListDataModel(String Phone, String SiteAddress, String ApproveStatusName, String IsEditDelete,
                                String ParentCustomerName, String ContactPersonId, String ContactPersonName, String Email, String CustomerID,
                                String AddDateText, String ApproveStatusRemark, String AddByName, String Designation, String Counter, String LoginUserName, String OtherContact) {
    }

    public String getCounter() {
        return Counter;
    }

    public void setCounter(String counter) {
        this.Counter = counter;
    }

    public String getOtherContact() {
        return OtherContact;
    }

    public void setOtherContact(String otherContact) {
        this.OtherContact = otherContact;
    }

    public String getLoginUserName() {
        return LoginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.LoginUserName = loginUserName;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getPageStartIndex() {
        return PageStartIndex;
    }

    public void setPageStartIndex(String PageStartIndex) {
        this.PageStartIndex = PageStartIndex;
    }

    public String getSiteAddress() {
        return SiteAddress;
    }

    public void setSiteAddress(String SiteAddress) {
        this.SiteAddress = SiteAddress;
    }

    public String getApproveStatusName() {
        return ApproveStatusName;
    }

    public void setApproveStatusName(String ApproveStatusName) {
        this.ApproveStatusName = ApproveStatusName;
    }

    public String getIsEditDelete() {
        return IsEditDelete;
    }

    public void setIsEditDelete(String IsEditDelete) {
        this.IsEditDelete = IsEditDelete;
    }

    public String getParentCustomerName() {
        return ParentCustomerName;
    }

    public void setParentCustomerName(String ParentCustomerName) {
        this.ParentCustomerName = ParentCustomerName;
    }

    public String getContactPersonId() {
        return ContactPersonId;
    }

    public void setContactPersonId(String ContactPersonId) {
        this.ContactPersonId = ContactPersonId;
    }

    public String getContactPersonName() {
        return ContactPersonName;
    }

    public void setContactPersonName(String ContactPersonName) {
        this.ContactPersonName = ContactPersonName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String CustomerID) {
        this.CustomerID = CustomerID;
    }

    public String getAddDateText() {
        return AddDateText;
    }

    public void setAddDateText(String AddDateText) {
        this.AddDateText = AddDateText;
    }

    public String getApproveStatusRemark() {
        return ApproveStatusRemark;
    }

    public void setApproveStatusRemark(String ApproveStatusRemark) {
        this.ApproveStatusRemark = ApproveStatusRemark;
    }

    public String getAddByName() {
        return AddByName;
    }

    public void setAddByName(String AddByName) {
        this.AddByName = AddByName;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String Designation) {
        this.Designation = Designation;
    }

    @Override
    public String toString() {
        return "ClassPojo [Phone = " + Phone + ", PageStartIndex = " + PageStartIndex + ", SiteAddress = " + SiteAddress + ", ApproveStatusName = " + ApproveStatusName + ", IsEditDelete = " + IsEditDelete + ", ParentCustomerName = " + ParentCustomerName + ", ContactPersonId = " + ContactPersonId + ", ContactPersonName = " + ContactPersonName + ", Email = " + Email + ", CustomerID = " + CustomerID + ", AddDateText = " + AddDateText + ", ApproveStatusRemark = " + ApproveStatusRemark + ", AddByName = " + AddByName + ", Designation = " + Designation + "]";
    }
}
