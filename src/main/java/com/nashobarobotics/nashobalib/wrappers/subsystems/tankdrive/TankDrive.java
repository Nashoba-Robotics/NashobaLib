package com.nashobarobotics.nashobalib.wrappers.subsystems.tankdrive;

import java.util.ArrayList;
import java.util.List;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
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
    protected TalonFX leftLeader;
    protected List<TalonFX> leftFollowers = new ArrayList<>();

    // these are all of the right motors
    protected TalonFX rightLeader;
    protected List<TalonFX> rightFollowers = new ArrayList<>();

    // USE THIS CONSTRUCTOR IF NOT SPECIFYING CONFIGURATIONS
    // ports[] should follow format {leftLeader, leftFollowers...} and {rightLeader, rightFollowers...}
    // CANBus specifies which CAN bus the drivetrain motors are using
    public TankDrive(
        int[] leftPorts, int[] rightPorts,
        String CANBus, TankDriveSide invertedSide
    ) {

        leftLeader = new TalonFX(leftPorts[0], CANBus);
        for(int i = 1; i < leftPorts.length; i++){
            leftFollowers.add(new TalonFX(leftPorts[i], CANBus));
            leftFollowers.get(i - 1).follow(leftLeader);
        }

        rightLeader = new TalonFX(rightPorts[0], CANBus);
        for(int i = 1; i < rightPorts.length; i++){
            rightFollowers.add(new TalonFX(rightPorts[i], CANBus));
            rightFollowers.get(i - 1).follow(rightLeader);
        }

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

        leftLeader = new TalonFX(leftPorts[0], CANBus);
        for(int i = 1; i < leftPorts.length; i++){
            leftFollowers.add(new TalonFX(leftPorts[i], CANBus));
            leftFollowers.get(i - 1).follow(leftLeader);
        }

        rightLeader = new TalonFX(rightPorts[0], CANBus);
        for(int i = 1; i < rightPorts.length; i++){
            rightFollowers.add(new TalonFX(rightPorts[i], CANBus));
            rightFollowers.get(i - 1).follow(rightLeader);
        }

        config(config, neutralMode, invertedSide);
        
    }

    public void config(TankDriveConfig config, NeutralMode neutralMode, TankDriveSide invertedSide) {

        leftLeader.configFactoryDefault();
        leftLeader.selectProfileSlot(0, 0);
        leftLeader.configAllSettings(config.leftConfig);
        leftLeader.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0);
        leftLeader.setSelectedSensorPosition(0);
        leftLeader.clearStickyFaults();

        rightLeader.configFactoryDefault();
        rightLeader.selectProfileSlot(0, 0);
        rightLeader.configAllSettings(config.rightConfig);
        rightLeader.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0);
        rightLeader.setSelectedSensorPosition(0);
        rightLeader.clearStickyFaults();

        invertSide(invertedSide);
        setNeutralMode(neutralMode);
        configPID(config.leftPID, config.rightPID);

    }

    public void configPID(PIDConfig leftPID, PIDConfig rightPID) {
        leftLeader.config_kF(0, leftPID.KF);
        leftLeader.config_kP(0, leftPID.KP);
        leftLeader.config_kI(0, leftPID.KI);
        leftLeader.config_kD(0, leftPID.KD);

        rightLeader.config_kF(0, rightPID.KF);
        rightLeader.config_kP(0, rightPID.KP);
        rightLeader.config_kI(0, rightPID.KI);
        rightLeader.config_kD(0, rightPID.KD);
    }

    // this will set the side that is specified as inverted
    private void invertSide(TankDriveSide side) {
        switch(side) {
            case LEFT:
                leftLeader.setInverted(InvertType.InvertMotorOutput);
                for(TalonFX motor : leftFollowers) 
                    motor.setInverted(InvertType.FollowMaster);

                rightLeader.setInverted(InvertType.None);
                for(TalonFX motor : rightFollowers) 
                    motor.setInverted(InvertType.FollowMaster);

                break;
            case RIGHT:
                rightLeader.setInverted(InvertType.InvertMotorOutput);
                for(TalonFX motor : rightFollowers) 
                    motor.setInverted(InvertType.FollowMaster);

                leftLeader.setInverted(InvertType.None);
                for(TalonFX motor : leftFollowers) 
                    motor.setInverted(InvertType.FollowMaster);

                break;
            case NONE:
                leftLeader.setInverted(InvertType.None);
                for(TalonFX motor : leftFollowers) 
                    motor.setInverted(InvertType.FollowMaster);

                rightLeader.setInverted(InvertType.None);
                for(TalonFX motor : rightFollowers) 
                    motor.setInverted(InvertType.FollowMaster);

                break;
        }
    }

    public void setLeft(ControlMode mode, double input) {
        leftLeader.set(mode, input);
    }

    public void setRight(ControlMode mode, double input) {
        rightLeader.set(mode, input);
    }

    public void set(ControlMode mode, double input) {
        setLeft(mode, input);
        setRight(mode, input);
    }

    public void setLeft(ControlMode mode, double input, double aff) {
        aff *= Math.signum(input);
        leftLeader.set(mode, input, DemandType.ArbitraryFeedForward, aff);
    }

    public void setRight(ControlMode mode, double input, double aff) {
        aff *= Math.signum(input);
        rightLeader.set(mode, input, DemandType.ArbitraryFeedForward, aff);
    }

    public void set(ControlMode mode, double input, double aff) {
        setLeft(mode, input, aff);
        setRight(mode, input, aff);
    }

    public double getLeftMotorVelocity() {
        return leftLeader.getSelectedSensorVelocity(0);
    }

    public double getRightMotorVelocity() {
        return rightLeader.getSelectedSensorVelocity(0);
    }

    public double getLeftPosition() {
        return leftLeader.getSelectedSensorPosition(0);
    }

    public double getRightPosition() {
        return rightLeader.getSelectedSensorPosition(0);
    }

    public double getLeftStatorCurrent() {
        return leftLeader.getStatorCurrent();
    }

    public double getRightStatorCurrent() {
        return rightLeader.getStatorCurrent();
    }

    public double getLeftSupplyCurrent() {
        return leftLeader.getSupplyCurrent();
    }

    public double getRightSupplyCurrent() {
        return rightLeader.getSupplyCurrent();
    }

    public void setNeutralMode(NeutralMode neutralMode) {
        leftLeader.setNeutralMode(neutralMode);
        for(TalonFX motor : leftFollowers)
            motor.setNeutralMode(neutralMode);

        rightLeader.setNeutralMode(neutralMode);
        for(TalonFX motor : rightFollowers)
            motor.setNeutralMode(neutralMode);
    }
    
}
