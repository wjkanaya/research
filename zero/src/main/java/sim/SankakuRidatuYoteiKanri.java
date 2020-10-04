package sim;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import deus.enums.EnrolledStatus;
import deus.enums.StopType;
import deus_proto.Member;
import mybaits.vo.ProjectEnrolledHistInfo;
import mybaits.vo.ProjectMemberNumInfo;
import mybatis.dao.ProjectEnrolledHistInfoDAO;
import mybatis.dao.ProjectMemberNumInfoDAO;

public class SankakuRidatuYoteiKanri {
	static Logger logger = LogManager.getLogger(SankakuRidatuYoteiKanri.class);

	AkiPool akiPool = null;

	public SankakuRidatuYoteiKanri(AkiPool akiPool) {
		this.akiPool = akiPool;

	}

	// 参画予定リスト
	List<SankakuRidatuYotei> slist = new LinkedList<SankakuRidatuYotei>();

	// 参画内定リスト
	List<SankakuRidatuYotei> snlist = new LinkedList<SankakuRidatuYotei>();

	// 離脱予定リスト
	List<SankakuRidatuYotei> llist = new LinkedList<SankakuRidatuYotei>();

	// 離脱内定リスト
	List<SankakuRidatuYotei> lnlist = new LinkedList<SankakuRidatuYotei>();


	// 参画予定リストに登録
    public void syoteiHimozuki( Member mem, Project pro, int itukara) {

    	SankakuRidatuYotei yotei = new SankakuRidatuYotei();

    	yotei.mem = mem;
        yotei.pro = pro;
        yotei.itukara = itukara;

        slist.add(yotei);
    }

	// 参画予定リストから削除
    public void syoteiSakujyo(SankakuRidatuYotei yotei) {


    	for (int i=0; i<slist.size();i++) {
    		if (slist.get(i).equals(yotei)){
    			slist.remove(i);
    			break;
    		}
    	}
    }

	// 参画内定リストに登録
    public void snyoteiHimozuki(SankakuRidatuYotei yotei) {

    	snlist.add(yotei);

    	syoteiSakujyo(yotei);


    }

	// 参画内定リストに即登録
    public void snyoteiHimozukiNow( Member mem, Project pro, int itukara) {
    	syoteiHimozuki( mem, pro, itukara);

    	 for (SankakuRidatuYotei yotei: getSYotei(pro) ) {

    		 if (yotei.mem.equals(mem) && yotei.pro.equals(pro) && yotei.itukara == itukara) {
    			 snyoteiHimozuki(yotei) ;
    		 }
    	 }
    }



    // 参画予定リストを取得
    public List<SankakuRidatuYotei> getSYotei(Project pro) {

    	List<SankakuRidatuYotei> l = new ArrayList<SankakuRidatuYotei>();

    	for (SankakuRidatuYotei yo : slist) {
    		if (yo.pro.equals(pro)){
    			l.add(yo);
    		}
    	}

        return l;
    }

    // 参画内定リストを取得
    public List<SankakuRidatuYotei> getSNYotei(Project pro) {

    	List<SankakuRidatuYotei> l = new ArrayList<SankakuRidatuYotei>();

    	for (SankakuRidatuYotei yo : snlist) {
    		if (yo.pro.equals(pro)){
    			l.add(yo);
    		}
    	}

        return l;
    }




	// 離脱予定リストに登録
    public void lyoteiHimozuki( Member mem, Project pro, int itukara,StopType stopType) {

    	SankakuRidatuYotei yotei = new SankakuRidatuYotei();

    	yotei.mem = mem;
        yotei.pro = pro;
        yotei.itukara = itukara;
        yotei.stopType = stopType;

        llist.add(yotei);
    }

	// 離脱予定リストから削除
    public void lyoteiSakujyo(SankakuRidatuYotei yotei) {

    	for (int i=0; i<llist.size();i++) {
    		if (llist.get(i).equals(yotei)){
    			llist.remove(i);
    			break;
    		}
    	}
    }



	// 離脱内定リストに登録
    public void lnyoteiHimozuki(SankakuRidatuYotei yotei, boolean taishokuFlg) {

    	for (int i = 0; i < lnlist.size(); i++) {
    		if (lnlist.get(i).mem.equals(yotei.mem) && lnlist.get(i).pro.equals(yotei.pro)) {
    			if (yotei.itukara < lnlist.get(i).itukara) {
    			// 先行して登録
    				lnlist.get(i).itukara = yotei.itukara;


    			}


    			return;
    		}
    	}

    	lnlist.add(yotei);

    	lyoteiSakujyo(yotei);

    	if (!taishokuFlg) {
    		akiPool.setAkiMember(yotei.mem, yotei.itukara);
    	}
    }



	// 離脱内定リストに即登録
    public void lnyoteiHimozukiNow( Member mem, Project pro, int itukara,StopType stopType, boolean taishokuFlg) {
    	lyoteiHimozuki( mem, pro, itukara, stopType);

    	 for (SankakuRidatuYotei yotei: getLYotei(pro) ) {

    		 if (yotei.mem.equals(mem) && yotei.pro.equals(pro) && yotei.itukara == itukara) {
    			 lnyoteiHimozuki(yotei, taishokuFlg) ;
    		 }
    	 }
    }

    // 離脱予定リストを取得
    public List<SankakuRidatuYotei> getLYotei(Project pro) {

    	List<SankakuRidatuYotei> l = new ArrayList<SankakuRidatuYotei>();

    	for (SankakuRidatuYotei yo : llist) {
    		if (yo.pro.equals(pro)){
    			l.add(yo);
    		}
    	}

        return l;
    }

    // 離脱内定リストを取得
    public List<SankakuRidatuYotei> getLNYotei(Project pro) {

    	List<SankakuRidatuYotei> l = new ArrayList<SankakuRidatuYotei>();

    	for (SankakuRidatuYotei yo : lnlist) {
    		if (yo.pro.equals(pro)){
    			l.add(yo);
    		}
    	}

        return l;
    }

    public boolean containLNYotei(Project pro, Member mem) {

    	boolean result = false;
    	for (SankakuRidatuYotei yo : lnlist) {
    		if (yo.pro.equals(pro) && yo.mem.equals(mem)){
    			result = true;
    			break;
    		}
    	}
    	return result;
    }


    public void inc(SimCalendar simCal,int jiki) {

		// プロジェクト在籍履歴情報
		ProjectEnrolledHistInfoDAO projectEnrolledHistInfoDAO = new ProjectEnrolledHistInfoDAO();

    	// 人数可変したプロジェクト
    	Set<Project> kahenSet = new HashSet<Project>();

    	// 参画予定リスト
//    	List<SankakuRidatuYotei> slist = new LinkedList<SankakuRidatuYotei>();
        // お流れ
    	for (int i = slist.size() -1  ;i >= 0;i--) {
    		if (slist.get(i).itukara == 0) {
    			slist.remove(i);
    		} else {
    			slist.get(i).itukara -= 1;
    		}
    	}

    	// 参画内定リスト
    	// 参画確定
    	//    	List<SankakuRidatuYotei> snlist = new LinkedList<SankakuRidatuYotei>();
    	for (int i = snlist.size() -1  ;i >= 0;i--) {
    		if (snlist.get(i).itukara == 0) {
    			SankakuRidatuYotei yotei =snlist.remove(i);
    			yotei.pro.memberSet.add(yotei.mem); //
    			Integer count =
    					projectEnrolledHistInfoDAO.selectCountProjectEnrolledHistInfo(yotei.mem.memberId, yotei.pro.name);

    			int branchNum = 0;
    			if (count > 0) {
    				branchNum = projectEnrolledHistInfoDAO.selectProjectEnrolledHistInfoBranchNum(yotei.mem.memberId, yotei.pro.name);
    				branchNum++;
    			}

				ProjectEnrolledHistInfo info  = new ProjectEnrolledHistInfo();

				info.setEnrolledHistId(yotei.mem.memberId + "_" + yotei.pro.name + "_" + branchNum);
				info.setMemberId(yotei.mem.memberId);
				info.setProjectId( yotei.pro.name);
				info.setBranchNum(Integer.valueOf(branchNum));
				info.setJoinDate(simCal.getJikiDate(jiki));
				info.setEnrolledStatus(EnrolledStatus.TYU.getInteger());

				projectEnrolledHistInfoDAO.insertProjectEnrolledHistInfo(info);


    			logger.debug(yotei.mem.name + "君が"+ yotei.pro.name + "に参画しました。");

    			if (!kahenSet.contains(yotei.pro)) {
    				kahenSet.add(yotei.pro);
    			}

    		} else {
    			snlist.get(i).itukara -= 1;
    		}


    	}




    	// 離脱予定リスト
    	// お流れ
    	//  	List<SankakuRidatuYotei> llist = new LinkedList<SankakuRidatuYotei>();
    	for (int i = llist.size() -1  ;i >= 0;i--) {
    		if (llist.get(i).itukara == 0) {
    			llist.remove(i);
    		} else {
    			llist.get(i).itukara -= 1;
    		}
    	}

    	// 離脱内定リスト
    	// 	List<SankakuRidatuYotei> lnlist = new LinkedList<SankakuRidatuYotei>();
    	for (int i = lnlist.size() -1  ;i >= 0;i--) {
    		if (lnlist.get(i).itukara == 0) {
    			SankakuRidatuYotei yotei =lnlist.remove(i);
    			yotei.pro.memberSet.remove(yotei.mem);

				projectEnrolledHistInfoDAO.updateProjectEnrolledHistInfo(
					yotei.mem.memberId, yotei.pro.name, simCal.getJikiDate(jiki),
					yotei.stopType.getInteger(), EnrolledStatus.DAT.getInteger());

    			logger.debug(yotei.mem.name + "君が"+ yotei.pro.name + "から離脱しました。");
       			if (!kahenSet.contains(yotei.pro)) {
    				kahenSet.add(yotei.pro);
    			}

    		} else {
    			lnlist.get(i).itukara -= 1;
    		}


    	}

    	if (kahenSet.size() > 0) {
    		ProjectMemberNumInfoDAO projectMemberNumInfoDAO = new ProjectMemberNumInfoDAO();
    		for (Project kahenPro :kahenSet) {

    			// レコード数
    			int recCount = projectMemberNumInfoDAO.selectCountProjectMemberNumInfo(kahenPro.name);

    			if (recCount > 0) {
    				ProjectMemberNumInfo projectMemberNumInfo
    				    = projectMemberNumInfoDAO.selectLastProjectMemberNumInfo(kahenPro.name);

    				if (projectMemberNumInfo.getMemberNum().intValue() == kahenPro.memberSet.size()) {
    					continue;
    				}
    			}

    			ProjectMemberNumInfo projectMemberNumInfo = new ProjectMemberNumInfo();
    			projectMemberNumInfo.setProjectId(kahenPro.name);
    			projectMemberNumInfo.setProjectMonths(jiki - kahenPro.startJiki);
    			projectMemberNumInfo.setMemberNum(Integer.valueOf(kahenPro.memberSet.size()));
    			projectMemberNumInfoDAO.insertProjectMemberNumInfo(projectMemberNumInfo);
    		}
    	}
    }
}
