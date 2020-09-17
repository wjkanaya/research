package sim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import deus_proto.Member;

public class SankakuRidatuYoteiKanri {

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
    public void lyoteiHimozuki( Member mem, Project pro, int itukara) {

    	SankakuRidatuYotei yotei = new SankakuRidatuYotei();

    	yotei.mem = mem;
        yotei.pro = pro;
        yotei.itukara = itukara;

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
    public void lnyoteiHimozuki(SankakuRidatuYotei yotei) {
    	lnlist.add(yotei);

    	lyoteiSakujyo(yotei);
    	
    	akiPool.setAkiMember(yotei.mem, yotei.itukara);
    	
    }

	// 離脱内定リストに即登録
    public void lnyoteiHimozukiNow( Member mem, Project pro, int itukara) {
    	lyoteiHimozuki( mem, pro, itukara);

    	 for (SankakuRidatuYotei yotei: getLYotei(pro) ) {

    		 if (yotei.mem.equals(mem) && yotei.pro.equals(pro) && yotei.itukara == itukara) {
    			 lnyoteiHimozuki(yotei) ;
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

    public void inc() {

    	// 参画予定リスト
//    	List<SankakuRidatuYotei> slist = new LinkedList<SankakuRidatuYotei>();
        // お流れ
    	for (int i = slist.size() -1  ;i <= 0;i--) {
    		if (slist.get(i).itukara == 1) {
    			slist.remove(i);
    		} else {
    			slist.get(i).itukara -= 1;
    		}
    	}

    	// 参画内定リスト
    	// 参画確定
    	//    	List<SankakuRidatuYotei> snlist = new LinkedList<SankakuRidatuYotei>();
    	for (int i = snlist.size() -1  ;i <= 0;i--) {
    		if (snlist.get(i).itukara == 1) {
    			SankakuRidatuYotei yotei =snlist.remove(i);
    			yotei.pro.memberSet.add(yotei.mem); //

    		} else {
    			snlist.get(i).itukara -= 1;
    		}


    	}




    	// 離脱予定リスト
    	// お流れ
    	//  	List<SankakuRidatuYotei> llist = new LinkedList<SankakuRidatuYotei>();
    	for (int i = llist.size() -1  ;i <= 0;i--) {
    		if (llist.get(i).itukara == 1) {
    			llist.remove(i);
    		} else {
    			llist.get(i).itukara -= 1;
    		}


    	}


    	// 離脱内定リスト
    	// 	List<SankakuRidatuYotei> lnlist = new LinkedList<SankakuRidatuYotei>();
    	for (int i = lnlist.size() -1  ;i <= 0;i--) {
    		if (lnlist.get(i).itukara == 1) {
    			SankakuRidatuYotei yotei =lnlist.remove(i);
    			yotei.pro.memberSet.remove(yotei.mem);


    		} else {
    			lnlist.get(i).itukara -= 1;
    		}


    	}

    }
}
