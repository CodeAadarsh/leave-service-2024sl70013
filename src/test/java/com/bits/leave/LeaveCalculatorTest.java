package com.bits.leave;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LeaveCalculatorTest {

    LeaveCalculator calculator = new LeaveCalculator();

    @Test
    public void testRemainingLeave() {
        assertEquals(20,
            calculator.calculateRemainingLeave(30, 10));
    }

    @Test
    public void testRemainingLeaveWhenAllUsed() {
        assertEquals(0,
            calculator.calculateRemainingLeave(20, 20));
    }

    @Test
    public void testLeaveEncashment() {
        assertEquals(10000.0,
            calculator.calculateEncashment(10, 1000.0),
            0.001);
    }
}
