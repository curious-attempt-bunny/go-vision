package org.merlyn.gocap

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.media.jai.*;

class BoardTransformOperator extends ImageOperator {
    def img

    BoardTransformOperator(jpg1, jp2) {
        println jpg1
        def s1 = JAI.create("url", new File(jpg1).toURL() )

//        def sourceCorners = [[223, 85], [514, 81], [215, 340], [540, 337]]
//        def sourceCorners = [[180, 63], [476,58], [171, 324], [498, 317]]
        def sourceCorners = [[137, 44], [556,28], [92, 427], [571,447]]

        def squareSizes = 32
        def targetCorners = [[squareSizes*0.5, squareSizes*0.5], [squareSizes*8.5, squareSizes*0.5], [squareSizes*0.5, squareSizes*8.5], [squareSizes*8.5, squareSizes*8.5]]

        double x0 = sourceCorners[0][0]
        double y0 = sourceCorners[0][1]
        double x1 = sourceCorners[1][0]
        double y1 = sourceCorners[1][1]
        double x2 = sourceCorners[2][0]
        double y2 = sourceCorners[2][1]
        double x3 = sourceCorners[3][0]
        double y3 = sourceCorners[3][1]

        double x0p = targetCorners[0][0]
        double y0p = targetCorners[0][1]
        double x1p = targetCorners[1][0]
        double y1p = targetCorners[1][1]
        double x2p = targetCorners[2][0]
        double y2p = targetCorners[2][1]
        double x3p = targetCorners[3][0]
        double y3p = targetCorners[3][1]

//        def transform = PerspectiveTransform.getQuadToQuad(x0, y0, x1, y1, x2, y2, x3, y3, x0p, y0p, x1p, y1p, x2p, y2p, x3p, y3p)
        def transform = PerspectiveTransform.getQuadToQuad(x0p, y0p, x1p, y1p, x2p, y2p, x3p, y3p, x0, y0, x1, y1, x2, y2, x3, y3)
//        def transform = PerspectiveTransform.getQuadToSquare(x0, y0, x1, y1, x2, y2, x3, y3)

//        println "?"
//        println transform.transform(new Point((int)x0, (int)y0), null)
//        println "!"

        Warp warp = new WarpPerspective(transform);

         // Create the interpolation parameter.
         Interpolation interp = new InterpolationNearest(); //new InterpolationBilinear() //

         // Create the ParameterBlock.
         ParameterBlock pb = new ParameterBlock();
         pb.addSource(s1);
         pb.add(warp);
         pb.add(interp);

         // Create the warp operation.
         def transformed = JAI.create("warp", pb);

//         img = transformed
         ParameterBlock params = new ParameterBlock();
         params.addSource(transformed);

         params.add(0f);//x origin
         params.add(0f);//y origin
         params.add((float)(squareSizes*9f));//width
         params.add((float)(squareSizes*9f));//height

         img = JAI.create("crop", params);

//       img = s1

    }

}