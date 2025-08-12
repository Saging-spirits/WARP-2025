package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Feed extends SubsystemBase {
    private final SparkMax motorFront = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax motorBack = new SparkMax(8, MotorType.kBrushless);

    private final DigitalInput frontSensor = new DigitalInput(1);
    private final DigitalInput backSensor = new DigitalInput(0);

    // public final Trigger front() {
    //     return new Trigger(() -> !frontSensor.get());
    // }

    public final Trigger front = new Trigger(() -> !frontSensor.get());
    

    // public final Trigger back() {
    //     return new Trigger(() -> !backSensor.get());
    // }

    public final Trigger back = new Trigger(() -> !backSensor.get());


    @Override
    public void periodic() {
        NetworkTableInstance.getDefault().getEntry("Front").setBoolean(front.getAsBoolean());
        NetworkTableInstance.getDefault().getEntry("Back").setBoolean(back.getAsBoolean());
    }

    public Command intakeFront(DoubleSupplier prop) {
        return run(() -> {
            motorFront.setVoltage(4 * prop.getAsDouble());
            motorBack.setVoltage(0);
        });
    }

    public Command outtakeFront(DoubleSupplier prop) {
        return run(() -> {
            motorFront.setVoltage(4 * prop.getAsDouble());
        });
    }

    public Command stop() {
        return run(() -> {
            motorBack.setVoltage(0);
            motorFront.setVoltage(0);
        });
    }

    public Command stopFront() {
        return run(() -> {
            motorFront.setVoltage(0);
        });
    }

    public Command intakeBack(DoubleSupplier prop) {
        return run(() -> {
            motorBack.setVoltage(-4 * prop.getAsDouble());
            motorFront.setVoltage(0);
        });
    }

    public Command outtakeBack(DoubleSupplier prop) {
        return run(() -> {
            motorBack.setVoltage(4 * prop.getAsDouble());
        });
    }

    public Command stopBack() {
        return run(() -> {
            motorBack.setVoltage(0);
        });
    }

    public Command intakeBoth() {
        return run(() -> {
            motorFront.setVoltage(3);
            motorBack.setVoltage(-3);
        });
    }
}
