package game;

public class Bullet extends Entity {
    private int speed = 6;


    public Bullet(int x,int y){
        this.x = x;
        this.y = y;
        this.image = Game.bullet;
    }

    @Override
    public void step(){
        y-=speed;
    }

    @Override
    public boolean outOfBounds() {
        return y<-height;
    }

}

