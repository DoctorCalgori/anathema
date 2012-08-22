package net.sf.anathema.character.generic.framework.additionaltemplate.model;

import net.sf.anathema.character.generic.additionaltemplate.IAdditionalModel;
import net.sf.anathema.character.generic.data.IExtensibleDataSetProvider;
import net.sf.anathema.character.generic.template.additional.IAdditionalTemplate;

public interface IAdditionalModelFactory {

  IAdditionalModel createModel(IAdditionalTemplate additionalTemplate, ICharacterModelContext context, IExtensibleDataSetProvider data);
}