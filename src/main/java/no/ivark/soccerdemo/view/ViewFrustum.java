package no.ivark.soccerdemo.view;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import no.ivark.soccerdemo.vectormath.Matrix4x4;
import no.ivark.soccerdemo.vectormath.Vector3D;

/**
 * @author ivark
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ViewFrustum extends GLCanvas {
	private ViewFrustum self=this;
	public Vector3D pos;
	public Vector3D dir;
	public Vector3D up;
	List<Drawable> drawables=new ArrayList<Drawable>();
	List<Updatable> updatables=new ArrayList<Updatable>();
	protected boolean filled=true;
	protected boolean texture=true;
	protected boolean fogging=true;

	private float[] proj=new float[16];
	private float[] modl=new float[16];

	// Clip plane values
	public float[] rightClip=new float[4];
	public float[] leftClip=new float[4];
	public float[] bottomClip=new float[4];
	public float[] upClip=new float[4];
	public float[] farClip=new float[4];
	public float[] nearClip=new float[4];

	protected boolean birdeye=false;

	public ViewFrustum(Vector3D pos, Vector3D dir, Vector3D up) {
		super(new GLCapabilities(null));
		this.pos=pos;
		this.dir=dir;
		this.up=up;
		normalize();
		init();
	}

	public ViewFrustum() {
		this(new Vector3D(-200.0f,-200.0f,100.0f),
             new Vector3D(1.0f,1.0f,-0.5f),
             new Vector3D(0.0f,0.0f,1.0f));
	}


	public void init() {
		setAutoSwapBufferMode(true);
		addGLEventListener(new GLEventListener() {
			GLU glu=new GLU();
			
			public void display(GLAutoDrawable glad) {
				final GL2 gl=glad.getGL().getGL2();
				fog(gl);
				material(gl);
				light(gl);
				texture(gl);
				gl.glShadeModel(GL2.GL_SMOOTH);
                
				gl.glEnable(GL2.GL_DEPTH_TEST);
				gl.glEnable(GL2.GL_CULL_FACE);

				gl.glCullFace(GL2.GL_BACK);

				gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

				gl.glMatrixMode(GL2.GL_PROJECTION);
				gl.glLoadIdentity();
				
				int w=glad.getWidth();
				int h=glad.getHeight();
				float aspect=(float)w/(float)h;
				
				
				glu.gluPerspective(60.0,aspect,0.1,3000.0);
				gl.glMatrixMode(GL2.GL_MODELVIEW);
				gl.glLoadIdentity();
				gl.glPushMatrix();
				gl.glEnable(GL2.GL_NORMALIZE);


                

				glu.gluLookAt(pos.getX(),pos.getY(),pos.getZ(),
						      pos.getX()+dir.getX(),pos.getY()+dir.getY(),pos.getZ()+dir.getZ(),
						      up.getX(),up.getY(),up.getZ());

				initClipPlanes(gl);

				if (birdeye) {
					gl.glMatrixMode(GL2.GL_MODELVIEW);
					gl.glLoadIdentity();
					glu.gluLookAt(1000.0f,1000.0f,2000.0f,
							      1000.0f,1000.0f,0.0f,
								  1.0f,0.0f,0.0f);
				}

				gl.glPolygonMode(GL2.GL_FRONT,filled ? GL2.GL_FILL : GL2.GL_LINE);

				// Draw the drawables
				for (Drawable d:drawables) {
					d.draw(self,gl,glu);
				}
				gl.glPopMatrix();
				gl.glFlush();
				
				// Update the updatables
				for (Updatable u:updatables) {
					u.update();
				}
			}
			
			public void dispose(GLAutoDrawable arg0) {
				// TODO Auto-generated method stub
				
			}

			public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
				System.out.println("DisplayChanged");
			}

			public void init(GLAutoDrawable glad) {
				GL2 gl=glad.getGL().getGL2();
				for (Drawable d:drawables) {
					try {
						d.init(self, gl, glu);
					} catch (Exception e) {
						System.out.println("Failed to init drawable :"+d+","+e);
						e.printStackTrace();
					}
				}

			}

			public void reshape(GLAutoDrawable glad, int x, int y, int w, int h) {
				System.out.println("Reshape");
				GL2 gl=glad.getGL().getGL2();
				gl.glViewport(0, 0, w, h);
				gl.glMatrixMode(GL2.GL_PROJECTION);
				initClipPlanes(gl);
			}
		});
	}

	public void registerDrawable(Drawable d)	{
		drawables.add(d);
	}
	
	public void registerUpdatable(Updatable u) {
		updatables.add(u);
	}

	private void fog(GL2 gl)
	{
		if (fogging)
			gl.glEnable(GL2.GL_FOG);
		else
			gl.glDisable(GL2.GL_FOG);

		float[] color={0.6f,0.6f,1.0f,1.0f};
		gl.glClearColor(0.6f,0.6f,1.0f,1.0f);
		gl.glFogfv(GL2.GL_FOG_COLOR,color,0);
		gl.glFogf(GL2.GL_FOG_START,10);
		gl.glFogf(GL2.GL_FOG_END,500);
		gl.glFogi(GL2.GL_FOG_MODE,GL2.GL_LINEAR);
	}

	private void material(GL2 gl)
	{
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		float[] specRef={0.2f,0.2f,0.3f,1.0f};
		gl.glColorMaterial(GL2.GL_FRONT,GL2.GL_AMBIENT_AND_DIFFUSE);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, specRef,0);
		gl.glMateriali(GL2.GL_FRONT, GL2.GL_SHININESS, 10);
	}

	public void texture(GL2 gl) {
		if (texture) {
			gl.glEnable(GL2.GL_TEXTURE_2D);
		} else {
			gl.glDisable(GL2.GL_TEXTURE_2D);
		}
	}

	private void light(GL2 gl) {
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT1);
        float[] lightAmbient={ 0.2f, 0.2f, 0.2f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, lightAmbient,0);
        float[] lightDiffuse={ 0.9f, 0.9f, 0.8f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, lightDiffuse,0);
        float[] lightPos={ 40.0f, 40.0f, 10.0f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, lightPos,0);
        float[] direction={ 40.0f, 40.0f, -10.0f, 1.0f };
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPOT_DIRECTION,direction,0);
	}

	protected void normalize()
	{
		dir=dir.getNormalized();
		up=up.getNormalized();
	}

	private void initClipPlanes(GL gl)	{
		gl.glGetFloatv( GL2.GL_PROJECTION_MATRIX, proj,0);
		gl.glGetFloatv( GL2.GL_MODELVIEW_MATRIX, modl,0);

		Matrix4x4 projMatrix=new Matrix4x4(proj[0],proj[1],proj[2],proj[3],
				                                   proj[4],proj[5],proj[6],proj[7],
												   proj[8],proj[9],proj[10],proj[11],
												   proj[12],proj[13],proj[14],proj[15]);

		Matrix4x4 modlMatrix=new Matrix4x4(modl[0],modl[1],modl[2],modl[3],
				                                   modl[4],modl[5],modl[6],modl[7],
												   modl[8],modl[9],modl[10],modl[11],
												   modl[12],modl[13],modl[14],modl[15]);

		Matrix4x4 clipMatrix=Matrix4x4.multiply(modlMatrix,projMatrix);

		for (int i=0;i<4;i++) {
			rightClip[i]=clipMatrix.val[3][i]-clipMatrix.val[0][i];
			leftClip[i]=clipMatrix.val[3][i]+clipMatrix.val[0][i];
			bottomClip[i]=clipMatrix.val[3][i]+clipMatrix.val[1][i];
			upClip[i]=clipMatrix.val[3][i]-clipMatrix.val[1][i];
			farClip[i]=clipMatrix.val[3][i]-clipMatrix.val[2][i];
			nearClip[i]=clipMatrix.val[3][i]+clipMatrix.val[2][i];
		}

		float rightLength=(float)Math.sqrt(rightClip[0]*rightClip[0]+
				                           rightClip[1]*rightClip[1]+
				                           rightClip[2]*rightClip[2]);

		float leftLength=(float)Math.sqrt(leftClip[0]*leftClip[0]+
		                                  leftClip[1]*leftClip[1]+
		                                  leftClip[2]*leftClip[2]);

		float bottomLength=(float)Math.sqrt(bottomClip[0]*bottomClip[0]+
				                            bottomClip[1]*bottomClip[1]+
				                            bottomClip[2]*bottomClip[2]);

		float upLength=(float)Math.sqrt(upClip[0]*upClip[0]+
				                        upClip[1]*upClip[1]+
				                        upClip[2]*upClip[2]);

		float farLength=(float)Math.sqrt(farClip[0]*farClip[0]+
				                         farClip[1]*farClip[1]+
				                         farClip[2]*farClip[2]);

		float nearLength=(float)Math.sqrt(nearClip[0]*nearClip[0]+
				                          nearClip[1]*nearClip[1]+
				                          nearClip[2]*nearClip[2]);

		for (int i=0;i<4;i++) {
			rightClip[i]/=rightLength;
			leftClip[i]/=leftLength;
			bottomClip[i]/=bottomLength;
			upClip[i]/=upLength;
			farClip[i]/=farLength;
			nearClip[i]/=nearLength;
		}
	}
}
