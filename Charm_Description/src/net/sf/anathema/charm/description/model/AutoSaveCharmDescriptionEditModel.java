package net.sf.anathema.charm.description.model;

import net.disy.commons.core.util.ObjectUtilities;
import net.sf.anathema.charm.description.persistence.CharmDescriptionDataBase;
import net.sf.anathema.lib.control.change.ChangeControl;
import net.sf.anathema.lib.control.change.IChangeListener;

public class AutoSaveCharmDescriptionEditModel implements CharmDescriptionEditModel {

  private final ChangeControl changeControl = new ChangeControl();
  private final CharmDescriptionDataBase dataBase;
  private String editId = "";
  private String currentDescription;

  public AutoSaveCharmDescriptionEditModel(CharmDescriptionDataBase dataBase) {
    this.dataBase = dataBase;
  }

  @Override
  public boolean isActive() {
    return true;
  }

  @Override
  public void setEditId(String charmId) {
    this.editId = charmId;
    setCurrentDescriptionInternal(dataBase.loadDescription(charmId));
  }

  @Override
  public String getCurrentDescription() {
    return currentDescription;
  }
  
  public String getEditId() {
    return editId;
  }

  @Override
  public void updateCurrentDescription(String newDescription) {
    if (ObjectUtilities.equals(currentDescription, newDescription)) {
      return;
    }
    dataBase.saveDescription(editId, newDescription);
    setCurrentDescriptionInternal(newDescription);
  }

  private void setCurrentDescriptionInternal(String newDescription) {
    if (ObjectUtilities.equals(currentDescription, newDescription)) {
      return;
    }
    this.currentDescription = newDescription;
    changeControl.fireChangedEvent();
  }

  @Override
  public void addDescriptionChangedListener(IChangeListener listener) {
    changeControl.addChangeListener(listener);
  }

  @Override
  public void removeDescriptionChangeListener(IChangeListener listener) {
    changeControl.removeChangeListener(listener);
  }
}