package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.XboxController;

public class Launcher extends SubsystemBase
{
    SparkMax m_motor;
    double motorSpeed = 0.6;

    public Launcher()
    {
        m_motor = new SparkMax(Constants.LauncherConstants.kLauncherMotorID, MotorType.kBrushless);
    }

    public Command launchCommand(XboxController controller)
    {
        return Commands.run(() -> {
            double speed = 0;
            if (controller.getAButton()) speed = motorSpeed;
            this.m_motor.set(speed);
        }, this);
    }
}
