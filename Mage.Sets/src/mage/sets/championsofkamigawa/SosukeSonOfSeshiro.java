/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.championsofkamigawa;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.SimpleStaticAbility;
import mage.abilities.common.delayed.AtTheEndOfCombatDelayedTriggeredAbility;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.CreateDelayedTriggeredAbilityEffect;
import mage.abilities.effects.common.DestroyTargetEffect;
import mage.abilities.effects.common.continuous.BoostControlledEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.mageobject.SubtypePredicate;
import mage.game.Game;
import mage.game.events.DamagedCreatureEvent;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.game.permanent.Permanent;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author LevelX
 */
public class SosukeSonOfSeshiro extends CardImpl {

    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("Snake creatures");

    static {
            filter.add(new SubtypePredicate("Snake"));
    }

    public SosukeSonOfSeshiro(UUID ownerId) {
        super(ownerId, 244, "Sosuke, Son of Seshiro", Rarity.UNCOMMON, new CardType[]{CardType.CREATURE}, "{2}{G}{G}");
        this.expansionSetCode = "CHK";
        this.supertype.add("Legendary");
        this.subtype.add("Snake");
        this.subtype.add("Warrior");

        this.power = new MageInt(3);
        this.toughness = new MageInt(4);

        // Other Snake creatures you control get +1/+0.
        this.addAbility(new SimpleStaticAbility(Zone.BATTLEFIELD, new BoostControlledEffect(1, 0, Duration.WhileOnBattlefield, filter, true)));
        // Whenever a Warrior you control deals combat damage to a creature, destroy that creature at end of combat.
        Effect effect = new CreateDelayedTriggeredAbilityEffect(
                new AtTheEndOfCombatDelayedTriggeredAbility(new DestroyTargetEffect()), true);
        effect.setText("destroy that creature at end of combat");
        this.addAbility(new SosukeSonOfSeshiroTriggeredAbility(effect));
    }

    public SosukeSonOfSeshiro(final SosukeSonOfSeshiro card) {
        super(card);
    }

    @Override
    public SosukeSonOfSeshiro copy() {
        return new SosukeSonOfSeshiro(this);
    }
}

class SosukeSonOfSeshiroTriggeredAbility extends TriggeredAbilityImpl {

    SosukeSonOfSeshiroTriggeredAbility(Effect effect) {
        super(Zone.BATTLEFIELD, effect);
    }

    SosukeSonOfSeshiroTriggeredAbility(final SosukeSonOfSeshiroTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public SosukeSonOfSeshiroTriggeredAbility copy() {
        return new SosukeSonOfSeshiroTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.DAMAGED_CREATURE;
    }

    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        if (((DamagedCreatureEvent) event).isCombatDamage()) {
            Permanent sourceCreature = game.getPermanent(event.getSourceId());
            Permanent targetCreature = game.getPermanent(event.getTargetId());
            if (sourceCreature != null && sourceCreature.getControllerId().equals(this.getControllerId()) 
                && targetCreature != null && sourceCreature.hasSubtype("Warrior")) {
                    this.getEffects().get(0).setTargetPointer(new FixedTarget(targetCreature.getId()));
                    return true;
            }
        }
        return false;
    }

    @Override
    public String getRule() {
        return "Whenever a Warrior you control deals combat damage to a creature, destroy that creature at end of combat.";
    }
}
