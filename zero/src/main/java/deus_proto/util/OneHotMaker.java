package deus_proto.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import deus_proto.DeusConst;
import mybaits.vo.ConvariateData;
import mybaits.vo.CovariatesMst;
import mybatis.dao.CovariatesMstDAO;

public class OneHotMaker {

	//		共変量種別	covariates_type	smallint
	static Map<String, Integer> convariatesTypeMap = new  HashMap<String, Integer>();
	//		範囲開始	range_start	decimal
	static Map<String, Integer> rangeStartMap = new HashMap<String, Integer>();
	//		範囲終了	range_end	decimal
	static Map<String, Integer> rangeEndMap = new HashMap<String, Integer>();

	public static List<ConvariateData> getConvariateDataList(Map<String, BigDecimal> map ) {

		synchronized(convariatesTypeMap) {
			if (convariatesTypeMap.isEmpty()) {
				List<CovariatesMst>   mstList = null;
				CovariatesMstDAO dao = new CovariatesMstDAO();
				mstList = dao.selectAllCovariatesMst();

				for (CovariatesMst mst :mstList) {
					convariatesTypeMap.put(mst.getCovariatesCode(), mst.getCovariatesType());
					rangeStartMap.put(mst.getCovariatesCode(), mst.getRangeStart());
					rangeEndMap.put(mst.getCovariatesCode(), mst.getRangeEnd());
				}
			}
		}

		List<ConvariateData> resultList = new ArrayList<ConvariateData>();

		if (map == null) {
			return resultList;
		}

		Set<Entry<String, BigDecimal>> set = map.entrySet();

		for (Entry<String, BigDecimal> entry : set) {

			if (convariatesTypeMap.get(entry.getKey()) != null) {
				int type = convariatesTypeMap.get(entry.getKey()).intValue();
				switch (type) {
				case DeusConst.COV_TYPE_BOOL: // boolean
				case DeusConst.COV_TYPE_CATE: // カテゴリ
				case DeusConst.COV_TYPE_ORDR: // 順序
					int start =  rangeStartMap.get(entry.getKey());
					int end   =  rangeEndMap.get(entry.getKey());
					for (int i = start; i <=  end; i++) {
						ConvariateData data = new ConvariateData();
						data.setConvariateCode(entry.getKey());
						data.setConvariateLabel(i);
						if (i == entry.getValue().intValue()) {
							data.setValue(BigDecimal.valueOf(1));
						} else {
							data.setValue(BigDecimal.valueOf(0));
						}
						resultList.add(data);
					}
					break;
				default:
					ConvariateData data = new ConvariateData();
					data.setConvariateCode(entry.getKey());
					data.setConvariateLabel(0);
					data.setValue(entry.getValue());
					resultList.add(data);

				}
			} else {
				ConvariateData data = new ConvariateData();
				data.setConvariateCode(entry.getKey());
				data.setConvariateLabel(0);
				data.setValue(entry.getValue());
				resultList.add(data);

			}

		}

		return resultList;
	}




}
