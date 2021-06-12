package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.events.HandlePlayerMovement;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SetPlayerMovement implements IMessage {
   boolean messageValid;
   boolean setActive;

   public SetPlayerMovement(boolean setActive) {
      this.setActive = setActive;
      this.messageValid = true;
   }

   public SetPlayerMovement() {
      this.messageValid = false;
   }

   public void fromBytes(ByteBuf buf) {
      this.setActive = buf.readBoolean();
      this.messageValid = true;
   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(this.setActive);
      this.messageValid = true;
   }

   public static class Handler implements IMessageHandler {
      public IMessage onMessage(SetPlayerMovement message, MessageContext ctx) {
         if (message.messageValid && ctx.side == Side.CLIENT) {
            HandlePlayerMovement.active = message.setActive;
            Minecraft.func_71410_x().field_71439_g.func_70016_h(0.0D, 0.0D, 0.0D);
         } else {
            System.out.println("received an invalid message @SetPlayerMovement :(");
         }

         return null;
      }
   }
}
