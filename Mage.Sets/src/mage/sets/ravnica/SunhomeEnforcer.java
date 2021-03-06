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
package mage.sets.ravnica;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.common.SimpleActivatedAbility;
import mage.abilities.costs.mana.ManaCostsImpl;
import mage.abilities.effects.common.GainLifeEffect;
import mage.abilities.effects.common.continuous.BoostSourceEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.DamagedEvent;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;

/**
 *
 * @author fireshoes
 */
public class SunhomeEnforcer extends CardImpl {

    public SunhomeEnforcer(UUID ownerId) {
        super(ownerId, 233, "Sunhome Enforcer", Rarity.UNCOMMON, new CardType[]{CardType.CREATURE}, "{2}{R}{W}");
        this.expansionSetCode = "RAV";
        this.subtype.add("Giant");
        this.subtype.add("Soldier");
        this.power = new MageInt(2);
        this.toughness = new MageInt(4);

        // Whenever Sunhome Enforcer deals combat damage, you gain that much life.
        this.addAbility(new SunhomeEnforcerTriggeredAbility());
        
        // {1}{R}: Sunhome Enforcer gets +1/+0 until end of turn.
        this.addAbility(new SimpleActivatedAbility(Zone.BATTLEFIELD, new BoostSourceEffect(1, 0, Duration.EndOfTurn), new ManaCostsImpl("{1}{R}")));
    }

    public SunhomeEnforcer(final SunhomeEnforcer card) {
        super(card);
    }

    @Override
    public SunhomeEnforcer copy() {
        return new SunhomeEnforcer(this);
    }
}

class SunhomeEnforcerTriggeredAbility extends TriggeredAbilityImpl {

    public SunhomeEnforcerTriggeredAbility() {
        super(Zone.BATTLEFIELD, null);
    }
    
    public SunhomeEnforcerTriggeredAbility(final SunhomeEnforcerTriggeredAbility ability) {
        super(ability);
    }

    @Override
    public SunhomeEnforcerTriggeredAbility copy() {
        return new SunhomeEnforcerTriggeredAbility(this);
    }

    @Override
    public boolean checkEventType(GameEvent event, Game game) {
        return event.getType() == EventType.DAMAGED_CREATURE 
                || event.getType() == EventType.DAMAGED_PLAYER 
                || event.getType() == EventType.DAMAGED_PLANESWALKER;
    }
    
    @Override
    public boolean checkTrigger(GameEvent event, Game game) {
        DamagedEvent damageEvent = (DamagedEvent) event;
        if (damageEvent.isCombatDamage()) {
            if (event.getSourceId().equals(this.sourceId)) {
                this.getEffects().clear();
                this.getEffects().add(new GainLifeEffect(damageEvent.getAmount()));
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getRule() {
        return "Whenever {this} deals combat damage, you gain that much life.";
    }
}
