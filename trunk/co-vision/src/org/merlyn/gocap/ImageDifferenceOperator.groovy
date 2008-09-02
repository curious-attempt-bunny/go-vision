package org.merlyn.gocap

import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class ImageDifferenceOperator extends ImageOperator {
    def img

    ImageDifferenceOperator(jpg1, jpg2) {
        def s1 = JAI.create("url", new File(jpg1).toURL() )
        def s2 = JAI.create("url", new File(jpg2).toURL() )
//        def g1 = grayscale(s1)
//        def g2 = grayscale(s2)
//        def diff = binarize(or(subtract(g1,g2), subtract(g2,g1)))
//        img = erode(diff)
        img = xor(s1,s2)
    }

}