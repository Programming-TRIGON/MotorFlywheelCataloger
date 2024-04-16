package frc.robot.flywheel;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;

public class TalonFXFlywheel extends Flywheel {
    private final TalonFX motor;
    private final VoltageOut voltageRequest = new VoltageOut(0).withEnableFOC(true);
    private final StatusSignal<Double> positionSignal, velocitySignal, voltageSignal, currentSignal;

    public TalonFXFlywheel(int deviceId) {
        this(deviceId, "");
    }

    public TalonFXFlywheel(int deviceId, String canbus) {
        motor = new TalonFX(deviceId, canbus);

        motor.getConfigurator().apply(new TalonFXConfiguration());
        positionSignal = motor.getPosition();
        velocitySignal = motor.getVelocity();
        voltageSignal = motor.getMotorVoltage();
        currentSignal = motor.getStatorCurrent();
        positionSignal.setUpdateFrequency(100);
        velocitySignal.setUpdateFrequency(100);
        voltageSignal.setUpdateFrequency(100);
        currentSignal.setUpdateFrequency(100);
    }

    @Override
    protected void setVoltage(double voltage) {
        motor.setControl(voltageRequest.withOutput(voltage));
    }

    @Override
    protected double getPosition() {
        return positionSignal.refresh().getValue();
    }

    @Override
    protected double getVelocity() {
        return velocitySignal.refresh().getValue();
    }

    @Override
    protected double getVoltage() {
        return voltageSignal.refresh().getValue();
    }

    @Override
    protected double getAmperage() {
        return currentSignal.refresh().getValue();
    }
}
