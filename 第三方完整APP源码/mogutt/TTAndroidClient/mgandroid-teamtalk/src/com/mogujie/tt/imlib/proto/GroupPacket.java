package com.mogujie.tt.imlib.proto;

import com.mogujie.tt.config.ProtocolConstant;
import com.mogujie.tt.config.SysConstant;
import com.mogujie.tt.imlib.IMSession;
import com.mogujie.tt.log.Logger;
import com.mogujie.tt.packet.base.DataBuffer;
import com.mogujie.tt.packet.base.DefaultHeader;
import com.mogujie.tt.packet.base.Header;
import com.mogujie.tt.packet.base.Packet;

import java.util.ArrayList;
import java.util.List;

/**
 * MsgServerPacket:请求(返回)登陆消息服务器 yugui 2014-05-04
 */

public class GroupPacket extends Packet {

	private Logger logger = Logger.getLogger(GroupPacket.class);

	public GroupPacket() {
		setNeedMonitor(true);
	}

	
	public GroupPacket(int sessionType) {
		mRequest = new PacketRequest(sessionType);
		setNeedMonitor(true);
	}

	@Override
	public DataBuffer encode() {

		Header header = mRequest.getHeader();
		DataBuffer headerBuffer = header.encode();
		DataBuffer bodyBuffer = new DataBuffer();

		PacketRequest req = (PacketRequest) mRequest;
		if (null == req)
			return null;

		// bodyBuffer.writeString(req.getUser_id_url());
		// bodyBuffer.writeString(req.getUser_token());
		// bodyBuffer.writeInt(req.getOnline_status());
		// bodyBuffer.writeInt(req.getClient_type());
		// bodyBuffer.writeString(req.getClient_version());

		int headLength = headerBuffer.readableBytes();
		int bodyLength = bodyBuffer.readableBytes();

		DataBuffer buffer = new DataBuffer(headLength + bodyLength);
		buffer.writeDataBuffer(headerBuffer);
		buffer.writeDataBuffer(bodyBuffer);

		return buffer;
	}

	@Override
	public void decode(DataBuffer buffer) {

		if (null == buffer)
			return;
		try {
			PacketResponse res = new PacketResponse();

			Header header = new Header();
			header.decode(buffer);
			res.setHeader(header);

			// starts filling from here
			int cnt = buffer.readInt();
			logger.d("group#group cnt:%d", cnt);
			
			int prevType = 0;
			for (int i = 0; i < cnt; ++i) {
				GroupEntity entity = new GroupEntity();

				/*
				 * String id; String name; String avatar; String creatorId; int
				 * type; // 1--normal group, 2--temporary group int updated;
				 * List<String> memberIdList;
				 */
				entity.id = buffer.readString();
				entity.name = buffer.readString();
				logger.d("group#name:%s", entity.name);

				entity.avatarUrl = buffer.readString();
				entity.creatorId = buffer.readString();
				entity.type = buffer.readInt();
								
				if (header.getCommandId() == ProtocolConstant.CID_GROUP_DIALOG_LIST_RESPONSE) {
					entity.updated = buffer.readInt();
				} else {
					entity.updated = 0;
				}
				
				logger.d("group#type:%d", entity.type);
				

				int memberListCnt = buffer.readInt();
				logger.d("group#member cnt:%d", memberListCnt);

				for (int j = 0; j < memberListCnt; ++j) {
					String id = buffer.readString();
					entity.memberIdList.add(id);
				}

				res.entityList.add(entity);
			}

			mResponse = res;
		} catch (Exception e) {
			Object tt[]=new Object[]{e.getStackTrace()};
			logger.e("group#exception:%s", tt);
			logger.e(e.getMessage());
		}

	}

	public static class PacketRequest extends Request {
		public PacketRequest(int groupType) {

			int cid = (groupType == IMSession.SESSION_GROUP) ? ProtocolConstant.CID_GROUP_LIST_REQUEST
					: ProtocolConstant.CID_GROUP_DIALOG_LIST_REQUEST;
			Header header = new DefaultHeader(ProtocolConstant.SID_GROUP,
					cid);

			int contentLength = 0;
			header.setLength(SysConstant.PROTOCOL_HEADER_LENGTH + contentLength);

			setHeader(header);
		}
	}

	public static class PacketResponse extends Response {

		public List<GroupEntity> entityList = new ArrayList<GroupEntity>();
	}
}
