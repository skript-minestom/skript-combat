package com.github.hapily04.skriptcombat.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.util.SimpleEvent;
import com.github.hapily04.skriptcombat.elements.events.wrapper.*;

public class SimpleCombatEvents {

	static {
		Skript.registerEvent("Combat Prepare Attack", SimpleEvent.class, PrepareAttackWrapper.class,
				"combat prepare attack", "prepare [combat] attack")
			.description("Called when an entity is about to prepare a combat attack.")
			.examples("""
				on combat prepare attack:
					broadcast "%attacker% is attacking %victim%\"""");

		Skript.registerEvent("Combat Final Attack", SimpleEvent.class, FinalAttackWrapper.class,
				"combat [final] attack", "final attack")
			.description("Called when a combat attack is finalized. Critical, sweeping, damage, and sound flags can be changed.")
			.examples("""
				on combat attack:
					if event-critical is true:
						set event-base-damage to 10""");

		Skript.registerEvent("Combat Final Damage", SimpleEvent.class, FinalDamageWrapper.class,
				"combat [final] damage", "final damage")
			.description("Called with the final damage after armor and effects. Damage, invulnerability, and animation can be changed.")
			.examples("""
				on final damage:
					set event-damage to event-damage / 2""");

		Skript.registerEvent("Combat Pre Death", SimpleEvent.class, EntityPreDeathWrapper.class,
				"combat pre[-| ]death", "[entity] pre[-| ]death")
			.description("Called before an entity dies from combat damage. Use cancel-death to keep after-damage effects.")
			.examples("""
				on combat pre-death:
					set event-cancel-death to true""");

		Skript.registerEvent("Combat Damage Block", SimpleEvent.class, DamageBlockWrapper.class,
				"combat [damage] block", "shield block")
			.description("Called when an entity blocks damage with a shield.")
			.examples("""
				on shield block:
					set event-resulting-damage to 0""");

		Skript.registerEvent("Combat Knockback", SimpleEvent.class, EntityKnockbackWrapper.class,
				"combat knockback", "[entity] knockback")
			.description("Called when an entity is knocked back by another entity in combat.")
			.examples("""
				on combat knockback:
					set event-horizontal to event-horizontal * 1.5""");

		Skript.registerEvent("Totem Use", SimpleEvent.class, TotemUseWrapper.class,
				"totem [of undying] [use]")
			.description("Called when a totem of undying prevents an entity from dying.")
			.examples("""
				on totem use:
					broadcast "%event-entity% used a totem\"""");

		Skript.registerEvent("Equipment Damage", SimpleEvent.class, EquipmentDamageWrapper.class,
				"equipment damage", "item durability damage")
			.description("Called when equipment takes durability damage.")
			.examples("""
				on equipment damage:
					set event-amount to 0""");

		Skript.registerEvent("Player Exhaust", SimpleEvent.class, PlayerExhaustWrapper.class,
				"[player] exhaust[ion]")
			.description("Called when a player's exhaustion level changes.")
			.examples("""
				on player exhaustion:
					set event-amount to 0""");

		Skript.registerEvent("Player Regenerate", SimpleEvent.class, PlayerRegenerateWrapper.class,
				"[player] regenerat(e|ion)")
			.description("Called when a player naturally regenerates health.")
			.examples("""
				on player regenerate:
					set event-health to 1""");

		Skript.registerEvent("Pickup Entity", SimpleEvent.class, PickupEntityWrapper.class,
				"[arrow] pickup", "pickup [arrow]")
			.description("Called when a player picks up an arrow or trident entity.")
			.examples("""
				on arrow pickup:
					cancel event""");

		Skript.registerEvent("Fishing Bobber Retrieve", SimpleEvent.class, FishingBobberRetrieveWrapper.class,
				"fishing [bobber] retrieve", "retrieve fishing bobber")
			.description("Called when a player retrieves a fishing bobber.")
			.examples("""
				on fishing bobber retrieve:
					broadcast "%event-bobber%\"""");

		Skript.registerEvent("Combat Spectate", SimpleEvent.class, CombatSpectateWrapper.class,
				"combat spectate")
			.description("Called when a spectator tries to spectate an entity by attacking it (PvP spectate).")
			.examples("""
				on combat spectate:
					broadcast "%event-target%\"""");

		Skript.registerEvent("Potion Visibility", SimpleEvent.class, PotionVisibilityWrapper.class,
				"potion visibility [update]")
			.description("Called when an entity's potion ambient/invisibility visibility state updates.")
			.examples("""
				on potion visibility:
					set event-invisible to false""");

		Skript.registerEvent("Explosive Prime", SimpleEvent.class, ExplosivePrimeWrapper.class,
				"explosive prime", "tnt prime")
			.description("Called when TNT or a similar explosive is primed.")
			.examples("""
				on tnt prime:
					set event-fuse to 40""");

		Skript.registerEvent("Combat Explosion", SimpleEvent.class, ExplosionWrapper.class,
				"combat explosion", "[pvp] explosion")
			.description("Called when a PvP explosion is about to take place.")
			.examples("""
				on combat explosion:
					cancel event""");

		Skript.registerEvent("Crystal Place", SimpleEvent.class, CrystalPlaceWrapper.class,
				"crystal place")
			.description("Called when a player places an end crystal.")
			.examples("""
				on crystal place:
					cancel event""");

		Skript.registerEvent("Anchor Charge", SimpleEvent.class, AnchorChargeWrapper.class,
				"anchor charge")
			.description("Called when a player charges a respawn anchor.")
			.examples("""
				on anchor charge:
					cancel event""");

		Skript.registerEvent("Anchor Explode", SimpleEvent.class, AnchorExplodeWrapper.class,
				"anchor explode")
			.description("Called when a player detonates a respawn anchor.")
			.examples("""
				on anchor explode:
					cancel event""");

		Skript.registerEvent("Bed Explode", SimpleEvent.class, BedExplodeWrapper.class,
				"bed explode")
			.description("Called when a player uses a bed in a dimension that triggers a bed explosion.")
			.examples("""
				on bed explode:
					cancel event""");

		Skript.registerEvent("Entity Shoot", SimpleEvent.class, EntityShootWrapper.class,
				"[entity] shoot")
			.description("Called when an entity shoots a projectile.")
			.examples("""
				on entity shoot:
					set event-power to 2""");

		Skript.registerEvent("Projectile Collide Block", SimpleEvent.class, ProjectileCollideBlockWrapper.class,
				"projectile (hit|collide) [with] block")
			.description("Called when a projectile collides with a block.")
			.examples("""
				on projectile hit block:
					broadcast "%event-block%\"""");

		Skript.registerEvent("Projectile Collide Entity", SimpleEvent.class, ProjectileCollideEntityWrapper.class,
				"projectile (hit|collide) [with] entity")
			.description("Called when a projectile collides with an entity.")
			.examples("""
				on projectile hit entity:
					broadcast "%victim%\"""");
	}

}
