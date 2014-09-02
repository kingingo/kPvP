package me.kingingo.kpvp;

import lombok.Getter;
import me.kingingo.kcore.kListener;

public class FameManager extends kListener{

	@Getter
	private kPvP manager;
	
	public FameManager(kPvP manager){
		super(manager.getInstance(),"[FameManager]");
		this.manager=manager;
		
	}
	
}
