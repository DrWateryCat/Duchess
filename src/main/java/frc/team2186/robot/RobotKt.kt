package frc.team2186.robot

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import edu.wpi.first.wpilibj.IterativeRobot

class Robot : IterativeRobot(){
    val motor = WPI_TalonSRX(0)
    val motor2 = WPI_TalonSRX(1)
    val ticksPerRev = 1440.0
    var currentState = 0

    fun ticksToDegrees(ticks: Double): Double{
        return ticks*360.0/ticksPerRev
    }

    fun angleToTicks(degrees: Double) = degrees * ticksPerRev/360.0
    override fun robotInit() {
        motor.apply {
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            config_kP(0, 0.5,0)
        }

        motor2.apply {
            follow(motor)
        }
    }

    fun stateMachine(): Double{
        return when(currentState){
            0 -> {
                if(motor.getSelectedSensorPosition(0) >= angleToTicks(90.0)) {
                    currentState++
                }
                angleToTicks(90.0)
            }
            1 -> {
                if(motor.getSelectedSensorPosition(0) >= angleToTicks(0.0)) {
                    currentState++
                }
                angleToTicks(0.0)
            }
            2 -> {
                if(motor.getSelectedSensorPosition(0) >= angleToTicks(180.0)) {
                    currentState++
                }
                angleToTicks(180.0)
            }
            else -> {
                currentState=0
                angleToTicks(0.0)
            }
        }
    }
    override fun teleopPeriodic() {
        motor.set(ControlMode.Position, stateMachine())
    }

}