package net.sf.anathema.character.generic.framework.xml;

import java.util.ArrayList;
import java.util.List;

import net.sf.anathema.character.generic.additionalrules.IAdditionalRules;
import net.sf.anathema.character.generic.caste.ICasteCollection;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.generic.framework.xml.creation.GenericBonusPointCosts;
import net.sf.anathema.character.generic.framework.xml.creation.GenericCreationPoints;
import net.sf.anathema.character.generic.framework.xml.essence.GenericEssenceTemplate;
import net.sf.anathema.character.generic.framework.xml.experience.GenericExperiencePointCosts;
import net.sf.anathema.character.generic.framework.xml.health.GenericHealthTemplate;
import net.sf.anathema.character.generic.framework.xml.health.IHealthTemplate;
import net.sf.anathema.character.generic.framework.xml.magic.GenericMagicTemplate;
import net.sf.anathema.character.generic.framework.xml.presentation.GenericPresentationTemplate;
import net.sf.anathema.character.generic.framework.xml.rules.GenericAdditionalRules;
import net.sf.anathema.character.generic.framework.xml.trait.GenericTraitTemplateFactory;
import net.sf.anathema.character.generic.impl.caste.CasteCollection;
import net.sf.anathema.character.generic.impl.rules.ExaltedEdition;
import net.sf.anathema.character.generic.impl.traits.TraitTemplateCollection;
import net.sf.anathema.character.generic.rules.IExaltedEdition;
import net.sf.anathema.character.generic.template.ICharacterTemplate;
import net.sf.anathema.character.generic.template.ITraitTemplateCollection;
import net.sf.anathema.character.generic.template.TemplateType;
import net.sf.anathema.character.generic.template.abilities.IGroupedTraitType;
import net.sf.anathema.character.generic.template.additional.IAdditionalTemplate;
import net.sf.anathema.character.generic.template.creation.IBonusPointCosts;
import net.sf.anathema.character.generic.template.creation.ICreationPoints;
import net.sf.anathema.character.generic.template.essence.IEssenceTemplate;
import net.sf.anathema.character.generic.template.experience.IExperiencePointCosts;
import net.sf.anathema.character.generic.template.magic.IMagicTemplate;
import net.sf.anathema.character.generic.template.presentation.IPresentationProperties;
import net.sf.anathema.character.generic.traits.ITraitType;
import net.sf.anathema.lib.lang.clone.ICloneable;

public class GenericCharacterTemplate implements ICharacterTemplate, ICloneable {

  private IAdditionalRules additionalRules = new GenericAdditionalRules();
  private TemplateType templateType;
  private ITraitTemplateCollection traitTemplateCollection;
  private GenericMagicTemplate magicTemplate;
  private GenericExperiencePointCosts experienceCosts;
  private GenericBonusPointCosts bonusPointCosts;
  private GenericCreationPoints creationPoints;
  private GenericEssenceTemplate essenceTemplate;
  private IGroupedTraitType[] abilityGroups;
  private GenericPresentationTemplate presentationTemplate;
  private IGroupedTraitType[] attributeGroups;
  private ICasteCollection casteCollection = new CasteCollection(new ICasteType[0]);
  private final List<IAdditionalTemplate> additionalTemplates = new ArrayList<IAdditionalTemplate>();
  private IHealthTemplate healthTemplate = new GenericHealthTemplate();

  public IGroupedTraitType[] getAbilityGroups() {
    return abilityGroups;
  }

  public IAdditionalRules getAdditionalRules() {
    return additionalRules;
  }

  public IBonusPointCosts getBonusPointCosts() {
    return bonusPointCosts;
  }

  public ICasteCollection getCasteCollection() {
    return casteCollection;
  }

  public void setCasteCollection(ICasteCollection casteCollection) {
    this.casteCollection = casteCollection;
  }

  public ICreationPoints getCreationPoints() {
    return creationPoints;
  }

  public IEssenceTemplate getEssenceTemplate() {
    return essenceTemplate;
  }

  public IExperiencePointCosts getExperienceCost() {
    return experienceCosts;
  }

  public IPresentationProperties getPresentationProperties() {
    return presentationTemplate;
  }

  public TemplateType getTemplateType() {
    return templateType;
  }

  public ITraitTemplateCollection getTraitTemplateCollection() {
    return traitTemplateCollection;
  }

  public ITraitType getToughnessControllingTraitType() {
    return healthTemplate.getToughnessControllingTrait();
  }

  public IAdditionalTemplate[] getAdditionalTemplates() {
    return additionalTemplates.toArray(new IAdditionalTemplate[additionalTemplates.size()]);
  }

  public IMagicTemplate getMagicTemplate() {
    return magicTemplate;
  }

  public void setAbilityGroups(IGroupedTraitType[] abilityGroups) {
    this.abilityGroups = abilityGroups;
  }

  public void setEssenceTemplate(GenericEssenceTemplate essenceTemplate) {
    this.essenceTemplate = essenceTemplate;
  }

  public void setCreationPoints(GenericCreationPoints creationPoints) {
    this.creationPoints = creationPoints;
  }

  public void setBonusPointCosts(GenericBonusPointCosts bonusPoints) {
    this.bonusPointCosts = bonusPoints;
  }

  public void setExperiencePointCosts(GenericExperiencePointCosts experienceCosts) {
    this.experienceCosts = experienceCosts;
  }

  public IExaltedEdition getEdition() {
    return ExaltedEdition.FirstEdition;
  }

  public void setTraitFactory(GenericTraitTemplateFactory factory) {
    traitTemplateCollection = new TraitTemplateCollection(factory);
  }

  public void setMagicTemplate(GenericMagicTemplate template) {
    magicTemplate = template;
  }

  public void setPresentationTemplate(GenericPresentationTemplate template) {
    this.presentationTemplate = template;
    if (presentationTemplate != null) {
      presentationTemplate.setParentTemplate(this);
    }
  }

  public void setTemplateType(TemplateType templateType) {
    this.templateType = templateType;
  }

  @Override
  public GenericCharacterTemplate clone() {
    GenericCharacterTemplate clone = new GenericCharacterTemplate();
    clone.abilityGroups = abilityGroups;
    clone.attributeGroups = attributeGroups;
    clone.templateType = templateType;
    clone.traitTemplateCollection = traitTemplateCollection;
    if (bonusPointCosts != null) {
      clone.bonusPointCosts = bonusPointCosts.clone();
    }
    if (creationPoints != null) {
      clone.creationPoints = creationPoints.clone();
    }
    if (essenceTemplate != null) {
      clone.essenceTemplate = (GenericEssenceTemplate) essenceTemplate.clone();
    }
    if (experienceCosts != null) {
      clone.experienceCosts = (GenericExperiencePointCosts) experienceCosts.clone();
    }
    if (magicTemplate != null) {
      clone.magicTemplate = (GenericMagicTemplate) magicTemplate.clone();
    }
    if (presentationTemplate != null) {
      clone.presentationTemplate = presentationTemplate.clone();
    }
    return clone;
  }

  public IGroupedTraitType[] getAttributeGroups() {
    return attributeGroups;
  }

  public void setAttributeGroups(IGroupedTraitType[] traitTypeGroups) {
    this.attributeGroups = traitTypeGroups;
  }

  public void addAdditionalTemplate(IAdditionalTemplate template) {
    additionalTemplates.add(template);
  }

  public void setHealthTemplate(IHealthTemplate template) {
    this.healthTemplate = template;
  }

  public void setAdditionalRules(GenericAdditionalRules rules) {
    this.additionalRules = rules;
  }
}