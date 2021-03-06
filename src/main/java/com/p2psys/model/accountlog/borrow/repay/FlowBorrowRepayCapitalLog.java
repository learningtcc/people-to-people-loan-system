package com.p2psys.model.accountlog.borrow.repay;

import java.util.Date;

import com.p2psys.common.enums.EnumAccountSumProperty;
import com.p2psys.context.Constant;
import com.p2psys.context.Global;
import com.p2psys.domain.Account;
import com.p2psys.domain.AccountSum;
import com.p2psys.domain.AccountSumLog;
import com.p2psys.model.accountlog.BaseBorrowLog;
import com.p2psys.util.DateUtils;
/**
 * 标还款扣除本金log
 * TODO 类说明
 * 
 
 * @version 1.0
 * @since 2013-9-5
 */
public class FlowBorrowRepayCapitalLog extends BaseBorrowLog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1879381929093016631L;
	private String sumLogRemarkTemplate = "还款合计：扣除还款本金${money}元，标ID：[${borrow.id}]";		
	// v1.6.6.2 流转标还款不区分还款方式 zza 2013-11-01 start
	private String templateNid=Constant.REPAID_CAPITAL;
	// v1.6.6.2 流转标还款不区分还款方式 zza 2013-11-01 end
	
	public FlowBorrowRepayCapitalLog() {
		super();
	}
	public FlowBorrowRepayCapitalLog(double money, Account act, long toUser) {
		super(money, act, toUser);
	}
	public FlowBorrowRepayCapitalLog(double money, Account act) {
		super(money, act);
	}
	@Override
	public void addAccountSumLog() {
		AccountSum sum = accountSumDao.getAccountSum(this.getUser_id());
		this.setAccountSumLogRemarkTemplate(sumLogRemarkTemplate);
		AccountSumLog sumLog = this.baseAccountSumLog();
		sumLog.setBefore_money( sum.getRepay_cash());
		double afterMoney = sum.getRepay_cash() + this.getMoney();
		sumLog.setAfter_money(afterMoney);
		accountSumLogDao.addAccountSumLog(sumLog);
	} 

	@Override
	public String getTemplateNid(){
		return templateNid;
	}
	
	@Override
	public void updateAccount() {
		accountDao.updateAccount(-this.getMoney(), -this.getMoney(), 0, 0, this.getUser_id());
	}
	
	@Override
	public void accountSumProperty() {
		this.sumManage();
		accountSumDao.editSumByProperty(EnumAccountSumProperty.REPAY_CASH.getValue(), this.getMoney(), this.getUser_id());
	}
	
}
