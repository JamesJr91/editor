/**
 * Copyright (c) 2015, rpgtoolkit.net <help@rpgtoolkit.net>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package net.rpgtoolkit.editor.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.rpgtoolkit.editor.editors.AbstractBoardView;
import net.rpgtoolkit.editor.editors.board.BoardLayerView;
import net.rpgtoolkit.editor.editors.board.BoardLayersTableModel;
import net.rpgtoolkit.editor.ui.resources.Icons;

/**
 *
 *
 * @author Joshua Michael Daly
 */
public class LayerPanel extends JPanel implements ChangeListener,
        ListSelectionListener
{

    private AbstractBoardView boardView;

    private JSlider opacitySlider;
    private JLabel opacitySliderLabel;
    private JTable layerTable;
    private JScrollPane layerScrollPane;

    private JButton newLayerButton;
    private JButton moveLayerUpButton;
    private JButton moveLayerDownButton;
    private JButton cloneLayerButton;
    private JButton deleteLayerButton;

    private JPanel sliderPanel;
    private JPanel buttonPanel;

    /*
     * *************************************************************************
     * Public Constructors
     * *************************************************************************
     */
    public LayerPanel()
    {
        this.initialize();
    }

    public LayerPanel(AbstractBoardView boardView)
    {
        this.boardView = boardView;
        this.initialize();
    }

    /*
     * *************************************************************************
     * Public Getters and Setters
     * *************************************************************************
     */
    public AbstractBoardView getBoardView()
    {
        return this.boardView;
    }

    public void setBoardView(AbstractBoardView boardView)
    {
        this.boardView = boardView;
        this.layerTable.setModel(new BoardLayersTableModel(this.boardView));

        if (this.boardView.getBoard().getLayers().size() > 0)
        {
            this.layerTable.changeSelection(0, 0, false, false);
        }
    }
    
    /*
     * *************************************************************************
     * Public Methods
     * *************************************************************************
     */
    /**
     * TODO: Possibly consider moving this to a dedicated listener class later.
     * For now leave it here for simplicity.
     *
     * Used to keep track of changes in on the opacity <code>JSlider</code>. If
     * there is an open board and a layer is selected then the layers opacity
     * will be updated.
     *
     * @param e
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        if (e.getSource().equals(this.opacitySlider))
        {
            if (this.boardView != null)
            {
                if (this.layerTable.getSelectedRow() > -1
                        && this.layerTable.getRowCount() > 0)
                {
                    this.boardView.getLayer((boardView.getBoard().getLayers().size()
                            - layerTable.getSelectedRow()) - 1).
                            setOpacity(this.opacitySlider.getValue() / 100.0f);
                }
            }
        }
    }

    /**
     * TODO: It is possible that in the future other parts of the editor will be
     * interested in layer selection changes.
     *
     * Handles selection changes on the Layer Table, updating the opacity slider
     * with the selected layers current opacity.
     *
     * @param e
     */
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        // If we have changed the selected layer update the position of the 
        // opacity slider to the new layers opacity.
        if (this.boardView != null)
        {
            if (this.layerTable.getSelectedRow() > -1)
            {
                BoardLayerView selectedLayer = this.boardView.getLayer(
                        (boardView.getBoard().getLayers().size()
                        - layerTable.getSelectedRow()) - 1);

                this.boardView.setCurrentSeletedLayer(selectedLayer);

                this.opacitySlider.setValue((int) selectedLayer.getOpacity() * 100);
            }
        }
    }

    public void clearTable()
    {
        this.layerTable.setModel(new BoardLayersTableModel());
    }

    /*
     * *************************************************************************
     * Private Methods
     * *************************************************************************
     */
    private void initialize()
    {
        this.opacitySlider = new JSlider(0, 100, 100);
        this.opacitySlider.addChangeListener(this);

        this.opacitySliderLabel = new JLabel("Opacity");
        this.opacitySliderLabel.setLabelFor(this.opacitySlider);

        this.sliderPanel = new JPanel();
        this.sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));
        this.sliderPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
        this.sliderPanel.add(this.opacitySliderLabel);
        this.sliderPanel.add(this.opacitySlider);
        this.sliderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                this.sliderPanel.getPreferredSize().height));

        if (this.boardView != null)
        {
            this.layerTable = new JTable(new BoardLayersTableModel(this.boardView));

            if (this.boardView.getBoard().getLayers().size() > 0)
            {
                this.layerTable.changeSelection(0, 0, false, false);
            }
        }
        else
        {
            this.layerTable = new JTable(new BoardLayersTableModel());
        }

        this.layerTable.getColumnModel().getColumn(0).setPreferredWidth(32);
        this.layerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.layerTable.getSelectionModel().addListSelectionListener(this);

        this.layerScrollPane = new JScrollPane(this.layerTable);

        this.newLayerButton = new JButton();
        this.newLayerButton.setIcon(Icons.getSmallIcon("new"));
        this.newLayerButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (boardView != null)
                {
                    boardView.getBoard().addLayer();
                }
            }
        });

        this.moveLayerUpButton = new JButton();
        this.moveLayerUpButton.setIcon(Icons.getSmallIcon("arrow-090"));
        this.moveLayerUpButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (boardView != null)
                {
                    if (boardView.getBoard().getLayers().size() > 0)
                    {
                        boardView.getBoard().moveLayerUp((boardView.getBoard().getLayers().size()
                                - layerTable.getSelectedRow()) - 1);
                    }
                }
            }
        });

        this.moveLayerDownButton = new JButton();
        this.moveLayerDownButton.setIcon(Icons.getSmallIcon("arrow-270"));
        this.moveLayerDownButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (boardView != null)
                {
                    if (layerTable.getSelectedRow() > -1)
                    {
                        boardView.getBoard().moveLayerDown((boardView.getBoard().getLayers().size()
                                - layerTable.getSelectedRow()) - 1);
                    }
                }
            }
        });

        this.cloneLayerButton = new JButton();
        this.cloneLayerButton.setIcon(Icons.getSmallIcon("copy"));
        this.cloneLayerButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (boardView != null)
                {
                    if (layerTable.getSelectedRow() > -1)
                    {
                        boardView.getBoard().cloneLayer((boardView.getBoard().getLayers().size()
                                - layerTable.getSelectedRow()) - 1);
                    }
                }
            }
        });

        this.deleteLayerButton = new JButton();
        this.deleteLayerButton.setIcon(Icons.getSmallIcon("delete"));
        this.deleteLayerButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (boardView != null)
                {
                    if (layerTable.getSelectedRow() > -1)
                    {
                        boardView.getBoard().deleteLayer((boardView.getBoard().getLayers().size()
                                - layerTable.getSelectedRow()) - 1);
                    }
                }
            }
        });

        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints buttonsConstraints = new GridBagConstraints();
        buttonsConstraints.fill = GridBagConstraints.BOTH;
        buttonsConstraints.weightx = 1;
        this.buttonPanel.add(this.newLayerButton, buttonsConstraints);
        this.buttonPanel.add(this.moveLayerUpButton, buttonsConstraints);
        this.buttonPanel.add(this.moveLayerDownButton, buttonsConstraints);
        this.buttonPanel.add(this.cloneLayerButton, buttonsConstraints);
        this.buttonPanel.add(this.deleteLayerButton, buttonsConstraints);
        this.buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,
                this.buttonPanel.getPreferredSize().height));

        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(3, 0, 0, 0);
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 0;
        constraints.gridy++;
        this.add(this.sliderPanel, constraints);
        constraints.weighty = 1;
        constraints.gridy++;
        this.add(this.layerScrollPane, constraints);
        constraints.weighty = 0;
        constraints.insets = new Insets(0, 0, 0, 0);
        constraints.gridy++;
        this.add(this.buttonPanel, constraints);
    }

}
