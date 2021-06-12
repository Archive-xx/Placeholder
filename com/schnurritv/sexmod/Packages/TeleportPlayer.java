package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.util.Reference;
import io.netty.buffer.ByteBuf;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class TeleportPlayer implements IMessage {
   boolean messageValid;
   String playerUUID;
   Vec3d pos;
   float yaw;
   float pitch;

   public TeleportPlayer() {
      this.messageValid = false;
   }

   public TeleportPlayer(String playerUUID, Vec3d pos) {
      this.playerUUID = playerUUID;
      this.pos = pos;
      this.yaw = 0.0F;
      this.pitch = 0.0F;
      this.messageValid = true;
   }

   public TeleportPlayer(String playerUUID, Vec3d pos, float yaw, float pitch) {
      this.playerUUID = playerUUID;
      this.pos = pos;
      this.yaw = yaw;
      this.pitch = pitch;
      this.messageValid = true;
   }

   public TeleportPlayer(String playerUUID, double x, double y, double z, float yaw, float pitch) {
      this.playerUUID = playerUUID;
      this.pos = new Vec3d(x, y, z);
      this.yaw = yaw;
      this.pitch = pitch;
      this.messageValid = true;
   }

   public void fromBytes(ByteBuf buf) {
      this.playerUUID = ByteBufUtils.readUTF8String(buf);
      this.pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
      this.yaw = buf.readFloat();
      this.pitch = buf.readFloat();
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.playerUUID);
      buf.writeDouble(this.pos.field_72450_a);
      buf.writeDouble(this.pos.field_72448_b);
      buf.writeDouble(this.pos.field_72449_c);
      buf.writeFloat(this.yaw);
      buf.writeFloat(this.pitch);
      this.messageValid = true;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(TeleportPlayer message, MessageContext ctx) {
         if (message.messageValid && ctx.side == Side.SERVER) {
            try {
               EntityPlayerMP player = Reference.server.func_184103_al().func_177451_a(UUID.fromString(message.playerUUID));
               player.func_70080_a(message.pos.field_72450_a, message.pos.field_72448_b, message.pos.field_72449_c, message.yaw, message.pitch);
               player.func_70634_a(message.pos.field_72450_a, message.pos.field_72448_b, message.pos.field_72449_c);
               player.func_70016_h(0.0D, 0.0D, 0.0D);
            } catch (NullPointerException var4) {
               System.out.println("couldn't find player with UUID: " + message.playerUUID);
               System.out.println("could only find the following players:");
               System.out.println(Reference.server.func_184103_al().func_181058_b(true));
            }
         } else {
            System.out.println("received an invalid message @TeleportPlayer :(");
         }

         return null;
      }
   }
}
