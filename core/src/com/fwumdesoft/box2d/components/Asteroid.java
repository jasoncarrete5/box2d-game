package com.fwumdesoft.box2d.components;

import com.badlogic.ashley.core.Component;

/**
 * Marks an entity as an asteroid.
 */
public class Asteroid implements Component {
	public static enum Type {
		big(25), medium(30), small(35);
		
		public final float SPEED; // m/s
		
		private Type(float speed) {
			SPEED = speed;
		}
	}
	
	public Type type;
}
