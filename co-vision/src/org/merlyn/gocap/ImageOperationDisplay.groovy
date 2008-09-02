package org.merlyn.gocap

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class ImageOperationDisplay extends JFrame implements FocusListener {
	static void main(args) {
	    //new ImageOperationDisplay("game3/squewed/gocap122335.jpg", "gamedata/00039.jpg")
	    new ImageOperationDisplay("game4/squewed/gocap122418.jpg", null)
	}

	def image1, image2, result, canvas

	ImageOperationDisplay(jpg1, jpg2) {
	    image1 = jpg1
	    image2 = jpg2

//	    updateImage()

	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
	    addFocusListener(this)
	    show()
	}

	def updateImage() {
	    try {
    	    ClassLoader parent = getClass().getClassLoader();
    	    GroovyClassLoader loader = new GroovyClassLoader(parent);
    	    Class groovyClass = loader.parseClass(new File("src/org/merlyn/gocap/BoardDetectionOperator.groovy"));

    	    long start = System.currentTimeMillis()

    	    Object[] cargs = [image1, image2]
    	    GroovyObject groovyObject = (GroovyObject) groovyClass.newInstance(cargs)
    	    Object[] args = []
    	    result = groovyObject.invokeMethod("getImg", args)

    	    long end = System.currentTimeMillis()

    	    println "Took ${end-start}"

    	    if (result instanceof BufferedImage) {
    	        def label = new JLabel(new ImageIcon(result))
    	        setContentPane(label)
//    	        getContentPane().add(label, BorderLayout.CENTER)
    	    } else {
    	        def label = new JLabel(new ImageIcon(result.getAsBufferedImage()))
    	        setContentPane(label)
    	    }
    	    pack()
	    } catch (Exception e) {
	        e.printStackTrace()
	    }
	}

	public void focusGained(FocusEvent e) { updateImage() }
	public void focusLost(FocusEvent e) {}
}