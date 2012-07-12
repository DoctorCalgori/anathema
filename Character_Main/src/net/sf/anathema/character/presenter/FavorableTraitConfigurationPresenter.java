package net.sf.anathema.character.presenter;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.framework.additionaltemplate.listening.DedicatedCharacterChangeAdapter;
import net.sf.anathema.character.generic.framework.additionaltemplate.model.ICharacterListening;
import net.sf.anathema.character.generic.template.presentation.IPresentationProperties;
import net.sf.anathema.character.generic.traits.groups.IIdentifiedTraitTypeGroup;
import net.sf.anathema.character.generic.traits.groups.TraitTypeGroup;
import net.sf.anathema.character.library.intvalue.IToggleButtonTraitView;
import net.sf.anathema.character.library.trait.IModifiableCapTrait;
import net.sf.anathema.character.library.trait.favorable.FavorableState;
import net.sf.anathema.character.library.trait.favorable.IFavorableStateChangedListener;
import net.sf.anathema.character.library.trait.favorable.IFavorableStateVisitor;
import net.sf.anathema.character.library.trait.favorable.IFavorableTrait;
import net.sf.anathema.character.library.trait.presenter.TraitPresenter;
import net.sf.anathema.character.library.trait.visitor.IAggregatedTrait;
import net.sf.anathema.character.library.trait.visitor.IDefaultTrait;
import net.sf.anathema.character.library.trait.visitor.ITraitVisitor;
import net.sf.anathema.character.model.ICharacter;
import net.sf.anathema.character.model.traits.ICoreTraitConfiguration;
import net.sf.anathema.character.view.IGroupedFavorableTraitConfigurationView;
import net.sf.anathema.lib.collection.IdentityMapping;
import net.sf.anathema.lib.control.IBooleanValueChangedListener;
import net.sf.anathema.lib.resources.IResources;

public class FavorableTraitConfigurationPresenter {

  private final IGroupedFavorableTraitConfigurationView configurationView;
  private final IdentityMapping<IFavorableTrait, IToggleButtonTraitView<?>> traitViewsByTrait =
          new IdentityMapping<IFavorableTrait, IToggleButtonTraitView<?>>();
  private final IResources resources;
  private final IIdentifiedTraitTypeGroup[] traitTypeGroups;
  private final ICoreTraitConfiguration traitConfiguration;
  private final IBasicCharacterData basicCharacterData;
  private final ICharacterListening characterListening;
  private final IPresentationProperties presentationProperties;
  private final int castePicks;

  public FavorableTraitConfigurationPresenter(IIdentifiedTraitTypeGroup[] traitTypeGroups, int castePicks, ICharacter character,
                                              IGroupedFavorableTraitConfigurationView configurationView, IResources resources) {
    this.traitTypeGroups = traitTypeGroups;
    this.traitConfiguration = character.getTraitConfiguration();
    this.basicCharacterData = character.getCharacterContext().getBasicCharacterContext();
    this.characterListening = character.getCharacterContext().getCharacterListening();
    this.presentationProperties = character.getCharacterTemplate().getPresentationProperties();
    this.resources = resources;
    this.configurationView = configurationView;
    this.castePicks = castePicks;
  }

  public void init(String typePrefix) {
    for (IIdentifiedTraitTypeGroup traitTypeGroup : traitTypeGroups) {
      configurationView.startNewTraitGroup(resources.getString(typePrefix + "." + traitTypeGroup.getGroupId().getId())); //$NON-NLS-1$
      addAbilityViews(traitConfiguration.getFavorableTraits(traitTypeGroup.getAllGroupTypes()));
    }
    characterListening.addChangeListener(new DedicatedCharacterChangeAdapter() {
      @Override
      public void experiencedChanged(boolean experienced) {
        updateButtons();
      }
    });
    updateButtons();
  }

  private void updateButtons() {
    for (IFavorableTrait trait : getAllTraits()) {
      IToggleButtonTraitView<?> view = traitViewsByTrait.get(trait);
      boolean disabled = basicCharacterData.isExperienced() || (!allowConfigureCasteTraits() && trait.getFavorization().isCasteOption());
      boolean favored = trait.getFavorization().isCasteOrFavored();
      view.setButtonState(favored, !disabled);
    }
  }

  private IFavorableTrait[] getAllTraits() {
    return traitConfiguration.getFavorableTraits(TraitTypeGroup.getAllTraitTypes(traitTypeGroups));
  }

  private void addAbilityViews(IFavorableTrait[] abilityGroup) {
    for (IFavorableTrait ability : abilityGroup) {
      traitViewsByTrait.put(ability, addAbilityView(ability));
    }
  }

  private IToggleButtonTraitView<?> addAbilityView(final IFavorableTrait favorableTrait) {
    final String id = favorableTrait.getType().getId();
    final IToggleButtonTraitView<?>[] view = new IToggleButtonTraitView<?>[1];
    favorableTrait.accept(new ITraitVisitor() {
      @Override
      public void visitAggregatedTrait(IAggregatedTrait visitedTrait) {
        view[0] = configurationView
                .addMarkerLessTraitView(resources.getString(id), favorableTrait.getCurrentValue(), favorableTrait.getMaximalValue(),
                        visitedTrait.getFallbackTrait(), favorableTrait.getFavorization().isFavored(),
                        new FavorableTraitViewProperties(presentationProperties, basicCharacterData, favorableTrait, resources));
      }

      @Override
      public void visitDefaultTrait(IDefaultTrait visitedTrait) {
        view[0] = configurationView.addTraitView(resources.getString(id), favorableTrait.getCurrentValue(), favorableTrait.getMaximalValue(),
                (IModifiableCapTrait) favorableTrait, favorableTrait.getFavorization().isFavored(),
                new FavorableTraitViewProperties(presentationProperties, basicCharacterData, favorableTrait, resources));
      }
    });
    final IToggleButtonTraitView<?> abilityView = view[0];
    new TraitPresenter(favorableTrait, abilityView).initPresentation();
    abilityView.addButtonSelectedListener(new IBooleanValueChangedListener() {
      @Override
      public void valueChanged(boolean newValue) {
    	switch (favorableTrait.getFavorization().getFavorableState()) {
    	case Default:
    		favorableTrait.getFavorization().setFavorableState(
    				allowConfigureCasteTraits() && favorableTrait.getFavorization().isCasteOption() ?
    				FavorableState.Caste : FavorableState.Favored);
    		if (favorableTrait.getFavorization().getFavorableState() == FavorableState.Default) {
    			favorableTrait.getFavorization().setFavored(true);
    		}
    		break;
    	case Favored:
    		favorableTrait.getFavorization().setFavored(false);
    		break;
    	case Caste:
    		favorableTrait.getFavorization().setFavorableState(FavorableState.Default);
    		favorableTrait.getFavorization().setFavored(true);
    		abilityView.setButtonState(true, true);
    		break;
    	}
      }
    });
    favorableTrait.getFavorization().addFavorableStateChangedListener(new IFavorableStateChangedListener() {
      @Override
      public void favorableStateChanged(FavorableState state) {
        updateView(abilityView, state);
      }
    });
    updateView(abilityView, favorableTrait.getFavorization().getFavorableState());
    return abilityView;
  }
  
  private boolean allowConfigureCasteTraits() {
	  if (castePicks == 0) {
		  return false;
	  }
	  for (IIdentifiedTraitTypeGroup group : traitTypeGroups) {
		  if (group.getGroupId().getId().equals(basicCharacterData.getCasteType().getId()) &&
			  group.getAllGroupTypes().length > castePicks) {
			  return true;
		  }
	  }
	  return false;
  }

  private void updateView(final IToggleButtonTraitView<?> abilityView, FavorableState state) {
    state.accept(new IFavorableStateVisitor() {
      @Override
      public void visitDefault(FavorableState visitedState) {
        abilityView.setButtonState(false, true);
      }

      @Override
      public void visitFavored(FavorableState visitedState) {
        abilityView.setButtonState(true, true);
      }

      @Override
      public void visitCaste(FavorableState visitedState) {
        abilityView.setButtonState(true, allowConfigureCasteTraits());
      }
    });
  }
}