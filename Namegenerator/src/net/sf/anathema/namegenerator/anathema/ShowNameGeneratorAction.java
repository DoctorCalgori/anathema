package net.sf.anathema.namegenerator.anathema;

import net.sf.anathema.framework.IAnathemaModel;
import net.sf.anathema.framework.item.IItemType;
import net.sf.anathema.framework.message.MessageUtilities;
import net.sf.anathema.framework.repository.AnathemaNullDataItem;
import net.sf.anathema.framework.repository.IItem;
import net.sf.anathema.lib.exception.AnathemaException;
import net.sf.anathema.lib.gui.action.SmartAction;
import net.sf.anathema.lib.message.Message;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.Identificate;

import javax.swing.Action;
import java.awt.Component;
import java.awt.Cursor;

public class ShowNameGeneratorAction extends SmartAction {

  private final IAnathemaModel anathemaModel;
  private final IResources resources;

  public static Action createMenuAction(IResources resources, IAnathemaModel anathemaModel) {
    SmartAction action = new ShowNameGeneratorAction(resources, anathemaModel);
    action.setName(resources.getString("NameGenerator.ShowGenerator.Name")); //$NON-NLS-1$
    return action;
  }

  private ShowNameGeneratorAction(IResources resources, IAnathemaModel anathemaModel) {
    this.resources = resources;
    this.anathemaModel = anathemaModel;
  }

  @Override
  protected void execute(Component parentComponent) {
    parentComponent.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    try {
      String id = NameGeneratorItemTypeConfiguration.NAME_GENERATOR_ITEM_TYPE_ID;
      IItemType itemType = anathemaModel.getItemTypeRegistry().getById(id);
      IItem generatorItem = new AnathemaNullDataItem(itemType, new Identificate(id));
      generatorItem.setPrintName(resources.getString("ItemType.NameGenerator.PrintName")); //$NON-NLS-1$
      anathemaModel.getItemManagement().addItem(generatorItem);
    } catch (AnathemaException e) {
      Message message = new Message("An error occured while starting the name generator: " + e.getMessage(), e);
      MessageUtilities.indicateMessage(getClass(), parentComponent, message);
    } catch (Throwable e) {
      Message message = new Message("An error occured while starting the name generator.", e);
      MessageUtilities.indicateMessage(getClass(), parentComponent, message);
    } finally {
      parentComponent.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }
}