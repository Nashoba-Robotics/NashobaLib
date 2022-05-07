package com.nashobarobotics.nashobalib;

public class PIDConfig {

    public double KF;
    public double KP;
    public double KI;
    public double KD;

    public PIDConfig(
        double KF, double KP,
        double KI, double KD
    ) {
        this.KF = KF;
        this.KP = KP;
        this.KI = KI;
        this.KD = KD;
    }
    
}
