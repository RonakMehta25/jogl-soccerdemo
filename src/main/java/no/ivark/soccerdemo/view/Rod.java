package no.ivark.soccerdemo.view;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import no.ivark.soccerdemo.vectormath.Matrix4x4;
import no.ivark.soccerdemo.vectormath.Vector3D;

public class Rod implements Drawable {
    private Vector3D[] fromCircle;
    private Vector3D[] toCircle;
    private Vector3D[] fromCircleNorm;
    private Vector3D[] toCircleNorm;
    private Vector3D fromPoint;
    private Vector3D toPoint;
    Vector3D direction;

    public Rod(Vector3D from, Vector3D to, int res, float radius) {
        float dx=to.getX()-from.getX();
        float dy=to.getY()-from.getY();
        float dz=to.getZ()-from.getZ();
        direction=new Vector3D(dx,dy,dz).getNormalized();
        float l=(float)Math.sqrt(dx*dx+dy*dy+dz*dz);

        double xAngle=Math.asin(dy/l);
        double yAngle=Math.asin(-(dx/l)/Math.cos(xAngle));
        if (Math.cos(xAngle)*Math.cos(yAngle)*dz < 0f) {
            yAngle=Math.PI-yAngle;
        }

        Matrix4x4 translation=new Matrix4x4(1f, 0f, 0f, from.getX(),
                                                    0f, 1f, 0f, from.getY(),
                                                    0f, 0f, 1f, from.getZ(),
                                                    0f, 0f, 0f, 1f);

        Matrix4x4 xRot=new Matrix4x4(1f, 0f, 0f,
                                             0f,(float)Math.cos(xAngle), (float)Math.sin(xAngle),
                                             0f, (float)-Math.sin(xAngle), (float)Math.cos(xAngle));

        Matrix4x4 yRot=new Matrix4x4((float)Math.cos(yAngle),0f,(float)-Math.sin(yAngle),
                                             0f,1f,0f,
                                             (float)Math.sin(yAngle),0f,(float)Math.cos(yAngle));

        Matrix4x4 rotation=Matrix4x4.multiply(yRot,xRot);
        Matrix4x4 transform=Matrix4x4.multiply(translation, rotation);

        fromCircle=new Vector3D[res];
        toCircle=new Vector3D[res];
        fromCircleNorm=new Vector3D[res];
        toCircleNorm=new Vector3D[res];

        for (int i=0;i<res;i++) {
            double angle=((double)i) / res * 2*Math.PI;
            float x=(float)(radius*Math.sin(angle));
            float y=(float)(radius*Math.cos(angle));
            fromCircle[i]=transform.multiply(new Vector3D(x, y, 0f));
            toCircle[i]=transform.multiply(new Vector3D(x, y, l));
            fromCircleNorm[i]=rotation.multiply(new Vector3D(x, y, -radius).getNormalized());
            toCircleNorm[i]=rotation.multiply(new Vector3D(x, y, radius).getNormalized());
        }

        fromPoint=transform.multiply(new Vector3D(0,0,-radius/2));
        toPoint=transform.multiply(new Vector3D(0,0,l+radius/2));
    }

    public void init(ViewFrustum frustum, GL2 gl, GLU glu) throws Exception {

    }

    public void draw(ViewFrustum frustum, GL2 gl, GLU glu) {
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glBegin(GL2.GL_TRIANGLE_STRIP);
        for (int i=0;i<fromCircle.length;i++) {
            Vector3D from=fromCircle[i];
            Vector3D to=toCircle[i];
            Vector3D fromNorm=fromCircleNorm[i];
            Vector3D toNorm=toCircleNorm[i];
            gl.glNormal3f(fromNorm.getX(),fromNorm.getY(),fromNorm.getZ());
            gl.glVertex3f(from.getX(),from.getY(),from.getZ());
            gl.glNormal3f(toNorm.getX(),toNorm.getY(),toNorm.getZ());
            gl.glVertex3f(to.getX(),to.getY(),to.getZ());
        }
        gl.glNormal3f(fromCircleNorm[0].getX(),fromCircleNorm[0].getY(),fromCircleNorm[0].getZ());
        gl.glVertex3f(fromCircle[0].getX(),fromCircle[0].getY(),fromCircle[0].getZ());
        gl.glNormal3f(toCircleNorm[0].getX(),toCircleNorm[0].getY(),toCircleNorm[0].getZ());
        gl.glVertex3f(toCircle[0].getX(),toCircle[0].getY(),toCircle[0].getZ());
        gl.glEnd();

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3f(-direction.getX(),-direction.getY(),-direction.getZ());
        gl.glVertex3f(fromPoint.getX(),fromPoint.getY(),fromPoint.getZ());
        for (int i=0;i<fromCircle.length;i++) {
                gl.glNormal3f(fromCircleNorm[i].getX(),fromCircleNorm[i].getY(),fromCircleNorm[i].getZ());
            gl.glVertex3f(fromCircle[i].getX(),fromCircle[i].getY(),fromCircle[i].getZ());
        }
        gl.glNormal3f(fromCircleNorm[0].getX(),fromCircleNorm[0].getY(),fromCircleNorm[0].getZ());
        gl.glVertex3f(fromCircle[0].getX(),fromCircle[0].getY(),fromCircle[0].getZ());
        gl.glEnd();

        gl.glBegin(GL2.GL_TRIANGLE_FAN);
        gl.glNormal3f(direction.getX(),direction.getY(),direction.getZ());
        gl.glVertex3f(toPoint.getX(),toPoint.getY(),toPoint.getZ());

        for (int j=0;j<toCircle.length;j++) {
            int i=toCircle.length-1-j;
            gl.glNormal3f(toCircleNorm[i].getX(),toCircleNorm[i].getY(),toCircleNorm[i].getZ());
            gl.glVertex3f(toCircle[i].getX(),toCircle[i].getY(),toCircle[i].getZ());
        }
        gl.glNormal3f(toCircleNorm[toCircle.length-1].getX(),toCircleNorm[toCircle.length-1].getY(),toCircleNorm[toCircle.length-1].getZ());
        gl.glVertex3f(toCircle[toCircle.length-1].getX(),toCircle[toCircle.length-1].getY(),toCircle[toCircle.length-1].getZ());
        gl.glEnd();
    }
}