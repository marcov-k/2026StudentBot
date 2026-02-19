// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.util.Units;

import com.revrobotics.spark.FeedbackSensor;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkMaxConfig;

public final class Constants
{
    public static class OperatorConstants
    {
        public static final int kDriverControllerPort = 0;
    }

    public static class MotorConstants
    {
        public static final SparkMaxConfig coastMotorConfig = new SparkMaxConfig();
        public static final SparkMaxConfig brakeMotorConfig = new SparkMaxConfig();
        static {
            coastMotorConfig.idleMode(IdleMode.kCoast);
            brakeMotorConfig.idleMode(IdleMode.kBrake);
        }
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

    @SuppressWarnings("removal")
    public static final class ModuleConstants
    {
        public static final int kDrivingMotorPinionTeeth = 14;
        public static final double kDrivingMotorFreeSpeedRps = 94.6;
        public static final double kWheelDiameterMeters = 0.0762;
        public static final double kWheelCircumferenceMeters = kWheelDiameterMeters * Math.PI;
        public static final double kDrivingMotorReduction = (45.0 * 22) / (kDrivingMotorPinionTeeth * 15);
        public static final double kDriveWheelFreeSpeedRps = (kDrivingMotorFreeSpeedRps * kWheelCircumferenceMeters) / kDrivingMotorReduction;
        public static final SparkFlexConfig drivingConfig = new SparkFlexConfig();
        public static final SparkMaxConfig turningConfig = new SparkMaxConfig();
        static {
            double drivingFactor = kWheelCircumferenceMeters / kDrivingMotorReduction;
            double turningFactor = 2 * Math.PI;
            double drivingVelocityFeedForward = 1 / kDriveWheelFreeSpeedRps;

            drivingConfig.idleMode(IdleMode.kCoast).smartCurrentLimit(50);
            drivingConfig.encoder.positionConversionFactor(drivingFactor).velocityConversionFactor(drivingFactor / 60.0);
            drivingConfig.closedLoop.feedbackSensor(FeedbackSensor.kPrimaryEncoder).pid(0.04,0,0).velocityFF(drivingVelocityFeedForward).outputRange(-1,1);

            turningConfig.idleMode(IdleMode.kBrake).smartCurrentLimit(20);
            turningConfig.absoluteEncoder.inverted(true).positionConversionFactor(turningFactor).velocityConversionFactor(turningFactor / 60.);
            turningConfig.closedLoop.feedbackSensor(FeedbackSensor.kAbsoluteEncoder).pid(1,0,0).outputRange(-1, 1).positionWrappingEnabled(true).positionWrappingInputRange(0, turningFactor);
        }
    }
}
