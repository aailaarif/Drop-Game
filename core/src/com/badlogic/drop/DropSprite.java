package com.badlogic.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
//Aaila Arif
//s1324028
public class DropSprite extends Sprite
{
    //Project 1:Created variables to hold velocity for x and y directions
    //in order to create the illusion of faster raindrops
    private float velocityX;
    private float velocityY;
    Texture dropImage;
    int rotateAngle;
    int rotateDirection; // -1 if clockwise; 1 if counterclockwise

    public DropSprite()
    {
        // load the images for the droplet and the bucket, 64x64 pixels each
        dropImage = new Texture(Gdx.files.internal("droplet.png"));

        // ==> A Sprite is not drawable until its texture (region) and bounds are set
        //     (per https://v.gd/wwEZqD).
        //     The Sprite(Texture texture) constructor would take care of these details for us, but
        //     we're not using that constructor.

        // setTexture(dropImage); // incorrect method; use setRegion()
        setRegion(dropImage);  // inherited from TextureRegion
        setBounds(0, 0, 64, 64);

        rotateAngle = 5; // degrees, per frame

        //Project 1: removed rotation from the raindrops
        /*
        // Assign random rotate direction:
        if (MathUtils.random(0, 1) == 0) // coin flip
            rotateDirection = 1;
        else
            rotateDirection = -1;
        */

        //Project 1: created a scale variable that is randomly assigned a float value
        //to change the size of the raindrops
        float scale = MathUtils.random(0.5f,1f);
        setScale(scale); // try

        //Project 1: created floats to hold values for red, green, and blue and assigned
        //them random values to change the tint of the raindrops
        float red, blue, green, alpha;
        red=MathUtils.random(0f,1f);
        green=MathUtils.random(0f,1f);
        blue=MathUtils.random(0f,1f);
        setColor(red, green, blue, 1f);
    } // end constructor


    public void rotate()
    {
        setOriginCenter(); // Need this Sprite class method for correct centered rotation.
        //Project 1 : remove the rotate method
        //super.rotate(rotateDirection * rotateAngle);
    }


    //Project 1: Getter for velocityX
    public float getVelocityX()
    {
        return velocityX;
    }

    //Project 1: Setter for velocityX
    public void setVelocityX(float velocityX)
    {
        this.velocityX = velocityX;
    }

    //Project 1: Getter for velocityY
    public float getVelocityY()
    {
        return velocityY;
    }

    //Project 1: Setter for velocityY
    public void setVelocityY(float velocityY)
    {
        this.velocityY = velocityY;
    }
}
