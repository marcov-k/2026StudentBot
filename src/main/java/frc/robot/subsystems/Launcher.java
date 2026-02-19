package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import frc.robot.Constants.MotorConstants;
import frc.robot.Constants.LauncherConstants;

public class Launcher extends SubsystemBase
{
    SparkMax m_motor;
    double motorSpeed = 1;

    @SuppressWarnings("removal")
    public Launcher()
    {
        m_motor = new SparkMax(LauncherConstants.kLauncherMotorID, MotorType.kBrushless);
        m_motor.configure(MotorConstants.coastMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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
