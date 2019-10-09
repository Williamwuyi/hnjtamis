package cn.com.ite.eap2.module.baseinfo.affiche;

import java.util.HashMap;
import java.util.Map;

import cn.com.ite.eap2.common.utils.StringUtils;
import cn.com.ite.eap2.core.service.DefaultServiceImpl;
import cn.com.ite.eap2.core.spring.SpringContextUtil;
import cn.com.ite.eap2.domain.baseinfo.SysAffiche;

public class AfficheServiceImpl extends DefaultServiceImpl implements AfficheService{
	/**
	 * 保存公告
	 * @param sa
	 * @param excuteId 任务实例ID
	 * @param toCode 导向编码
	 */
	public void saveAffiche(SysAffiche sa,String excuteId,String toCode,String employeeId,String advice) throws Exception{
		//IWorkFlow flowService = (IWorkFlow)SpringContextUtil.getBean("workflowServer");
		String[] states=null;
		if(StringUtils.isEmpty(sa.getSaId())){//增加操作
			this.save(sa);
			Map param = new HashMap();
			//一般至少要加id这个业务参数，标识具体的一次业务对象
			param.put("id",sa.getSaId());
			//参数1为流程编码，参数2为员工ID，参数3为业务参数，参数4一般设置为null
			//states = flowService.startWorkFlow("1", LoginAction.getUserSessionInfo().getEmployeeId(), param, null);
		}else if(!StringUtils.isEmpty(toCode)){//流程操作	
			Map param = new HashMap();//看业务需要，是否要向流程中传递变量
			param.put("s","120");
			//参数1为任务实例ID，一般任务打开时会自动设置此参数
			//参数2为任务导向编码，一般任务在执行自动创建这隐藏变量
			//参数3为当前员工ID，参数4为审核意见，参数5为IP地址
			//states = flowService.next(excuteId, toCode,param, employeeId, advice, ServletContent.getIP(), true);
		}
		//if(states!=null)
			//sa.setAccessoriesItemId(states[0]);
		this.update(sa);
	}
	/**
	 * 删除系统公告
	 * @param sa
	 * @throws Exception
	 * @modified
	 */
	public void deleteAffiche(SysAffiche sa) throws Exception{
		//IWorkFlow flowService = (IWorkFlow)SpringContextUtil.getBean("workflowServer");
		Map param=new HashMap();
		param.put("id", sa.getSaId());
		//if(sa.getAccessoriesItemId()==null||
		//flowService.judgeStartState("1", sa.getAccessoriesItemId(), param)){
		   this.delete(sa);
		  // flowService.deleteFLowData("1", param);
		//}else
			//throw new Exception("此公告已经开始审核，不能删除！");
	}
}
