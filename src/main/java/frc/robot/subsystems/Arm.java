package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Arm extends SubsystemBase {
private final SparkMax Motor = new SparkMax(7, MotorType.kBrushless);
private final RelativeEncoder encoder = Motor.getEncoder();
// private final PIDController Loop1 = new PIDController(0.55, 0, 0);
private final PIDController Loop1 = new PIDController(0.25, 0, 0);
private final PIDController Loop2 = new PIDController(0.65, 0, 0);

public Trigger atSetpoint() { 
    return new Trigger(() -> Loop1.atSetpoint());
}

public Trigger atSetpoint2() { 
    return new Trigger(() -> Loop2.atSetpoint());
}


public Arm() {
    Loop1.setTolerance(0.5);
    Loop2.setTolerance(0.5);
}

public Command GotoPos(double position) {
    return run(()->{
        double output = Loop1.calculate(encoder.getPosition(), position);
        double max = Math.max(output, -4);
        double min = Math.min(max, 4);
        Motor.setVoltage(min);

        NetworkTableInstance.getDefault().getEntry("Arm Error").setNumber(Loop1.getError());
        NetworkTableInstance.getDefault().getEntry("Arm At Setpoint").setBoolean(Loop1.atSetpoint());

    });
}

public Command GotoPosSlow(double position) {
    return run(()->{
        double output = Loop2.calculate(encoder.getPosition(), position);
        double max = Math.max(output, -3);
        double min = Math.min(max, 3);
        Motor.setVoltage(min);

        NetworkTableInstance.getDefault().getEntry("Arm Error").setNumber(Loop2.getError());
        NetworkTableInstance.getDefault().getEntry("Arm At Setpoint").setBoolean(Loop2.atSetpoint());

    });
}


public Command Stop() {
    return run(()->{
        Motor.setVoltage(0);
    });
}
}
