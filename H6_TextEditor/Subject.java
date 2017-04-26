
import java.util.ArrayList;
import java.util.List;

public class Subject 
{
	//a list of observers that will be notified when changes occur
	private List < Observer > observers;
	
	public Subject()
	{

		observers = new ArrayList <Observer> ();
	}
	
	//used by observers to tell the subject it is interested in updates
	public void attach(Observer o)
	{
		observers.add(o);
	}
	
	//used by the observers to discontinue updates
	public void detach(Observer o)
	{
		observers.remove(o);
	}
	
	//called each time the subject changes, notifying all attached observers that they should get the new subject state from their references
	public void notifyObservers()
	{

		for(Observer o : observers)
		{
			//tell it to update itself
			o.update();
		}
	}
}