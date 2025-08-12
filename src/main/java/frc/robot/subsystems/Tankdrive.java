package frc.robot.subsystems;

import java.util.function.DoubleSupplier;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

// LED signaller imports
import frc.robot.subsystems.LedSignaller;
import frc.robot.subsystems.LedSignaller.Pattern;

public class Tankdrive extends SubsystemBase {
    private final SparkMax driveLeft = new SparkMax(3, MotorType.kBrushed);
    private final SparkMax driveRight = new SparkMax(5, MotorType.kBrushed);

    // Reference to LED signaller (set from RobotContainer)
    private LedSignaller mLed = null;
    public void setLed(LedSignaller led) { this.mLed = led; }

    public Command Drive(DoubleSupplier Joy1Y, DoubleSupplier Joy2Y) {
        return run(() -> {
            // Tank drive voltage mapping
            final double leftCmdVolts  = 5 * Joy1Y.getAsDouble(); // Left motor voltage
            final double rightCmdVolts = -5 * Joy2Y.getAsDouble(); // Right motor voltage

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
