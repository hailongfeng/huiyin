//
//  DDDDGroupsUnreadMessageAPI.m
//  Duoduo
//
//  Created by 独嘉 on 14-5-7.
//  Copyright (c) 2014年 zuoye. All rights reserved.
//

#import "DDGroupsUnreadMessageAPI.h"
#import "MessageEntity.h"
#import "GroupEntity.h"
@implementation DDGroupsUnreadMessageAPI
/**
 *  请求超时时间
 *
 *  @return 超时时间
 */
- (int)requestTimeOutTimeInterval
{
    return 8;
}

/**
 *  请求的serviceID
 *
 *  @return 对应的serviceID
 */
- (int)requestServiceID
{
    return MODULE_ID_GROUP;
}

/**
 *  请求返回的serviceID
 *
 *  @return 对应的serviceID
 */
- (int)responseServiceID
{
    return MODULE_ID_GROUP;
}

/**
 *  请求的commendID
 *
 *  @return 对应的commendID
 */
- (int)requestCommendID
{
    return CMD_ID_GROUP_UNREAD_MSG_REQ;
}

/**
 *  请求返回的commendID
 *
 *  @return 对应的commendID
 */
- (int)responseCommendID
{
    return CMD_ID_GROUP_UNREAD_MSG_RES;
}

/**
 *  解析数据的block
 *
 *  @return 解析数据的block
 */
- (Analysis)analysisReturnData
{
    Analysis analysis = (id)^(NSData* data)
    {
        DataInputStream* bodyData = [DataInputStream dataInputStreamWithData:data];
        NSMutableDictionary* msgDict = [[NSMutableDictionary alloc] init];
        NSMutableArray* msgArray = [[NSMutableArray alloc] init];
        
        NSString *groupId = [bodyData readUTF];
        uint32_t msgCnt = [bodyData readInt];
        for (uint32_t i = 0; i < msgCnt; i++)
        {
            NSString *fromUserId = [bodyData readUTF];
            uint32_t createTime = [bodyData readInt];
            NSString *msgContent = [bodyData readUTF];
            
            MessageEntity *msg = [[MessageEntity alloc ] init];
            msg.msgTime = createTime;
            msg.msgType = MESSAGE_TYPE_GROUP;
            msg.msgContent = msgContent;
            msg.sessionId = groupId;
            msg.senderId = fromUserId;
            
            [msgArray addObject:msg];
            log4CInfo(@"receive group msg content:%@",msgContent);
        }
        [msgDict setObject:[GroupEntity getSessionId:groupId] forKey:@"sessionId"];
        [msgDict setObject:msgArray forKey:@"msg"];
        return msgDict;
    };
    return analysis;
}

/**
 *  打包数据的block
 *
 *  @return 打包数据的block
 */
- (Package)packageRequestObject
{
    Package package = (id)^(id object,uint32_t seqNo)
    {
        NSString* groupId = (NSString*)object;
        DataOutputStream *dataout = [[DataOutputStream alloc] init];
        uint32_t totalLen = IM_PDU_HEADER_LEN + 4 + strLen(groupId);
        
        [dataout writeInt:totalLen];
        [dataout writeTcpProtocolHeader:MODULE_ID_GROUP cId:CMD_ID_GROUP_UNREAD_MSG_REQ seqNo:seqNo];

        [dataout writeUTF:groupId];
        log4CInfo(@"serviceID:%i cmdID:%i --> get group message groupID:%@",MODULE_ID_GROUP,CMD_ID_GROUP_UNREAD_MSG_REQ,groupId);
        return [dataout toByteArray];
    };
    return package;
}
@end
