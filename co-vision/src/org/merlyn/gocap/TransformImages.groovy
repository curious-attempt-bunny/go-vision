package org.merlyn.gocap

import com.sun.media.jai.codec.*
import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class TransformImages {
	static void main(args) {
	    def last
	    def source = 'game11/squewed'
	    def target = 'game11/square'

	    new File(target).listFiles().each { it.delete() }

		new File(source).list().each { jpg ->
		    if (jpg ==~ /.*\.jpg/) {
    		    if (last) {
    		        def result = new BoardTransformOperator("$source/$last", null).img

       		        outputImage(result, "$target/$last")
    		    }
   		        last = jpg
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