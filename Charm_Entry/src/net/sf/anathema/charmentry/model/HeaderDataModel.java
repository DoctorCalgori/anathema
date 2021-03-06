package net.sf.anathema.charmentry.model;

import net.sf.anathema.character.generic.type.CharacterType;
import net.sf.anathema.character.generic.type.ICharacterType;
import net.sf.anathema.charmentry.model.data.IConfigurableCharmData;
import net.sf.anathema.charmentry.presenter.model.ISourceEntryModel;
import net.sf.anathema.lib.control.IChangeListener;
import net.sf.anathema.lib.control.ObjectValueListener;
import net.sf.anathema.lib.gui.wizard.workflow.CheckInputListener;
import net.sf.anathema.lib.workflow.textualdescription.ITextualDescription;
import org.jmock.example.announcer.Announcer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HeaderDataModel implements IHeaderDataModel {
  private final ISourceEntryModel sourceModel;
  private final Announcer<IChangeListener> control = Announcer.to(IChangeListener.class);
  private final IConfigurableCharmData charmData;

  public HeaderDataModel(final IConfigurableCharmData charmData) {
    this.charmData = charmData;
    this.sourceModel = new SourceEntryModel(charmData);
    charmData.getName().addTextChangedListener(new ObjectValueListener<String>() {
      @Override
      public void valueChanged(String newValue) {
        ICharacterType type = charmData.getCharacterType();
        if (type != null) {
          charmData.setId(CharmIdGenerator.createIDFromName(type, newValue));
        }
      }
    });
  }

  @Override
  public ICharacterType[] getCharacterTypes() {
    List<ICharacterType> legalTypes = new ArrayList<ICharacterType>();
    Collections.addAll(legalTypes, CharacterType.getAllExaltTypes());
    legalTypes.remove(CharacterType.SIDEREAL);
    return legalTypes.toArray(new ICharacterType[legalTypes.size()]);
  }

  @Override
  public ISourceEntryModel getSourceModel() {
    return sourceModel;
  }

  @Override
  public ICharacterType getCharacterType() {
    return charmData.getCharacterType();
  }

  @Override
  public ITextualDescription getName() {
    return charmData.getName();
  }

  @Override
  public void setCharacterType(ICharacterType type) {
    charmData.setCharacterType(type);
    String text = charmData.getName().getText();
    if (text != null) {
      charmData.setId(CharmIdGenerator.createIDFromName(type, text));
    }
    control.announce().changeOccurred();
  }

  @Override
  public void addModelListener(CheckInputListener inputListener) {
    control.addListener(inputListener);
    charmData.getName().addTextChangedListener(inputListener);
  }

  @Override
  public void addChangeListener(IChangeListener inputListener) {
    control.addListener(inputListener);
  }
}