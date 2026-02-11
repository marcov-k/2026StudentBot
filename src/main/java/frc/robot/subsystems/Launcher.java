package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants.LauncherConstants;

public class Launcher extends SubsystemBase
{
    SparkMax m_motor;
    double motorSpeed = 0.6;

    public Launcher()
    {
        m_motor = new SparkMax(LauncherConstants.kLauncherMotorID, MotorType.kBrushless);
    }

    public Command launch()
    {
        return Commands.runOnce(() -> {
            m_motor.set(motorSpeed);
        });
    }

    public Command stop()
    {
        return Commands.runOnce(() -> {
            m_motor.set(0);
        });
    }
}
