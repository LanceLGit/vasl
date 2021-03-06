/*
 * $Id$
 *
 * Copyright (c) 2000-2003 by Rodney Kinney
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License (LGPL) as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, copies are available
 * at http://www.opensource.org.
 */
package VASL.build.module.map;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.BitSet;
import VASL.counters.ASLProperties;
import VASSAL.build.module.map.StackMetrics;
import VASSAL.counters.GamePiece;
import VASSAL.counters.Properties;
import VASSAL.counters.Stack;

public class ASLStackMetrics extends StackMetrics {
  protected void drawUnexpanded(GamePiece p, Graphics g,
                                int x, int y, Component obs, double zoom) {
    if (p.getProperty(ASLProperties.LOCATION) != null) {
      p.draw(g, x, y, obs, zoom);
    }
    else {
      super.drawUnexpanded(p, g, x, y, obs, zoom);
    }
  }

  public int getContents(Stack parent, Point[] positions, Shape[] shapes, Rectangle[] boundingBoxes, int x, int y) {
    int val = super.getContents(parent, positions, shapes, boundingBoxes, x, y);
    if (!parent.isExpanded()) {
      int count = parent.getPieceCount();
      BitSet visibleLocations = new BitSet(count);
      BitSet visibleOther = new BitSet(count);
      for (int i = 0; i < count; ++i) {
        GamePiece p = parent.getPieceAt(i);
        boolean visibleToMe = !Boolean.TRUE.equals(p.getProperty(Properties.INVISIBLE_TO_ME));
        boolean isLocation = p.getProperty((ASLProperties.LOCATION)) != null;
        visibleLocations.set(i, isLocation && visibleToMe);
        visibleOther.set(i,!isLocation && visibleToMe);
      }
      if (visibleLocations.cardinality() > 0 && visibleOther.cardinality() > 0) {
        for (int i=0;i<count;++i) {
          if (visibleLocations.get(i)) {
            if (positions != null) {
              positions[i].translate(-15,0);
            }
            if (boundingBoxes != null) {
              boundingBoxes[i].translate(-15,0);
            }
            if (shapes != null) {
              shapes[i] = AffineTransform.getTranslateInstance(-15,0).createTransformedShape(shapes[i]);
            }
          }
        }
      }
    }
    return val;
  }
}
