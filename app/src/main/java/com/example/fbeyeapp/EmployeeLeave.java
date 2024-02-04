package com.example.fbeyeapp;

public class EmployeeLeave {
    private String LeaveRequestId;
    private Long EmployeeId;
    private String EmployeeName;
    private String StartDate;
    private String EndDate;
    private String LeaveReason;
    private String AbsenceReason;
    private String CurrentDateTime;
    private String UserId;
    private String ApprovalStatus;
    private String Department;

    // Default constructor required for calls to DataSnapshot.getValue(EmployeeLeave.class)
    public EmployeeLeave() {
    }

    public EmployeeLeave(String leaveRequestId, Long employeeId, String employeeName,
                         String startDate, String endDate, String leaveReason,
                         String absenceReason, String currentDateTime, String userId,
                         String approvalStatus, String department) {
        LeaveRequestId = leaveRequestId;
        EmployeeId = employeeId;
        EmployeeName = employeeName;
        StartDate = startDate;
        EndDate = endDate;
        LeaveReason = leaveReason;
        AbsenceReason = absenceReason;
        CurrentDateTime = currentDateTime;
        UserId = userId;
        ApprovalStatus = approvalStatus;
        Department = department;
    }

    public String getLeaveRequestId() {
        return LeaveRequestId;
    }

    public void setLeaveRequestId(String leaveRequestId) {
        LeaveRequestId = leaveRequestId;
    }

    public Long getEmployeeId() {
        return EmployeeId;
    }

    public void setEmployeeId(Long employeeId) {
        EmployeeId = employeeId;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getLeaveReason() {
        return LeaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        LeaveReason = leaveReason;
    }

    public String getAbsenceReason() {
        return AbsenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        AbsenceReason = absenceReason;
    }

    public String getCurrentDateTime() {
        return CurrentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        CurrentDateTime = currentDateTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }
}
