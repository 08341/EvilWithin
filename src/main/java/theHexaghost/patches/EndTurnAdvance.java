package theHexaghost.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import downfall.downfallMod;
import theHexaghost.GhostflameHelper;
import theHexaghost.HexaMod;
import theHexaghost.actions.AdvanceAction;
import theHexaghost.ghostflames.MayhemGhostflame;
import theHexaghost.powers.AgainPower;
import theHexaghost.powers.GainStrengthThatGoesAwayPower;

@SpirePatch(
        clz = GameActionManager.class,
        method = "callEndOfTurnActions"
)
public class EndTurnAdvance {
    public static void Postfix(GameActionManager __instance) {
        if (HexaMod.renderFlames) {
            if (GhostflameHelper.activeGhostFlame instanceof MayhemGhostflame)
                GhostflameHelper.activeGhostFlame.advanceTrigger(null);
            if (AbstractDungeon.player.hasPower(AgainPower.POWER_ID)) {
                AbstractPower p = AbstractDungeon.player.getPower(AgainPower.POWER_ID);
                p.flash();
                AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(p.owner, p.owner, p, 1));
            } else if (AbstractDungeon.player.hasPower(GainStrengthThatGoesAwayPower.POWER_ID)) {
                AbstractPower p = AbstractDungeon.player.getPower(GainStrengthThatGoesAwayPower.POWER_ID);
                p.flash();
            } else if (GhostflameHelper.activeGhostFlame.charged)
                AbstractDungeon.actionManager.addToBottom(new AdvanceAction(true));
        }
        downfallMod.playedBossCardThisTurn = false;
    }
}