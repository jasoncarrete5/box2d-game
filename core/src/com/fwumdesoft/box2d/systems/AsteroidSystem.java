package com.fwumdesoft.box2d.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fwumdesoft.box2d.App;
import com.fwumdesoft.box2d.App.Assets;
import com.fwumdesoft.box2d.Constants;
import com.fwumdesoft.box2d.components.Asteroid;
import com.fwumdesoft.box2d.components.Asteroid.Type;
import com.fwumdesoft.box2d.components.Physics;
import com.fwumdesoft.box2d.components.Render;

public class AsteroidSystem extends IntervalSystem {
	public static final float SPAWN_DELAY = 2.5f; // Seconds between asteroid spawns
	
	private int maxAsteroids, curAsteroids; // Since asteroids break, these values refer to big asteroids only
	private Viewport viewport;
	private World world;
	
	public AsteroidSystem(int initAsteroids, Viewport viewport, World world) {
		super(SPAWN_DELAY);
		maxAsteroids = initAsteroids;
		curAsteroids = 0;
		this.viewport = viewport;
		this.world = world;
	}
	
	/**
	 * Spawns a new big asteroid.
	 */
	private void spawnAsteroid() {
		Vector2 topLeft = new Vector2();
		viewport.unproject(topLeft.set(viewport.getScreenX(), viewport.getScreenY()));
		Vector2 bottomRight = new Vector2();
		viewport.unproject(bottomRight.set(viewport.getScreenX() + viewport.getScreenWidth(), viewport.getScreenY() + viewport.getScreenHeight()));
		
		Entity asteroid = ((PooledEngine)getEngine()).createEntity();
		Asteroid asteroidComp = ((PooledEngine)getEngine()).createComponent(Asteroid.class);
		asteroidComp.type = Type.big;
		asteroid.add(asteroidComp);
		
		Render renderComp = ((PooledEngine)getEngine()).createComponent(Render.class);
		Sprite sprite = new Sprite(App.asset.get(Assets.ASTEROID_TEXTURE));
		sprite.setSize(5, 5);
		sprite.setOriginCenter();
		renderComp.sprite = sprite;
		asteroid.add(renderComp);
		
		Physics physComp = ((PooledEngine)getEngine()).createComponent(Physics.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.angle = MathUtils.random(MathUtils.PI2);
		Vector2[] spawns = new Vector2[4];
		spawns[0] = new Vector2(bottomRight.x, MathUtils.random(topLeft.y - sprite.getHeight(), bottomRight.y));
		spawns[1] = new Vector2(MathUtils.random(topLeft.x, bottomRight.x - sprite.getWidth()), topLeft.y);
		spawns[2] = new Vector2(topLeft.x - sprite.getWidth(), MathUtils.random(topLeft.y - sprite.getHeight(), bottomRight.y));
		spawns[3] = new Vector2(MathUtils.random(topLeft.x, bottomRight.x - sprite.getWidth()), bottomRight.y - sprite.getHeight());
		int spawnLoc = MathUtils.random(3);
		bodyDef.position.set(spawns[spawnLoc]);
		float upperBound = spawnLoc * 90 + 225;
		bodyDef.linearVelocity.set(1, 0).rotate(MathUtils.random(upperBound - 90, upperBound)).scl(asteroidComp.type.SPEED);
		Body body = world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(sprite.getWidth() / 2);
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		fixtureDef.isSensor = true;
		fixtureDef.friction = 0.0f;
		fixtureDef.filter.categoryBits = Constants.ASTEROID_CATEGORY_BITS;
		body.createFixture(fixtureDef);
		body.setUserData(sprite);
		physComp.body = body;
		asteroid.add(physComp);
		
		getEngine().addEntity(asteroid);
		
		curAsteroids++;
	}

	@Override
	protected void updateInterval() {
		if(curAsteroids < maxAsteroids) {
			spawnAsteroid();
		}
	}
}
