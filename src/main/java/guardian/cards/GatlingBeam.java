package guardian.cards;


import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import gremlin.actions.PseudoDamageRandomEnemyAction;
import guardian.GuardianMod;
import guardian.actions.PlaceActualCardIntoStasis;
import guardian.orbs.StasisOrb;
import guardian.patches.AbstractCardEnum;
import guardian.vfx.SmallLaserEffectColored;


public class GatlingBeam extends AbstractGuardianCard implements InStasisCard {
    public static final String ID = GuardianMod.makeID("GatlingBeam");
    public static final String NAME;
    public static final String[] EXTENDED_DESCRIPTION;
    public static final String IMG_PATH = "cards/gatlingBeam.png";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardStrings cardStrings;
    private static final int COST = 1;

    //TUNING CONSTANTS
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 2;

    private static final int TICK_DURATION = 4;
    private static final int UPGRADE_TICK_DURATION = 1;



    private static final int SOCKETS = 0;
    private static final boolean SOCKETSAREAFTER = true;
    public static String DESCRIPTION;
    public static String UPGRADED_DESCRIPTION;

    //END TUNING CONSTANTS

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
        UPGRADED_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
        EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    }

    public GatlingBeam() {

        super(ID, NAME, GuardianMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.GUARDIAN, RARITY, TARGET);

        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = TICK_DURATION;

        this.tags.add(GuardianMod.BEAM);
        this.tags.add(GuardianMod.TICK);
        this.tags.add(GuardianMod.SELFSTASIS);
        this.socketCount = SOCKETS;
        updateDescription();
        loadGemMisc();

    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp) {
        return tmp + calculateBeamDamage();
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, float tmp) {
        return tmp + calculateBeamDamage();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        super.use(p, m);
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffectColored(m.hb.cX, m.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.BLUE), 0.1F));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new com.megacrit.cardcrawl.cards.DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.FIRE));

    }

    @Override
    public void whenEnteredStasis(StasisOrb orb) {
        orb.passiveAmount = this.baseMagicNumber;
    }

    public AbstractCard makeCopy() {

        return new GatlingBeam();

    }

    public void upgrade() {

        if (!this.upgraded) {

            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeMagicNumber(UPGRADE_TICK_DURATION);

        }

    }


    public void updateDescription() {

        if (this.socketCount > 0) {
            if (upgraded && UPGRADED_DESCRIPTION != null) {
                this.rawDescription = this.updateGemDescription(UPGRADED_DESCRIPTION, true);
            } else {
                this.rawDescription = this.updateGemDescription(DESCRIPTION, true);
            }
        }
        this.initializeDescription();
    }

    @Override
    public void onStartOfTurn(StasisOrb orb) {
        AbstractMonster m = AbstractDungeon.getMonsters().getRandomMonster(true);

        if (m != null)
        {
            AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new SmallLaserEffectColored(m.hb.cX, m.hb.cY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.BLUE), 0.1F));
            AbstractDungeon.actionManager.addToBottom(new PseudoDamageRandomEnemyAction(m, new DamageInfo(AbstractDungeon.player, this.damage, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));
        }
    }

    @Override
    public void onEvoke(StasisOrb orb) {

    }
}


