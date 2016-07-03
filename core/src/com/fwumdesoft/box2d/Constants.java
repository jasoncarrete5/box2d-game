package com.fwumdesoft.box2d;

import com.badlogic.gdx.math.MathUtils;

/**
 * Contains various constants.
 */
public class Constants {
	/** Change in player rotation. */
	public static final float PLAYER_ANGULAR_ACC = MathUtils.PI2; // rad/s^2
	public static final float MAX_PLAYER_ANGULAR_SPEED = MathUtils.PI2 * 2; // rad/s
	
	/** Player change in speed. */
	public static final float PLAYER_ACC = 30f; // m/s^2
	public static final float MAX_PLAYER_SPEED = 50f; // m/s
	
	public static final short PLAYER_CATEGORY_BITS = 1 << 1;
}
