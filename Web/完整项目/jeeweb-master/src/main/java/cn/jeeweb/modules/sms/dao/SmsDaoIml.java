package cn.jeeweb.modules.sms.dao;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import cn.jeeweb.core.disruptor.sms.SmsDao;
import cn.jeeweb.core.disruptor.sms.SmsData;
import cn.jeeweb.core.utils.StringUtils;
import cn.jeeweb.core.utils.sms.data.SmsResult;
import cn.jeeweb.modules.sms.entity.SmsSendLogEntity;
import cn.jeeweb.modules.sms.entity.SmsTemplateEntity;
import cn.jeeweb.modules.sms.service.ISmsSendLogService;
import cn.jeeweb.modules.sms.service.ISmsTemplateService;

/**
 * 
 * All rights Reserved, Designed By www.jeeweb.cn
 * 
 * @title: SmsDaoIml.java
 * @package cn.jeeweb.modules.sms.dao
 * @description: 短信信息返回
 * @author: 王存见
 * @date: 2017年6月8日 上午11:20:04
 * @version V1.0
 * @copyright: 2017 www.jeeweb.cn Inc. All rights reserved.
 *
 */
public class SmsDaoIml implements SmsDao {
	@Autowired
	private ISmsSendLogService smsSendLogService;
	@Autowired
	private ISmsTemplateService smsTemplateService;

	@Override
	public void doStart() {

	}

	@Override
	public void doSend(String eventId, SmsData smsData) {
		SmsSendLogEntity smsSendLog = new SmsSendLogEntity();
		smsSendLog.setId(eventId);
		String templateId = smsData.getSmsTemplate().getTemplateId();
		try {
			SmsTemplateEntity smsTemplateEntity = smsTemplateService.get("templateId", templateId);
			if (smsTemplateEntity != null) {
				smsSendLog.setBusinessType(smsTemplateEntity.getBusinessType());
				// smsSendLog.setCode(smsTemplateEntity.getCode());
			} else {
				smsSendLog.setBusinessType("99");
			}
		} catch (Exception e) {
			smsSendLog.setBusinessType("99");
		}

		smsSendLog.setTemplateContent(smsData.getSmsTemplate().getTemplateContent());
		smsSendLog.setTemplateId(smsData.getSmsTemplate().getTemplateId());
		smsSendLog.setPhone(smsData.getPhone());
		smsSendLog.setSenddata(StringUtils.join(smsData.getDatas(), ","));
		smsSendLog.setStatus("0");
		smsSendLogService.save(smsSendLog);
	}

	@Override
	public void doResult(String eventId, SmsData smsData, SmsResult smsResult) {
		SmsSendLogEntity smsSendLog = smsSendLogService.get(eventId);
		smsSendLog.setCode(smsResult.getCode());
		smsSendLog.setMsg(smsResult.getMsg());
		smsSendLog.setSmsid(smsResult.getSmsid());
		if (smsResult.isSuccess()) {
			smsSendLog.setStatus("1");
		} else {
			smsSendLog.setStatus("0");
		}
		smsSendLog.setResponseDate(new Date());
		smsSendLogService.merge(smsSendLog);
	}

	@Override
	public void doShutdown() {

	}

}
