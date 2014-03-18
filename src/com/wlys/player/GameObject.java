package com.wlys.player;

import com.badlogic.gdx.math.Rectangle;

public class GameObject
{
    public static final int NORMAL_STATE = 1;
    public static final int DEAD_STATE = 2;

    private float x;
    private float y;
    private float width;
    private float height;

    protected float stateTime;
    protected int state;
    protected CollisionGeometry geometry;// 碰撞区域集合,相对坐标

    private boolean inCollision;// 是否碰撞
    private boolean isDemandRemove;// 是否请求删除
    private Rectangle bounds;

    public GameObject()
    {
        bounds = new Rectangle();
        inCollision = false;
        bounds = new Rectangle();
        state = NORMAL_STATE;
    }

    /**
     * Returns this <code>GameObject</code>'s bounding rectangle.
     * 
     * @return the bounding rectangle.
     */
    public Rectangle bounds()
    {
        bounds.x = getX();
        bounds.y = getY();
        bounds.width = getWidth();
        bounds.height = getHeight();
        return bounds;
    }

    /**
     * Returns true if this game object's bounds intersect with the given
     * rectangle.
     * 
     * @param r
     *            the rectangle to intersect.
     * @return true if the bounds intersect.
     */
    public boolean boundsIntersect(Rectangle r)
    {
        return Colliders.intersects(bounds(), r);
    }

    /**
     * Returns true if this game object's bounds intersect with the given game
     * object.
     * 
     * @param go
     *            the other game object.
     * @return true if the bounds intersect.
     */
    public boolean boundsIntersect(GameObject go)
    {
        return Colliders.intersects(bounds(), go.bounds());
    }

    /**
     * Returns true if this game object's collision geometry intersects with the
     * given rectangle.
     * 
     * @param r
     *            the rectangle to intersect.
     * @return true if the geometry intersects with the rectangle.
     */
    public boolean geometryIntersects(Rectangle r)
    {
        return geometry.intersects(r, getX(), getY());
    }

    /**
     * Returns true if this game object's collision geometry intersects with
     * another game object's collision geometry.
     * 
     * @param go
     *            the other game object.
     * @return true if the geometries intersect.
     */
    public boolean geometryIntersects(GameObject go)
    {
        return geometry.intersects(getX(), getY(), go.geometry, go.getX(),
                go.getY());
    }

    /**
     * Returns true if this game object is in collision with a rectangle. It
     * first does a simple box test against this game object's bounds, then, if
     * that's true, tests its collision geometry against the rectangle.
     * 
     * @param r
     *            the rectangle to intersect.
     * @return true if this game object intersects the rectangle.
     */
    public boolean intersects(Rectangle r)
    {
        return boundsIntersect(r)
                && (geometry == null || geometryIntersects(r));
    }

    /**
     * Returns true if this game object is in collision with another game
     * object. It first does a bounds test, then, if that's true, tests its
     * collision geometry against the other game object's collision geometry.
     */
    public boolean intersects(GameObject go)
    {
        if (!boundsIntersect(go))
        {
            return false;
        }
        if (geometry == null)
        {
            return go.geometry == null || go.geometryIntersects(bounds());
        }
        else if (go.geometry == null)
        {
            return geometryIntersects(go.bounds());
        }
        return geometryIntersects(go);
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public float getY()
    {
        return y;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width = width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height = height;
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void translate(float offsetX, float offsetY)
    {
        this.x += offsetX;
        this.y += offsetY;
    }
    
    public boolean isInCollision()
    {
        return inCollision;
    }

    public void setInCollision(boolean inCollision)
    {
        this.inCollision = inCollision;
    }

    public boolean isDemandRemove()
    {
        return isDemandRemove;
    }

    public void setDemandRemove(boolean isDemandRemove)
    {
        this.isDemandRemove = isDemandRemove;
    }

    public void reset()
    {
        inCollision = false;
        isDemandRemove = false;
        state = NORMAL_STATE;
    }

}
