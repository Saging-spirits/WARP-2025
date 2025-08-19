package frc.robot.subsystems;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// LED signaller imports
import frc.robot.subsystems.LedSignaller;
import frc.robot.subsystems.LedSignaller.Pattern;

public class Tankdrive extends SubsystemBase {
    private final SparkMax driveLeft = new SparkMax(3, MotorType.kBrushed);
    private final SparkMax driveRight = new SparkMax(5, MotorType.kBrushed);

    public final RelativeEncoder encoderLeft = driveLeft.getEncoder();
    public final RelativeEncoder encoderRight = driveRight.getEncoder();

    // Reference to LED signaller (set from RobotContainer)
    private LedSignaller mLed = null;
    public void setLed(LedSignaller led) { this.mLed = led; }

    @Override
    public void periodic() {
        NetworkTableInstance.getDefault().getEntry("LeftEncoder").setNumber(encoderLeft.getPosition());
        NetworkTableInstance.getDefault().getEntry("RightEncoder").setNumber(encoderRight.getPosition());
    }

    public Command Drive(DoubleSupplier Joy1Y, DoubleSupplier Joy2Y,BooleanSupplier boost, BooleanSupplier slow) {
        return run(() -> {
            // Tank drive voltage mapping
            double leftCmdVolts  = 5 * Joy1Y.getAsDouble(); // Left motor voltage
            double rightCmdVolts = -5 * Joy2Y.getAsDouble(); // Right motor voltage

            if(boost.getAsBoolean()==true){
                leftCmdVolts=leftCmdVolts*2;
                rightCmdVolts=rightCmdVolts*2;
            }

            if(slow.getAsBoolean()==true){
                leftCmdVolts=leftCmdVolts*0.5;
                rightCmdVolts=rightCmdVolts*0.5;
            }

            // LED pattern selection
            if (mLed != null) {
                final double deadbandVolts = 5 * 0.05; // ~10% stick movement
                final double leftMag  = Math.abs(leftCmdVolts);
                final double rightMag = Math.abs(rightCmdVolts);

                Pattern p = Pattern.IDLE;

                if (leftMag > deadbandVolts) {
                    driveLeft.setVoltage(leftCmdVolts);
                } else {
                    driveLeft.setVoltage(0);
                }
                if (rightMag > deadbandVolts) {
                    driveRight.setVoltage(rightCmdVolts);
                } else {
                    driveRight.setVoltage(0);
                }

                if ((rightCmdVolts > 0 && rightMag > deadbandVolts) && (leftCmdVolts < 0 && leftMag > deadbandVolts)) { p = Pattern.FORWARDS; }
                if ((rightCmdVolts < 0 && rightMag > deadbandVolts) && (leftCmdVolts > 0 && leftMag > deadbandVolts)) { p = Pattern.BACKWARDS; }
                if ((leftCmdVolts < 0 && leftMag > deadbandVolts) && (rightCmdVolts < 0 && rightMag > deadbandVolts)) { p = Pattern.REV_FWD; }

                if ((leftCmdVolts > 0 && leftMag > deadbandVolts) && (rightCmdVolts > 0 && rightMag > deadbandVolts)) { p = Pattern.FWD_REV; }

                if ((leftCmdVolts < 0 && leftMag > deadbandVolts) && (rightMag < deadbandVolts)) { p = Pattern.SIDE2_FWD; }
                if ((leftCmdVolts > 0 && leftMag > deadbandVolts) && (rightMag < deadbandVolts)) { p = Pattern.SIDE2_REV; }

                if ((rightCmdVolts > 0 && rightMag > deadbandVolts) && (leftMag < deadbandVolts)) { p = Pattern.SIDE1_FWD; }
                if ((rightCmdVolts < 0 && rightMag > deadbandVolts) && (leftMag < deadbandVolts)) { p = Pattern.SIDE1_REV; }

                mLed.setPattern(p);
            }
        });
    }
}
