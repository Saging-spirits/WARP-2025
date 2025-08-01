package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Tankdrive extends SubsystemBase {
    private final SparkMax driveLeft = new SparkMax(1, MotorType.kBrushed);
    private final SparkMax driveRight = new SparkMax(3, MotorType.kBrushed);

    public Command Drive(DoubleSupplier Joy1Y, DoubleSupplier Joy2Y) {
        return run(()->{
            driveLeft.setVoltage(11*Joy1Y.getAsDouble());
            driveRight.setVoltage(11*Joy2Y.getAsDouble());
        });
    }
}
