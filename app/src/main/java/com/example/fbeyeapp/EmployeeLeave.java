package com.example.fbeyeapp;

import android.widget.TextView;

public class EmployeeLeave {
    private String leaveRequestId;
    private Long employeeId;
    private String employeeName;
    private String startDate;
    private String endDate;
    private String leaveReason;
    private TextView absenceReason;
    private String leaveID;
    private String department;

    public String getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(String leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public TextView getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(TextView absenceReason) {
        this.absenceReason = absenceReason;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public void setLeaveID(String leaveID) {
        this.leaveID = leaveID;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    private String submitDate;
    private String userID;
    private String approvalStatus;

    public EmployeeLeave(String leaveID, Long employeeId, String employeeName, String startDate, String endDate, String leaveReason, TextView absenceReason, String submitDate, String userID, String approvalStatus, String department) {
        this.leaveID = leaveID;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveReason = leaveReason;
        this.absenceReason = absenceReason;
        this.submitDate = submitDate;
        this.userID = userID;
        this.approvalStatus = approvalStatus;
        this.department = department;
    }





}
