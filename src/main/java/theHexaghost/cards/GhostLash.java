package theHexaghost.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theHexaghost.HexaMod;
import theHexaghost.patches.ExhaustCardTickPatch;

public class GhostLash extends AbstractHexaCard {

    public final static String ID = makeID("GhostLash");

    //stupid intellij stuff ATTACK, ENEMY, COMMON

    private static final int DAMAGE = 7;
    private static final int UPG_DAMAGE = 3;

    public GhostLash() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = DAMAGE;
        isEthereal = true;
        tags.add(HexaMod.AFTERLIFE);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, makeInfo(), AbstractGameAction.AttackEffect.SLASH_HEAVY);
    }

    @Override
    public void afterlife() {
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        if (m == null) return;
        use(AbstractDungeon.player, m);
        atb(new DamageAction(m, makeInfo(), AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }
/*
    public void triggerOnGlowCheck() {
        this.glowColor = ExhaustCardTickPatch.exhaustedLastTurn ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;// 65
    }// 68
 */
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPG_DAMAGE);
        }
    }
}