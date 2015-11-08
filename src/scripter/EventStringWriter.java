package scripter;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

public class EventStringWriter extends StringWriter {
	
	private Boolean isEvent = false;
	private ActionEvent event = new ActionEvent(this, 0, "New Line");
	List<ActionListener> listeners = new LinkedList<>();
	
	public void addActionListener(ActionListener listener)
	{
		listeners.add(listener);
	}
	
	public void removeActionListener(ActionListener listener)
	{
		listeners.remove(listener);
	}
	
	public Boolean getIsEvent() {
		return isEvent;
	}

	public void setIsEvent(Boolean isEvent) {
		this.isEvent = isEvent;
	}

	@Override
	public void write(String str) {
		super.write(str);
		if(getIsEvent())
			for(ActionListener i: listeners)
				i.actionPerformed(event);
		
	}	

}