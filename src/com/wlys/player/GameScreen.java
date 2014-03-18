package com.wlys.player;

import java.util.logging.Logger;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.wlys.player.Colliders.ColliderHandler;
import com.wlys.player.Colliders.RemovalHandler;

public class GameScreen implements Screen, GestureListener
{
    private static Logger logger = Logger.getLogger("feiji");

    private static final int MAX_PLAYER_BULLET = 10;
    private static final int MAX_ENEMY = 20;
    private static final int MAX_ENEMY_BULLET = MAX_ENEMY * 2;
    private static final int MAX_EXPLODE = 20;

    private Game game;
    private Camera camera;
    private SpriteBatch spriteBatch;
    private BitmapFont mBitmapFont;
    private FreeTypeFontGenerator generator;
    private FreeTypeBitmapFontData fontData;
    private BitmapFont mScoreFont;
    private Player player;
    // .size 是真实大小
    private Array<PlayerBullet> playerBulletList;
    private Array<Enemy> enemyList;
    private Array<EnemyBullet> enemyBulletList;
    private Array<Explode> explodeList;

    private Texture bgTexture;
    private float stateTime;
    private WorldNotifier notifier;

    private final Pool<PlayerBullet> playerBulletPool;// 主角子弹池
    private final Pool<Enemy> enemyPool;// 敌人池
    private final Pool<EnemyBullet> enemyBulletPool;// 敌人子弹池
    private final Pool<Explode> explodePool;// 爆炸池
    private Rectangle roomBounds;
    private float deltaY;

    private int score;

    private final ColliderHandler<GameObject, GameObject> playerEnemyCollisionHandler = new ColliderHandler<GameObject, GameObject>()
    {
        @Override
        public void onCollision(GameObject t, GameObject u)
        {
            t.setInCollision(true);
            u.setInCollision(true);
            notifier.onPlayerHit((Player) t);
        }
    };
    private final ColliderHandler<GameObject, GameObject> playerEnemyBulletCollisionHandler = new ColliderHandler<GameObject, GameObject>()
    {
        @Override
        public void onCollision(GameObject t, GameObject u)
        {
            t.setInCollision(true);
            u.setInCollision(true);
            notifier.onPlayerHit((Player) t);
        }
    };
    private final ColliderHandler<GameObject, GameObject> playerBulletEnemyCollisionHandler = new ColliderHandler<GameObject, GameObject>()
    {
        @Override
        public void onCollision(GameObject t, GameObject u)
        {
            t.setInCollision(true);
            u.setInCollision(true);
        }
    };
    private RemovalHandler<PlayerBullet> playerBulletRemovalHandler = new RemovalHandler<PlayerBullet>()
    {
        @Override
        public void onRemove(PlayerBullet t)
        {

        }
    };
    private RemovalHandler<Enemy> enemyRemovalHandler = new RemovalHandler<Enemy>()
    {
        @Override
        public void onRemove(Enemy t)
        {
            notifier.onEnemyDestroyed(t);
        }
    };
    private RemovalHandler<EnemyBullet> enemyBulletRemovalHandler = new RemovalHandler<EnemyBullet>()
    {
        @Override
        public void onRemove(EnemyBullet t)
        {

        }
    };
    private RemovalHandler<Explode> explodeRemovalHandler = new RemovalHandler<Explode>()
    {
        @Override
        public void onRemove(Explode t)
        {

        }
    };

    public GameScreen(Game game)
    {
        super();
        this.game = game;
        spriteBatch = new SpriteBatch();

        playerBulletPool = new Pool<PlayerBullet>(MAX_PLAYER_BULLET,
                MAX_PLAYER_BULLET)
        {
            @Override
            protected PlayerBullet newObject()
            {
                return new PlayerBullet();
            }
        };
        enemyPool = new Pool<Enemy>(MAX_ENEMY, MAX_ENEMY)
        {
            @Override
            protected Enemy newObject()
            {
                return new Enemy(GameScreen.this);
            }
        };
        enemyBulletPool = new Pool<EnemyBullet>(MAX_ENEMY_BULLET,
                MAX_ENEMY_BULLET)
        {
            @Override
            protected EnemyBullet newObject()
            {
                return new EnemyBullet();
            }
        };
        explodePool = new Pool<Explode>(MAX_EXPLODE, MAX_EXPLODE)
        {
            @Override
            protected Explode newObject()
            {
                return new Explode();
            }
        };
        roomBounds = new Rectangle();
        notifier = new WorldNotifier();
        camera = new OrthographicCamera(Config.WIDTH, Config.HEIGHT);
        camera.position.set(camera.viewportWidth / 2,
                camera.viewportHeight / 2, 0);
        mBitmapFont = new BitmapFont();
        roomBounds.x = 0;
        roomBounds.y = 0;
        roomBounds.width = Config.WIDTH;
        roomBounds.height = Config.HEIGHT;

        generator = new FreeTypeFontGenerator(
                Gdx.files.internal("font/font.ttf"));
        fontData = generator.generateData(40,
                FreeTypeFontGenerator.DEFAULT_CHARS, false);
        mScoreFont = new BitmapFont(fontData, fontData.getTextureRegion(),
                false);

        bgTexture = new Texture(Gdx.files.internal("gfx/main/back.png"));

        player = new Player(this);
        playerBulletList = Pools.makeArrayFromPool(playerBulletList,
                playerBulletPool, MAX_PLAYER_BULLET);
        enemyList = Pools.makeArrayFromPool(enemyList, enemyPool, MAX_ENEMY);
        enemyBulletList = Pools.makeArrayFromPool(enemyBulletList,
                enemyBulletPool, MAX_ENEMY_BULLET);
        explodeList = Pools.makeArrayFromPool(explodeList, explodePool,
                MAX_EXPLODE);
        player.setPosition((Gdx.graphics.getWidth() - player.getWidth()) / 2f,
                Gdx.graphics.getHeight() / 3f);

        notifier.addListener(new ExplodeAdapter(this));

        addEnemyGroup();

        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        draw(delta);
    }

    private void update(float delta)
    {
        stateTime += delta;
        // 4秒一次敌人的编队出现
        if (stateTime > 4)
        {
            stateTime = 0;
            addEnemyGroup();
        }
        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);
        player.update();
        for (PlayerBullet bullet : playerBulletList)
        {
            bullet.update();
        }
        for (Enemy enemy : enemyList)
        {
            enemy.update();
        }
        for (EnemyBullet bullet : enemyBulletList)
        {
            bullet.update();
        }
        for (Explode explode : explodeList)
        {
            explode.update();
        }
        checkCollisions();
        removeMarkedMobiles();
        deltaY--;
    }

    public void draw(float delta)
    {
        // 设置背景
        Gdx.gl.glClearColor(0.7f, 0.7f, 1.0f, 1);
        // 清屏
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        spriteBatch.begin();
        spriteBatch.draw(bgTexture, 0, deltaY % Config.HEIGHT, Config.WIDTH,
                Config.HEIGHT, 0, 1, 1, 0);// u v u2 v2
        spriteBatch.draw(bgTexture, 0, Config.HEIGHT + deltaY % Config.HEIGHT,
                Config.WIDTH, Config.HEIGHT, 0, 1, 1, 0);// u v u2 v2
        player.draw(spriteBatch);
        for (PlayerBullet bullet : playerBulletList)
        {
            bullet.draw(spriteBatch);
        }
        for (Enemy enemy : enemyList)
        {
            enemy.draw(spriteBatch);
        }
        for (EnemyBullet bullet : enemyBulletList)
        {
            bullet.draw(spriteBatch);
        }
        for (Explode explode : explodeList)
        {
            explode.draw(spriteBatch);
        }
        mScoreFont.setColor(Color.BLACK);
        mScoreFont.draw(spriteBatch, "score:" + score, 100, 100);
        mScoreFont.setColor(Color.WHITE);
        mScoreFont.draw(spriteBatch, "score:" + score, 100 - 5, 100 + 5);

        mBitmapFont.draw(spriteBatch,
                "fps:" + Gdx.graphics.getFramesPerSecond(), 20, 20);
        spriteBatch.end();
    }

    private void checkCollisions()
    {
        Colliders.collide(player, enemyList, playerEnemyCollisionHandler);
        Colliders.collide(player, enemyBulletList,
                playerEnemyBulletCollisionHandler);
        Colliders.collide(playerBulletList, enemyList,
                playerBulletEnemyCollisionHandler);
    }

    private void removeMarkedMobiles()
    {
        // 删除掉超出屏幕的对象
        Colliders.removeOutOfBounds(playerBulletPool, playerBulletList,
                roomBounds);
        Colliders.removeOutOfBounds(enemyPool, enemyList, roomBounds);
        Colliders.removeOutOfBounds(enemyBulletPool, enemyBulletList,
                roomBounds);
        // 删除掉被标记的对象
        Colliders.removeMarkedCollisions(playerBulletPool, playerBulletList,
                playerBulletRemovalHandler);
        Colliders.removeMarkedCollisions(enemyPool, enemyList,
                enemyRemovalHandler);
        Colliders.removeMarkedCollisions(enemyBulletPool, enemyBulletList,
                enemyBulletRemovalHandler);
        Colliders.removeMarkedCollisions(explodePool, explodeList,
                explodeRemovalHandler);
    }

    @Override
    public void resize(int width, int height)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void show()
    {
        Gdx.input.setInputProcessor(new GestureDetector(this));
        player.reset();
    }

    private void addEnemyGroup()
    {
        for (int i = 0; i < 5; i++)
        {
            addEnemy();
        }
    }

    private void addEnemy()
    {
        if (enemyList.size < MAX_ENEMY)
        {
            Enemy enemy = enemyPool.obtain();
            enemy.reset();
            float x = (Gdx.graphics.getWidth() - enemy.getWidth())
                    * (float) Math.random();
            float y = (Gdx.graphics.getHeight() - enemy.getHeight())
                    - Gdx.graphics.getHeight() / 4f * (float) Math.random();
            enemy.setPosition(x, y);
            enemyList.add(enemy);
            logger.info("添加敌人：" + enemy.toString() + " 总数：" + enemyList.size);
        }
    }

    public void addPlayerBullet(float x, float y)
    {
        if (playerBulletList.size < MAX_PLAYER_BULLET)
        {
            PlayerBullet playerBullet = playerBulletPool.obtain();
            playerBullet.reset();
            playerBullet.setPosition(x, y);
            playerBulletList.add(playerBullet);
            logger.info("添加玩家子弹：" + playerBullet.toString() + " 总数："
                    + playerBulletList.size);
        }
    }

    public void addEnemyBullet(float x, float y)
    {
        if (enemyBulletList.size < MAX_ENEMY_BULLET)
        {
            EnemyBullet bullet = enemyBulletPool.obtain();
            bullet.reset();
            bullet.setPosition(x, y);
            enemyBulletList.add(bullet);
            logger.info("添加敌人子弹：" + bullet.toString() + " 总数："
                    + enemyBulletList.size);
        }
    }

    public void addExplode(float x, float y)
    {
        if (explodeList.size < MAX_EXPLODE)
        {
            Explode explode = explodePool.obtain();
            explode.reset();
            explode.setPosition(x, y);
            explodeList.add(explode);
            logger.info("添加爆炸：" + explode.toString() + " 总数："
                    + explodeList.size);
        }
    }

    @Override
    public void hide()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void resume()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void dispose()
    {
        player.clear();
        for (PlayerBullet bullet : playerBulletList)
        {
            bullet.clear();
        }
        playerBulletList.clear();
        for (Enemy enemy : enemyList)
        {
            enemy.clear();
        }
        enemyList.clear();
        for (EnemyBullet bullet : enemyBulletList)
        {
            bullet.clear();
        }
        enemyBulletList.clear();
        for (Explode explode : explodeList)
        {
            explode.clear();
        }
        explodeList.clear();
        playerBulletPool.clear();
        enemyPool.clear();
        enemyBulletPool.clear();
        explodePool.clear();
        bgTexture.dispose();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean longPress(float x, float y)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY)
    {
        float centerX = player.getX() + player.getWidth() / 2f;
        float centerY = player.getY() + player.getHeight() / 2f;
        float newCenterX = centerX + deltaX;
        float newCenterY = centerY - deltaY;
        if (newCenterX > 0 && newCenterX < Gdx.graphics.getWidth())
        {
            player.translate(deltaX, 0);
        }
        if (newCenterY > 0 && newCenterY < Gdx.graphics.getHeight())
        {
            player.translate(0, -deltaY);
        }
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance)
    {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
            Vector2 pointer1, Vector2 pointer2)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Game getGame()
    {
        return game;
    }

    public void addScore(int value)
    {
        score += value;
    }
}
