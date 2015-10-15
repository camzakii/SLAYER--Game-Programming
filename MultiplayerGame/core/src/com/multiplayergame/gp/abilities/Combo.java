package com.multiplayergame.gp.abilities;

public class Combo {

	private float combo_timer;
	private int combo_count;
	
	public Combo(){
		
		this.combo_timer = 0f;
		this.combo_count = 0;
	}
	
	public void update(float dt){
		if(combo_timer != 0) combo_timer--;
		else combo_count = 0;
	}
	
	public float getTimer(){
		return combo_timer;
	}
	
	public void setTimer(float timer){
		combo_timer = timer;
	}
	
	public void setComboCount(){
		combo_count++;
	}
	
	public void setCombo(){
		combo_timer = 30;
		combo_count++;
	}
	
	public int getComboCount(){
		return combo_count;
	}
	
}
