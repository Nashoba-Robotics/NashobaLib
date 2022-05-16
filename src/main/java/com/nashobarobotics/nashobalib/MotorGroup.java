package com.nashobarobotics.nashobalib;

import java.util.List;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXInvertType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;

public class MotorGroup {

    private TalonFX leader;
    private List<TalonFX> motorGroup;

    public MotorGroup(int[] ports){

        leader = new TalonFX(ports[0]);

        for(int i = 1; i < ports.length; i++) {
            motorGroup.add(new TalonFX(ports[i]));
        }
    }

    public MotorGroup(int[] ports, String canBus){

        leader = new TalonFX(ports[0], canBus);

        for(int i = 1; i < ports.length; i++) {
            motorGroup.add(new TalonFX(ports[i], canBus));
            motorGroup.get(motorGroup.size() - 1).follow(leader);
        }
    }

    public void config(TalonFXConfiguration config, PIDConfig pidConfig) {

        leader.configFactoryDefault();
        leader.selectProfileSlot(0, 0);
        leader.configAllSettings(config);
        leader.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, 0);
        leader.setSelectedSensorPosition(0);
        leader.clearStickyFaults();

        leader.config_kF(0, pidConfig.KF);
        leader.config_kP(0, pidConfig.KP);
        leader.config_kI(0, pidConfig.KI);
        leader.config_kD(0, pidConfig.KD);

    }

    public void setNeutralMode(NeutralMode neutralMode) {
        leader.setNeutralMode(neutralMode);

        for(TalonFX motor : motorGroup) {
            motor.setNeutralMode(neutralMode);
        }
    }

    public void setNeutralMode(NeutralMode[] neutralModes) {
        leader.setNeutralMode(neutralModes[0]);

        for(int i = 1; i < neutralModes.length; i++) {
            if(i >= motorGroup.size()){
                System.err.println("MotorGroup.setNeutralMode given a parameter with more neutralModes than motors");
                break;
            }
            motorGroup.get(i).setNeutralMode(neutralModes[i]);
        }
    }

    public void setInverted(TalonFXInvertType invertType) {
        leader.setInverted(invertType);

        for(TalonFX motor : motorGroup) {
            motor.setInverted(invertType);
        }
    }

    public void setInverted(TalonFXInvertType[] invertTypes) {
        leader.setInverted(invertTypes[0]);

        for(int i = 1; i < invertTypes.length; i++) {
            if(i >= motorGroup.size()){
                System.err.println("MotorGroup.setInverted given a parameter with more invertTypes than motors");
                break;
            }
            motorGroup.get(i).setInverted(invertTypes[i]);
        }
    }
    
    public void setInverted(boolean invertType) {
        leader.setInverted(invertType);

        for(TalonFX motor : motorGroup) {
            motor.setInverted(invertType);
        }
    }

    public void setInverted(boolean[] invertTypes) {
        leader.setInverted(invertTypes[0]);

        for(int i = 1; i < invertTypes.length; i++) {
            if(i >= motorGroup.size()){
                System.err.println("MotorGroup.setInverted given a parameter with more invertTypes than motors");
                break;
            }
            motorGroup.get(i).setInverted(invertTypes[i]);
        }
    }

    public void set(ControlMode controlMode, double value) {
        leader.set(controlMode, value);
    }

    public double getPosition() {
        return leader.getSelectedSensorPosition();
    }

    public void setPosition(double position) {
        leader.setSelectedSensorPosition(position);
    }

    public double getVelocity() {
        return leader.getSelectedSensorVelocity();
    }

    public double getStatorCurrent() {
        return leader.getStatorCurrent();
    }

    public double getSupplyCurrent() {
        return leader.getSupplyCurrent();
    }
}
