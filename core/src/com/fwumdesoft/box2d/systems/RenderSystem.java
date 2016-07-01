package com.fwumdesoft.box2d.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.fwumdesoft.box2d.components.Render;

public class RenderSystem extends IteratingSystem {
	private static ComponentMapper<Render> renderMapper = ComponentMapper.getFor(Render.class);
	private Batch batch;
	private Viewport viewport;
	
	public RenderSystem(Batch batch, Viewport viewport) {
		super(Family.all(Render.class).get());
		this.batch = batch;
		this.viewport = viewport;
	}
	
	@Override
	public void update(float deltaTime) {
		batch.setProjectionMatrix(viewport.getCamera().combined);
		batch.begin();
		super.update(deltaTime);
		batch.end();
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Render render = renderMapper.get(entity);
		render.sprite.draw(batch);
	}
}
