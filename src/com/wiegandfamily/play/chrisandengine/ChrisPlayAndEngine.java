package com.wiegandfamily.play.chrisandengine;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.hardware.SensorManager;

public class ChrisPlayAndEngine extends BaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private static int mCameraWidth = 480; // just initial versions!
	private static int mCameraHeight = 800;
	private Camera mCamera;
	private Texture mTexture;
	private static final MySensorListener mSensors = new MySensorListener();

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
		mCameraWidth = this.getWindowManager().getDefaultDisplay().getWidth();
		mCameraHeight = this.getWindowManager().getDefaultDisplay().getHeight();
		this.mCamera = new Camera(0, 0, mCameraWidth, mCameraHeight);
		return new Engine(new EngineOptions(true, ScreenOrientation.PORTRAIT,
				new RatioResolutionPolicy(mCameraWidth, mCameraHeight),
				this.mCamera, false));
	}

	@Override
	public void onLoadResources() {
		mTexture = new Texture(64, 32, TextureOptions.BILINEAR);
		this.mEngine.getTextureManager().loadTexture(mTexture);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerPreFrameHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		scene.setBackgroundColor((float) Math.random(), (float) Math.random(),
				(float) Math.random());

		for (int i = 1; i <= 15; i++)
			CreateBall(0, 0, scene);

		return scene;
	}

	@Override
	public void onLoadComplete() {
		// Locate the SensorManager using Activity.getSystemService
		SensorManager sm;
		sm = (SensorManager) getSystemService(SENSOR_SERVICE);

		// Register your SensorListener
		sm.registerListener(mSensors, sm
				.getDefaultSensor(SensorManager.SENSOR_ORIENTATION),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	// ===========================================================
	// Methods
	// ===========================================================

	public void CreateBall(float x, float y, Scene scene) {
		TiledTextureRegion mFaceTextureRegion = TextureRegionFactory
				.createTiledFromAsset(mTexture, this,
						"gfx/circleface_tiled.png", 0, 0, 2, 1);

		if (x == 0)
			x = (int) ((mCameraWidth - mFaceTextureRegion.getWidth()) * Math
					.random());
		if (y == 0)
			y = (int) ((mCameraHeight - mFaceTextureRegion.getHeight()) * Math
					.random());

		final Ball ball = new Ball(x, y, mFaceTextureRegion, mSensors,
				mCameraWidth, mCameraHeight);

		scene.getTopLayer().addEntity(ball);
		ball.start();
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

}