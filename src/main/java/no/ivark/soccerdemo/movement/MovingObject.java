package no.ivark.soccerdemo.movement;

import no.ivark.soccerdemo.vectormath.Vector3D;
import no.ivark.soccerdemo.view.Drawable;


public interface MovingObject {
    public Vector3D getPos();
    public Vector3D getVelocity();   
    public float getRadius();
    public void move();
    public void onCollision(MovingObject otherObject);
}
