package com.kunteng.cyria.dashboard.domain;

import com.esotericsoftware.minlog.Log;

public class ProvinceNameOfChina {

	private static String[][] nameMaps = {
			{"北京", "北京市", "京"},
			{"上海", "上海市", "沪"},
			{"天津", "天津市", "津"},
			{"重庆", "重庆市", "渝"},
			{"黑龙江", "黑龙江省", "黑"},
			{"吉林", "吉林省", "吉"},
			{"辽宁", "辽宁省", "辽"},
			{"河北", "河北省", "冀"},
			{"山东", "山东省", "鲁"},
			{"山西", "山西省", "晋"},
			{"河南", "河南省", "豫"},
			{"内蒙古", "内蒙古自治区", "内蒙古"},
			{"陕西", "陕西省", "陕", "秦"},
			{"安徽", "安徽省", "皖"},
			{"江苏", "江苏省", "苏"},
			{"浙江", "浙江省", "浙"},
			{"湖南", "湖南省", "湘"},
			{"湖北", "湖北省", "鄂"},
			{"四川", "四川省", "川", "蜀"},
			{"江西", "江西省", "赣"},
			{"福建", "福建省", "闽"},
			{"广东", "广东省", "粤"},
			{"广西", "广西壮族自治区", "桂"},
			{"云南", "云南省", "滇", "云"},
			{"贵州", "贵州省", "贵", "黔"},
			{"甘肃", "甘肃省", "甘", "陇"},
			{"宁夏", "宁夏回族自治区", "宁"},
			{"青海", "青海省", "青"},
			{"新疆", "新疆维吾尔自治区", "新"},
			{"西藏", "西藏自治区", "藏"},
			{"海南", "海南省", "琼"},
			{"台湾", "台湾省", "台"},
			{"香港", "香港特别行政区", "港"},
			{"澳门", "澳门特别行政区", "澳"}
	};

	public static String volidName(String name) {
		if (name == null || name.isEmpty())
			return null;
		name = name.replaceAll("\\s*", "");
		for(String[] line: nameMaps) {
			Log.info(String.format("trans before : %s",line.toString()));
			for(String field : line) {
				Log.info(String.format("%s vs %s",field, name));
				if (field.equals(name))
					return line[0];
			}
		}
		return null;
	}
}
