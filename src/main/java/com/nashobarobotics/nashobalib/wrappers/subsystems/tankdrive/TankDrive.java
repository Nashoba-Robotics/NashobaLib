package com.nashobarobotics.nashobalib.wrappers.subsystems.tankdrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.nashobarobotics.nashobalib.MotorGroup;
import com.nashobarobotics.nashobalib.PIDConfig;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// this is a bare necessity class, containing just enough to work by itself
// for best use, extend in order to implement the rest of the needs for the drive train
public class TankDrive extends SubsystemBase {

    public enum TankDriveSide {
        LEFT,
        RIGHT,
        NONE
    }
    
    // these are all of the left motors
    protected MotorGroup leftMotors;

    // these are all of the right motors
    protected MotorGroup rightMotors;

    // USE THIS CONSTRUCTOR IF NOT SPECIFYING CONFIGURATIONS
    // ports[] should follow format {leftLeader, leftFollowers...} and {rightLeader, rightFollowers...}
    // CANBus specifies which CAN bus the drivetrain motors are using
    public TankDrive(
        int[] leftPorts, int[] rightPorts,
        String CANBus, TankDriveSide invertedSide
    ) {

        leftMotors = new MotorGroup(leftPorts, CANBus);
        rightMotors = new MotorGroup(rightPorts, CANBus);

        invertSide(invertedSide);
        
    }

    // USE THIS CONSTRUCTOR IF SPECIFYING CONFIGURATIONS
    // ports[] should follow format {leftLeader, leftFollowers...} and {rightLeader, rightFollowers...}
    // CANBus specifies which can bus the drivetrain motors are using
    public TankDrive(
        int[] leftPorts, int[] rightPorts,
        String CANBus, TankDriveSide invertedSide,
        TankDriveConfig config, NeutralMode neutralMode
    ) {

        leftMotors = new MotorGroup(leftPorts, CANBus);
        rightMotors = new MotorGroup(rightPorts, CANBus);

        config(config, neutralMode, invertedSide);
        
    }

    public void config(TankDriveConfig config, NeutralMode neutralMode, TankDriveSide invertedSide) {

        leftMotors.config(config.leftConfig, config.leftPID);
        rightMotors.config(config.rightConfig, config.rightPID);

        invertSide(invertedSide);
        setNeutralMode(neutralMode);

    }

    public void configPID(PIDConfig leftPID, PIDConfig rightPID) {
        leftMotors.configPID(leftPID);
        rightMotors.configPID(rightPID);
    }

    // this will set the side that is specified as inverted
    private void invertSide(TankDriveSide side) {
        switch(side) {
            case LEFT:
                leftMotors.setInverted(InvertType.InvertMotorOutput);
                rightMotors.setInverted(InvertType.None);

                break;
            case RIGHT:
                leftMotors.setInverted(InvertType.None);
                rightMotors.setInverted(InvertType.InvertMotorOutput);

                break;
            case NONE:
                leftMotors.setInverted(InvertType.None);
                rightMotors.setInverted(InvertType.None);

                break;
        }
    }

    public void setLeft(ControlMode mode, double input) {
        leftMotors.set(mode, input);
    }

    public void setRight(ControlMode mode, double input) {
        rightMotors.set(mode, input);
    }

    public void set(ControlMode mode, double input) {
        setLeft(mode, input);
        setRight(mode, input);
    }

    public void setLeft(ControlMode mode, double input, double aff) {
        aff *= Math.signum(input);
        leftMotors.set(mode, input, DemandType.ArbitraryFeedForward, aff);
    }

    public void setRight(ControlMode mode, double input, double aff) {
        aff *= Math.signum(input);
        rightMotors.set(mode, input, DemandType.ArbitraryFeedForward, aff);
    }

    public void set(ControlMode mode, double input, double aff) {
        setLeft(mode, input, aff);
        setRight(mode, input, aff);
    }

    public double getLeftMotorVelocity() {
        return leftMotors.getVelocity();
    }

    public double getRightMotorVelocity() {
        return rightMotors.getVelocity();
    }

    public double getLeftPosition() {
        return leftMotors.getPosition();
    }

    public double getRightPosition() {
        return rightMotors.getPosition();
    }

    public double getLeftStatorCurrent() {
        return leftMotors.getStatorCurrent();
    }

    public double getRightStatorCurrent() {
        return rightMotors.getStatorCurrent();
    }

    public double getLeftSupplyCurrent() {
        return leftMotors.getSupplyCurrent();
    }

    public double getRightSupplyCurrent() {
        return rightMotors.getSupplyCurrent();
    }

    public void setNeutralMode(NeutralMode neutralMode) {
        leftMotors.setNeutralMode(neutralMode);
        rightMotors.setNeutralMode(neutralMode);
    }
    
}