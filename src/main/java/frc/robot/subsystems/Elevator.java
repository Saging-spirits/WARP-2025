package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Elevator extends SubsystemBase {
private final SparkMax Motor = new SparkMax(2, MotorType.kBrushless);
public final RelativeEncoder encoder = Motor.getEncoder();
public final PIDController Loop1 = new PIDController(1.5, 0, 0); //PID GAINS
public boolean goingForTransfer = false;
private final DigitalInput atPosition = new DigitalInput(2);

public Trigger atPositionTrigger() {
    return new Trigger(() -> atPosition.get());
}

public Trigger atSetpoint() { 
    return new Trigger(() -> Loop1.atSetpoint());
}
public Elevator() {
    Loop1.setTolerance(0.25);
}

@Override
public void periodic() {
    NetworkTableInstance.getDefault().getEntry("Elevator Setpoint").setBoolean(atSetpoint().getAsBoolean());
    NetworkTableInstance.getDefault().getEntry("Elevator Position").setNumber(encoder.getPosition());
}

public Command runUp() {
    return run(() -> {
        goingForTransfer = true;
        Motor.setVoltage(2);
    });
}

public Command GotoPos(double position, boolean _goingForTransfer) {
    return run(()->{
        double output = Loop1.calculate(encoder.getPosition(), position);
        double max = Math.max(output, -2);  //MAX AND MIN VOLTS
        double min = Math.min(max, 2);
        Motor.setVoltage(min);

        goingForTransfer = _goingForTransfer;
 NetworkTableInstance.getDefault().getEntry("Elevator Error").setNumber(Loop1.getError());

    });
}

public Command GotoPosSlow(double position, boolean _goingForTransfer) {
    return run(()->{
        double output = Loop1.calculate(encoder.getPosition(), position);
        double max = Math.max(output, -1); // DO NOT TOUCH!!
        double min = Math.min(max, 1);
        Motor.setVoltage(min);

        goingForTransfer = _goingForTransfer;

 NetworkTableInstance.getDefault().getEntry("Elevator Error").setNumber(Loop1.getError());
 NetworkTableInstance.getDefault().getEntry("Elevator Output").setNumber(min);


    });
}

public Command Stop() {
    return run(()->{
        Motor.setVoltage(0);
    });
}
}

