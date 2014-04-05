package oldschool.LegoRemote;

/**
 * Created by hanjo on 04.04.14.
 */

import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.application.implementation.AbstractTinkerforgeApplication;
import com.tinkerforge.Device;

public class LegoRemoteApplication extends AbstractTinkerforgeApplication {

	private MasterApplication masterApplication;
	private MotorApplication motorApplication;

	public LegoRemoteApplication() {
		this.masterApplication = new MasterApplication();
		this.motorApplication = new MotorApplication();
		super.addTinkerforgeApplication(motorApplication);
	}

	@Override
	public void deviceDisconnected(
			final TinkerforgeStackAgent tinkerforgeStackAgent,
			final Device device) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deviceConnected(
			final TinkerforgeStackAgent tinkerforgeStackAgent,
			final Device device) {
		// TODO Auto-generated method stub

	}

	private int latestx;
	private int latesty;

	public void setVelocity(int x, int y) {
		if (this.latestx != x) {
			this.latestx = x;
			this.motorApplication.setVelocityDC1(x * 250);
		}
		if (this.latesty != y) {
			this.latesty = y;
			this.motorApplication.setVelocityDC2(y * 250);
		}
	}

	public void fullStop(){
		this.latestx=0;
		this.latesty=0;
		this.motorApplication.fullStop();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
