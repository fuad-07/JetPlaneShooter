package game;

import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends Entity implements EnemyScore
{
    private int speed = 3;

    public Enemy()
    {
        this.image = Game.enemy;
        //images = new BufferedImage[]{Game.enemy1, Game.enemy2};
        width = image.getWidth();
        height = image.getHeight();
        y = -height;
        Random rand = new Random();
        x = rand.nextInt(Game.WIDTH - width);
    }

    @Override
    public int getScore()
    {
        return 5;
    }

    @Override
    public boolean outOfBounds()
    {
        return y > Game.HEIGHT;
    }

    @Override
    public void step()
    {
        y += speed;
    }

}
