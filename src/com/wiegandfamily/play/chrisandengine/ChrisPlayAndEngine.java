package com.wiegandfamily.play.chrisandengine;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.hardware.SensorManager;

public class ChrisPlayAndEngine extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static int CAMERA_WIDTH = 480;
	private static int CAMERA_HEIGHT = 800;

	// ===========================================================
	// Fields
	// ===========================================================

	private Camera mCamera;
	private Texture mTexture;
	private TiledTextureRegion mFaceTextureRegion;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		CAMERA_WIDTH = this.getWindowManager().getDefaultDisplay().getWidth();
		CAMERA_HEIGHT = this.getWindowManager().getDefaultDisplay().getHeight();
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mCamera, false));
	}

	@Override
	public void onLoadResources() {
		this.mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
		this.mFaceTextureRegion = TextureRegionFactory.createTiledFromAsset(
				this.mTexture, this, "gfx/circleface_tiled.png", 0, 0, 2, 1);

		this.mEngine.getTextureManager().loadTexture(this.mTexture);
	}

	@Override
	public Scene onLoadScene() {
		FPSLogger logger = new FPSLogger();
		this.mEngine.registerPreFrameHandler(logger);

		final Scene scene = new Scene(1);
		scene.setBackgroundColor((float) Math.random(), (float) Math.random(),
				(float) Math.random());

		final int x = (CAMERA_WIDTH - this.mFaceTextureRegion.getWidth()) / 2;
		final int y = (CAMERA_HEIGHT - this.mFaceTextureRegion.getHeight()) / 2;
		final Ball ball = new Ball(x, y, this.mFaceTextureRegion);
		ball.setVelocity(0f, 0f);

		t = new Thread(ball);
		t.start();

		scene.getTopLayer().addEntity(ball);

		return scene;
	}

	@Override
	public void onLoadComplete() {
		// Locate the SensorManager using Activity.getSystemService
		SensorManager sm;
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Register your SensorListener
		sm.registerListener(sl, sm
				.getDefaultSensor(SensorManager.SENSOR_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public static final MySensorListener sl = new MySensorListener();
	public static Thread t = null;

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	private static class Ball extends AnimatedSprite implements Runnable {
		public Ball(final float pX, final float pY,
				final TiledTextureRegion pTextureRegion) {
			super(pX, pY, pTextureRegion);
		}

		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {
			float directionX = 0 - sl.getDimensions()[0];
			float directionY = 0 + sl.getDimensions()[1];

			if (this.mX <= 0 && directionX < 0)
				// on left edge going left
				directionX = 0;
			else if (this.mX + this.getWidth() >= CAMERA_WIDTH
					&& directionX > 0) {
				// on right edge going right
				directionX = 0;
			}
			this.mVelocityX = directionX * 10;

			if (this.mY < 0 && directionY < 0)
				// on top edge going up
				directionY = 0;
			else if (this.mY + this.getHeight() >= CAMERA_HEIGHT
					&& directionY > 0) {
				// on bottom edge going down
				directionY = 0;
			}
			this.mVelocityY = directionY * 10;

			if (directionX == 0 || directionY == 0)
				this.setCurrentTileIndex(1);
			else
				this.setCurrentTileIndex(0);

			super.onManagedUpdate(pSecondsElapsed);
		}

		@Override
		public void run() {
			if (sl != null && sl.getDimensions() != null) {
				this.mVelocityX = 0 - sl.getDimensions()[0] * 10;
				this.mVelocityY = sl.getDimensions()[1] * 10;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
}