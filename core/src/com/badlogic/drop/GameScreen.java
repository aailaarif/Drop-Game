package com.badlogic.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite; // Sprite demo
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
//Aaila Arif
//s1324028
public class GameScreen implements Screen {
   final Drop game;
   Texture dropImage;
   Texture miniDrop;//Project 1: Created a new variable to load the texture of the Drop to display it next
   //to the counter
   boolean showFPS = true; //Project 1: Created a variable to determine whether to show FPS or not
   Sprite bucketSprite; // Project 1
   Sprite backgroundSprite; //Project 1: created a background sprite to load the texture of the background image
   Texture bucketImage;
   Texture backgroundImage; //Project 1: created backgroundImage to hold the texture of the background asset
   Sound dropSound;
   Music rainMusic;
   Music backgroundMusic; //Project 1: created a new Music variable to hold the second track that plays in the background
   //along with the first rainMusic
   OrthographicCamera camera;

   Array<DropSprite> raindrops; // Project 1
   long lastDropTime;//Project 1:created a variable to keep track of last drop time of a raindrop
   long lastCaughtTime; //Project 1:created a variable to keep track of last caught time of a raindrop
   int dropsGathered;
   
   public GameScreen(final Drop gam) {
      this.game = gam;

      // load the image for the bucket, 64x64 pixels
      bucketImage = new Texture(Gdx.files.internal("bucket.png"));
      //Project 1:created the texture for the background from the asset
      backgroundImage = new Texture(Gdx.files.internal("backgroundRain.png"));
      //Project 1:created the texture for the raindrop next to the counter
      miniDrop = new Texture(Gdx.files.internal("droplet.png"));
      // construct sprite from bucketImage texture
      bucketSprite = new Sprite(bucketImage);
      //Project 1:construct sprite from backgroundImage texture
      backgroundSprite = new Sprite(backgroundImage);
      // load the drop sound effect and the rain background "music"
      dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
      rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
      //Project 1:Load the second background "music"
      backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("frozen_winter.mp3"));
      rainMusic.setLooping(true);
      //Project 1:Loop the second track
      backgroundMusic.setLooping(true);

      // create the camera and the SpriteBatch
      camera = new OrthographicCamera();
      camera.setToOrtho(false, 800, 480);

      // Project 1 - bucket sprite
      // Set initial bucket sprite position:
      bucketSprite.setX(800 / 2 - 64 / 2); // center the bucket horizontally
      bucketSprite.setY(20);
      // alternative: bucketSprite.setPosition(800 / 2 - 64 / 2, 20);

      // create the raindrops array and spawn the first raindrop
      raindrops = new Array<DropSprite>(); // Project 1
      spawnRaindrop();
   } // end constructor


   private void spawnRaindrop() {
      DropSprite raindrop = new DropSprite(); // Project 1
      float x = MathUtils.random(0, 800 - 64);
      float y = 480;

      raindrop.setPosition(x, y);
      raindrops.add(raindrop);
      lastDropTime = TimeUtils.nanoTime(); //Project 1:keep track of last drop time in lastDropTime variable
   } // end spawnRaindrop()



   @Override
   public void render(float delta) {

      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // This cryptic line clears the screen.
      //Project 1:Disabled blending and drew the background sprite -> re-enabled after
      game.batch.begin();
      game.batch.disableBlending();
      backgroundSprite.draw(game.batch);
      game.batch.enableBlending();
      // Other drawing here.
      game.batch.end();
      // clear the screen with a dark blue color. The
      // arguments to glClearColor are the red, green
      // blue and alpha component in the range [0,1]
      // of the color to be used to clear the screen.
      Gdx.gl.glClearColor(0, 0, 0.2f, 1);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

      // tell the camera to update its matrices.
      camera.update();

      // tell the SpriteBatch to render in the
      // coordinate system specified by the camera.
      game.batch.setProjectionMatrix(camera.combined);

      // begin a new batch and draw the bucket and
      // all drops
      game.batch.begin();

      Sprite miniDropSprite = new Sprite(miniDrop); //Project 1: Create a sprite for miniDrop

      backgroundSprite.setSize(800, 480); //Project 1:Set the backgroundSprite size to match the screen dimensions
      backgroundSprite.setPosition(0, 0); //Project 1:Set the position and drew the background
      backgroundSprite.draw(game.batch);

      game.batch.draw(miniDropSprite, 700, 430, 30, 30); //Project 1: Draw the miniDropSprite
      game.font.draw(game.batch, " " + dropsGathered, 730, 450); //Project 1: Draw the text for the counter
      // Project 1 - bucket sprite
      bucketSprite.draw(game.batch);

      // Project 1
      for (DropSprite raindrop : raindrops) {
         raindrop.draw(game.batch);
         //Project 1:create variables to hold the velocity values and y position of the raindrop
         float velocityX = raindrop.getVelocityX();
         float velocityY;
         float raindropY = raindrop.getY();
         //Project 1: Update the velocity based on the raindrop's position/region
         if (raindropY > 400) {
            velocityY = -50;
         } else if(raindropY >= 250 && raindropY < 400){
            velocityY = -150;
         }else{
            velocityY=-300;
         }
         //Project 1:set the velocity of the raindrop based on which region it is in
         raindrop.setVelocityX(velocityX);
         raindrop.setVelocityY(velocityY);
         //Project 1:set the position of the raindrop with the new velocity values
         raindrop.setPosition(raindrop.getX() + velocityX * delta, raindrop.getY() + velocityY * delta);
      }
      //Project 1:Check if the "F" ket (34) has been pressed and update showFPS
      if(Gdx.input.isKeyJustPressed(34)){
         showFPS = !showFPS;
      }
      //Project 1:Display the FPS if showFPS is set to true
      if(showFPS){
         game.font.draw(game.batch, "FPS: " + Gdx.graphics.getFramesPerSecond() , 50,450);
      }
      game.batch.end();

      // process user input
      if (Gdx.input.isTouched()) {
         Vector3 touchPos = new Vector3();
         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
         camera.unproject(touchPos);

         // Project 1 - bucket sprite
         bucketSprite.setX(touchPos.x - 64 / 2);
      }
      if (Gdx.input.isKeyPressed(Keys.LEFT)) {
         float newBucketX = bucketSprite.getX() - 200 * Gdx.graphics.getDeltaTime();
         bucketSprite.setX(newBucketX);
      }
      if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
         float newBucketX = bucketSprite.getX() + 200 * Gdx.graphics.getDeltaTime();
         bucketSprite.setX(newBucketX);
      }

      // keep bucket sprite within screen margins
      if (bucketSprite.getX() < 0)
         bucketSprite.setX(0);
      if (bucketSprite.getX() > 800 - 64)
         bucketSprite.setX(800 - 64);

      // check if we need to create a new raindrop
      if (TimeUtils.nanoTime() - lastDropTime > 250000000) //Project 1:Adjust nanoseconds to display more drops at a time
         spawnRaindrop();

      // move the raindrops, remove any that are beneath the bottom edge of
      // the screen or that hit the bucket. In the later case we play back
      // a sound effect as well.
      Iterator<DropSprite> iter = raindrops.iterator(); // Project 1
      while (iter.hasNext()) {
         DropSprite raindrop = iter.next();

         // Update raindrop vertical (y) position:
         float y = raindrop.getY();
         y -= 200 * Gdx.graphics.getDeltaTime();
         if (y + 64 < 0)
            iter.remove();
         else
            raindrop.setY(y);

         //Project 1:remove rotation
         // Update raindrop rotation:
         //raindrop.rotate();

         // Project 1 - Rectangle overlap collision detection
         Rectangle bucketRectangle = bucketSprite.getBoundingRectangle();
         Rectangle dropRectangle = raindrop.getBoundingRectangle();
         if (dropRectangle.overlaps(bucketRectangle)) {

            dropsGathered++;
            dropSound.play();
            iter.remove();
            //Project 1:set the color of the bucketSprite to the color of the raindrop just caught and update lastCaughtTime
            bucketSprite.setColor(raindrop.getColor());
            //Project 1:keep track of the last time a drop was caught
            lastCaughtTime = TimeUtils.nanoTime();

         }
         //Project 1:Set bucket back to original color after a quarter second has passed
         if (TimeUtils.timeSinceNanos(lastCaughtTime) > 0.25f*1000000000) {
            bucketSprite.setColor(Color.WHITE);
         }
      } // end while
       
   } //end render()

   @Override
   public void resize(int width, int height) {
   }

   @Override
   public void show() {
      // start the playback of the background music
      // when the screen is shown
      rainMusic.play();
      backgroundMusic.play(); //Project 1:play backgroundMusic track
   }

   @Override
   public void hide() {
   }

   @Override
   public void pause() {
   }

   @Override
   public void resume() {
   }

   @Override
   public void dispose() {
      dropImage.dispose();
      bucketImage.dispose();
      dropSound.dispose();
      rainMusic.dispose();
      miniDrop.dispose(); //Dispose of miniDrop
      backgroundMusic.dispose(); //Dispose of backgroundMusic
   }

}
