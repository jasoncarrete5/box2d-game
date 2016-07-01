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
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.fwumdesoft.box2d.App.Assets;
import com.fwumdesoft.box2d.components.Physics;
import com.fwumdesoft.box2d.components.Render;
import com.fwumdesoft.box2d.systems.PhysicsSystem;
import com.fwumdesoft.box2d.systems.RenderSystem;

public class GameScreen extends ScreenAdapter {
	public static final float MAX_PLAYER_SPEED = 3f; // m/s
	public static final float MAX_PLAYER_ANGULAR_SPEED = MathUtils.PI; // rad/s
	
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
		
		PhysicsSystem physicsSystem = new PhysicsSystem(world);
		physicsSystem.priority = 0;
		RenderSystem renderSystem = new RenderSystem(batch, viewport);
		renderSystem.priority = 1;
		engine.addSystem(physicsSystem);
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
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(-sprite.getWidth() / 2, -sprite.getHeight() / 2);
		bodyDef.angle = MathUtils.PI / 2;
		Body body = world.createBody(bodyDef);
		body.getTransform().setRotation(MathUtils.PI / 2);
		body.setUserData(sprite);
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.set(new float[] {0, 0, 0, 3, 3, 3, 0, 3});
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;
		body.createFixture(fixtureDef);
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
			body.setLinearVelocity(body.getLinearVelocity().limit(MAX_PLAYER_SPEED));
			body.applyForceToCenter(Vector2.X.cpy().scl(3).rotateRad(body.getTransform().getRotation()), true);
		}
		if(Gdx.input.isKeyPressed(Keys.A)) {
			Body body = player.getComponent(Physics.class).body;
			//limit angular velocity
			body.setAngularVelocity(Math.signum(body.getAngularVelocity()) * MathUtils.clamp(Math.abs(body.getAngularVelocity()), 0, MAX_PLAYER_ANGULAR_SPEED));
			body.applyAngularImpulse(90, true);
		}
		if(Gdx.input.isKeyPressed(Keys.S)) {
			Body body = player.getComponent(Physics.class).body;
			body.setLinearVelocity(body.getLinearVelocity().limit(MAX_PLAYER_SPEED));
			body.applyForceToCenter(Vector2.X.cpy().scl(3).rotateRad(body.getTransform().getRotation() + MathUtils.PI), true);
		}
		if(Gdx.input.isKeyPressed(Keys.D)) {
			Body body = player.getComponent(Physics.class).body;
			//limit angular velocity
			body.setAngularVelocity(Math.signum(body.getAngularVelocity()) * MathUtils.clamp(Math.abs(body.getAngularVelocity()), 0, MAX_PLAYER_ANGULAR_SPEED));
			body.applyAngularImpulse(-90, true);
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
