package no.ivark.soccerdemo.vectormath;

public class SinCos {
	static public final float[] SIN=new float[360];
	static public final float[] COS=new float[360];

	static {
		for (int i=0;i<360;i++) {
			SIN[i]=(float)Math.sin(i/180.0*Math.PI);
			COS[i]=(float)Math.cos(i/180.0*Math.PI);
		}
	}

//	static public void main(String[] args) {
//		long start=System.currentTimeMillis();
//		float a=0;
//		for (int i=0;i<10000000;i++) {
//			for (int j=0;j<360;j++) {
//				float ang=(float)(j/180.0*Math.PI);
//				//a+=SIN[j];
//				a+=Math.sin(ang);
//			}
//		}
//		System.out.println("Took "+(System.currentTimeMillis()-start)+"ms to calculate "+a);
//	}

}
