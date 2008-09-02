package org.merlyn.gocap

import com.sun.media.jai.codec.*
import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class DifferenceImages {
	static void main(args) {
	    def last

	    new File('game11/diff').listFiles().each { it.delete() }

		new File('game11/edge').list().each { jpg ->
		    if (jpg ==~ /.*\.jpg/) {
    		    if (last) {

    		        def result = new ImageDifferenceOperator("game11/edge/$last", "game11/edge/$jpg").img

    		        Histogram histogram =
    		            (Histogram)JAI.create("histogram", result).getProperty("histogram");
    		        println "$last vs $jpg --> ${histogram.bins[0][0]}/${histogram.bins[0][1]}"

    		        if (histogram.bins[0][1] > 10 && histogram.bins[0][1] < 100000) {
        		        outputImage(result, "game11/diff/$last-$jpg")
//
//        		        outputImage(JAI.create("url", new File("game4/edge/$last").toURL() ), "game4/diff/$last")
        		        last = jpg
    		        }
    		    } else {
    		        last = jpg
    		    }
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