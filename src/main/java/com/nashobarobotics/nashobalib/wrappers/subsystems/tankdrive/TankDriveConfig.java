package com.nashobarobotics.nashobalib.wrappers.subsystems.tankdrive;

import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.nashobarobotics.nashobalib.PIDConfig;

public class TankDriveConfig {
    
    public TalonFXConfiguration leftConfig;
    public TalonFXConfiguration rightConfig;

    public PIDConfig leftPID;
    public PIDConfig rightPID;

    public TankDriveConfig(
        TalonFXConfiguration leftConfig, TalonFXConfiguration rightConfig,
        PIDConfig leftPID, PIDConfig rightPID
    ) {
        this.leftConfig = leftConfig;
        this.rightConfig = rightConfig;

        this.leftPID = leftPID;
        this.rightPID = rightPID;
    }
}
