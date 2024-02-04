package com.example.fbeyeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<employeeAttendance> employeeAttendanceList;

    public MyAdapter(Context context, ArrayList<employeeAttendance> employeeLeaveList) {
        this.context = context;
        this.employeeAttendanceList = employeeLeaveList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.attendance_list, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        employeeAttendance employeeAttendance = employeeAttendanceList.get(position);
        String date = employeeAttendance.getDate().toString();
        holder.Date.setText(date);
        String clockInString = employeeAttendance.getClockIn().toString();
        String clockOutString = employeeAttendance.getClockOut().toString();
        holder.ClockIn.setText(clockInString);
        holder.ClockOut.setText(clockOutString);
    }

    @Override
    public int getItemCount() {
        return employeeAttendanceList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView Date;

        TextView ClockIn;

        TextView ClockOut;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Date = itemView.findViewById(R.id.employeeDate);
            ClockIn = itemView.findViewById(R.id.employeeClockIn);
            ClockOut = itemView.findViewById(R.id.employeeClockOut);
        }
    }
}
