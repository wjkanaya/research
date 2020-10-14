package sim;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import deus.enums.ProjectStatus;
import deus.enums.StopType;
import deus_proto.Member;
import mybaits.vo.ClientInfo;
import mybaits.vo.MemberHistInfo;
import mybaits.vo.PriceTransitionInfo;
import mybaits.vo.ProjectInfo;
import mybatis.dao.ClientInfoDAO;
import mybatis.dao.MemberHistInfoDAO;
import mybatis.dao.PriceTransitionInfoDAO;
import mybatis.dao.ProjectEnrolledHistInfoDAO;
import mybatis.dao.ProjectInfoDAO;
import mybatis.dao.ProjectMemberNumInfoDAO;

public class DoProcess {
	static Logger logger = LogManager.getLogger(DoProcess.class);

	// 計算期間
	private static final int KIKAN  = 360;

	private void execute() {


		SimCalendar simCal = new SimCalendar(KIKAN);

		MemberHistInfoDAO memberHIstInfoDAO = new MemberHistInfoDAO();
		memberHIstInfoDAO.trancateMemberHistInfo();

		// 顧客情報
		ClientInfoDAO clientInfoDAO = new ClientInfoDAO();
		clientInfoDAO.truncateClientInfo();

		// プロジェクト情報
		ProjectInfoDAO projectInfoDAO = new ProjectInfoDAO();
		projectInfoDAO.truncateProjectInfo();

		// プロジェクト人数推移情報
		ProjectMemberNumInfoDAO projectMemberNumInfoDAO = new ProjectMemberNumInfoDAO();
		projectMemberNumInfoDAO.truncateProjectMemberNumInfo();

		// プロジェクト在籍履歴情報
		ProjectEnrolledHistInfoDAO projectEnrolledHistInfoDAO = new ProjectEnrolledHistInfoDAO();
		projectEnrolledHistInfoDAO.truncateProjectEnrolledHistInfo();

		// 単金推移情報
		PriceTransitionInfoDAO priceTransitionDAO = new PriceTransitionInfoDAO();
		priceTransitionDAO.truncatePriceTransitionInfo();

		// 社員リスト
		//List<Set<Member>> memberList = new ArrayList<Set<Member>>();
		MemberKanri memKanri = new MemberKanri();

		// 空き要員リスト
		AkiPool akiPool = new AkiPool();

		// 参画予定管理クラス
		SankakuRidatuYoteiKanri yoteikanri = new SankakuRidatuYoteiKanri(akiPool);

		//引き合プロジェクトプール
		ProPool proPool = new ProPool();

		// 営業管理
		EigyouKanri eigyouKanri = new EigyouKanri();

		// 取引あり顧客リスト
		Set<Kyaku> toriKyakuList = new TreeSet<Kyaku>();

		// ハザード計算
		MakeHazad mh = new MakeHazad();


		// TODO 自動生成されたメソッド・スタブ
		// 名前カウント
		int namecnt = 0;

		// 新人の人数
		int freshnum = 5;

		int jinkenhi = 52;

		SimRandom random = new SimRandom(1234);


		int jiki = 0;
		GeneSyuha syuha= new GeneSyuha();

		int nenUri = 0; // 年間売上

		while (jiki <= KIKAN){
			logger.debug("時期：" + jiki + "人数:" + memKanri.getAllCnt());

			int jikiUri = 0;

			// 売上を確認しよう。
			for (Project pro : eigyouKanri.eigyoutyuProList) {
				if (pro.memberSet.size() > 0) {
					int proUriSum = 0;
					for (Member mem :pro.memberSet) {

						int uri = pro.tankin;
						if (jiki -mem.entT <= 12) {
							uri = pro.tankin / 2;
						}
						logger.debug(mem.name + "売上:" + uri);

						String enrolledHistId =
								projectEnrolledHistInfoDAO.selectProjectEnrolledHistId(mem.memberId, pro.name);

						int count  = priceTransitionDAO.selectCountPriceTransitionInfo(enrolledHistId);

						boolean doInsert = true;
						if (count > 0) {

							logger.debug("hosi:" + enrolledHistId);
							int price  = priceTransitionDAO.selectNowPrice(enrolledHistId);
							if (price == uri) {
								doInsert = false;
							}
						}

						if (doInsert) {
							PriceTransitionInfo priceTransitionInfo = new PriceTransitionInfo();

							priceTransitionInfo.setMemberId(mem.memberId);
							priceTransitionInfo.setMenberMonths(jiki - mem.entT-1);
							priceTransitionInfo.setEnrolledHistId(enrolledHistId);
							priceTransitionInfo.setPriceStartDate(simCal.getJikiDate(jiki-1));
							priceTransitionInfo.setPrice(uri);

							priceTransitionDAO.insertPriceTransitionInfo(priceTransitionInfo);
						}


//						String memberId;
//						Integer menberMonths;
//						String enrolledHistId;
//						Date priceStartDate;
//						Integer price;

						proUriSum += uri;
					}
					logger.debug(pro.name + "売上:" + proUriSum);
					jikiUri += proUriSum;
				}
			}
			logger.debug("時期:" + jiki + " 売上:" + jikiUri);

			nenUri += jikiUri;

			if (jiki % 12 == 0) {

				logger.debug(jiki / 12 + "年度売上:" + nenUri);

				int allCount = memKanri.getAllCnt() ;
				int allCountKanri = allCount;
//				if (allCount > 20) {
//					allCountKanri = (int)Math.ceil(memKanri.getAllCnt() * (1 + 1.0/30.0)) ;
//				}
				int rieki  = nenUri - (int)Math.floor(memKanri.getAllCnt() * 1.1) * jinkenhi * 12;
				logger.debug(jiki / 12 + "利益:" + rieki);

				freshnum = (int)Math.floor( rieki * 0.97 / (jinkenhi  * 12));

				if (freshnum <= 0) {
					freshnum = 1;
				}

				nenUri = 0;
			}

			Set<Member> memberSet = new TreeSet<Member>();
			GeneFreshMembers gene = new GeneFreshMembers();
			// -- 一番最初
			// とりあえず新人を雇おう

			namecnt = gene.getFreshMembers(memberSet,random, freshnum, namecnt, jiki);

			// 社員リストに格納しよう。
			memKanri.memberList.add(memberSet);
			List<MemberHistInfo> infoList = new ArrayList<MemberHistInfo>();
			for (Member mem :memberSet) {
				MemberHistInfo inf = new MemberHistInfo();
				inf.setMemberId(mem.memberId);
				inf.setStartDate(simCal.getJikiDate(jiki));
				inf.setName(mem.name);
				inf.setEnterDate(simCal.getJikiDate(jiki));
				inf.setEnterOld(20);
				inf.setStatus(0);
				inf.setSex(mem.sex);
//				if (mem.retire == 1) {
//					inf.setStatus(1);
//					calendar.setTime(day1);
//					calendar.add(Calendar.MONTH, mem.retT);
//					inf.setRetirementDate(calendar.getTime());
//					inf.setRetirementType(0);
//				}
				infoList.add(inf);
			}

			if (infoList.size() > 0) {
				memberHIstInfoDAO.insertManyMemberHistInfo(infoList);
			}
			// memberHIstInfoDAO.

			// 空き社員リストに格納しよう。

			Date nowDate = simCal.getJikiDate(jiki);
			for (Member m :memberSet) {

				AkiMember akimem = new AkiMember();

				akimem.member = m;
				akimem.itukara = jiki;

				akiPool.setAkiMember(akimem, 0);
			}

			// プロジェクトが終わるか確認しよう

			for (Project pro :eigyouKanri.eigyoutyuProList){

				// 参画& まだ終了じゃない
				if (pro.memberSet.size() > 0 && pro.endJiki < 0) {

					double proh =  mh.culcprohzard(pro);
					if (random.nextDouble() < proh) { // プロジェクト終了する確率
						int ituowaru =  MakePoasonRandom.getPoisson(random,
								(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 12));
						logger.debug("プロジェクト終了!! ：" + pro.name + ":時期=" + (jiki + ituowaru));

						projectInfoDAO.updateProjectInfo(pro.name, ProjectStatus.KAN.getInteger(), simCal.getJikiDate(jiki + ituowaru));

						pro.endJiki = jiki + ituowaru;

						// プロジェクトの予定管理の参画内定 or 予定をリセット
						List<AkiMember> akiMemberList =  yoteikanri.torikesiSankakuNaiteiForPro(pro);

						for (AkiMember akimem :akiMemberList) {

							int soutaiItukara = akimem.itukara - jiki;
							if (soutaiItukara < 0) {
								akiPool.setAkiMember(akimem, 0);
							} else {
								akiPool.setAkiMember(akimem, soutaiItukara);
							}
						}

						// 引き合いプールからも削除
						proPool.deleteTransaction(pro);

						for (Member mem :pro.memberSet) {

							AkiMember akiMember = new AkiMember();
							akiMember.member = mem;
							akiMember.itukara = jiki + ituowaru;

							if (!yoteikanri.containLNYotei(pro, akiMember)){
								yoteikanri.lnyoteiHimozukiNow(akiMember,pro,ituowaru,StopType.KOKYAKU, false);
							}
						}
					}
				}
			}



			// プロジェクトの人数増減
			List<Transaction> list = eigyouKanri.getTransaction(jiki);
			List<Transaction> addList = new ArrayList<Transaction>();
			for (Transaction tran :list) {
				if (tran.genFlg) {
					Util.gensyoMember(random, yoteikanri, tran , simCal);
				} else {
					addList.add(tran);

				}
			}
			proPool.setProjectList(addList, jiki);


			// 個人的にプロジェクト辞めたい
			for (Project pro :eigyouKanri.eigyoutyuProList){

				// 参画中
				if (pro.memberSet.size() > 0) {
					for (Member mem :pro.memberSet){

						AkiMember akiMember = new AkiMember();
						akiMember.member = mem;

						if (!yoteikanri.containLNYotei(pro, akiMember)){
							double h = mh.culcTaiProhazard(mem, pro);
							if (random.nextDouble() < h) { // プロジェクト終了する確率
								int ituowaru =  MakePoasonRandom.getPoisson(random,
										(double)MakePoasonRandom.senkeiNormalToInto(random.nextGaussian(), 1, 3));
								logger.debug(mem.name + "プロジェクトやめたい!! ：" + pro.name + ":" + (jiki + ituowaru) + "に終了");

								akiMember.itukara = jiki + ituowaru;

								yoteikanri.lnyoteiHimozukiNow(akiMember,pro,ituowaru,StopType.KOJIN, false);
							}
						}
					}
				}
			}

			// とりあえず引き合い情報を確認しよう

			GeneHikiai gen = new GeneHikiai();
			List<Transaction> proList = gen.makeHikiai(random, jiki);

			proPool.setProjectList(proList, jiki);


			// 直近のプロジェクトから一番売上が上がるプロジェクトを充てる
			// 12か月後一番トータルで儲かるのはどこ？
			List<Transaction> yuusenJunList = proPool.moukaruJun(jiki, 6);

			logger.debug("-儲かる順--");
			for (int i = 0; i < 15 ; i++) {
				Transaction tran = yuusenJunList.get(i);
				Project pro = tran.pro;
				int n1 = tran.nannin- tran.juutounin;

				logger.debug(jiki + ":" + pro.name + ":" + tran.getItukara(jiki) + "月後:" + n1 + "人:" +
						pro.tankin +":" + (6 - tran.getItukara(jiki)) *  pro.tankin);
			}

			// 一番儲かる順に空き要員を入れる
			for (Transaction tran : yuusenJunList) {

				Project pro = tran.pro;

				// 開始時期人数
				List<AkiMember> akiList = akiPool.getList(tran.getItukara(jiki));



				for (int i = 0 ; i < akiList.size(); i++) {
					if (tran.nannin - tran.juutounin == 0) {
						break;
					}

					// 参画内定
					logger.debug(tran.jiki + tran.nannkagetugo +"に"+ akiList.get(i).member.name+ "を"+ pro.name + "に参画予定");
					yoteikanri.snyoteiHimozukiNow(akiList.get(i), pro, tran.jiki + tran.nannkagetugo - jiki);
					tran.juutounin += 1;

					// 営業中リスト
					if (!eigyouKanri.eigyoutyuProList.contains(pro)) {

						// 開始時期確定
						pro.startJiki = tran.jiki + tran.nannkagetugo;

						ProjectInfo proInfo = new ProjectInfo();
						proInfo.setProjectId(pro.name);
						proInfo.setProjectName(pro.name);

						proInfo.setClientId(pro.kyaku.kyakuCd);
						proInfo.setStartDate(simCal.getJikiDate(tran.jiki + tran.nannkagetugo));
						proInfo.setProjectStatus(ProjectStatus.TYU.getInteger());
						projectInfoDAO.insertProjectInfo(proInfo);

						eigyouKanri.eigyoutyuProList.add(pro);

					}
					// 取引あり顧客リスト
					if (!toriKyakuList.contains(pro.kyaku)) {

						toriKyakuList.add(pro.kyaku);
						ClientInfo clientInfo = new ClientInfo();
						clientInfo.setClientId(pro.kyaku.kyakuCd);
						clientInfo.setClientName(pro.kyaku.name);
						clientInfoDAO.insertClientInfo(clientInfo);
					}

					// 空き要員から減らす。
					//akiList.remove(i);
					akiPool.delete(akiList.get(i));

				}

			}

			// 一か月経過
			memKanri.inc(random, jiki, eigyouKanri, yoteikanri, mh,memberHIstInfoDAO, simCal, akiPool);
			akiPool.inc();
			yoteikanri.inc(simCal, jiki);
			proPool.inc();


			Util.commitTransaction();

		     SimpleDateFormat sdf = new SimpleDateFormat("yyyy'年'MM'月'");

			logger.debug("時期：" + sdf.format(simCal.getJikiDate(jiki)) +":" + jiki );
			jiki++;
		}


	///	Util.makeMemberHIstInfo(jiki,memKanri.getList());
	}

	public static void main(String[] args) {
		Util.startTransaction();
		try {
		DoProcess process = new DoProcess();
		process.execute();

		} finally {
			Util.endTransaction();;
		}
	}

}
