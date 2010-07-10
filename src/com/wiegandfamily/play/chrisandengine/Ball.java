package com.wiegandfamily.play.chrisandengine;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

public class Ball extends AnimatedSprite implements Runnable {
	private Thread mT = null;
	private MySensorListener mSensor = null;
	private int mCameraWidth = 0;
	private int mCameraHeight = 0;

	public Ball(final float pX, final float pY,
			final TiledTextureRegion pTextureRegion, MySensorListener pSensor,
			final int pCameraWidth, final int pCameraHeight) {
		super(pX, pY, pTextureRegion);
		mT = new Thread(this);
		this.mSensor = pSensor;
		this.mCameraWidth = pCameraWidth;
		this.mCameraHeight = pCameraHeight;
	}

	public void start() {
		mT.start();
	}

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		if (mSensor != null) {
			float directionX = 0 - mSensor.getDimensions()[0];
			float directionY = 0 + mSensor.getDimensions()[1];

			directionX *= 15 * (1 + Math.random());
			directionY *= 15 * (1 + Math.random());

			if (this.mX <= 0 && directionX < 0)
				// on left edge going left
				directionX = 0;
			else if (this.mX + this.getWidth() >= mCameraWidth
					&& directionX > 0) {
				// on right edge going right
				directionX = 0;
			}
			this.mVelocityX = directionX;

			if (this.mY < 0 && directionY < 0)
				// on top edge going up
				directionY = 0;
			else if (this.mY + this.getHeight() >= mCameraHeight
					&& directionY > 0) {
				// on bottom edge going down
				directionY = 0;
			}
			this.mVelocityY = directionY;

			if (directionX == 0 || directionY == 0)
				this.setCurrentTileIndex(1);
			else
				this.setCurrentTileIndex(0);
		}
		super.onManagedUpdate(pSecondsElapsed);
	}

	@Override
	public void run() {
		if (mSensor != null && mSensor.getDimensions() != null) {
			this.mVelocityX = 0 - mSensor.getDimensions()[0] * 10;
			this.mVelocityY = mSensor.getDimensions()[1] * 10;
		}

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
	}
}