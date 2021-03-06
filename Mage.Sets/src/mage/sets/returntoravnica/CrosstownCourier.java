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
package mage.sets.returntoravnica;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.TriggeredAbilityImpl;
import mage.abilities.dynamicvalue.common.StaticValue;
import mage.abilities.effects.Effect;
import mage.abilities.effects.common.PutLibraryIntoGraveTargetEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.constants.Zone;
import mage.game.Game;
import mage.game.events.DamagedPlayerEvent;
import mage.game.events.GameEvent;
import mage.game.events.GameEvent.EventType;
import mage.target.targetpointer.FixedTarget;

/**
 *
 * @author LevelX2
 */
public class CrosstownCourier extends CardImpl {

    public CrosstownCourier(UUID ownerId) {
        super(ownerId, 34, "Crosstown Courier", Rarity.COMMON, new CardType[]{CardType.CREATURE}, "{1}{U}");
        this.expansionSetCode = "RTR";
        this.subtype.add("Vedalken");

        this.power = new MageInt(2);
        this.toughness = new MageInt(1);

        // Whenever Crosstown Courier deals combat damage to a player, that player puts that many cards from the top of his or her library into his or her graveyard.
        this.addAbility(new CrosstownCourierTriggeredAbility());
    }

    public CrosstownCourier(final CrosstownCourier card) {
        super(card);
    }

    @Override
    public CrosstownCourier copy() {
        return new CrosstownCourier(this);
    }

    class CrosstownCourierTriggeredAbility extends TriggeredAbilityImpl {

        public CrosstownCourierTriggeredAbility() {
            super(Zone.BATTLEFIELD, new PutLibraryIntoGraveTargetEffect(0), false);
        }

        public CrosstownCourierTriggeredAbility(final CrosstownCourierTriggeredAbility ability) {
            super(ability);
        }

        @Override
        public CrosstownCourierTriggeredAbility copy() {
            return new CrosstownCourierTriggeredAbility(this);
        }

        @Override
        public boolean checkEventType(GameEvent event, Game game) {
            return event.getType() == EventType.DAMAGED_PLAYER;
        }

        @Override
        public boolean checkTrigger(GameEvent event, Game game) {
            if (event.getSourceId().equals(this.sourceId) && ((DamagedPlayerEvent) event).isCombatDamage()) {
                for (Effect effect : getEffects()) {
                    if (effect instanceof PutLibraryIntoGraveTargetEffect) {
                        effect.setTargetPointer(new FixedTarget(event.getTargetId()));
                        ((PutLibraryIntoGraveTargetEffect) effect).setAmount(new StaticValue(event.getAmount()));
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public String getRule() {
            return "Whenever {this} deals combat damage to a player, that player puts that many cards from the top of his or her library into his or her graveyard.";
        }
    }
}
