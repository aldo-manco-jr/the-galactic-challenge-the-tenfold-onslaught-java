package org.aldomanco.game_screen;

import org.aldomanco.display.Display;
import org.aldomanco.handler.EnemyBulletHandler;
import org.aldomanco.levels.Level;
import org.aldomanco.sound.Sound;
import org.aldomanco.sprite.SpriteAnimation;
import org.aldomanco.state.StateMachine;
import org.aldomanco.state.SuperStateMachine;
import org.aldomanco.timer.TickTimer;

import java.awt.*;

/**
 * Created by unieuro on 12/23/2019.
 */
public class SinglePlayerScreen extends SuperStateMachine {

    private Player player;
    private BasicBlocks blocks;

    private EnemyBulletHandler bulletHandler;

    public static int SCORE = 0;

    private Font font = new Font("Arial", Font.PLAIN, 48);
    private TickTimer gameOverTimer = new TickTimer(60);
    private TickTimer completeTimer = new TickTimer(120);

    private Level level;

    private SpriteAnimation wallpaper;
    private Rectangle rectangle;

    private Font fontData = new Font("Arial", Font.PLAIN, 24);

    private int limitBullet, scoreIncrement, violence, rowsOfEnemies;
    double speedEnemies;

    private static Sound floatingInSpace = new Sound("/org/aldomanco/media/sounds/floatingInSpace.wav");

    public SinglePlayerScreen(StateMachine stateMachine) {
        super(stateMachine);

        blocks = new BasicBlocks();
        bulletHandler = new EnemyBulletHandler();
        player = new Player(Display.WIDTH / 2 - 50, Display.HEIGHT - 175, 50, 50, blocks, 200);

        this.limitBullet = 100;
        this.speedEnemies = 1.2;
        this.scoreIncrement = 1;
        this.violence = 11_750;
        this.rowsOfEnemies = 5;

        level = new Level(player, bulletHandler, limitBullet, speedEnemies, scoreIncrement, violence, 1, rowsOfEnemies);

        wallpaper = new SpriteAnimation(0, 0, 1, 29, 100, "/org/aldomanco/media/sprites/GiphyGameScreen.png");
        wallpaper.setWidth(800);
        wallpaper.setHeight(700);

        this.setRectangle(new Rectangle((int) wallpaper.getXPosition(), (int) wallpaper.getYPosition(), wallpaper.getWidth(), wallpaper.getHeight()));
        wallpaper.setLoop(true);
    }

    @SuppressWarnings("all")
    @Override
    public void update(double lastLoopTicks) {

        wallpaper.update(lastLoopTicks);
        this.getRectangle().x = (int) wallpaper.getXPosition();

        player.update(lastLoopTicks);
        level.update(lastLoopTicks, blocks);

        if (level.isGameOver()) {

            gameOverTimer.tick(lastLoopTicks);

            if (gameOverTimer.isEventReady()) {

                level.reset();

                if (SCORE < 150) {
                    level = new Level(player, bulletHandler, 100, 1.2, 1, 11_750, 1, 5);
                    SinglePlayerScreen.SCORE=0;
                    ScoreScreen.state = 1;
                    getStateMachine().setState((byte) 3);
                    blocks.reset();
                    bulletHandler.reset();
                } else {
                    SCORE -= 150;
                    level = new Level(player, bulletHandler, limitBullet, speedEnemies, 0, violence, getLevel(), rowsOfEnemies);
                }
            }
        }

        if (level.isComplete()) {

            completeTimer.tick(lastLoopTicks);

            if (completeTimer.isEventReady()) {

                level.reset();

                if (level.getCurrentLevel() == 1) {
                    limitBullet = 100;
                    speedEnemies = 1.25;
                    scoreIncrement = 2;
                    violence = 11_250;
                    level.setCurrentLevel(2);
                    rowsOfEnemies = 5;
                } else if (level.getCurrentLevel() == 2) {
                    limitBullet = 100;
                    speedEnemies = 1.3;
                    scoreIncrement = 3;
                    violence = 10_750;
                    level.setCurrentLevel(3);
                    rowsOfEnemies = 5;
                } else if (level.getCurrentLevel() == 3) {
                    limitBullet = 100;
                    speedEnemies = 1.35;
                    scoreIncrement = 3;
                    violence = 10_500;
                    level.setCurrentLevel(4);
                    rowsOfEnemies = 5;
                } else if (level.getCurrentLevel() == 4) {
                    limitBullet = 100;
                    speedEnemies = 1.4;
                    scoreIncrement = 4;
                    violence = 10_250;
                    level.setCurrentLevel(5);
                    rowsOfEnemies = 5;
                } else if (level.getCurrentLevel() == 5) {
                    limitBullet = 100;
                    speedEnemies = 1.45;
                    scoreIncrement = 4;
                    violence = 10_000;
                    level.setCurrentLevel(6);
                    rowsOfEnemies = 5;
                } else if (level.getCurrentLevel() == 6) {
                    limitBullet = 100;
                    speedEnemies = 1.3;
                    scoreIncrement = 5;
                    violence = 11_000;
                    level.setCurrentLevel(7);
                    rowsOfEnemies = 6;
                } else if (level.getCurrentLevel() == 7) {
                    limitBullet = 100;
                    speedEnemies = 1.35;
                    scoreIncrement = 5;
                    violence = 10_750;
                    level.setCurrentLevel(8);
                    rowsOfEnemies = 6;
                } else if (level.getCurrentLevel() == 8) {
                    limitBullet = 80;
                    speedEnemies = 1.2;
                    scoreIncrement = 6;
                    violence = 11_500;
                    level.setCurrentLevel(9);
                    rowsOfEnemies = 6;
                } else if (level.getCurrentLevel() == 9) {
                    limitBullet = 64;
                    speedEnemies = 1.2;
                    scoreIncrement = 7;
                    violence = 11_000;
                    level.setCurrentLevel(10);
                    rowsOfEnemies = 5;
                } else if (level.getCurrentLevel() == 10) {
                    ScoreScreen.state = 2;
                    getStateMachine().setState((byte) 3);
                    level.reset();
                }

                level = new Level(player, bulletHandler, limitBullet, speedEnemies, scoreIncrement, violence, getLevel(), rowsOfEnemies);
            }
        }
    }

    public static boolean isRunning() {
        return floatingInSpace.isPlaying();
    }

    @SuppressWarnings("all")
    @Override
    public void draw(Graphics2D graphic) {

        wallpaper.draw(graphic);

        graphic.setColor(Color.CYAN);
        graphic.setFont(fontData);
        graphic.drawString("Score: " + SCORE, 5, 625);

        graphic.setColor(Color.CYAN);
        graphic.drawString("Health: " + player.getHealth(), 5, 655);

        graphic.setColor(Color.CYAN);
        graphic.drawString("Level: " + getLevel(), 405, 625);

        graphic.setColor(Color.CYAN);
        graphic.drawString("Bullets: " + player.getLimitBullet(), 405, 655);

        blocks.draw(graphic);
        player.draw(graphic);
        level.draw(graphic);

        if (level.isGameOver()) {
            graphic.setColor(Color.RED);
            graphic.setFont(font);
            String textGameOver = "GAME OVER!";
            int textGameOverWidth = graphic.getFontMetrics().stringWidth(textGameOver);
            graphic.drawString(textGameOver, (Display.WIDTH / 2) - (textGameOverWidth / 2), Display.HEIGHT / 2);
        }

        if (level.isComplete()) {
            graphic.setColor(Color.GREEN);
            graphic.setFont(font);
            String textComplete = "LEVEL COMPLETE!";
            int textCompleteWidth = graphic.getFontMetrics().stringWidth(textComplete);
            graphic.drawString(textComplete, (Display.WIDTH / 2) - (textCompleteWidth / 2), Display.HEIGHT / 2);
        }
    }

    @SuppressWarnings("all")
    public int getLevel() {

        switch (this.level.getCurrentLevel()) {

            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            default:
                this.level.setCurrentLevel(1);
                return 1;
        }
    }

    @Override
    public void initialize(Canvas canvas) {
        canvas.addKeyListener(player);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public static void playSpaceSound() {
        floatingInSpace.play();
        floatingInSpace.loop();
    }

    public static void stopSpaceSound() {
        floatingInSpace.stop();
    }
}
