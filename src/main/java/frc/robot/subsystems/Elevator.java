package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {

public enum SetPoint {
    L1 (1),
    L2 (1),
    L3 (1),
    Reset (0);

    private double _position;
    SetPoint(double position ) {
        this ._position = position;
    }
public double Get () {
    return this ._position;
}
}

private SparkMax Motor=new SparkMax(0,MotorType.kBrushless);
private RelativeEncoder encoder = Motor.getEncoder();

private PIDController FirstStageController = new PIDController(1, 0, 0);
public Command GoToSetpoint (SetPoint setpoint){
    return run(() ->{
    Motor.setVoltage(Math.min(FirstStageController.calculate(encoder.getPosition(), setpoint.Get()),12));
    });
}
}
