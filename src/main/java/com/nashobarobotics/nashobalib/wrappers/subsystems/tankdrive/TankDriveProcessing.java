package com.nashobarobotics.nashobalib.wrappers.subsystems.tankdrive;

public class TankDriveProcessing {

    public static double scaleJoystick(double value, double deadzone) {
        if(Math.abs(value) < deadzone) {
            return 0;
        } else {
            return Math.signum(value) * (Math.abs(value) - deadzone) / (1 - deadzone);
        }
    }

    // Apply scaling to the turning and movement inputs
    public static TankDriveJoystickValues scaleJoysticks(TankDriveJoystickValues joystickValues, double deadzone) {
        double movementScaled = scaleJoystick(joystickValues.movement, deadzone);
        double turningScaled = scaleJoystick(joystickValues.turning, deadzone);
        return new TankDriveJoystickValues(movementScaled, turningScaled);
    }

    // Process the joystick value to make the controls easier to use
    // Equation: 
    public static double shapeJoystick(double value, double sensetivity) {
        return Math.pow(Math.abs(value), sensetivity) * Math.signum(value);
    }

    // Apply shaping to the turning and movement inputs
    // Sensitivity represents the exponent (any number) of the joystick values (between -1 & 1)
    public static TankDriveJoystickValues shapeJoysticks(
        TankDriveJoystickValues joystickValues,
        double moveSensitivity, double turnSensitivity
    ) {
        double movementShaped = shapeJoystick(joystickValues.movement, moveSensitivity);
        double turningShaped = shapeJoystick(joystickValues.turning, turnSensitivity);
        return new TankDriveJoystickValues(movementShaped, turningShaped);
    }

    public static TankDriveJoystickValues shapeJoysticksRadiusDrive(
        TankDriveJoystickValues joystickValues,
        double moveSensitivity, double turnSensitivity
    ) {
        double movementShaped = shapeJoystick(joystickValues.movement, moveSensitivity);
        double turningShaped = shapeJoystick(joystickValues.turning, turnSensitivity);
        return new TankDriveJoystickValues(movementShaped, turningShaped);
    }

    // Combine the movement and turing values to calculate
    // the left and right motor speeds
    public static TankDriveMotorValues arcadeDrive(TankDriveJoystickValues joystickValues) {
        double left = joystickValues.movement + joystickValues.turning;
        double right = joystickValues.movement - joystickValues.turning;
        // If either side is above 1, divide by the max amount
        // to limit the maximum speed to 1 while preserving turn angle
        if(Math.abs(left) > 1 || Math.abs(right) > 1) {
            double factor = Math.max(Math.abs(left), Math.abs(right));
            left /= factor;
            right /= factor;
        }
        return new TankDriveMotorValues(left, right);
    }

    public static TankDriveMotorValues arcadeDrive(TankDriveJoystickValues joystickValues, boolean invertedDrive) {
        // if(joystickValues.movement < 0) {
        //     joystickValues.turning *= -1;
        // }
        double left;
        double right;
        if(!invertedDrive){
            left = joystickValues.movement + joystickValues.turning;
            right = joystickValues.movement - joystickValues.turning;
            // If either side is above 1, divide by the max amount
            // to limit the maximum speed to 1 while preserving turn angle
            if(Math.abs(left) > 1 || Math.abs(right) > 1) {
                double factor = Math.max(Math.abs(left), Math.abs(right));
                left /= factor;
                right /= factor;
            }
        }
        else{
            left = joystickValues.movement - joystickValues.turning;
            right = joystickValues.movement + joystickValues.turning;
            // If either side is above 1, divide by the max amount
            // to limit the maximum speed to 1 while preserving turn angle
            if(Math.abs(left) > 1 || Math.abs(right) > 1) {
                double factor = Math.max(Math.abs(left), Math.abs(right));
                left /= factor;
                right /= factor;
            }
        }
        return new TankDriveMotorValues(left, right);
    }

    //returns MotorValues that allow robot to turn at a constant radius
    public static TankDriveMotorValues radiusDrive(TankDriveJoystickValues joystickValues) {
        double left, right;
        double turning = joystickValues.turning*Math.abs(joystickValues.movement);
        if(joystickValues.movement > 0) {
            left = joystickValues.movement + turning;
            right = joystickValues.movement - turning;
        } else {
            left = joystickValues.movement - turning;
            right = joystickValues.movement + turning;
        }
        if(Math.abs(left) > 1 || Math.abs(right) > 1) {
            double factor = Math.max(Math.abs(left), Math.abs(right));
            left /= factor;
            right /= factor;
        }
        return new TankDriveMotorValues(left, right);
    }

    // Apply all joystick processing
    public static TankDriveMotorValues processJoysticksArcadeDrive(
        TankDriveJoystickValues joystickValues, double deadzone,
        double moveSensitivity, double turnSensitivity
    ) {
        TankDriveJoystickValues scaledJoysticks = scaleJoysticks(joystickValues, deadzone);
        TankDriveJoystickValues shapedJoysticks = shapeJoysticks(scaledJoysticks, moveSensitivity, turnSensitivity);
        TankDriveMotorValues motorSpeeds = arcadeDrive(shapedJoysticks);
        return motorSpeeds;
    }

    public static TankDriveMotorValues processJoysticksRadiusDrive(
        TankDriveJoystickValues joystickValues, double deadzone,
        double moveSensitivity, double turnSensitivity
    ) {
        TankDriveJoystickValues scaledJoysticks = scaleJoysticks(joystickValues, deadzone);
        TankDriveJoystickValues shapedJoysticks = shapeJoysticksRadiusDrive(scaledJoysticks, moveSensitivity, turnSensitivity);
        TankDriveMotorValues motorSpeeds = radiusDrive(shapedJoysticks);
        return motorSpeeds;
    }
}