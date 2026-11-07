package com.bits.leave;

public class LeaveCalculator {

    public int calculateRemainingLeave(int totalLeave, int usedLeave) {
        return totalLeave - usedLeave;
    }

    public double calculateEncashment(int unusedLeave, double dailySalary) {
        return unusedLeave * dailySalary;
    }
}
