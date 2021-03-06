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
package mage.sets.zendikar;

import java.util.UUID;
import mage.abilities.Ability;
import mage.abilities.costs.AlternativeCostImpl;
import mage.abilities.costs.mana.GenericManaCost;
import mage.abilities.effects.common.ExileGraveyardAllTargetPlayerEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Rarity;
import mage.game.Game;
import mage.target.TargetPlayer;
import mage.watchers.common.CardsPutIntoGraveyardWatcher;

/**
 *
 * @author LevelX2
 */
public class RavenousTrap extends CardImpl {

    public RavenousTrap(UUID ownerId) {
        super(ownerId, 109, "Ravenous Trap", Rarity.UNCOMMON, new CardType[]{CardType.INSTANT}, "{2}{B}{B}");
        this.expansionSetCode = "ZEN";
        this.subtype.add("Trap");

        // If an opponent had three or more cards put into his or her graveyard from anywhere this turn, you may pay {0} rather than pay Ravenous Trap's mana cost.
        this.getSpellAbility().addAlternativeCost(
                new RavenousTrapAlternativeCost());
        this.getSpellAbility().addWatcher(new CardsPutIntoGraveyardWatcher());

        // Exile all cards from target player's graveyard.
        this.getSpellAbility().addEffect(new ExileGraveyardAllTargetPlayerEffect());
        this.getSpellAbility().addTarget(new TargetPlayer());
    }

    public RavenousTrap(final RavenousTrap card) {
        super(card);
    }

    @Override
    public RavenousTrap copy() {
        return new RavenousTrap(this);
    }
}

class RavenousTrapAlternativeCost extends AlternativeCostImpl {

    public RavenousTrapAlternativeCost() {
        super("If an opponent had three or more cards put into his or her graveyard from anywhere this turn, you may pay {0} rather than pay Ravenous Trap's mana cost");
        this.add(new GenericManaCost(0));
    }

    public RavenousTrapAlternativeCost(final RavenousTrapAlternativeCost cost) {
        super(cost);
    }

    @Override
    public RavenousTrapAlternativeCost copy() {
        return new RavenousTrapAlternativeCost(this);
    }

    @Override
    public boolean isAvailable(Game game, Ability source) {
        CardsPutIntoGraveyardWatcher watcher = (CardsPutIntoGraveyardWatcher) game.getState().getWatchers().get("CardsPutIntoGraveyardWatcher");
        if (watcher != null) {
            for (UUID opponentId : game.getOpponents(source.getControllerId())) {
                if (watcher.getAmountCardsPutToGraveyard(opponentId) > 2) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getText() {
        return "If an opponent had three or more cards put into his or her graveyard from anywhere this turn, you may pay {0} rather than pay {this}'s mana cost";
    }
}
