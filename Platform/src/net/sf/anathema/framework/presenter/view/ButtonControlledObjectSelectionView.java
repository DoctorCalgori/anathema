package net.sf.anathema.framework.presenter.view;

import net.disy.commons.swing.layout.grid.GridDialogLayoutData;
import net.sf.anathema.lib.control.ObjectValueListener;
import net.sf.anathema.lib.gui.dialog.events.AbstractDocumentChangeListener;
import net.sf.anathema.lib.gui.widgets.ColoredJComboBox;
import net.sf.anathema.lib.lang.StringUtilities;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ButtonControlledObjectSelectionView<V> implements IButtonControlledObjectSelectionView<V> {

  protected final JComboBox comboBox;
  protected final JButton addButton;
  protected final JLabel label;

  public ButtonControlledObjectSelectionView(ListCellRenderer renderer, Icon addIcon, String labelText) {
    this(renderer, addIcon, labelText, null);
  }

  public ButtonControlledObjectSelectionView(
      ListCellRenderer renderer,
      Icon addIcon,
      String labelText,
      ITextFieldComboBoxEditor editor) {
    this.comboBox = new ColoredJComboBox(new DefaultComboBoxModel(new Object[0]));
    this.label = new JLabel(labelText);
    this.comboBox.setRenderer(renderer);
    addButton = new JButton(addIcon);
    addButton.setPreferredSize(new Dimension(addIcon.getIconWidth() + 4, addIcon.getIconHeight() + 4));
    if (editor != null) {
      this.comboBox.setEditable(true);
      this.comboBox.setEditor(editor);
      final JTextField editField = editor.getEditorComponent();
      editField.getDocument().addDocumentListener(new AbstractDocumentChangeListener() {
        @Override
        protected void documentChanged() {
          addButton.setEnabled(!StringUtilities.isNullOrEmpty(editField.getText()));
        }
      });
      addButton.setEnabled(!StringUtilities.isNullOrEmpty(editField.getText()));
    }
  }

  @Override
  public void addButtonListener(final ObjectValueListener<V> listener) {
    addButton.addActionListener(new ActionListener() {
      @Override
      @SuppressWarnings("unchecked")
      public void actionPerformed(ActionEvent e) {
        listener.valueChanged((V) comboBox.getSelectedItem());
      }
    });
  }

  /** GridDialogLayout, 3 columns */
  public void addComponents(JPanel panel) {
    panel.add(label);
    panel.add(comboBox, GridDialogLayoutData.FILL_HORIZONTAL);
    panel.add(addButton, GridDialogLayoutData.RIGHT);
  }

  @Override
  public void addObjectSelectionChangedListener(final ObjectValueListener<V> listener) {
    comboBox.addItemListener(new ItemListener() {
      @Override
      @SuppressWarnings("unchecked")
      public void itemStateChanged(ItemEvent e) {
        listener.valueChanged((V) comboBox.getSelectedItem());
      }
    });
  }

  @Override
  @SuppressWarnings("unchecked")
  public V getSelectedObject() {
    return (V) comboBox.getSelectedItem();
  }

  @Override
  public boolean isObjectSelected() {
    return getSelectedObject() != null;
  }

  @Override
  public void setButtonEnabled(boolean enabled) {
    addButton.setEnabled(enabled);
  }

  @Override
  public void setEnabled(boolean enabled) {
    comboBox.setEnabled(enabled);
    addButton.setEnabled(enabled);
    label.setEnabled(enabled);
  }

  @Override
  public void setObjects(V[] objects) {
    Object selectedItem = comboBox.getSelectedItem();
    DefaultComboBoxModel model = (DefaultComboBoxModel) comboBox.getModel();
    model.removeAllElements();
    for (Object object : objects) {
      model.addElement(object);
    }
    comboBox.setSelectedItem(selectedItem);
  }

  @Override
  public void setSelectedObject(Object object) {
    comboBox.setSelectedItem(object);
  }
}