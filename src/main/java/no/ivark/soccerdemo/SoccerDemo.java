package no.ivark.soccerdemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import no.ivark.soccerdemo.movement.MovementManager;
import no.ivark.soccerdemo.vectormath.Vector3D;
import no.ivark.soccerdemo.view.Football;
import no.ivark.soccerdemo.view.MovingObjectDrawer;
import no.ivark.soccerdemo.view.Pilot;
import no.ivark.soccerdemo.view.Player;
import no.ivark.soccerdemo.view.SoccerField;



/**
   * This is a basic JOGL app. Feel free to
   * reuse this code or modify it.
   */
public class SoccerDemo extends JFrame {
	static private final Random rand=new Random();
   public static void main(String[] args) throws Exception {
	   System.out.println("java.library.path="+System.getProperty("java.library.path"));
      final SoccerDemo app = new SoccerDemo();


      // show what we've done
      SwingUtilities.invokeLater (
         new Runnable() {
            public void run() {
               app.setVisible(true);
            }
         }
      );
   }


   public SoccerDemo() throws Exception {
      super("Marius' funny football simulator");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Pilot p=new Pilot(new Vector3D(-28.090666f,16.929398f,14.503402f),
                        new Vector3D(0.7510448f,-0.4235228f,-0.50651765f),
                        new Vector3D(0,0,1));
      
      MovementManager mm=new MovementManager();
      MovingObjectDrawer mod=new MovingObjectDrawer();
      
      Football football=new Football();
      football.setPos(new Vector3D(0,0,2));
      mm.register(football);
      mod.register(football);
      
      for (int i=0;i<2000;i++) {
    	  Football fb=new Football();
    	  fb.setPos(new Vector3D((float)(i/20*Math.sin(i)/10)+0f,(float)(i/20*Math.cos(i)/10),10));
    	  mm.register(fb);
    	  mod.register(fb);
      }

      for (int i=0;i<200;i++) {
    	  Player player=new Player(getRandomPosition(),new Color(i/4,250-i/4,i/4)) {
    		  @Override
    		public void onTarget() {
    			setTarget(getRandomPosition());
    		}

    	  };
    	  mm.register(player);
    	  mod.register(player);
    	  player.setTarget(getRandomPosition());
      }


      p.registerDrawable(mod);
      p.registerDrawable(new SoccerField());
      p.registerUpdatable(mm);
      
      
      getContentPane().add(p, BorderLayout.CENTER);
      setSize(1920, 1200);

      centerWindow(this);

      p.start();
   }

   private Vector3D getRandomPosition() {
	   return new Vector3D((float)(rand.nextFloat()*100-50f),(float)(rand.nextFloat()*100-50f),0f);
   }

   public void centerWindow(Component frame) {
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension frameSize = frame.getSize();


      if (frameSize.width > screenSize.width )
         frameSize.width = screenSize.width;
      if (frameSize.height > screenSize.height)
         frameSize.height = screenSize.height;


      frame.setLocation ((screenSize.width - frameSize.width ) >> 1,
    		             (screenSize.height - frameSize.height) >> 1);
   }
}
