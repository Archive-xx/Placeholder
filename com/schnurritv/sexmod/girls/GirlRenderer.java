package com.schnurritv.sexmod.girls;

import com.schnurritv.sexmod.util.Reference;
import java.awt.Color;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.core.IAnimatableModel;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.RenderHurtColor;
import software.bernie.shadowed.eliotlash.mclib.utils.Interpolations;

public class GirlRenderer extends GeoEntityRenderer {
   double leashHeightOffset;

   public GirlRenderer(RenderManager renderManager, AnimatedGeoModel model, double leashHeightOffset) {
      super(renderManager, model);
      this.leashHeightOffset = leashHeightOffset;
   }

   public void doRender(GirlEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (entity.func_110167_bD()) {
         this.renderLeash(entity, x, y + this.leashHeightOffset, z, partialTicks);
      }

      if (entity.isForPreloading) {
         GL11.glScalef(0.1F, 0.1F, 0.1F);
      }

      GlStateManager.func_179094_E();
      GlStateManager.func_179137_b(x, y, z);
      GL11.glDisable(2896);
      boolean shouldSit = entity.func_184187_bx() != null && entity.func_184187_bx().shouldRiderSit();
      EntityModelData entityModelData = new EntityModelData();
      entityModelData.isSitting = shouldSit;
      entityModelData.isChild = entity.func_70631_g_();
      float f = Interpolations.lerpYaw(entity.field_70760_ar, entity.field_70761_aq, partialTicks);
      float f1 = Interpolations.lerpYaw(entity.field_70758_at, entity.field_70759_as, partialTicks);
      float netHeadYaw = f1 - f;
      float f3;
      if (shouldSit && entity.func_184187_bx() instanceof EntityLivingBase) {
         EntityLivingBase livingentity = (EntityLivingBase)entity.func_184187_bx();
         f = Interpolations.lerpYaw(livingentity.field_70760_ar, livingentity.field_70761_aq, partialTicks);
         netHeadYaw = f1 - f;
         f3 = MathHelper.func_76142_g(netHeadYaw);
         if (f3 < -85.0F) {
            f3 = -85.0F;
         }

         if (f3 >= 85.0F) {
            f3 = 85.0F;
         }

         f = f1 - f3;
         if (f3 * f3 > 2500.0F) {
            f += f3 * 0.2F;
         }

         netHeadYaw = f1 - f;
      }

      float headPitch = Interpolations.lerp(entity.field_70127_C, entity.field_70125_A, partialTicks);
      f3 = this.handleRotationFloat(entity, partialTicks);
      this.applyRotations(entity, f3, f, partialTicks);
      float limbSwingAmount = 0.0F;
      float limbSwing = 0.0F;
      if (!shouldSit && entity.func_70089_S()) {
         limbSwingAmount = Interpolations.lerp(entity.field_184618_aE, entity.field_70721_aZ, partialTicks);
         limbSwing = entity.field_184619_aG - entity.field_70721_aZ * (1.0F - partialTicks);
         if (entity.func_70631_g_()) {
            limbSwing *= 3.0F;
         }

         if (limbSwingAmount > 1.0F) {
            limbSwingAmount = 1.0F;
         }
      }

      entityModelData.headPitch = -headPitch;
      entityModelData.netHeadYaw = -netHeadYaw;
      AnimationEvent predicate = new AnimationEvent(entity, limbSwing, limbSwingAmount, partialTicks, limbSwingAmount <= -0.15F || limbSwingAmount >= 0.15F, Collections.singletonList(entityModelData));
      GeoModelProvider modelProvider = super.getGeoModelProvider();
      ResourceLocation location = modelProvider.getModelLocation(entity);
      GeoModel model = modelProvider.getModel(location);
      if (modelProvider instanceof IAnimatableModel) {
         ((IAnimatableModel)modelProvider).setLivingAnimations(entity, this.getUniqueID(entity), predicate);
      }

      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b(0.0F, 0.01F, 0.0F);
      Minecraft.func_71410_x().field_71446_o.func_110577_a(this.getEntityTexture(entity));
      Color renderColor = this.getRenderColor(entity, partialTicks);
      boolean flag = this.setDoRenderBrightness(entity, partialTicks);
      this.render(model, entity, partialTicks, (float)renderColor.getRed() / 255.0F, (float)renderColor.getBlue() / 255.0F, (float)renderColor.getGreen() / 255.0F, (float)renderColor.getAlpha() / 255.0F);
      if (flag) {
         RenderHurtColor.unset();
      }

      GL11.glEnable(2896);
      GlStateManager.func_179121_F();
      GlStateManager.func_179121_F();
   }

   protected void renderLeash(GirlEntity entityLivingIn, double x, double y, double z, float partialTicks) {
      Entity entity = entityLivingIn.func_110166_bE();
      if (entity != null) {
         y -= (1.6D - (double)entityLivingIn.field_70131_O) * 0.5D;
         Tessellator tessellator = Tessellator.func_178181_a();
         BufferBuilder bufferbuilder = tessellator.func_178180_c();
         double d0 = Reference.Lerp((double)entity.field_70126_B, (double)entity.field_70177_z, (double)(partialTicks * 0.5F)) * 0.01745329238474369D;
         double d1 = Reference.Lerp((double)entity.field_70127_C, (double)entity.field_70125_A, (double)(partialTicks * 0.5F)) * 0.01745329238474369D;
         double d2 = Math.cos(d0);
         double d3 = Math.sin(d0);
         double d4 = Math.sin(d1);
         if (entity instanceof EntityHanging) {
            d2 = 0.0D;
            d3 = 0.0D;
            d4 = -1.0D;
         }

         double d5 = Math.cos(d1);
         double d6 = Reference.Lerp(entity.field_70169_q, entity.field_70165_t, (double)partialTicks) - d2 * 0.7D - d3 * 0.5D * d5;
         double d7 = Reference.Lerp(entity.field_70167_r + (double)entity.func_70047_e() * 0.7D, entity.field_70163_u + (double)entity.func_70047_e() * 0.7D, (double)partialTicks) - d4 * 0.5D - 0.25D;
         double d8 = Reference.Lerp(entity.field_70166_s, entity.field_70161_v, (double)partialTicks) - d3 * 0.7D + d2 * 0.5D * d5;
         double d9 = Reference.Lerp((double)entityLivingIn.field_70760_ar, (double)entityLivingIn.field_70761_aq, (double)partialTicks) * 0.01745329238474369D + 1.5707963267948966D;
         d2 = Math.cos(d9) * (double)entityLivingIn.field_70130_N * 0.4D;
         d3 = Math.sin(d9) * (double)entityLivingIn.field_70130_N * 0.4D;
         double d10 = Reference.Lerp(entityLivingIn.field_70169_q, entityLivingIn.field_70165_t, (double)partialTicks) + d2;
         double d11 = Reference.Lerp(entityLivingIn.field_70167_r, entityLivingIn.field_70163_u, (double)partialTicks);
         double d12 = Reference.Lerp(entityLivingIn.field_70166_s, entityLivingIn.field_70161_v, (double)partialTicks) + d3;
         x += d2;
         z += d3;
         double d13 = (double)((float)(d6 - d10));
         double d14 = (double)((float)(d7 - d11));
         double d15 = (double)((float)(d8 - d12));
         GlStateManager.func_179090_x();
         GlStateManager.func_179140_f();
         GlStateManager.func_179129_p();
         bufferbuilder.func_181668_a(5, DefaultVertexFormats.field_181706_f);

         int k;
         float f4;
         float f5;
         float f6;
         float f7;
         for(k = 0; k <= 24; ++k) {
            f4 = 0.5F;
            f5 = 0.4F;
            f6 = 0.3F;
            if (k % 2 == 0) {
               f4 *= 0.7F;
               f5 *= 0.7F;
               f6 *= 0.7F;
            }

            f7 = (float)k / 24.0F;
            bufferbuilder.func_181662_b(x + d13 * (double)f7 + 0.0D, y + d14 * (double)(f7 * f7 + f7) * 0.5D + (double)((24.0F - (float)k) / 18.0F + 0.125F), z + d15 * (double)f7).func_181666_a(f4, f5, f6, 1.0F).func_181675_d();
            bufferbuilder.func_181662_b(x + d13 * (double)f7 + 0.025D, y + d14 * (double)(f7 * f7 + f7) * 0.5D + (double)((24.0F - (float)k) / 18.0F + 0.125F) + 0.025D, z + d15 * (double)f7).func_181666_a(f4, f5, f6, 1.0F).func_181675_d();
         }

         tessellator.func_78381_a();
         bufferbuilder.func_181668_a(5, DefaultVertexFormats.field_181706_f);

         for(k = 0; k <= 24; ++k) {
            f4 = 0.5F;
            f5 = 0.4F;
            f6 = 0.3F;
            if (k % 2 == 0) {
               f4 *= 0.7F;
               f5 *= 0.7F;
               f6 *= 0.7F;
            }

            f7 = (float)k / 24.0F;
            bufferbuilder.func_181662_b(x + d13 * (double)f7 + 0.0D, y + d14 * (double)(f7 * f7 + f7) * 0.5D + (double)((24.0F - (float)k) / 18.0F + 0.125F) + 0.025D, z + d15 * (double)f7).func_181666_a(f4, f5, f6, 1.0F).func_181675_d();
            bufferbuilder.func_181662_b(x + d13 * (double)f7 + 0.025D, y + d14 * (double)(f7 * f7 + f7) * 0.5D + (double)((24.0F - (float)k) / 18.0F + 0.125F), z + d15 * (double)f7 + 0.025D).func_181666_a(f4, f5, f6, 1.0F).func_181675_d();
         }

         tessellator.func_78381_a();
         GlStateManager.func_179145_e();
         GlStateManager.func_179098_w();
         GlStateManager.func_179089_o();
      }

   }
}
