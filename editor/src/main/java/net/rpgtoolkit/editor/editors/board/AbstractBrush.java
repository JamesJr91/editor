/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.editor.editors.board;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import net.rpgtoolkit.editor.ui.AssetEditorWindow;

/**
 *
 *
 * @author Joshua Michael Daly
 */
public abstract class AbstractBrush implements Brush {

  /**
   *
   */
  protected int affectedLayers = 1;

  /**
   *
   */
  protected MultiLayerContainer affectedContainer;

  /**
   *
   */
  protected boolean isPainting = false;

  /**
   *
   */
  protected int currentLayer;

  /**
   *
   */
  public AbstractBrush() {

  }

  /**
   *
   * @param abstractBrush
   */
  public AbstractBrush(AbstractBrush abstractBrush) {
    affectedLayers = abstractBrush.affectedLayers;
  }

  @Override
  public int getAffectedLayers() {
    return affectedLayers;
  }

  /**
   *
   * @param layers
   */
  public void setAffectedLayers(int layers) {
    affectedLayers = layers;
  }

  /**
   *
   * @return
   */
  public int getInitialLayer() {
    return currentLayer;
  }

  /**
   *
   * @return
   */
  public abstract Shape getShape();

  @Override
  public void startPaint(MultiLayerContainer container, int layer) {
    affectedContainer = container;
    currentLayer = layer;
    isPainting = true;
  }

  @Override
  public Rectangle doPaint(int x, int y, Rectangle selection) throws Exception {
    if (!isPainting) {
      throw new Exception("Attempted to call doPaint() without calling"
              + "startPaint() beforehand.");
    }

    return null;
  }

  @Override
  public void endPaint() {
    isPainting = false;
  }

  @Override
  public void drawPreview(Graphics2D g2d, Dimension dimension,
          AbstractBoardView view) {
    // TODO: draw an off-board preview here.
  }
  
  public abstract void doMouseButton1Pressed(Point point, AssetEditorWindow editor);
  
  public abstract void doMouseButton2Pressed(Point point, AssetEditorWindow editor);
  
  public abstract void doMouseButton3Pressed(Point point, AssetEditorWindow editor);
  
  public abstract void doMouseButton1Dragged(Point point, Point origin, AssetEditorWindow editor);
  
  public abstract boolean isPixelBased();

}
