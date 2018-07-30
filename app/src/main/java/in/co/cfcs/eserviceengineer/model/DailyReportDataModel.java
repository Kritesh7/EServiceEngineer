package in.co.cfcs.eserviceengineer.model;

public class DailyReportDataModel {
    private String Servicetime;

    private String NextFollowUpTimeText;

    private String DailyReportDateText;

    private String IsDelete;

    private String ComplainNo;

    private String IsEdit;

    private String Workdone;

    private String Workdone1;

    private String NextFollowUpDateText;

    private String DailyReportNo;

    private String Traveltime;

    public DailyReportDataModel(String Servicetime, String NextFollowUpTimeText, String DailyReportDateText, String IsDelete,
                                String ComplainNo, String IsEdit, String Workdone, String Workdone1, String NextFollowUpDateText,
                                String DailyReportNo, String Traveltime) {
    }

    public String getServicetime() {
        return Servicetime;
    }

    public void setServicetime(String Servicetime) {
        this.Servicetime = Servicetime;
    }

    public String getNextFollowUpTimeText() {
        return NextFollowUpTimeText;
    }

    public void setNextFollowUpTimeText(String NextFollowUpTimeText) {
        this.NextFollowUpTimeText = NextFollowUpTimeText;
    }

    public String getDailyReportDateText() {
        return DailyReportDateText;
    }

    public void setDailyReportDateText(String DailyReportDateText) {
        this.DailyReportDateText = DailyReportDateText;
    }

    public String getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(String IsDelete) {
        this.IsDelete = IsDelete;
    }

    public String getComplainNo() {
        return ComplainNo;
    }

    public void setComplainNo(String ComplainNo) {
        this.ComplainNo = ComplainNo;
    }

    public String getIsEdit() {
        return IsEdit;
    }

    public void setIsEdit(String IsEdit) {
        this.IsEdit = IsEdit;
    }

    public String getWorkdone() {
        return Workdone;
    }

    public void setWorkdone(String Workdone) {
        this.Workdone = Workdone;
    }

    public String getWorkdone1() {
        return Workdone1;
    }

    public void setWorkdone1(String Workdone1) {
        this.Workdone1 = Workdone1;
    }

    public String getNextFollowUpDateText() {
        return NextFollowUpDateText;
    }

    public void setNextFollowUpDateText(String NextFollowUpDateText) {
        this.NextFollowUpDateText = NextFollowUpDateText;
    }

    public String getDailyReportNo() {
        return DailyReportNo;
    }

    public void setDailyReportNo(String DailyReportNo) {
        this.DailyReportNo = DailyReportNo;
    }

    public String getTraveltime() {
        return Traveltime;
    }

    public void setTraveltime(String Traveltime) {
        this.Traveltime = Traveltime;
    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [Servicetime = "+Servicetime+", NextFollowUpTimeText = "+NextFollowUpTimeText+", DailyReportDateText = "+DailyReportDateText+", IsDelete = "+IsDelete+", ComplainNo = "+ComplainNo+", IsEdit = "+IsEdit+", Workdone = "+Workdone+", Workdone1 = "+Workdone1+", NextFollowUpDateText = "+NextFollowUpDateText+", DailyReportNo = "+DailyReportNo+", Traveltime = "+Traveltime+"]";
//    }
}
