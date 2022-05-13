package com.nashobarobotics.nashobalib.NRWrappers.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import java.util.ArrayList;

public class Climb {
    private ArrayList<TalonFX> climbers;
    private static enum LimitSwitchConfig {
        FORWARD,
        REVERSE
    }
    
    public Climb(TalonFX... talons){
        climbers = new ArrayList<>();
        for(TalonFX talon : talons){
            climbers.add(talon);
        }
    }


    
    //Climber Methods:
    //Zeroes the climber(s)
    public void zero(){
        
    };

    //The robot deploys the climber(s)
    public void deploy(){

    };

    //The robot pulls itself up
    public void climb(){

    };



    //Config:
    //Configures each motor
    public void config(){
        
    }



    //Limit Switches:
    //Configures a limit switch on a specified motor that is normally closed
    //Requires a limit switch direction, which is either FORWARD or REVERSE
    //Forward is if the motor has to run in a positive direction in order to hit the limit switch
    //Reverse is if the motor has to run in a negative direction in order to hit the limit switch
    public void configClosedLimitSwitch(int index, LimitSwitchConfig limitSwitchConfig){
        TalonFX motor = climbers.get(index);
        switch(limitSwitchConfig){
            case FORWARD:
                motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
                break;

            case REVERSE:
                motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
                break;
        }
    }

    public void configClosedLimitSwitch(LimitSwitchConfig limitSwitchConfig){
        configClosedLimitSwitch(0, limitSwitchConfig);
    }

    //Similar to configClosedLimitSwitch, but configures a limit switch that is usually open instead of closed
    public void configOpenLimitSwitch(int index, LimitSwitchConfig limitSwitchConfig){
        TalonFX motor = climbers.get(index);
        switch(limitSwitchConfig){
            case FORWARD:
                motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
                break;

            case REVERSE:
                motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);
                break;
        }
    }

    public void configOpenLimitSwitch(LimitSwitchConfig limitSwitchConfig){
        configOpenLimitSwitch(0, limitSwitchConfig);
    }

    //Sets the soft limit of a motor in the forward direction
    public void configForwardSoftLimit(int index, double forwardSensorLimit){
        climbers.get(index).configForwardSoftLimitEnable(true);
        climbers.get(index).configForwardSoftLimitThreshold(forwardSensorLimit);
    }

    public void configForwardSoftLimit(double forwardSensorLimit){
        configForwardSoftLimit(0, forwardSensorLimit);
    }

    //Disables the soft limit of a forward motor
    public void disableForwardSoftLimit(int index){
        climbers.get(index).configForwardSoftLimitEnable(false);
    }

    public void disableForwardSoftLimit(){
        disableForwardSoftLimit(0);
    }

    //Sets the soft limit of a motor in the reverse direction
    public void configReverseSoftLimit(int index, double reverseSensorLimit){
        climbers.get(index).configReverseSoftLimitEnable(true);
        climbers.get(index).configReverseSoftLimitThreshold(reverseSensorLimit);
    }

    public void configReverseSoftLimit(double reverseSensorLimit){
        configReverseSoftLimit(0, reverseSensorLimit);
    }

    //Disables the soft limit of a reverse motor
    public void disableReverseSoftLimit(int index){
        climbers.get(index).configReverseSoftLimitEnable(false);
    }

    public void disableReverseSoftLimit(){
        disableReverseSoftLimit(0);
    }



    //Using climbers:
    //Uses motion magic to set the climber to specified NU
    public void set(int index, int nu){ 
        climbers.get(index).set(ControlMode.MotionMagic, nu);
    }
    //If no climber is specified, use the first one
    public void set(int nu){
        set(0, nu);
    }
    //Set function that can also set the cruise velocity and acceleration
    public void set(int index, double cruiseVelocity, double acceleration, int nu){
        setCruise(index, cruiseVelocity);
        setAccel(index, acceleration);
        set(index, nu);
    }

    public void set(double cruiseVelocity, double acceleration, int nu){
        set(0, cruiseVelocity, acceleration, nu);
    }

    //Runs the motors using Percent Output for testing
    public void test(int index, double speed){
        climbers.get(index).set(ControlMode.PercentOutput, speed);
    }

    public void test(double speed){
        test(0, speed);
    }

    //Returns the native units of the specified climber
    public double getNU(int index){
        return climbers.get(index).getSelectedSensorPosition();
    }

    public double getNU(){
        return getNU(0);
    }

    //Sets the cruise velocity of the motion magic for the specified motor
    public void setCruise(int index, double sensorUnitsPer100ms){
        climbers.get(index).configMotionCruiseVelocity(sensorUnitsPer100ms);
    }

    public void setCruise(double sensorUnitsPer100ms){
        setCruise(0, sensorUnitsPer100ms);
    }

    //Sets the max acceleration of the motion magic for the specified motor
    public void setAccel(int index, double sensorUnitsPer100ms){
        climbers.get(index).configMotionAcceleration(sensorUnitsPer100ms);
    }

    public void setAccel(double sensorUnitsPer100ms){
        setAccel(0, sensorUnitsPer100ms);
    }
}
