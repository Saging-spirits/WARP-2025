package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj.PS5Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class Controller {
    public interface ControllerInner {
        Trigger leftBumper();
        Trigger rightBumper();

        Trigger leftTrigger();
        Trigger rightTrigger();
        double getLeftTrigger();
        double getRightTrigger();

        Trigger a();
        Trigger b();
        Trigger x();
        Trigger y();

        Trigger start();
        Trigger back();
        Trigger leftStick();
        Trigger rightStick();

        Trigger getPov(int angle);

        double getLeftX();
        double getLeftY();
        double getRightX();
        double getRightY();
    }

    private final ControllerInner inner;

    public Controller(int port) {
        GenericHID hid = new GenericHID(port);
        String name = hid.getName().toLowerCase();

        System.out.println("Controller name: " + name);
        if (name.contains("xbox") || name.contains("360")) {
            inner = new XboxInner(port);
        } else if (name.contains("ps4")) {
            inner = new PS4Inner(port);
        } else if (name.contains("ps5")) {
            inner = new PS5Inner(port);
        } else if (name.contains("nintendo") || name.contains("switch") || name.contains("nsw")) {
            inner = new NintendoInner(hid);
        } else {
            throw new IllegalArgumentException("Unknown controller type: " + name);
        }
    }

    public ControllerInner get() {
        return inner;
    }

    public Trigger a() { return inner.a(); }
    public Trigger b() { return inner.b(); }
    public Trigger x() { return inner.x(); }
    public Trigger y() { return inner.y(); }

    public Trigger leftBumper() { return inner.leftBumper(); }
    public Trigger rightBumper() { return inner.rightBumper(); }
    public Trigger leftTrigger() { return inner.leftTrigger(); }
    public Trigger rightTrigger() { return inner.rightTrigger(); }

    public double getLeftTrigger() { return inner.getLeftTrigger(); }
    public double getRightTrigger() { return inner.getRightTrigger(); }

    public Trigger start() { return inner.start(); }
    public Trigger back() { return inner.back(); }
    public Trigger leftStick() { return inner.leftStick(); }
    public Trigger rightStick() { return inner.rightStick(); }

    public Trigger getPov(int angle) { return inner.getPov(angle); }

    public double getLeftX() { return inner.getLeftX(); }
    public double getLeftY() { return inner.getLeftY(); }
    public double getRightX() { return inner.getRightX(); }
    public double getRightY() { return inner.getRightY(); }


    public static class XboxInner implements ControllerInner {
        private final XboxController controller;

        public XboxInner(int port) {
            controller = new XboxController(port);
        }

        public Trigger leftBumper() { return new Trigger(controller::getLeftBumper); }
        public Trigger rightBumper() { return new Trigger(controller::getRightBumper); }

        public Trigger leftTrigger() { return new Trigger(() -> controller.getLeftTriggerAxis() > 0.5); }
        public Trigger rightTrigger() { return new Trigger(() -> controller.getRightTriggerAxis() > 0.5); }
        public double getLeftTrigger() { return controller.getLeftTriggerAxis(); }
        public double getRightTrigger() { return controller.getRightTriggerAxis(); }

        public Trigger a() { return new Trigger(controller::getAButton); }
        public Trigger b() { return new Trigger(controller::getBButton); }
        public Trigger x() { return new Trigger(controller::getXButton); }
        public Trigger y() { return new Trigger(controller::getYButton); }

        public Trigger start() { return new Trigger(controller::getStartButton); }
        public Trigger back() { return new Trigger(controller::getBackButton); }
        public Trigger leftStick() { return new Trigger(controller::getLeftStickButton); }
        public Trigger rightStick() { return new Trigger(controller::getRightStickButton); }

        public Trigger getPov(int angle) { return new Trigger(() -> controller.getPOV() == angle); }

        public double getLeftX() { return controller.getLeftX(); }
        public double getLeftY() { return controller.getLeftY(); }
        public double getRightX() { return controller.getRightX(); }
        public double getRightY() { return controller.getRightY(); }
    }

    public static class PS4Inner implements ControllerInner {
        private final PS4Controller controller;

        public PS4Inner(int port) {
            controller = new PS4Controller(port);
        }

        public Trigger leftBumper() { return new Trigger(controller::getL1Button); }
        public Trigger rightBumper() { return new Trigger(controller::getR1Button); }

        public Trigger leftTrigger() { return new Trigger(() -> controller.getL2Axis() > 0.5); }
        public Trigger rightTrigger() { return new Trigger(() -> controller.getR2Axis() > 0.5); }
        public double getLeftTrigger() { return controller.getL2Axis(); }
        public double getRightTrigger() { return controller.getR2Axis(); }

        public Trigger a() { return new Trigger(controller::getCrossButton); }
        public Trigger b() { return new Trigger(controller::getCircleButton); }
        public Trigger x() { return new Trigger(controller::getSquareButton); }
        public Trigger y() { return new Trigger(controller::getTriangleButton); }

        public Trigger start() { return new Trigger(controller::getOptionsButton); }
        public Trigger back() { return new Trigger(controller::getShareButton); }
        public Trigger leftStick() { return new Trigger(controller::getL3Button); }
        public Trigger rightStick() { return new Trigger(controller::getR3Button); }

        public Trigger getPov(int angle) { return new Trigger(() -> controller.getPOV() == angle); }

        public double getLeftX() { return controller.getLeftX(); }
        public double getLeftY() { return controller.getLeftY(); }
        public double getRightX() { return controller.getRightX(); }
        public double getRightY() { return controller.getRightY(); }
    }

    public static class PS5Inner implements ControllerInner {
        private final PS5Controller controller;

        public PS5Inner(int port) {
            controller = new PS5Controller(port);
        }

        public Trigger leftBumper() { return new Trigger(controller::getL1Button); }
        public Trigger rightBumper() { return new Trigger(controller::getR1Button); }

        public Trigger leftTrigger() { return new Trigger(() -> controller.getL2Axis() > 0.5); }
        public Trigger rightTrigger() { return new Trigger(() -> controller.getR2Axis() > 0.5); }
        public double getLeftTrigger() { return controller.getL2Axis(); }
        public double getRightTrigger() { return controller.getR2Axis(); }

        public Trigger a() { return new Trigger(controller::getCrossButton); }
        public Trigger b() { return new Trigger(controller::getCircleButton); }
        public Trigger x() { return new Trigger(controller::getSquareButton); }
        public Trigger y() { return new Trigger(controller::getTriangleButton); }

        public Trigger start() { return new Trigger(controller::getOptionsButton); }
        public Trigger back() { return new Trigger(controller::getCreateButton); }
        public Trigger leftStick() { return new Trigger(controller::getL3Button); }
        public Trigger rightStick() { return new Trigger(controller::getR3Button); }

        public Trigger getPov(int angle) { return new Trigger(() -> controller.getPOV() == angle); }

        public double getLeftX() { return controller.getLeftX(); }
        public double getLeftY() { return controller.getLeftY(); }
        public double getRightX() { return controller.getRightX(); }
        public double getRightY() { return controller.getRightY(); }
    }

    public static class NintendoInner implements ControllerInner {
        private final GenericHID hid;

        public NintendoInner(GenericHID hid) {
            this.hid = hid;
        }

        public Trigger leftBumper() { return new Trigger(() -> hid.getRawButton(5)); }
        public Trigger rightBumper() { return new Trigger(() -> hid.getRawButton(6)); }

        public Trigger leftTrigger() { return new Trigger(() -> hid.getRawButton(7)); }
        public Trigger rightTrigger() { return new Trigger(() -> hid.getRawButton(8)); }
        public double getLeftTrigger() { return leftTrigger().getAsBoolean() ? 1.0 : 0.0; }
        public double getRightTrigger() { return rightTrigger().getAsBoolean() ? 1.0 : 0.0; }

        public Trigger a() { return new Trigger(() -> hid.getRawButton(2)); }
        public Trigger b() { return new Trigger(() -> hid.getRawButton(3)); }
        public Trigger x() { return new Trigger(() -> hid.getRawButton(1)); }
        public Trigger y() { return new Trigger(() -> hid.getRawButton(4)); }

        public Trigger start() { return new Trigger(() -> hid.getRawButton(10)); }
        public Trigger back() { return new Trigger(() -> hid.getRawButton(9)); }
        public Trigger leftStick() { return new Trigger(() -> hid.getRawButton(11)); }
        public Trigger rightStick() { return new Trigger(() -> hid.getRawButton(12)); }

        public Trigger getPov(int angle) { return new Trigger(() -> hid.getPOV() == angle); }

        public double getLeftX() { return hid.getRawAxis(0); }
        public double getLeftY() { return hid.getRawAxis(1); }
        public double getRightX() { return hid.getRawAxis(2); }
        public double getRightY() { return hid.getRawAxis(3); }
    }
}
