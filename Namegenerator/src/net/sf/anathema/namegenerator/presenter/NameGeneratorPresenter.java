package net.sf.anathema.namegenerator.presenter;

import com.google.common.base.Joiner;
import net.sf.anathema.lib.control.IChangeListener;
import net.sf.anathema.lib.gui.Presenter;
import net.sf.anathema.lib.gui.action.SmartAction;
import net.sf.anathema.lib.resources.IResources;
import net.sf.anathema.lib.util.Identified;
import net.sf.anathema.namegenerator.presenter.model.INameGeneratorModel;
import net.sf.anathema.namegenerator.presenter.view.INameGeneratorView;

import java.awt.Component;

public class NameGeneratorPresenter implements Presenter {

  private final INameGeneratorView view;
  private final INameGeneratorModel model;
  private final IResources resources;

  public NameGeneratorPresenter(IResources resources, INameGeneratorView view, INameGeneratorModel model) {
    this.view = view;
    this.model = model;
    this.resources = resources;
  }

  @Override
  public void initPresentation() {
    for (Identified generatorType : model.getGeneratorTypes()) {
      String formattedLabel = resources.getString("NameGeneratorPresenter." + generatorType); //$NON-NLS-1$
      view.addNameGeneratorType(formattedLabel, generatorType);
    }
    initSelectedGeneratorTypePresentation();
    initGenerationPresentation();
  }

  private void initGenerationPresentation() {
    view.addGenerationAction(
            new SmartAction(resources.getString("NameGeneratorPresenter.GenerateButtonLabel")) { //$NON-NLS-1$
              private static final long serialVersionUID = 4272323507368472400L;

              @Override
              protected void execute(Component parentComponent) {
                String[] generatedNames = model.generateNames(50);
                view.setResult(Joiner.on("\n").join(generatedNames)); //$NON-NLS-1$
              }
            });
  }

  private void initSelectedGeneratorTypePresentation() {
    view.addGeneratorTypeChangeListener(new IChangeListener() {
      @Override
      public void changeOccurred() {
        model.setGeneratorType((Identified) view.getSelectedGeneratorType());
      }
    });
    model.addGeneratorTypeChangeListener(new IChangeListener() {
      @Override
      public void changeOccurred() {
        view.setSelectedGeneratorType(model.getSelectedGeneratorType());
      }
    });
    view.setSelectedGeneratorType(model.getSelectedGeneratorType());
  }
}
