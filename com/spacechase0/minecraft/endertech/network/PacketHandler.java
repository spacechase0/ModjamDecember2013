package com.spacechase0.minecraft.endertech.network;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class PacketHandler implements IPacketHandler
{
	public PacketHandler()
	{
		NetworkRegistry.instance().registerChannel( this, SyncBlockPacket.getChannel(), Side.CLIENT );
	}
	
	@Override
	public void onPacketData( INetworkManager manager, Packet250CustomPayload packet, Player player )
	{
		if ( packet.channel == SyncBlockPacket.getChannel() )
		{
			SyncBlockPacket.fromPacket( packet ).process( manager, player );
		}
	}

}
