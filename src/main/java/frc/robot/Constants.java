// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

public final class Constants
{
    public static class OperatorConstants
    {
        public static final int kDriverControllerPort = 0;
    }

    public static class LauncherConstants
    {
        public static final int kLauncherMotorID = 11;
    }

    public static class IntakeConstants
    {
        public static final int kIntakeLowerMotorID = 9;
        public static final int kIntakeUpperMotorID = 10;
    }

    public static class DriveConstants
    {
        // Driving Parameters 
        public static final double kMaxSpeedMPS = 4.8; // Default is 4.8 meters per second     
        public static final double kMaxAngularSpeed = 2 * Math.PI; // Default is 2 PI radians (one full rotation) per second 

        public static final double kDirectionSlewRate = 1.2; // radians per second
        public static final double kMagnitudeSlewRate = 1.8; // percent per second (1 = 100%)
        public static final double kRotationalSlewRate = 2.0; // percent per second (1 = 100%)

        // Chassis configuration
        public static final double kTrackWidth = Units.inchesToMeters(23);
        public static final double kWheelBase = Units.inchesToMeters(30.25);

        // Relative positions from center
        public static final SwerveDriveKinematics kDriveKinematics = new SwerveDriveKinematics(
        new Translation2d(kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(kWheelBase / 2, -kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, kTrackWidth / 2),
        new Translation2d(-kWheelBase / 2, -kTrackWidth / 2));

        // Angular offsets of the modules relative to the chassis in radians
        public static final double kFlOffset = -Math.PI / 2;
        public static final double kFrOffset = 0;
        public static final double kRlOffset = Math.PI;
        public static final double kRrOffset = Math.PI / 2;

        // SparkMax and SparkFlex CAN ID's
        public static final int kFlDriveId = 1;
        public static final int kFrDriveId = 2;
        public static final int kRrDriveId = 3;
        public static final int kRlDriveId = 4;
        public static final int kFlTurnId = 5;
        public static final int kFrTurnId = 6;
        public static final int kRrTurnId = 7;
        public static final int kRlTurnId = 8;

        public static final double kUnitstoFeet = 4.2;
    }
}
