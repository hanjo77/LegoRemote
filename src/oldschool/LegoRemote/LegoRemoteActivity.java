package oldschool.LegoRemote;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import ch.quantasy.tinkerforge.tinker.agency.implementation.TinkerforgeStackAgency;
import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgent;
import ch.quantasy.tinkerforge.tinker.agent.implementation.TinkerforgeStackAgentIdentifier;

public class LegoRemoteActivity extends Activity
{
	private LegoRemoteApplication application;
	private boolean isPressed;
	private float[] speed = { 0, 0 };
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		try {

			TinkerforgeStackAgentIdentifier identifier1 = new TinkerforgeStackAgentIdentifier("localhost");
			TinkerforgeStackAgentIdentifier identifier2 = new TinkerforgeStackAgentIdentifier("MasterBrick01");
			TinkerforgeStackAgent agent1 = TinkerforgeStackAgency.getInstance().getStackAgent(identifier1);
			TinkerforgeStackAgent agent2 =TinkerforgeStackAgency.getInstance().getStackAgent(identifier2);

			application = new LegoRemoteApplication();
			agent1.addApplication(application);
			agent2.addApplication(application);
		}
		catch (Exception e)
		{
			System.out.println("Could not connect " + e.getStackTrace());
		}

		setContentView(R.layout.main);
		GridLayout gridLayout = (GridLayout) findViewById(R.id.controlGrid);
		int childIndex = 0;
		ImageView imgView = (ImageView) gridLayout.getChildAt(childIndex);
		while (imgView != null)
		{
			imgView.setOnTouchListener(new View.OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					float[] pos = {event.getX(), event.getY()};
					if ((pos[0] > 0) && (pos[0] < v.getWidth())
							&& (pos[1] > 0) && (pos[1] < v.getHeight()))
					{
						if (event.getAction() == MotionEvent.ACTION_DOWN)
						{
							imagePress(v);
						}
						else if (event.getAction() == MotionEvent.ACTION_UP)
						{
							imageRelease(v);
						}
					} else
					{
						imageRelease(v);
					}
					if (isPressed)
					{
						setSpeed(v, pos);
					}
					System.out.println(speed[0] + " - " + speed[1]);
					application.setVelocity((int) speed[0], (int) speed[1]);
					return true;
				}
			});
			imgView = (ImageView) gridLayout.getChildAt(++childIndex);
		}
	}

	public void imagePress(View view)
	{
		ImageView button = (ImageView) view;
		switch(button.getId())
		{
			case R.id.buttonUp:
				swapImage(button, R.drawable.button_up_active);
				break;
			case R.id.buttonDown:
				swapImage(button, R.drawable.button_down_active);
				break;
			case R.id.buttonLeft:
				swapImage(button, R.drawable.button_left_active);
				break;
			case R.id.buttonRight:
				swapImage(button, R.drawable.button_right_active);
				break;
			case R.id.buttonStop:
				swapImage(button, R.drawable.button_stop_active);
				break;
		}
		isPressed = true;
	}

	public void setSpeed(View view, float[] pos)
	{
		switch(view.getId())
		{
			case R.id.buttonUp:
				speed[1] = view.getHeight()-pos[1];
				break;
			case R.id.buttonDown:
				speed[1] = -1*pos[1];
				break;
			case R.id.buttonLeft:
				speed[0] = -1*(view.getWidth()-pos[0]);
				break;
			case R.id.buttonRight:
				speed[0] = pos[0];
				break;
			case R.id.buttonStop:
				stop();
				break;
		}
	}

	public void imageRelease(View view)
	{
		stop();
		ImageView button = (ImageView) view;
		switch(button.getId())
		{
			case R.id.buttonDown:
				swapImage(button, R.drawable.button_down);
				break;
			case R.id.buttonLeft:
				swapImage(button, R.drawable.button_left);
				break;
			case R.id.buttonRight:
				swapImage(button, R.drawable.button_right);
				break;
			case R.id.buttonUp:
				swapImage(button, R.drawable.button_up);
				break;
			case R.id.buttonStop:
				swapImage(button, R.drawable.button_stop);
				break;
		}
		isPressed = false;
	}

	private void stop()
	{
		speed = new float[] { 0, 0 };
		try
		{
			application.fullStop();
		}
		catch (Exception e)
		{
			System.out.println("Could not connect to motor bricks!");
		}
	}

	private void swapImage(ImageView button, int resourceId)
	{
		Bitmap image = BitmapFactory.decodeResource(this.getResources(), resourceId);
		button.setImageBitmap(image);
	}
}
