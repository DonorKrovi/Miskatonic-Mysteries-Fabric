package com.miskatonicmysteries.mixin.biomes;

import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkSection;

import net.minecraft.world.chunk.ReadableContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChunkSection.class)
public interface ChunkSectionAccessor {

	@Accessor("biomeContainer")
	ReadableContainer<RegistryEntry<Biome>> getBiomeContainer();
}
