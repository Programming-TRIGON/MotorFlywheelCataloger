package frc.robot.flywheel;

import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.Notifier;

public class SparkMaxFlywheel extends Flywheel {
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;

    public SparkMaxFlywheel(int deviceId) {
        motor = new CANSparkMax(deviceId, CANSparkMax.MotorType.kBrushless);
        encoder = motor.getEncoder();
        configureMotor();
        configureEncoder();
    }

    @Override
    protected void setVoltage(double voltage) {
        motor.setVoltage(voltage);
    }

    @Override
    protected double getPosition() {
        return encoder.getPosition();
    }

    @Override
    protected double getVelocity() {
        return encoder.getVelocity();
    }

    @Override
    protected double getVoltage() {
        return motor.getAppliedOutput() * motor.getBusVoltage();
    }

    @Override
    protected double getAmperage() {
        return motor.getOutputCurrent();
    }

    private void configureMotor() {
        motor.restoreFactoryDefaults();
        motor.enableVoltageCompensation(12);
        motor.setSmartCurrentLimit(200);
        motor.setSecondaryCurrentLimit(200);

        for (CANSparkLowLevel.PeriodicFrame mode : CANSparkLowLevel.PeriodicFrame.values())
            motor.setPeriodicFramePeriod(mode, 10);

        new Notifier(motor::burnFlash).startPeriodic(3);
    }

    private void configureEncoder() {
        encoder.setPositionConversionFactor(1);
        encoder.setVelocityConversionFactor(1);
    }
}
