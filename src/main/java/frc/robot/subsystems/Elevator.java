package frc.robot.subsystems;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Elevator extends SubsystemBase {

public enum SetPoint {
Transport(0),
Eject(2.5),
EjectStraight(8.5),
Collection(13.14);

    private double _position;
    SetPoint(double position ) {
        this ._position = position;
    }
public double Get () {
    return this ._position;
}
}

private SetPoint last = SetPoint.Transport;
private SparkMax Motor=new SparkMax(6,MotorType.kBrushless);
private RelativeEncoder encoder = Motor.getEncoder();

private PIDController FirstStageController = new PIDController(0.2, 0, 0);
public Command GoToSetpoint (SetPoint setpoint){
    return run(() ->{
        last = setpoint;
    Motor.setVoltage(Math.max(Math.min(FirstStageController.calculate(encoder.getPosition(), setpoint.Get()),12), -12));
    });
}
public Command Stop() {
    return run(() ->{
        Motor.setVoltage(0);
    });
}

public Command Default() {
    return GoToSetpoint(last);
}
}
