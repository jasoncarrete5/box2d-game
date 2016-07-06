package com.fwumdesoft.box2d.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IntervalIteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.fwumdesoft.box2d.components.Physics;

public class PhysicsSystem extends IntervalIteratingSystem {
	public static final float TIME_STEP = 1 / 60f;
	
	private static ComponentMapper<Physics> physicsMapper = ComponentMapper.getFor(Physics.class);
	private World world;
	
	public PhysicsSystem(World world) {
		super(Family.all(Physics.class).get(), TIME_STEP);
		this.world = world;
	}

	@Override
	protected void updateInterval() {
		world.step(TIME_STEP, 6, 2);
		super.updateInterval();
	}
	
	@Override
	protected void processEntity(Entity entity) {
		Physics physics = physicsMapper.get(entity);
		if(physics.body.getUserData() instanceof Sprite) {
			//update sprite information
			Sprite sprite = (Sprite)physics.body.getUserData();
			Transform transform = physics.body.getTransform();
			sprite.setPosition(transform.getPosition().x, transform.getPosition().y);
			sprite.setRotation(transform.getRotation() * MathUtils.radDeg);
		} else {
			Gdx.app.debug("PhysicsSystem", "A body may be missing a sprite user object...");
		}
	}
}
