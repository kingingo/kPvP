package me.kingingo.kpvp.Manager;

import lombok.Getter;
import lombok.Setter;
import me.kingingo.kpvp.kPvP;
import me.kingingo.kpvp.Events.IEvent;

public class kEventManager extends IPvPManager{

	@Setter
	@Getter
	public IEvent event;
	
	public kEventManager(kPvP PvP) {
		super(PvP);
		
	}
	
	public void onDisable(){
		if(event!=null)event.cancel();
	}

}
