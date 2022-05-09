package com.nashobarobotics.nashobalib.wrappers.subsystems.tankdrive;

//used to represent joystick input using move (y axis) and turn (x axis)
public class TankDriveJoystickValues{
    public double movement;
    public double turning;

    public TankDriveJoystickValues(double movement, double turning){
        this.movement = movement;
        this.turning = turning;
    }
}