package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel
{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int PAUSE = 2;
    private static final int GAME_OVER = 3;

    private int shootIndex = 0;
    private int state;
    private int flyEnteredIndex = 0;
    private int score = 0;
    private int highScore = -1;
    private Timer timer;
    private int intervel = 1000 / 100;

    public static BufferedImage background;
    public static BufferedImage start;
    public static BufferedImage enemy;
    public static BufferedImage bullet;
    public static BufferedImage ani1;
    public static BufferedImage ani2;
    public static BufferedImage pause;
    public static BufferedImage gameover;

    private Entity[] flyings =
            {};
    private Bullet[] bullets =
            {};
    private Player_1 P1 = new Player_1();

    static
    {
        try
        {
            background = ImageIO.read(Game.class.getResource("background.png"));
            start = ImageIO.read(Game.class.getResource("start.png"));
            enemy = ImageIO.read(Game.class.getResource("en1.png"));
            //enemy = ImageIO.read(Game.class.getResource("en2.png"));
            bullet = ImageIO.read(Game.class.getResource("bullet.png"));
            ani1 = ImageIO.read(Game.class.getResource("ani1.png"));
            ani2 = ImageIO.read(Game.class.getResource("ani2.png"));
            pause = ImageIO.read(Game.class.getResource("pause.png"));
            gameover = ImageIO.read(Game.class.getResource("gameover.png"));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g)
    {
        g.drawImage(background, 0, 0, null);
        paintPlayer_1(g);
        paintBullets(g);
        paintEntity(g);
        paintScore(g);
        paintState(g);
    }

    public void checkScore()
    {
        if (highScore == -1)
        {
            highScore = this.getHighScore();
        }

        if(score > highScore)
        {
            highScore = score;
            System.out.println(highScore);
            File scoreFile = new File("highscore.dat");

            if(!scoreFile.exists())
            {
                try
                {
                    scoreFile.createNewFile();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            FileWriter writeFile = null;
            BufferedWriter writer = null;

            try
            {
                writeFile = new FileWriter(scoreFile);
                writer = new BufferedWriter(writeFile);
                writer.write(this.highScore);
            }

            catch(Exception e)
            {

            }

            finally
            {
                try
                {
                    if(writer != null)
                    {
                        writer.close();
                    }
                }

                catch(Exception e)
                {

                }

            }
        }
    }

    public int getHighScore()
    {
        FileReader readFile = null;
        BufferedReader reader = null;
        try
        {
            readFile = new FileReader("highscore.dat");
            reader = new BufferedReader(readFile);
            return reader.read();
        }

        catch (Exception e)
        {

            return 0;
        }

        finally
        {
            try
            {
                if (reader != null)
                {
                    reader.close();

                }
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

    }

    public void paintPlayer_1(Graphics g)
    {
        g.drawImage(P1.getImage(), P1.getX(), P1.getY(), null);
    }

    public void paintBullets(Graphics g)
    {
        for (int i = 0; i < bullets.length; i++)
        {
            Bullet b = bullets[i];
            g.drawImage(b.getImage(), b.getX() - b.getWidth() / 2, b.getY(), null);
        }
    }

    public void paintEntity(Graphics g)
    {
        for (int i = 0; i < flyings.length; i++)
        {
            Entity f = flyings[i];
            g.drawImage(f.getImage(), f.getX(), f.getY(), null);
        }
    }

    public void paintScore(Graphics g)
    {
        int x = 14;
        int y = 720;
        g.setColor(new Color(0xFFFFFF));


        g.setFont(new Font("Squares", Font.BOLD, 6));


        g.drawString("SCORE: " + score, x, y);
        y = y + 24;
        g.drawString("LIFE: " + P1.getLife(), x, y);

    }

    public void paintState(Graphics g)
    {
        int x = 215;
        int y = 350;

        switch (state)
        {

            case START:
                g.drawImage(start, 0, 0, null);
                break;
            case PAUSE:
                g.drawImage(pause, 0, 0, null);
                break;
            case GAME_OVER:
                checkScore();

                g.setFont(new Font("Squares", Font.BOLD, 8));
                g.setColor(new Color(0xFFFFFF));
                g.drawImage(gameover, 0, 0, null);
                if (score < highScore)
                {
                    g.drawString("SCORE: " + score, x, y);

                }
                y = y + 45;

                if(score > 99)
                {
                    x = x - 48;

                }
                else
                {
                    x = x - 42;
                }
                g.drawString("Best Score: " + highScore, x, y);

                break;
        }
    }



    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Jet Plane Shooter"); // window
        frame.setCursor(frame.getToolkit().createCustomCursor(new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0), "null"));// hides the cursor while in game
        Game game = new Game(); // panel
        frame.add(game); // add panel to our frame
        frame.setSize(WIDTH, HEIGHT); // adjust window size
        frame.setAlwaysOnTop(true); // always on top
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set that when the window is closed, the program
        // terminates
        frame.setLocationRelativeTo(null); // set relative position to null, that is, always in the center
        frame.setVisible(true); // 1. set visibility of the window to true 2.call the paint() function

        game.action();
    }

    public void action()
    {

        MouseAdapter l = new MouseAdapter()
        {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                if (state == RUNNING)
                {
                    int x = e.getX();
                    int y = e.getY();
                    P1.moveTo(x, y);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                if (state == PAUSE)
                {
                    state = RUNNING;
                }
            }

            @Override
            public void mouseExited(MouseEvent e)
            {
                if (state == RUNNING)
                {
                    state = PAUSE;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e)
            {
                switch (state)
                {
                    case START:
                        state = RUNNING;
                        break;
                    case GAME_OVER:
                        flyings = new Entity[0];
                        bullets = new Bullet[0];
                        P1 = new Player_1();
                        score = 0;
                        state = START;
                        break;
                }
            }
        };
        this.addMouseListener(l);
        this.addMouseMotionListener(l);

        timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (state == RUNNING)
                {
                    enterAction();
                    stepAction();
                    shootAction();
                    bangAction();
                    outOfBoundsAction();
                    checkGameOverAction();
                    //music();
                }
                repaint();
            }

        }, intervel, intervel);
    }

    public void music()  // DOES NOT WORK
    {
        try {
            // Open an audio input stream.
            File soundFile = new File("Music.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void enterAction()
    {
        flyEnteredIndex++;
        if (flyEnteredIndex % 40 == 0)
        {
            Entity obj = new Enemy();
            flyings = Arrays.copyOf(flyings, flyings.length + 1);
            flyings[flyings.length - 1] = obj;
        }
    }

    public void stepAction()
    {
        for (int i = 0; i < flyings.length; i++)
        {
            Entity f = flyings[i];
            f.step();
        }

        for (int i = 0; i < bullets.length; i++)
        {
            Bullet b = bullets[i];
            b.step();
        }
        P1.step();
    }



    public void shootAction()
    {
        shootIndex++;
        if (shootIndex % 10 == 0)
        {
            Bullet[] bs = P1.shoot();
            bullets = Arrays.copyOf(bullets, bullets.length + bs.length);
            System.arraycopy(bs, 0, bullets, bullets.length - bs.length, bs.length);
        }
    }

    public void bangAction()
    {
        for (int i = 0; i < bullets.length; i++)
        {
            Bullet b = bullets[i];
            bang(b);
        }
    }

    public void outOfBoundsAction()
    {
        int index = 0;
        Entity[] flyingLives = new Entity[flyings.length];
        for (int i = 0; i < flyings.length; i++)
        {
            Entity f = flyings[i];
            if (!f.outOfBounds())
            {
                flyingLives[index++] = f;

            } else
                P1.subtractLife();
        }

        flyings = Arrays.copyOf(flyingLives, index);
    }

    public void checkGameOverAction()
    {
        if (isGameOver() == true)
        {
            state = GAME_OVER;
        }
    }

    public boolean isGameOver()
    {

        for (int i = 0; i < flyings.length; i++)
        {
            int index = -1;
            Entity obj = flyings[i];
            if (P1.hit(obj))
            {
                P1.subtractLife();
                index = i;
            }
            if (index != -1)
            {
                Entity t = flyings[index];
                flyings[index] = flyings[flyings.length - 1];
                flyings[flyings.length - 1] = t;
                flyings = Arrays.copyOf(flyings, flyings.length - 1);
            }
        }

        return P1.getLife() <= 0;
    }

    public void bang(Bullet bullet)
    {
        int index = -1;
        for (int i = 0; i < flyings.length; i++)
        {
            Entity obj = flyings[i];
            if (obj.shootBy(bullet))
            {
                index = i;
                break;
            }
        }
        if (index != -1)
        {
            Entity one = flyings[index];

            Entity temp = flyings[index];
            flyings[index] = flyings[flyings.length - 1];
            flyings[flyings.length - 1] = temp;

            flyings = Arrays.copyOf(flyings, flyings.length - 1);

            if (one instanceof EnemyScore)
            {
                EnemyScore e = (EnemyScore) one;
                score += e.getScore();
            }
        }
    }
}
