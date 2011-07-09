/*
 * Created on 31.jan.2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package no.ivark.soccerdemo.vectormath;



/**
 * @author ivark
 *
 */
public class Matrix4x4 { 
	static public final Matrix4x4 IDENTITY=new Matrix4x4(1,0,0,0,
                                                         0,1,0,0,
                                                         0,0,1,0,
                                                         0,0,0,1);	
	public float[][] val=new float[4][4];

    public Matrix4x4(float v00,float v10,float v20,float v30,
                     float v01,float v11,float v21,float v31,
                     float v02,float v12,float v22,float v32,
                     float v03,float v13,float v23,float v33) {
        init(v00, v10, v20, v30, v01, v11, v21, v31, v02, v12, v22, v32, v03,v13, v23, v33);
    }



	public void init(float v00, float v10, float v20, float v30, 
			         float v01, float v11, float v21, float v31, 
			         float v02, float v12, float v22, float v32, 
			         float v03, float v13, float v23, float v33) {
		val[0][0]=v00; val[1][0]=v10;val[2][0]=v20;val[3][0]=v30;
        val[0][1]=v01; val[1][1]=v11;val[2][1]=v21;val[3][1]=v31;
        val[0][2]=v02; val[1][2]=v12;val[2][2]=v22;val[3][2]=v32;
        val[0][3]=v03; val[1][3]=v13;val[2][3]=v23;val[3][3]=v33;
	}
    

	
	public Matrix4x4(float v00,float v10,float v20,
                     float v01,float v11,float v21,
                     float v02,float v12,float v22) {
		init(v00,v10,v20,0f,
	         v01,v11,v21,0f,
	         v02,v12,v22,0f,
	         0f,0f,0f,1f);	
	}
		
	static public Matrix4x4 multiply(Matrix4x4 a,Matrix4x4 b) {
		return new Matrix4x4
                   (a.val[0][0]*b.val[0][0]+a.val[1][0]*b.val[0][1]+a.val[2][0]*b.val[0][2]+a.val[3][0]*b.val[0][3],
                    a.val[0][0]*b.val[1][0]+a.val[1][0]*b.val[1][1]+a.val[2][0]*b.val[1][2]+a.val[3][0]*b.val[1][3],
                    a.val[0][0]*b.val[2][0]+a.val[1][0]*b.val[2][1]+a.val[2][0]*b.val[2][2]+a.val[3][0]*b.val[2][3],
                    a.val[0][0]*b.val[3][0]+a.val[1][0]*b.val[3][1]+a.val[2][0]*b.val[3][2]+a.val[3][0]*b.val[3][3],
					
                    a.val[0][1]*b.val[0][0]+a.val[1][1]*b.val[0][1]+a.val[2][1]*b.val[0][2]+a.val[3][1]*b.val[0][3],
                    a.val[0][1]*b.val[1][0]+a.val[1][1]*b.val[1][1]+a.val[2][1]*b.val[1][2]+a.val[3][1]*b.val[1][3],
                    a.val[0][1]*b.val[2][0]+a.val[1][1]*b.val[2][1]+a.val[2][1]*b.val[2][2]+a.val[3][1]*b.val[2][3],
                    a.val[0][1]*b.val[3][0]+a.val[1][1]*b.val[3][1]+a.val[2][1]*b.val[3][2]+a.val[3][1]*b.val[3][3],

                    a.val[0][2]*b.val[0][0]+a.val[1][2]*b.val[0][1]+a.val[2][2]*b.val[0][2]+a.val[3][2]*b.val[0][3],
                    a.val[0][2]*b.val[1][0]+a.val[1][2]*b.val[1][1]+a.val[2][2]*b.val[1][2]+a.val[3][2]*b.val[1][3],
                    a.val[0][2]*b.val[2][0]+a.val[1][2]*b.val[2][1]+a.val[2][2]*b.val[2][2]+a.val[3][2]*b.val[2][3],
                    a.val[0][2]*b.val[3][0]+a.val[1][2]*b.val[3][1]+a.val[2][2]*b.val[3][2]+a.val[3][2]*b.val[3][3],

                    a.val[0][3]*b.val[0][0]+a.val[1][3]*b.val[0][1]+a.val[2][3]*b.val[0][2]+a.val[3][3]*b.val[0][3],
                    a.val[0][3]*b.val[1][0]+a.val[1][3]*b.val[1][1]+a.val[2][3]*b.val[1][2]+a.val[3][3]*b.val[1][3],
                    a.val[0][3]*b.val[2][0]+a.val[1][3]*b.val[2][1]+a.val[2][3]*b.val[2][2]+a.val[3][3]*b.val[2][3],
                    a.val[0][3]*b.val[3][0]+a.val[1][3]*b.val[3][1]+a.val[2][3]*b.val[3][2]+a.val[3][3]*b.val[3][3]);		
	}
	
	public void leftMultiply(Matrix4x4 b) {
		init(val[0][0]*b.val[0][0]+val[1][0]*b.val[0][1]+val[2][0]*b.val[0][2]+val[3][0]*b.val[0][3],
             val[0][0]*b.val[1][0]+val[1][0]*b.val[1][1]+val[2][0]*b.val[1][2]+val[3][0]*b.val[1][3],
             val[0][0]*b.val[2][0]+val[1][0]*b.val[2][1]+val[2][0]*b.val[2][2]+val[3][0]*b.val[2][3],
             val[0][0]*b.val[3][0]+val[1][0]*b.val[3][1]+val[2][0]*b.val[3][2]+val[3][0]*b.val[3][3],
					
             val[0][1]*b.val[0][0]+val[1][1]*b.val[0][1]+val[2][1]*b.val[0][2]+val[3][1]*b.val[0][3],
             val[0][1]*b.val[1][0]+val[1][1]*b.val[1][1]+val[2][1]*b.val[1][2]+val[3][1]*b.val[1][3],
             val[0][1]*b.val[2][0]+val[1][1]*b.val[2][1]+val[2][1]*b.val[2][2]+val[3][1]*b.val[2][3],
             val[0][1]*b.val[3][0]+val[1][1]*b.val[3][1]+val[2][1]*b.val[3][2]+val[3][1]*b.val[3][3],

             val[0][2]*b.val[0][0]+val[1][2]*b.val[0][1]+val[2][2]*b.val[0][2]+val[3][2]*b.val[0][3],
             val[0][2]*b.val[1][0]+val[1][2]*b.val[1][1]+val[2][2]*b.val[1][2]+val[3][2]*b.val[1][3],
             val[0][2]*b.val[2][0]+val[1][2]*b.val[2][1]+val[2][2]*b.val[2][2]+val[3][2]*b.val[2][3],
             val[0][2]*b.val[3][0]+val[1][2]*b.val[3][1]+val[2][2]*b.val[3][2]+val[3][2]*b.val[3][3],

             val[0][3]*b.val[0][0]+val[1][3]*b.val[0][1]+val[2][3]*b.val[0][2]+val[3][3]*b.val[0][3],
             val[0][3]*b.val[1][0]+val[1][3]*b.val[1][1]+val[2][3]*b.val[1][2]+val[3][3]*b.val[1][3],
             val[0][3]*b.val[2][0]+val[1][3]*b.val[2][1]+val[2][3]*b.val[2][2]+val[3][3]*b.val[2][3],
             val[0][3]*b.val[3][0]+val[1][3]*b.val[3][1]+val[2][3]*b.val[3][2]+val[3][3]*b.val[3][3]);		
	}
    
    public float[] getAsFloatArray() {
        return new float[] {val[0][0],val[0][1],val[0][2],val[0][3],
                            val[1][0],val[1][1],val[1][2],val[1][3],
                            val[2][0],val[2][1],val[2][2],val[2][3],
                            val[3][0],val[3][1],val[3][2],val[3][3]};
    }
	
	public Vector3D multiply(Vector3D v) {
        float x=v.getX();
        float y=v.getY();
        float z=v.getZ();
		return new Vector3D(val[0][0]*x+val[1][0]*y+val[2][0]*z+val[3][0],
				            val[0][1]*x+val[1][1]*y+val[2][1]*z+val[3][1],
				            val[0][2]*x+val[1][2]*y+val[2][2]*z+val[3][2]);
	}
	
	public void dump() {
		for (int i=0;i<4;i++) {
			System.out.println("["+val[0][i]+","+val[1][i]+","+val[2][i]+","+val[3][i]+"]" );
		}
	}
}


