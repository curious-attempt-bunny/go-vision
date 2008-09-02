package org.merlyn.gocap

import java.awt.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class ImageOperator {
    def subtract(s1, s2) {
        def pb = new ParameterBlock();

         // Create the ParameterBlock for the operation
         pb = new ParameterBlock();
         pb.addSource(s1);
         pb.addSource(s2);

         // Create the Add operation.
         (RenderedImage)JAI.create("subtract", pb);
    }

       def and(s1, s2) {
            def pb = new ParameterBlock();

             // Create the ParameterBlock for the operation
             pb = new ParameterBlock();
             pb.addSource(s1);
             pb.addSource(s2);

             // Create the Add operation.
             (RenderedImage)JAI.create("and", pb);
        }

       def or(s1, s2) {
           def pb = new ParameterBlock();

            // Create the ParameterBlock for the operation
            pb = new ParameterBlock();
            pb.addSource(s1);
            pb.addSource(s2);

            // Create the Add operation.
            (RenderedImage)JAI.create("or", pb);
       }

       def xor(s1, s2) {
           def pb = new ParameterBlock();

            // Create the ParameterBlock for the operation
            pb = new ParameterBlock();
            pb.addSource(s1);
            pb.addSource(s2);

            // Create the Add operation.
            (RenderedImage)JAI.create("xor", pb);
       }
    def grayscale(PlanarImage i) {
          double[][] matrix = [ [0.114D, 0.587D, 0.299D, 0.0D] ];

          if (i.getSampleModel().getNumBands() != 3) {
            throw new IllegalArgumentException("Image # bands <> 3");
          }

          ParameterBlock pb = new ParameterBlock();
          pb.addSource(i);
          pb.add(matrix);
          return ( (PlanarImage) JAI.create("bandcombine", pb, null));
        }

    def binarize(s1) {
        Histogram histogram =
            (Histogram)JAI.create("histogram", s1).getProperty("histogram");
        double[] threshold = histogram.getPTileThreshold(0.5);

        threshold[0] = 32
        // Binarize the image.
        PlanarImage dst =
            JAI.create("binarize", s1, new Double(threshold[0]));
    }

    def edge(s1) {
         // Create the two kernels.
         float[] data_h= [ 1.0F,   0.0F,   -1.0F,
                                        1.414F, 0.0F,   -1.414F,
                                        1.0F,   0.0F,   -1.0F];
         float[] data_v = [-1.0F,  -1.414F, -1.0F,
                                        0.0F,   0.0F,    0.0F,
                                        1.0F,   1.414F,  1.0F];

         KernelJAI kern_h = new KernelJAI(3,3,data_h);
         KernelJAI kern_v = new KernelJAI(3,3,data_v);

         // Create the Gradient operation.
         (PlanarImage)JAI.create("gradientmagnitude", s1,
                                          kern_h, kern_v);
    }

    def dilate(s1) {
        def kernel = new KernelJAI( new Kernel(5,5,
        (float[])[  1.0F, 1.00F,  1.0F, 1.00F,  1.0F,
                    1.0F, 1.00F,  1.0F, 1.00F,  1.0F,
                    1.0F, 1.00F,  1.0F, 1.00F,  1.0F,
                    1.0F, 1.00F,  1.0F, 1.00F,  1.0F,
                    1.0F, 1.00F,  1.0F, 1.00F,  1.0F]))

        JAI.create("dilate", s1, kernel);
    }

    def erode(s1) {
        def kernel = new KernelJAI( new Kernel(5,5,
        (float[])[  1.0F, 1.00F,  1.0F, 1.00F,  1.0F,
                    1.0F, 1.00F,  1.0F, 1.00F,  1.0F,
                    1.0F, 1.00F,  1.0F, 1.00F,  1.0F,
                    1.0F, 1.00F,  1.0F, 1.00F,  1.0F,
                    1.0F, 1.00F,  1.0F, 1.00F,  1.0F]))

        JAI.create("erode", s1, kernel);
   }

    def convertToAwt(s1) {
        def awt = new BufferedImage(s1.data.width, s1.data.height, BufferedImage.TYPE_INT_RGB)
        def gfx = awt.createGraphics()
        gfx.drawImage(s1.getAsBufferedImage(), null, null)
        awt
    }
}