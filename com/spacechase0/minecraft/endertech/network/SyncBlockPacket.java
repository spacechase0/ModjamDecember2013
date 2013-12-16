package com.spacechase0.minecraft.endertech.network;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.spacechase0.minecraft.endertech.entity.VehicleEntity;

import cpw.mods.fml.common.network.Player;

public class SyncBlockPacket
{
	public SyncBlockPacket( VehicleEntity entity, int theX, int theY, int theZ )
	{
		entityId = entity.entityId;
		x = theX;
		y = theY;
		z = theZ;
		
		id = ( short ) entity.getFakeWorld().getBlockId( x, y, z );
		meta = ( byte ) entity.getFakeWorld().getBlockMetadata( x, y, z );
	}
	
	public SyncBlockPacket( int theEntityId, int theX, int theY, int theZ, short theId, byte theMeta )
	{
		entityId = theEntityId;
		x = theX;
		y = theY;
		z = theZ;
		id = theId;
		meta = theMeta;
	}
	
	public static String getChannel()
	{
		return "SC0_ET|SB";
	}
	
	public Packet250CustomPayload toPacket()
	{
		try
		{
			ByteArrayOutputStream boas = new ByteArrayOutputStream();
			DataOutputStream stream = new DataOutputStream( boas );
			stream.writeInt( entityId );
			stream.writeInt( x );
			stream.writeInt( y );
			stream.writeInt( z );
			stream.writeShort( id );
			stream.writeByte( meta );
			
			return new Packet250CustomPayload( getChannel(), boas.toByteArray() );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
			return null;
		}
	}
	
	public static SyncBlockPacket fromPacket( Packet250CustomPayload packet )
	{
		try
		{
			ByteArrayInputStream boas = new ByteArrayInputStream( packet.data );
			DataInputStream stream = new DataInputStream( boas );
			int entityId = stream.readInt();
			int x = stream.readInt();
			int y = stream.readInt();
			int z = stream.readInt();
			short id = stream.readShort();
			byte meta = stream.readByte();
			
			return new SyncBlockPacket( entityId, x, y, z, id, meta );
		}
		catch ( Exception exception )
		{
			exception.printStackTrace();
			return null;
		}
	}
	
	public void process( INetworkManager manager, Player player )
	{
		EntityPlayer entity = ( EntityPlayer ) player;
		if ( !entity.worldObj.isRemote ) return;

		VehicleEntity vehicle = ( VehicleEntity ) entity.worldObj.getEntityByID( entityId );
		if ( vehicle == null )
		{
			System.out.println( "Null?" );
			return;
		}
		
		vehicle.getFakeWorld().setBlock( x, y, z, ( int ) id, ( int ) meta, 0 );
	}
	
	private int entityId;
	private int x;
	private int y;
	private int z;
	private short id;
	private byte meta;
	// TODO: TE
}
