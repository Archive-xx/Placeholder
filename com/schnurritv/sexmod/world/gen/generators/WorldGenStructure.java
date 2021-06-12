package com.schnurritv.sexmod.world.gen.generators;

import com.schnurritv.sexmod.util.interfaces.IStructure;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;

public class WorldGenStructure extends WorldGenerator implements IStructure {
   public static String structureName;

   public WorldGenStructure(String name) {
      structureName = name;
   }

   public static void generateStructure(World world, BlockPos pos) {
      MinecraftServer mcServer = world.func_73046_m();
      TemplateManager manager = worldServer.func_184163_y();
      ResourceLocation location = new ResourceLocation("sexmod", structureName);
      Template template = manager.func_189942_b(mcServer, location);
      if (template != null) {
         IBlockState state = world.func_180495_p(pos);
         world.func_184138_a(pos, state, state, 3);
         template.func_186253_b(world, pos, settings);
      }

   }

   public boolean func_180709_b(World worldIn, Random rand, BlockPos position) {
      generateStructure(worldIn, position);
      return false;
   }
}
