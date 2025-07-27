package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Arm extends SubsystemBase {
private final SparkMax Motor = new SparkMax(7, MotorType.kBrushless);
private final RelativeEncoder encoder = Motor.getEncoder();
private final PIDController Loop1 = new PIDController(0.05, 0, 0);

public Arm() {
    Loop1.setTolerance(2);
}

public Command GotoPos(double position) {
    return run(()->{
        double output = Loop1.calculate(encoder.getPosition(), position);
        double max = Math.max(output, -12);
        double min = Math.min(max, 12);
        Motor.setVoltage(min);

        System.out.println(Loop1.atSetpoint());

        NetworkTableInstance.getDefault().getEntry("Arm Error").setNumber(Loop1.getError());
    }).until(() -> Loop1.atSetpoint());
}

public Command Stop() {
    return run(()->{
        Motor.setVoltage(0);
    });
}
}
