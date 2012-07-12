package net.sf.anathema.character.library.trait.favorable;

import net.sf.anathema.character.generic.IBasicCharacterData;
import net.sf.anathema.character.generic.caste.ICasteType;
import net.sf.anathema.character.library.ITraitFavorization;
import net.sf.anathema.character.library.trait.ITrait;
import net.sf.anathema.character.library.trait.visitor.IAggregatedTrait;
import net.sf.anathema.character.library.trait.visitor.IDefaultTrait;
import net.sf.anathema.character.library.trait.visitor.ITraitVisitor;
import org.jmock.example.announcer.Announcer;

public class TraitFavorization implements ITraitFavorization {

  private FavorableState state;
  private final Announcer<IFavorableStateChangedListener> favorableStateControl = Announcer.to(IFavorableStateChangedListener.class);
  private final IIncrementChecker favoredIncrementChecker;
  private final IIncrementChecker casteIncrementChecker;
  private final ITrait trait;
  private final ICasteType[] castes;
  private final boolean isRequiredFavored;
  private final IBasicCharacterData basicData;
  
  public TraitFavorization(
	      IBasicCharacterData basicData,
	      ICasteType[] castes,
	      IIncrementChecker favoredIncrementChecker,
	      ITrait trait,
	      boolean isRequiredFavored) {
	  this(basicData, castes, null, favoredIncrementChecker, trait, isRequiredFavored);
  }

  public TraitFavorization(
      IBasicCharacterData basicData,
      ICasteType[] castes,
      IIncrementChecker casteIncrementChecker,
      IIncrementChecker favoredIncrementChecker,
      ITrait trait,
      boolean isRequiredFavored) {
    this.basicData = basicData;
    this.castes = castes;
    this.casteIncrementChecker = casteIncrementChecker;
    this.favoredIncrementChecker = favoredIncrementChecker;
    this.trait = trait;
    this.isRequiredFavored = isRequiredFavored;
    this.state = isRequiredFavored ? FavorableState.Favored : FavorableState.Default;
  }

  @Override
  public final void setFavorableState(FavorableState state) {
    if (state == FavorableState.Caste && isRequiredFavored) {
      throw new IllegalStateException("Traits with required favored must not  be of any caste"); //$NON-NLS-1$
    }
    if (this.state == state && state != FavorableState.Caste) {
      return;
    }
    if (state == FavorableState.Favored && !favoredIncrementChecker.isValidIncrement(1)) {
      return;
    }
    if (state == FavorableState.Caste && casteIncrementChecker != null && 
    	!casteIncrementChecker.isValidIncrement(1)) {
      return;
    }
    if (isRequiredFavored && state == FavorableState.Default) {
      state = FavorableState.Favored;
    }
    this.state = state;
    ensureMinimalValue();
    fireFavorableStateChangedEvent();
  }

  @Override
  public void ensureMinimalValue() {
    final int minimalValue = getMinimalValue();
    if (trait.getCurrentValue() < minimalValue) {
      trait.accept(new ITraitVisitor() {

        @Override
        public void visitAggregatedTrait(IAggregatedTrait visitedTrait) {
          visitedTrait.getFallbackTrait().setCurrentValue(minimalValue);
        }

        @Override
        public void visitDefaultTrait(IDefaultTrait visitedTrait) {
          visitedTrait.setCurrentValue(minimalValue);
        }
      });
    }
  }

  @Override
  public int getMinimalValue() {
    return this.state == FavorableState.Favored ? 1 : 0;
  }

  @Override
  public void setFavored(boolean favored) {
    if (isCaste() || isFavored() == favored) {
      return;
    }
    setFavorableState(favored ? FavorableState.Favored : FavorableState.Default);
  }

  public void setCaste(boolean caste) {
    if (!caste && isCaste() == caste) {
      return;
    }
    setFavorableState(caste ? FavorableState.Caste : (isCaste() ? FavorableState.Default : FavorableState.Favored));
  }

  @Override
  public final FavorableState getFavorableState() {
    return state;
  }

  @Override
  public final void addFavorableStateChangedListener(IFavorableStateChangedListener listener) {
    favorableStateControl.addListener(listener);
  }

  private void fireFavorableStateChangedEvent() {
    favorableStateControl.announce().favorableStateChanged(state);
  }

  @Override
  public final boolean isFavored() {
    return state == FavorableState.Favored;
  }

  @Override
  public final boolean isCaste() {
    return state == FavorableState.Caste;
  }

  @Override
  public final boolean isCasteOrFavored() {
    return isCaste() || isFavored();
  }

  @Override
  public ICasteType[] getCastes() {
    return castes;
  }

  @Override
  public void updateFavorableStateToCaste() {
    setCaste(isCasteOption());
  }

  private boolean isSupportedCasteType(ICasteType casteType) {
    ICasteType[] favorizationCaste = getCastes();
    if (favorizationCaste == null) return false;
    for (ICasteType caste : favorizationCaste)
    	if (caste == casteType)
    		return true;
    return false;
  }

  @Override
  public boolean isCasteOption() {
	return isSupportedCasteType(basicData.getCasteType());
  }
}