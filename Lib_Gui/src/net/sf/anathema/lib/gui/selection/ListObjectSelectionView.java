package net.sf.anathema.lib.gui.selection;

import javax.swing.JComponent;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import net.sf.anathema.lib.control.objectvalue.IObjectValueChangedListener;
import net.sf.anathema.lib.gui.list.ListSelectionMode;
import net.sf.anathema.lib.gui.list.SmartJList;
import net.sf.anathema.lib.gui.list.veto.IVetor;
import net.sf.anathema.lib.gui.list.veto.VetoableListSelectionModel;

public class ListObjectSelectionView<V> implements IListObjectSelectionView<V> {

  private final SmartJList<V> smartList;
  private final Class< ? extends V> contentClazz;
  private final VetoableListSelectionModel selectionModel;

  public ListObjectSelectionView(Class< ? extends V> contentClazz) {
    this.contentClazz = contentClazz;
    this.smartList = new SmartJList<V>(contentClazz);
    this.selectionModel = new VetoableListSelectionModel();
    this.smartList.setSelectionModel(selectionModel);
  }

  public void setCellRenderer(ListCellRenderer renderer) {
    smartList.setCellRenderer(renderer);

  }

  public void addObjectSelectionChangedListener(final IObjectValueChangedListener<V> listener) {
    smartList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

      public void valueChanged(ListSelectionEvent e) {
        listener.valueChanged(smartList.getSelectedValue());
      }
    });
  }

  public void setObjects(V[] objects) {
    smartList.setObjects(objects);
  }

  public void setSelectedObject(V object) {
    // URS: Ich habe die Array-Konversion an dieser Stelle entfernt, weil ich einen Fehler vermutete.
    smartList.setSelectedObjects(object);
  }

  public JComponent getContent() {
    return smartList;
  }

  public V getSelectedObject() {
    return smartList.getSelectedValue();
  }

  public boolean isObjectSelected() {
    return getSelectedObject() != null;
  }

  public void setSelectionType(ListSelectionMode mode) {
    smartList.setSelectionMode(mode);
  }

  public void addSelectionVetor(IVetor vetor) {
    selectionModel.addVetor(vetor);
  }

  public void removeSelectionVetor(IVetor vetor) {
    selectionModel.removeVetor(vetor);
  }
}