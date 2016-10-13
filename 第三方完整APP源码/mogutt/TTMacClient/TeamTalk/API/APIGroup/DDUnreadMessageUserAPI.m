//
//  DDUserUnreadMessageAPI.m
//  Duoduo
//
//  Created by 独嘉 on 14-5-7.
//  Copyright (c) 2014年 zuoye. All rights reserved.
//

#import "DDUnreadMessageUserAPI.h"

@implementation DDUnreadMessageUserAPI
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
    return MODULE_ID_MESSAGE;
}

/**
 *  请求返回的serviceID
 *
 *  @return 对应的serviceID
 */
- (int)responseServiceID
{
    return MODULE_ID_MESSAGE;
}

/**
 *  请求的commendID
 *
 *  @return 对应的commendID
 */
- (int)requestCommendID
{
    return CMD_MSG_UNREAD_CNT_REQ;
}

/**
 *  请求返回的commendID
 *
 *  @return 对应的commendID
 */
- (int)responseCommendID
{
    return CMD_MSG_UNREAD_CNT_RES;
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
        NSMutableArray* unReadMsgUserIds = [[NSMutableArray alloc] init];
        uint32_t unreadUserCnt = [bodyData readInt];
        for (uint32_t i = 0; i < unreadUserCnt; i++)
        {
            NSString *fromId = [bodyData readUTF];
            /*uint32_t unreadCnt = */[bodyData readInt];
            [unReadMsgUserIds addObject:fromId];
        }
        log4CInfo(@"receive un read msg count:%i",[unReadMsgUserIds count]);
        return unReadMsgUserIds;

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
        DataOutputStream *dataout = [[DataOutputStream alloc] init];
        
        [dataout writeInt:IM_PDU_HEADER_LEN];
        [dataout writeTcpProtocolHeader:MODULE_ID_MESSAGE cId:CMD_MSG_UNREAD_CNT_REQ seqNo:seqNo];
        log4CInfo(@"serviceID:%i cmdID:%i --> get unread msg cnt",MODULE_ID_MESSAGE,CMD_MSG_UNREAD_CNT_REQ);
        return [dataout toByteArray];
    };
    return package;
}
@end
