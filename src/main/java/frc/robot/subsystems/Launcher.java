package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants;

public class Launcher extends SubsystemBase
{
    SparkMax m_motor;
    double motorSpeed = 0.25;

    public Launcher()
    {
        m_motor = new SparkMax(Constants.LauncherConstants.kLauncherMotorID, MotorType.kBrushless);
    }

    public Command launchCommand()
    {
        return Commands.sequence(
            Commands.runOnce(() -> this.m_motor.set(this.motorSpeed)),
            Commands.waitSeconds(0.5)
        ).finallyDo(() ->
        {
            this.m_motor.stopMotor();
        });
    }
}
