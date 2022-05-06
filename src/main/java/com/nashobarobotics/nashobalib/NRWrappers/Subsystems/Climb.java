package com.nashobarobotics.nashobalib.NRWrappers.Subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import java.util.ArrayList;

public abstract class Climb {
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

    public abstract void zer0();    //Zeroes the climber(s)
    public abstract void deploy();  //The robot deploys the climber(s)
    public abstract void climb();   //The robot pulls itself up

    public void config(){
        TalonFX motor = new TalonFX(0);
        motor.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyClosed);
    }

    public void configLimitSwitch(int index, LimitSwitchConfig fuckBenLess){
        TalonFX motor = climbers.get(index);
        
    }

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

    //Gets the native units of the specified climber
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
