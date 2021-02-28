package deus_proto;

import java.util.ArrayList;
import java.util.List;

import mybaits.vo.ClientInfo;
import mybaits.vo.CovariatesInfoParam;
import mybaits.vo.MemberHistInfo;
import mybaits.vo.PeriodCovariatesInfo;
import mybatis.dao.ProjectEnrolledHistInfoDAO;

public class GetProjectSurvData implements GetSurvData {

	// 退プロ(客都合)
	private final static String culcTargetCode = DeusConst.CT0201;

	// デフォルト三か月(四半期)
	private int periodMonths = 3;

	public List<PeriodCovariatesInfo> getData(List<String> targetCodeList) {

		CovariatesInfoParam param = new CovariatesInfoParam();
		param.setClientList(new ArrayList<ClientInfo>());
		param.setMemberList(new ArrayList<MemberHistInfo>());

		for (String code :targetCodeList) {
			if (code.startsWith(DeusConst.CLIENT_PREFIX)) {
				ClientInfo info = new ClientInfo();
				info.setClientId(code);
				param.getClientList().add(info);
			} else if (code.startsWith(DeusConst.MEMBER_PREFIX)) {
				 MemberHistInfo minfo = new MemberHistInfo();
				 minfo.setMemberId(code);
			}
		}

		ProjectEnrolledHistInfoDAO dao = new ProjectEnrolledHistInfoDAO();
		// 何か年か？
		return dao.selectProjectEnrolledHistQuarterCovariatesInfoMap(param);

	}

	public String getCulcTargetCode() {
		return culcTargetCode;
	}

	public int getPeriodMonths() {
		return periodMonths;
	}

	public void setPeriodMonths(int periodMonths) {
		this.periodMonths = periodMonths;

	}

	public int selectMaxLastPeriod() {
		ProjectEnrolledHistInfoDAO dao = new ProjectEnrolledHistInfoDAO();
		CovariatesInfoParam param = new CovariatesInfoParam();
		param.setPeriodMonths(periodMonths);
		return dao.selectMaxLastPeriod(param);
	}


}
