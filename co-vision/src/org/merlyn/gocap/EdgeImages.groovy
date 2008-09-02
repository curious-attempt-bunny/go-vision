package org.merlyn.gocap

import com.sun.media.jai.codec.*
import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class EdgeImages {
	static void main(args) {
	    new File('game11/edge').listFiles().each { it.delete() }

		new File('game11/square').list().each { jpg ->
		    if (jpg ==~ /.*\.jpg/) {
		        println "$jpg"
   		        def result = new EdgeOperator("game11/square/$jpg", null).img
   		        outputImage(result, "game11/edge/$jpg")
		    }
		}
	}

	static def outputImage(result, filename) {
	    def out = new File(filename).newOutputStream()
        ImageEncoder encoder = ImageCodec.createImageEncoder("PNG", out, null)
        PNGEncodeParam param = new PNGEncodeParam.Gray()
        encoder.setParam(param)
        encoder.encode(result)
        out.close()
	}
}