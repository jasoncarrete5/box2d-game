package com.fwumdesoft.box2d;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.fwumdesoft.box2d.App.Assets;
import com.fwumdesoft.box2d.components.Physics;
import com.fwumdesoft.box2d.components.Render;
import com.fwumdesoft.box2d.systems.AsteroidSystem;
import com.fwumdesoft.box2d.systems.PhysicsSystem;
import com.fwumdesoft.box2d.systems.RenderSystem;

public class GameScreen extends ScreenAdapter {
	private FillViewport viewport;
	private Batch batch;
	private World world;
	private PooledEngine engine;
	
	private Entity player;
	
	@Override
	public void show() {
		viewport = new FillViewport(100, 100 * (1 / App.game.getScreenRatio()));
		batch = new SpriteBatch();
		world = new World(Vector2.Zero.cpy(), true);
		engine = new PooledEngine();
		
		// Add contact listener to the world
		world.setContactListener(new ContactListener() {
			@Override
			public void beginContact(Contact contact) {
				
			}
			
			@Override
			public void endContact(Contact contact) {
				
			}
			
			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				
			}
			
			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
			}
		});
		
		PhysicsSystem physicsSystem = new PhysicsSystem(world);
		physicsSystem.priority = 0;
		AsteroidSystem asteroidSystem = new AsteroidSystem(10, viewport, world);
		asteroidSystem.priority = 1;
		RenderSystem renderSystem = new RenderSystem(batch, viewport);
		renderSystem.priority = 2;
		engine.addSystem(physicsSystem);
		engine.addSystem(asteroidSystem);
		engine.addSystem(renderSystem);
		
		createPlayer();
	}
	
	/**
	 * Adds player entity to the world.
	 */
	private void createPlayer() {
		player = engine.createEntity();
		
		Render renderComp = engine.createComponent(Render.class);
		Sprite sprite = new Sprite(App.asset.get(Assets.PLAYER_TEXTURE));
		sprite.setSize(3, 3);
		sprite.setOriginCenter();
		renderComp.sprite = sprite;
		player.add(renderComp);
		
		Physics physComp = engine.createComponent(Physics.class);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody; // Needs to be dynamic to be affected by damping
		bodyDef.position.set(-sprite.getWidth() / 2, -sprite.getHeight() / 2); // Center of screen
		bodyDef.linearDamping = 0.25f;
		bodyDef.angle = MathUtils.PI / 2; // 90 degrees -- face up
		bodyDef.angularDamping = 2f;
		Body body = world.createBody(bodyDef);
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.set(new float[] {0, 0, sprite.getWidth(), 0, sprite.getWidth(), sprite.getHeight(), 0, sprite.getHeight()});
		fixtureDef.shape = shape;
		fixtureDef.friction = 0;
		fixtureDef.isSensor = true;
		fixtureDef.filter.categoryBits = Constants.PLAYER_CATEGORY_BITS;
		fixtureDef.filter.maskBits = ~Constants.PLAYER_CATEGORY_BITS; // Can collide with non-player fixtures
		body.createFixture(fixtureDef);
		body.setUserData(sprite);
		physComp.body = body;
		player.add(physComp);
		
		engine.addEntity(player);
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(Gdx.input.isKeyPressed(Keys.W)) {
			Body body = player.getComponent(Physics.class).body;
			Vector2 vel = body.getLinearVelocity();
			vel.add(body.getTransform().getOrientation().nor().scl(Constants.PLAYER_ACC * delta));
			if(vel.len() <= Constants.MAX_PLAYER_SPEED) {
				body.setLinearVelocity(vel);
			}
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			Body body = player.getComponent(Physics.class).body;
			float angVel = body.getAngularVelocity();
			angVel += Constants.PLAYER_ANGULAR_ACC * delta;
			if(angVel <= Constants.MAX_PLAYER_ANGULAR_SPEED) {
				body.setAngularVelocity(angVel);
			}
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			Body body = player.getComponent(Physics.class).body;
			float angVel = body.getAngularVelocity();
			angVel -= Constants.PLAYER_ANGULAR_ACC * delta;
			if(angVel <= Constants.MAX_PLAYER_ANGULAR_SPEED) {
				body.setAngularVelocity(angVel);
			}
		}
		if(Gdx.input.isKeyPressed(Keys.SPACE)) {
			
		}
		
		engine.update(delta);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		world.dispose();
		batch.dispose();
	}
}
