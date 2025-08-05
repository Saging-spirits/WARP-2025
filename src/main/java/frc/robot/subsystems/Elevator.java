package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Elevator extends SubsystemBase {
private final SparkMax Motor = new SparkMax(2, MotorType.kBrushless);
public final RelativeEncoder encoder = Motor.getEncoder();
public final PIDController Loop1 = new PIDController(0.3, 0, 0);
public boolean goingForTransfer = false;


public Trigger atSetpoint() { 
    return new Trigger(() -> Loop1.atSetpoint());
}
public Elevator() {
    Loop1.setTolerance(1.3);
}

@Override
public void periodic() {
    NetworkTableInstance.getDefault().getEntry("Elevator Setpoint").setBoolean(atSetpoint().getAsBoolean());
}

public Command GotoPos(double position, boolean _goingForTransfer) {
    return run(()->{
        double output = Loop1.calculate(encoder.getPosition(), position);
        double max = Math.max(output, -6);
        double min = Math.min(max, 6);
        Motor.setVoltage(min);

        goingForTransfer = _goingForTransfer;
 NetworkTableInstance.getDefault().getEntry("Elevator Error").setNumber(Loop1.getError());

    });
}

public Command GotoPosSlow(double position, boolean _goingForTransfer) {
    return run(()->{
        double output = Loop1.calculate(encoder.getPosition(), position);
        double max = Math.max(output, -3);
        double min = Math.min(max, 3);
        Motor.setVoltage(min);

        goingForTransfer = _goingForTransfer;

 NetworkTableInstance.getDefault().getEntry("Elevator Error").setNumber(Loop1.getError());

    });
}

public Command Stop() {
    return run(()->{
        Motor.setVoltage(0);
    });
}
}

