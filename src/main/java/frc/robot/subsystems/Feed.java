package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feed extends SubsystemBase {
    private final SparkMax motor = new SparkMax(8, MotorType.kBrushless);

    public Command intake(DoubleSupplier prop) {
        return run(() -> {
            motor.setVoltage(8 * prop.getAsDouble());
        });
    }

    public Command outtake(DoubleSupplier prop) {
        return run(() -> {
            motor.setVoltage(-8 * prop.getAsDouble());
        });
    }

    public Command stop() {
        return run(() -> {
            motor.setVoltage(0);
        });
    }
}
