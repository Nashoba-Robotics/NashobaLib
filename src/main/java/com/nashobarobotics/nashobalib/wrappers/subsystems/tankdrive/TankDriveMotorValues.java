package com.nashobarobotics.nashobalib.wrappers.subsystems.tankdrive;

//used to represent the final motor values that will be sent to the robot (left & right)
public class TankDriveMotorValues{
    public double left;
    public double right;

    public TankDriveMotorValues(double left, double right){
        this.left = left;
        this.right = right;
    }
}