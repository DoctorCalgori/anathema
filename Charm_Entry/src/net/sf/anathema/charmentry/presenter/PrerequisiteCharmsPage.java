package net.sf.anathema.charmentry.presenter;

import net.sf.anathema.character.generic.magic.ICharm;
import net.sf.anathema.charmentry.module.ICharmEntryViewFactory;
import net.sf.anathema.charmentry.presenter.model.ICharmEntryModel;
import net.sf.anathema.charmentry.presenter.model.ICharmPrerequisitesEntryModel;
import net.sf.anathema.charmentry.presenter.view.IPrerequisiteCharmsEntryView;
import net.sf.anathema.charmentry.properties.IPrerequisiteCharmsPageProperties;
import net.sf.anathema.charmentry.properties.PrerequisiteCharmsPageProperties;
import net.sf.anathema.framework.presenter.view.IdentificateListCellRenderer;
import net.sf.anathema.lib.control.IChangeListener;
import net.sf.anathema.lib.exception.PersistenceException;
import net.sf.anathema.lib.gui.wizard.AbstractAnathemaWizardPage;
import net.sf.anathema.lib.gui.wizard.workflow.CheckInputListener;
import net.sf.anathema.lib.message.IBasicMessage;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.workflow.container.ISelectionContainerView;

import java.util.Arrays;

public class PrerequisiteCharmsPage extends AbstractAnathemaWizardPage {

  private final IResources resources;
  private final ICharmEntryModel model;
  private final ICharmEntryViewFactory viewFactory;
  private final IPrerequisiteCharmsPageProperties properties;
  private IPrerequisiteCharmsEntryView view;

  public PrerequisiteCharmsPage(IResources resources, ICharmEntryModel model, ICharmEntryViewFactory viewFactory) {
    this.resources = resources;
    this.properties = new PrerequisiteCharmsPageProperties(resources);
    this.model = model;
    this.viewFactory = viewFactory;
  }

  @Override
  protected void addFollowUpPages(CheckInputListener inputListener) {
    // nothing to do
  }

  @Override
  protected void initModelListening(CheckInputListener inputListener) {
    getPageModel().addModelListener(inputListener);
  }

  protected ICharmPrerequisitesEntryModel getPageModel() {
    return model.getCharmPrerequisitesModel();
  }

  @Override
  protected void initPageContent() {
    this.view = viewFactory.createPrerequisiteCharmsView();
    final ISelectionContainerView<ICharm> charmView = view.addPrerequisiteCharmView(
            new IdentificateListCellRenderer(resources));
    charmView.addSelectionChangeListener(new IChangeListener() {
      @Override
      public void changeOccurred() {
        ICharm[] selectedValues = charmView.getSelectedValues();
        ICharm[] charms = Arrays.copyOf(selectedValues, selectedValues.length);
        getPageModel().setPrerequisiteCharms(charms);
      }
    });
    getPageModel().addModelListener(new IChangeListener() {
      @Override
      public void changeOccurred() {
        try {
          charmView.populate(getPageModel().getAvailableCharms());
        } catch (PersistenceException e) {
          // TODO : Handle
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  public boolean canFinish() {
    return true;
  }

  @Override
  public String getDescription() {
    return properties.getPageTitle();
  }

  @Override
  public IBasicMessage getMessage() {
    return properties.getDefaultMessage();
  }

  @Override
  public IPrerequisiteCharmsEntryView getPageContent() {
    return view;
  }

  protected IPrerequisiteCharmsPageProperties getProperties() {
    return properties;
  }

  protected IResources getResources() {
    return resources;
  }

  protected ICharmEntryModel getModel() {
    return model;
  }

  protected ICharmEntryViewFactory getViewFactory() {
    return viewFactory;
  }
}