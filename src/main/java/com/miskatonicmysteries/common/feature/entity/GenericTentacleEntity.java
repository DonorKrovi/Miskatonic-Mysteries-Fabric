package com.miskatonicmysteries.common.feature.entity;

import com.miskatonicmysteries.api.registry.Affiliation;
import com.miskatonicmysteries.common.registry.MMAffiliations;
import java.util.Random;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class GenericTentacleEntity extends TentacleEntity {

	public GenericTentacleEntity(EntityType<? extends GenericTentacleEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public Affiliation getAffiliation(boolean apparent) {
		return MMAffiliations.NONE;
	}

	public static boolean canSpawn(EntityType<GenericTentacleEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		return world.getBlockState(pos.down()).isSolidBlock(world, pos.down());
	}


}
