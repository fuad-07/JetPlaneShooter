package game;

import java.awt.image.BufferedImage;

public class Player_1 extends Entity{

    private BufferedImage[] images = {};
    private int index = 0;

    private int doubleFire;
    private int life;


    public Player_1(){
        life = 3;
        doubleFire = 0;
        images = new BufferedImage[]{Game.ani1, Game.ani2};
        image = Game.ani1;
        width = image.getWidth();
        height = image.getHeight();
        x = 150;
        y = 400;
    }


    public int isDoubleFire() {
        return doubleFire;
    }


    public void setDoubleFire(int doubleFire) {
        this.doubleFire = doubleFire;
    }


    public void addDoubleFire(){
        doubleFire = 40;
    }


    public void addLife(){  //����
        life++;
    }


    public void subtractLife(){   //����
        life--;
    }


    public int getLife(){
        return life;
    }


    public void moveTo(int x,int y){
        this.x = x - width/2;
        this.y = y - height/2;
    }


    @Override
    public boolean outOfBounds() {
        return false;
    }

    public Bullet[] shoot(){
        int xStep = width/6;
        int yStep = 20;
        if(doubleFire>0){
            Bullet[] bullets = new Bullet[2];
            bullets[0] = new Bullet(x+xStep,y-yStep);
            bullets[1] = new Bullet(x+3*xStep,y-yStep);
            return bullets;
        }
        else{
            Bullet[] bullets = new Bullet[1];
            bullets[0] = new Bullet(x+2*xStep,y-yStep);
            return bullets;
        }
    }


    @Override
    public void step() {
        if(images.length>0){
            image = images[index++/10%images.length];
        }
    }


    public boolean hit(Entity other){

        int x1 = other.x - this.width/2;
        int x2 = other.x + this.width/2 + other.width;
        int y1 = other.y - this.height/2;
        int y2 = other.y + this.height/2 + other.height;

        int p1x = this.x + this.width/2;
        int p1y = this.y + this.height/2;

        return p1x>x1 && p1x<x2 && p1y>y1 && p1y<y2;
    }

}
