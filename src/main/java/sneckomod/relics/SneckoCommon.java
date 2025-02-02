package sneckomod.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import sneckomod.SneckoMod;
import sneckomod.cards.unknowns.UnknownClass;
import downfall.util.TextureLoader;

public class SneckoCommon extends CustomRelic implements CustomSavable<AbstractCard.CardColor> {

    public static final String ID = SneckoMod.makeID("SneckoCommon");
    private static final Texture IMG = TextureLoader.getTexture(SneckoMod.makeRelicPath("SealOfApproval.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(SneckoMod.makeRelicOutlinePath("SealOfApproval.png"));

    public SneckoCommon() {
        super(ID, IMG, OUTLINE, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    private boolean chosenInGeneral = true;

    @Override
    public void onEquip() {
        if (SneckoBoss.myColor != null && AbstractDungeon.player.hasRelic(SneckoCommon.ID)) { // already got Lucky Horseshoe
            for (AbstractCard c : CardLibrary.getAllCards()) {
                if (c instanceof UnknownClass && SneckoBoss.myColor == ((UnknownClass) c).myColor) {
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.makeCopy(), Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                }
            }
        } else {
            chosenInGeneral = false;
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            CardGroup c = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard q : CardLibrary.getAllCards()) {
                if (q instanceof UnknownClass) {
                    if (SneckoMod.validColors.contains(((UnknownClass) q).myColor) || SneckoMod.pureSneckoMode) {
                        c.addToTop(q.makeCopy());
                    }
                }
            }
            if (SneckoMod.pureSneckoMode) {
                c.shuffle();
                CardGroup r = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                for (int i = 0; i < 4; i++) {
                    r.addToTop(c.group.get(i));
                }
                AbstractDungeon.gridSelectScreen.open(r, 1, false, CardCrawlGame.languagePack.getUIString("bronze:MiscStrings").TEXT[8]);
            } else
                AbstractDungeon.gridSelectScreen.open(c, 1, false, CardCrawlGame.languagePack.getUIString("bronze:MiscStrings").TEXT[8]);
        }

    }

    @Override
    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty() && !chosenInGeneral) {
            chosenInGeneral = true;
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            SneckoBoss.myColor = ((UnknownClass)c).myColor;
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(c.makeCopy(), Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
            SneckoBoss.updateCardPools();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.description = getUpdatedDescription(); this.tips.clear();
            this.tips.add(new PowerTip(this.name, this.description));
        }
    }

    public String getUpdatedDescription() {
        if (SneckoBoss.myColor != null) {
            return DESCRIPTIONS[1] + SneckoMod.getClassFromColor(SneckoBoss.myColor) + DESCRIPTIONS[2];
        }
        return DESCRIPTIONS[0];
    }

    @Override
    public boolean canSpawn() {
        return !AbstractDungeon.player.hasRelic(SneckoBoss.ID);
    }

    @Override
    public AbstractCard.CardColor onSave() {
        return SneckoBoss.myColor;
    }

    @Override
    public void onLoad(AbstractCard.CardColor s) {
        SneckoBoss.myColor = s;
    }
}
