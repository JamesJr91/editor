/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of
 * the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.editor.editors.board.panels;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.rpgtoolkit.editor.editors.board.BoardLayerView;
import net.rpgtoolkit.common.assets.BoardVector;
import net.rpgtoolkit.editor.ui.AbstractModelPanel;

/**
 *
 *
 * @author Joshua Michael Daly
 */
public class BoardVectorPanel extends AbstractModelPanel {

  private final JSpinner layerSpinner;
  private final JCheckBox isClosedCheckBox;
  private final JTextField handleTextField;
  private final JComboBox<String> tileTypeComboBox;

  private static final String[] TILE_TYPES = {
    "SOLID", "UNDER", "STAIRS", "WAYPOINT"
  };

  private int lastSpinnerLayer; // Used to ensure that the selection is valid.

  public BoardVectorPanel(BoardVector boardVector) {
    ///
    /// super
    ///
    super(boardVector);
    ///
    /// layerSpinner
    ///
    layerSpinner = new JSpinner();
    layerSpinner.setValue(((BoardVector) model).getLayer());
    layerSpinner.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        BoardLayerView lastLayerView = getBoardEditor().getBoardView().
                getLayer(((BoardVector) model).getLayer());

        BoardLayerView newLayerView = getBoardEditor().getBoardView().
                getLayer((int) layerSpinner.getValue());

        // Make sure this is a valid move.
        if (lastLayerView != null && newLayerView != null) {
          // Do the swap.
          ((BoardVector) model).setLayer((int) layerSpinner.getValue());
          newLayerView.getLayer().getVectors().add((BoardVector) model);
          lastLayerView.getLayer().getVectors().remove((BoardVector) model);
          updateCurrentBoardView();

          // Store new layer selection index.
          lastSpinnerLayer = (int) layerSpinner.getValue();
        } else {
          // Not a valid layer revert selection.
          layerSpinner.setValue(lastSpinnerLayer);
        }
      }
    });

    // Store currently selected layer.
    lastSpinnerLayer = (int) layerSpinner.getValue();
    ///
    /// isClosedCheckBox
    ///
    isClosedCheckBox = new JCheckBox();
    isClosedCheckBox.setSelected(((BoardVector) model).isClosed());
    isClosedCheckBox.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        ((BoardVector) model).setClosed(isClosedCheckBox.isSelected());
        updateCurrentBoardView();
      }
    });
    ///
    /// handleTextField
    ///
    handleTextField = new JTextField();
    handleTextField.setText(((BoardVector) model).getHandle());
    handleTextField.addFocusListener(new FocusListener() {

      @Override
      public void focusGained(FocusEvent e) {

      }

      @Override
      public void focusLost(FocusEvent e) {
        if (!((BoardVector) model).getHandle().
                equals(handleTextField.getText())) {
          ((BoardVector) model).setHandle(handleTextField.getText());
        }
      }
    });
    ///
    /// tileTypeComboBox
    ///
    tileTypeComboBox = new JComboBox<>(TILE_TYPES);

    switch (((BoardVector) model).getTileType()) {
      case 1:
        tileTypeComboBox.setSelectedIndex(0);
        break;
      case 2:
        tileTypeComboBox.setSelectedIndex(1);
        break;
      case 8:
        tileTypeComboBox.setSelectedIndex(2);
        break;
      case 16:
        tileTypeComboBox.setSelectedIndex(3);
    }

    tileTypeComboBox.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        switch (tileTypeComboBox.getSelectedIndex()) {
          case 0:
            ((BoardVector) model).setTileType(1);
            break;
          case 1:
            ((BoardVector) model).setTileType(2);
            break;
          case 2:
            ((BoardVector) model).setTileType(8);
            break;
          case 3:
            ((BoardVector) model).setTileType(16);
        }

        updateCurrentBoardView();
      }
    });
    ///
    /// constraints
    ///
    constraints.insets = new Insets(4, 15, 0, 30);
    constraintsRight.insets = new Insets(0, 0, 10, 15);

    constraints.gridx = 0;
    constraints.gridy = 1;
    add(new JLabel("Handle"), constraints);

    constraints.gridx = 0;
    constraints.gridy = 2;
    add(new JLabel("Is Closed"), constraints);

    constraints.gridx = 0;
    constraints.gridy = 3;
    add(new JLabel("Layer"), constraints);

    constraints.gridx = 0;
    constraints.gridy = 4;
    add(new JLabel("Type"), constraints);
    ///
    /// constraintsRight
    ///
    constraintsRight.gridx = 1;
    constraintsRight.gridy = 1;
    add(handleTextField, constraintsRight);

    constraintsRight.gridx = 1;
    constraintsRight.gridy = 2;
    add(isClosedCheckBox, constraintsRight);

    constraintsRight.gridx = 1;
    constraintsRight.gridy = 3;
    add(layerSpinner, constraintsRight);

    constraintsRight.gridx = 1;
    constraintsRight.gridy = 4;
    add(tileTypeComboBox, constraintsRight);
  }
}