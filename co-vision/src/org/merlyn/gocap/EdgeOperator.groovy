package org.merlyn.gocap

import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class EdgeOperator extends ImageOperator {
    def img

    EdgeOperator(jpg1, jpg2) {
        def s1 = JAI.create("url", new File(jpg1).toURL() )
        def g1 = grayscale(s1)
        def e1 = edge(g1)
        def c1 = erode(dilate(e1))

//        img = c1
        img = binarizeNorm(edge(g1))

    }


    def binarizeNorm(s1) {
        Histogram histogram =
            (Histogram)JAI.create("histogram", s1).getProperty("histogram");
        double[] threshold = histogram.getPTileThreshold(0.8);

        // Binarize the image.
        PlanarImage dst =
            JAI.create("binarize", s1, new Double(threshold[0]));
    }
}