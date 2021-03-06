package net.sf.anathema.framework.presenter.itemmanagement;

import net.sf.anathema.framework.repository.IItem;
import net.sf.anathema.lib.control.ObjectValueListener;

public class PrintNameAdjuster implements ObjectValueListener<String> {

  private final IItem item;

  public PrintNameAdjuster(IItem item) {
    this.item = item;
  }

  @Override
  public void valueChanged(String newValue) {
    item.setPrintName(newValue);
  }
}