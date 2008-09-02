package org.merlyn.gocap

import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class BoardDetectionOperator extends ImageOperator {
    def img

    BoardDetectionOperator(jpg1, jpg2) {
        def s = JAI.create("url", new File(jpg1).toURL() )
        def g = grayscale(s)
        def b = blur(g)
        def d = dilate(b)
        def e = erode(d)
        img = subtract(e, g)
        img = binarizeNorm(img)

//        img = detectCorners(img)
        println img.data.class

        println img.data.width
        println img.data.height
        println img.data.numBands

        def matrix = new int[img.data.height][img.data.width]
        (0..img.data.height-1).each { y ->
            img.data.getPixels(0,y,img.data.width, 1, matrix[y])
        }

        def histogramX = new int[img.data.width]
        def histogramY = new int[img.data.height]

        (0..img.data.width-1).each { x ->
            (0..img.data.height-1).each { y ->
                if (matrix[y][x] > 0) {
                    histogramX[x]++
                    histogramY[y]++
                }
            }
        }

        def threshold = 50
        normalizeHistogram(histogramX, threshold)
        normalizeHistogram(histogramY, threshold)

        def presentation = convertToAwt(img)
        def gfx = presentation.createGraphics()
        gfx.setColor(Color.RED)

        (0..img.data.width-1).each { x ->
            if (histogramX[x]) gfx.drawLine(x,0,x,img.data.height)
        }

        (0..img.data.height-1).each { y ->
            if (histogramY[y]) gfx.drawLine(0,y,img.data.width,y)
        }

        img = presentation
    }

    def normalizeHistogram(h, threshold) {
        int firstIndex
        boolean isTriggered
        (0..h.length-1).each { i ->
            boolean isActive = (h[i] > threshold)

            h[i] = 0

            if (isTriggered) {
                if (!isActive) {
                    int mean = (firstIndex+(i-1))/2
                    h[mean] = 1
                    isTriggered = false
                }
            } else {
                if (isActive) {
                    firstIndex = i
                    isTriggered = true
                }
            }
        }
    }

    def blur(s1) {
        def kernel = new KernelJAI( 3, 3,
                    [  1/10F, 1/10F,  1/10F,
                       1/10F, 1/10F,  1/10F,
                       1/10F, 1/10F,  1/10F ] as float[] )

        // Create the convolve operation.
        JAI.create("convolve", s1, kernel);
    }

//    def detectCorners(s1) {
//        def k ="""-----+-----
//-----+-----
//-----+-----
//-----+-----
//-----+-----
//+++++++++++
//-----+-----
//-----+-----
//-----+-----
//-----+-----
//-----+-----"""
//
//        def matrix = new int[11*11]
//        println k.replace('\n', '')
//
//        def kernel = new KernelJAI( 11, 11,
//            [  1/10F, 1/10F,  1/10F,
//               1/10F, 1/10F,  1/10F,
//               1/10F, 1/10F,  1/10F ] as float[] )
//
//         Create the convolve operation.
//        JAI.create("convolve", s1, kernel);
//    }

    def binarizeNorm(s1) {
        Histogram histogram =
            (Histogram)JAI.create("histogram", s1).getProperty("histogram");
        double[] threshold = histogram.getPTileThreshold(0.8);

        def t = 5 //threshold[0]
        // Binarize the image.
        PlanarImage dst =
            JAI.create("binarize", s1, new Double(t));
    }
}