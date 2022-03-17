package downfall.monsters.gauntletbosses;

import charbosses.core.EnemyEnergyManager;
import charbosses.powers.cardpowers.EnemyWraithFormPower;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Doubt;
import com.megacrit.cardcrawl.cards.green.LegSweep;
import com.megacrit.cardcrawl.cards.green.WraithForm;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbGreen;
import downfall.downfallMod;
import downfall.powers.DrawReductionPowerPlus;
import downfall.powers.gauntletpowers.MonsterVigor;
import downfall.powers.gauntletpowers.OnDeathEveryoneBuffer;
import downfall.powers.gauntletpowers.OnDeathEveryoneThorns;

public class Silent extends GauntletBoss {

    public static final String ID = downfallMod.makeID("GauntletSilent");
    public static final String NAME = CardCrawlGame.languagePack.getCharacterString("Silent").NAMES[0];
    private static final float HB_X = 0.0F;
    private static final float HB_Y = 0.0F;
    private static final float HB_W = 225.0F;
    private static final float HB_H = 250.0F;

    int turnNum = 0;
    public Silent(float x, float y) {
        super(NAME, ID, 70, -4.0f, -16.0f, 240.0f, 290.0f, null, x, y);
        this.loadAnimation("images/characters/theSilent/idle/skeleton.atlas", "images/characters/theSilent/idle/skeleton.json", 1.0f);
        final AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Hit", "Idle", 0.1f);
        this.flipHorizontal = true;
        e.setTimeScale(0.9f);
        type = EnemyType.ELITE;

        this.damage.add(new DamageInfo(this, 6));
        this.damage.add(new DamageInfo(this, 6));

    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new OnDeathEveryoneThorns(this, 4), 4));
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case 1:
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (hasPower(MonsterVigor.POWER_ID)) {
                    addToBot(new RemoveSpecificPowerAction(this, this, MonsterVigor.POWER_ID));
                }
                break;
            case 2:
                addToBot(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                addToBot(new GainBlockAction(this, 5));
                if (hasPower(MonsterVigor.POWER_ID)) {
                    addToBot(new RemoveSpecificPowerAction(this, this, MonsterVigor.POWER_ID));
                }
                break;
            case 3:
                addToBot(new GainBlockAction(this, 10));
                break;
            case 4:
                addToBot(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 2, true), 2));
                addToBot(new GainBlockAction(this, 11));
                break;
            case 5:
                addToBot(new ApplyPowerAction(this, this, new EnemyWraithFormPower(this, 2), 2));
                break;
        }


        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
        turnNum ++;
        if (turnNum == 5) {
            setMove(CardLibrary.getCard(WraithForm.ID).name, (byte)5, Intent.BUFF);
        }
        else {
            int rnd = AbstractDungeon.cardRandomRng.random(0, 3);
            if (hasPower(EnemyWraithFormPower.POWER_ID)) {
                rnd = 0;
            }
            switch (rnd) {
                case 0:
                    setMove((byte)1, Intent.ATTACK, this.damage.get(0).base, 2, true);
                    break;
                case 1:
                    setMove((byte)2, Intent.ATTACK_DEFEND, this.damage.get(1).base);
                    break;
                case 2:
                    setMove((byte)3, Intent.DEFEND);
                    break;
                case 3:
                    setMove(CardLibrary.getCard(LegSweep.ID).name, (byte)4, Intent.DEFEND_DEBUFF);
                    break;
            }
        }
    }


}
