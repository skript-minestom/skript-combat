package com.github.hapily04.skriptcombat.elements.util;

import io.github.togar2.pvp.utils.PotionFlags;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.Entity;
import net.minestom.server.item.component.PotionContents;
import net.minestom.server.potion.CustomPotionEffect;
import net.minestom.server.potion.Potion;
import net.minestom.server.potion.PotionEffect;
import net.minestom.server.potion.TimedPotion;
import ch.njol.skript.util.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Skript-facing potion effect with amplifier and duration, similar to {@link ch.njol.skript.util.Enchantment}.
 */
public record AppliedPotion(
		PotionEffect type,
		int amplifier,
		int duration,
		boolean ambient,
		boolean particles,
		boolean icon
) {

	public AppliedPotion(PotionEffect type, int amplifier, int duration) {
		this(type, amplifier, duration, false, true, true);
	}

	public Potion toPotion() {
		return new Potion(type, amplifier, duration, PotionFlags.create(ambient, particles, icon));
	}

	public CustomPotionEffect toCustomPotionEffect() {
		return new CustomPotionEffect(type, amplifier, duration, ambient, particles, icon);
	}

	public static AppliedPotion from(Potion potion) {
		byte flags = potion.flags();
		return new AppliedPotion(
				potion.effect(),
				potion.amplifier(),
				potion.duration(),
				(flags & Potion.AMBIENT_FLAG) != 0,
				(flags & Potion.PARTICLES_FLAG) != 0,
				(flags & Potion.ICON_FLAG) != 0
		);
	}

	public static AppliedPotion from(TimedPotion timed) {
		return from(timed.potion());
	}

	public static AppliedPotion from(CustomPotionEffect custom) {
		CustomPotionEffect.Settings settings = custom.settings();
		return new AppliedPotion(
				custom.id(),
				settings.amplifier(),
				settings.duration(),
				settings.isAmbient(),
				settings.showParticles(),
				settings.showIcon()
		);
	}

	public static void apply(Entity entity, AppliedPotion... effects) {
		for (AppliedPotion effect : effects) {
			entity.addEffect(effect.toPotion());
		}
	}

	public static void remove(Entity entity, AppliedPotion... effects) {
		for (AppliedPotion effect : effects) {
			entity.removeEffect(effect.type());
		}
	}

	public static void removeTypes(Entity entity, PotionEffect... types) {
		for (PotionEffect type : types) {
			entity.removeEffect(type);
		}
	}

	public static AppliedPotion[] getEffects(Entity entity) {
		List<TimedPotion> active = entity.getActiveEffects();
		AppliedPotion[] result = new AppliedPotion[active.size()];
		for (int i = 0; i < active.size(); i++) {
			result[i] = from(active.get(i));
		}
		return result;
	}

	public static void set(Entity entity, AppliedPotion... effects) {
		entity.clearEffects();
		apply(entity, effects);
	}

	public static AppliedPotion[] getItemEffects(Item item) {
		PotionContents contents = item.getItem().get(DataComponents.POTION_CONTENTS);
		if (contents == null) return new AppliedPotion[0];
		List<CustomPotionEffect> customs = contents.customEffects();
		AppliedPotion[] result = new AppliedPotion[customs.size()];
		for (int i = 0; i < customs.size(); i++) {
			result[i] = from(customs.get(i));
		}
		return result;
	}

	public static void setItemEffects(Item item, AppliedPotion... effects) {
		item.modify(stack -> {
			PotionContents current = stack.get(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
			List<CustomPotionEffect> customs = new ArrayList<>(effects.length);
			for (AppliedPotion effect : effects) {
				customs.add(effect.toCustomPotionEffect());
			}
			PotionContents updated = new PotionContents(current.potion(), current.customColor(), customs, current.customName());
			return stack.with(DataComponents.POTION_CONTENTS, updated);
		}, true);
	}

	public static void addItemEffects(Item item, AppliedPotion... effects) {
		item.modify(stack -> {
			PotionContents current = stack.get(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
			List<CustomPotionEffect> customs = new ArrayList<>(current.customEffects());
			for (AppliedPotion effect : effects) {
				customs.add(effect.toCustomPotionEffect());
			}
			PotionContents updated = new PotionContents(current.potion(), current.customColor(), customs, current.customName());
			return stack.with(DataComponents.POTION_CONTENTS, updated);
		}, true);
	}

	public static void removeItemEffects(Item item, AppliedPotion... effects) {
		item.modify(stack -> {
			PotionContents current = stack.get(DataComponents.POTION_CONTENTS);
			if (current == null) return stack;
			List<CustomPotionEffect> customs = new ArrayList<>(current.customEffects());
			customs.removeIf(custom -> {
				for (AppliedPotion effect : effects) {
					if (custom.id() == effect.type()
							&& (effect.amplifier() < 0 || custom.settings().amplifier() == effect.amplifier())) {
						return true;
					}
				}
				return false;
			});
			PotionContents updated = new PotionContents(current.potion(), current.customColor(), customs, current.customName());
			return stack.with(DataComponents.POTION_CONTENTS, updated);
		}, true);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AppliedPotion that)) return false;
		return amplifier == that.amplifier
				&& duration == that.duration
				&& ambient == that.ambient
				&& particles == that.particles
				&& icon == that.icon
				&& Objects.equals(type, that.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, amplifier, duration, ambient, particles, icon);
	}
}
