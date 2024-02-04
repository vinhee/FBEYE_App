package com.example.fbeyeapp;

import android.widget.TextView;

public class EmployeeLeaveMC {
    private String LeaveID;
    private Long Employeeid;
    private String Name;
    private String StartDate;
    private String EndDate;
    private String LeaveReason;
    private String AbsenceReason;
    private String MCNo;
    private String MedicalCentre;

    public String getLeaveID() {
        return LeaveID;
    }

    public void setLeaveID(String leaveID) {
        LeaveID = leaveID;
    }

    public Long getEmployeeid() {
        return Employeeid;
    }

    public void setEmployeeid(Long employeeid) {
        Employeeid = employeeid;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getMCNo() {
        return MCNo;
    }

    public void setMCNo(String MCNo) {
        this.MCNo = MCNo;
    }

    public String getMedicalCentre() {
        return MedicalCentre;
    }

    public void setMedicalCentre(String medicalCentre) {
        MedicalCentre = medicalCentre;
    }

    public String getSubmitDate() {
        return SubmitDate;
    }

    public void setSubmitDate(String submitDate) {
        SubmitDate = submitDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    private String SubmitDate;
    private String userID;
    private String ApprovalStatus;
    private String Department;

    // Empty constructor for Firebase
    public EmployeeLeaveMC() {
    }

    // Constructor with parameters
    public EmployeeLeaveMC(String LeaveID, Long Employeeid, String Name, String StartDate, String EndDate, String AbsenceReason, String SubmitDate, String userID, String ApprovalStatus, String Department, String McNo, String MedicalCentre) {
        this.LeaveID = LeaveID;
        this.Employeeid = Employeeid;
        this.Name = Name;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.AbsenceReason = AbsenceReason;
        this.SubmitDate = SubmitDate;
        this.userID = userID;
        this.ApprovalStatus = ApprovalStatus;
        this.Department = Department;
        this.MCNo = McNo;
        this.MedicalCentre = MedicalCentre;
    }
}
