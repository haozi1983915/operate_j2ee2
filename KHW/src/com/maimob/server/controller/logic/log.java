package com.maimob.server.controller.logic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.maimob.server.importData.dao.SysDao;

public class log {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		log.save(ss);
	}

	private static String savefile = "C:/workspace/";
//	private static String savefile = "/Users/zhanghao/Downloads/电话注册/";
	
	public static String saveAsFileWriter(String content) {
		
		FileWriter fwriter = null;
		try {
			File f = new File(savefile);
			if(!f.exists())
				f.mkdirs();
			String path = savefile+"aa.txt";
			f = new File(path);
			if(!f.exists())
				f.createNewFile();
			fwriter = new FileWriter(path);
			fwriter.write(content);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				fwriter.flush();
				fwriter.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return "ok";
	}
	
	static String ss = "000008.XSHE[(20161028000000, 10.20540342, 10.23536057, 10.04563194, 10.06560337, 22968768.84021734, 232298395) (20161031000000,  9.98571763, 10.05561766,  9.76603184,  9.795989  , 17946428.74942864, 176621095) (20161101000000,  9.78600328, 10.10554624,  9.76603184,  9.97573191, 24718041.2153441 , 246813704) (20161102000000,  9.95576048, 10.46503208,  9.78600328, 10.1355034 , 39659151.65866265, 403278564) (20161103000000, 10.01567479, 10.28528916,  9.93578904, 10.08557481, 31232995.1120537 , 315910920) (20161104000000, 10.04563194, 10.05561766,  9.80597471,  9.8559033 , 24035602.53132236, 238049204) (20161107000000,  9.78600328,  9.95576048,  9.78600328,  9.87587474, 10913816.5140455 , 107686840) (20161108000000,  9.88586046,  9.94577476,  9.795989  ,  9.8559033 , 13622567.2514492 , 134458266) (20161109000000,  9.8559033 ,  9.8559033 ,  9.53636034,  9.61624608, 19378076.48136096, 188000713) (20161110000000,  9.69613182,  9.83593187,  9.69613182,  9.75604613, 16719577.51538496, 163397374) (20161111000000,  9.76603184, 10.37516062,  9.71610326, 10.0256605 , 37517666.11026017, 377940961) (20161114000000,  9.89584617, 10.18543198,  9.86588902, 10.08557481, 24734759.09243087, 248212962) (20161115000000, 10.11553196, 10.18543198,  9.91581761, 10.07558909, 21080902.52012229, 212075499) (20161116000000, 10.08557481, 10.14548911,  9.99570335, 10.07558909, 14952699.99594376, 150499771) (20161117000000, 10.10554624, 10.16546055,  9.92580333, 10.06560337, 21135072.88823387, 212577059) (20161118000000,  9.98571763, 10.1355034 ,  9.9657462 , 10.08557481, 20627699.23853128, 207951524) (20161121000000, 10.03564622, 10.12551768,  9.9657462 , 10.07558909, 14412277.14415553, 144809810) (20161122000000, 10.03564622, 10.3651749 ,  9.99570335, 10.21538914, 25166759.09071302, 255951241) (20161123000000, 10.1954177 , 10.27530344, 10.01567479, 10.04563194, 24198426.08190431, 245655845) (20161124000000, 10.03564622, 10.23536057,  9.97573191, 10.00568907, 14975477.52765207, 150670739) (20161125000000,  9.9657462 , 10.15547483,  9.80597471, 10.07558909, 17940120.74009777, 179057464) (20161128000000, 10.04563194, 10.4750178 , 10.00568907, 10.29527488, 32810156.67218598, 335311581) (20161129000000, 10.31524631, 10.46503208, 10.1954177 , 10.21538914, 30277616.60534122, 311742246) (20161130000000, 10.1954177 , 10.20540342,  9.98571763, 10.04563194, 20930546.77652071, 210765825) (20161201000000,  9.99570335, 10.08557481,  9.94577476, 10.0256605 ,  9208028.24451726,  92313127) (20161202000000, 10.04563194, 10.09556053,  9.89584617,  9.93578904, 13050589.33155242, 130330957) (20161205000000,  9.89584617, 10.08557481,  9.73607469, 10.01567479, 14289001.0767432 , 140843729) (20161206000000, 10.0256605 , 10.23536057,  9.94577476, 10.03564622, 13883286.62043862, 139957982) (20161207000000, 10.03564622, 10.10554624,  9.98571763, 10.08557481,  9896854.05086195,  99537119) (20161208000000, 10.08557481, 10.11553196,  9.97573191,  9.97573191,  9096504.9628657 ,  91110451) (20161209000000,  9.95576048, 10.1355034 ,  9.8559033 ,  9.95576048, 16998073.27326664, 169506980) (20161212000000,  9.93578904,  9.97573191,  9.23678881,  9.27673168, 20933513.01300873, 200875141) (20161213000000,  9.24677453,  9.98571763,  9.23678881,  9.61624608, 13355069.20089752, 126448924) (20161214000000,  9.60626036,  9.83593187,  9.41653173,  9.44648888, 10340479.65325937,  98904169) (20161215000000,  9.41653173,  9.53636034,  9.2867174 ,  9.5163889 ,  8065170.97374037,  76111635) (20161216000000,  9.42651744,  9.50640319,  9.31667455,  9.38657457,  8094565.95673594,  76023854) (20161219000000,  9.38657457,  9.795989  ,  8.99713159,  9.64620323, 30173636.09655522, 284115270) (20161220000000,  9.64620323,  9.74606041,  9.36660314,  9.53636034, 21818400.84234384, 207517970) (20161221000000,  9.56631749,  9.57630321,  9.38657457,  9.43650316, 13501467.29203433, 127340449) (20161222000000,  9.43650316,  9.43650316,  9.2867174 ,  9.41653173,  8778252.42288599,  82114724) (20161223000000,  9.37658886,  9.37658886,  9.23678881,  9.27673168,  9624715.37239485,  89367831) (20161226000000,  9.23678881,  9.32666027,  9.18686022,  9.2867174 ,  6174722.96638465,  57245631) (20161227000000,  9.29670312,  9.3466317 ,  9.26674596,  9.29670312,  4738612.86112667,  44096994) (20161228000000,  9.29670312,  9.39656029,  9.26674596,  9.38657457,  7848385.35254499,  73456783) (20161229000000,  9.42651744,  9.43650316,  9.30668883,  9.38657457,  6267457.41313133,  58789469) (20161230000000,  9.32666027,  9.37658886,  9.27673168,  9.30668883,  5006735.80417278,  46691337) (20170103000000,  9.31667455,  9.35661742,  9.27673168,  9.32666027,  4788250.75579301,  44600660) (20170104000000,  9.33664599,  9.39656029,  9.30668883,  9.37658886,  7253662.94813912,  67927317) (20170105000000,  9.39656029,  9.4564746 ,  9.36660314,  9.39656029,  6866318.72888257,  64580013) (20170106000000,  9.40654601,  9.58628893,  9.39656029,  9.46646032, 12858292.6864131 , 121896107) (20170109000000,  9.46646032,  9.55633177,  9.33664599,  9.5163889 ,  8534171.81799125,  80701145) (20170110000000,  9.52637462,  9.55633177,  9.42651744,  9.4564746 ,  7222306.16322592,  68415949) (20170111000000,  9.4564746 ,  9.46646032,  9.24677453,  9.3466317 ,  7168701.60322229,  66994616) (20170112000000,  9.3466317 ,  9.5163889 ,  9.23678881,  9.3466317 , 10009400.79425921,  93693683) (20170113000000,  9.40654601,  9.40654601,  9.14691735,  9.19684594,  6369585.27590017,  59109263) (20170116000000,  9.20683166,  9.26674596,  8.30811707,  8.73750293, 10409017.54159162,  92710303) (20170117000000,  8.76746008,  8.84734582,  8.61767432,  8.67758862,  8769826.38851387,  76534393) (20170118000000,  8.68757434,  8.74748865,  8.57773145,  8.58771716,  6026671.51385631,  52206266) (20170119000000,  8.57773145,  8.62766003,  8.48785999,  8.53778858,  4757244.47147765,  40745881) (20170120000000,  8.55776001,  8.67758862,  8.53778858,  8.62766003,  5363858.86045315,  46238838) (20170123000000,  8.63764575,  8.72751721,  8.62766003,  8.70754578,  5393982.88469222,  46873025) (20170124000000,  8.70754578,  8.72751721,  8.64763147,  8.6676029 ,  3283289.31452528,  28498229) (20170125000000,  8.65761719,  8.65761719,  8.56774573,  8.61767432,  3535098.95831757,  30456891) (20170126000000,  8.62766003,  8.72751721,  8.61767432,  8.69756006,  4003357.7427313 ,  34797381) (20170203000000,  8.68757434,  8.7774458 ,  8.62766003,  8.62766003,  4457266.03123912,  38661750) (20170206000000,  8.62766003,  8.70754578,  8.56774573,  8.62766003, 11039212.60935963,  95265024) (20170207000000,  8.63764575,  8.6676029 ,  8.59770288,  8.64763147,  4797575.07312588,  41442352) (20170208000000,  8.64763147,  8.6676029 ,  8.57773145,  8.64763147,  6297710.62187647,  54294661) (20170209000000,  8.64763147,  8.73750293,  8.63764575,  8.72751721,  8589497.83664543,  74756553) (20170210000000,  8.74748865,  8.86731726,  8.70754578,  8.7774458 , 12202757.42685142, 107512112) (20170213000000,  8.78743152,  8.85733154,  8.74748865,  8.80740295,  9421553.20872243,  82849345) (20170214000000,  8.80740295,  8.82737439,  8.74748865,  8.75747436,  6147381.91689243,  53895728) (20170215000000,  8.76746008,  8.95718872,  8.76746008,  8.79741723, 14856927.20972893, 131530617) (20170216000000,  8.79741723,  8.8373601 ,  8.70754578,  8.78743152,  7733837.7514492 ,  67777240) (20170217000000,  8.79741723,  8.81738867,  8.68757434,  8.70754578,  6578644.86248884,  57373724) (20170220000000,  8.72751721,  8.75747436,  8.67758862,  8.74748865,  6334176.70407592,  55192978) (20170221000000,  8.74748865,  8.78743152,  8.71753149,  8.7774458 ,  6991356.31215662,  61228942) (20170222000000,  8.76746008,  8.76746008,  8.72751721,  8.76746008,  4718952.78187858,  41299112) (20170223000000,  8.76746008,  8.78743152,  8.69756006,  8.74748865,  5324566.74200481,  46498516) (20170224000000,  8.73750293,  8.76746008,  8.69756006,  8.76746008,  6605857.72890527,  57677762) (20170227000000,  8.76746008,  8.8373601 ,  8.71753149,  8.73750293,  8756601.50024216,  76723096) (20170228000000,  8.73750293,  8.7774458 ,  8.70754578,  8.74748865,  6376076.5469722 ,  55792134) (20170301000000,  8.74748865,  8.76746008,  8.71753149,  8.71753149,  8560984.11229586,  74828540) (20170302000000,  8.71753149,  8.74748865,  8.68757434,  8.68757434,  6619914.80573928,  57640565) (20170303000000,  8.67758862,  8.67758862,  8.56774573,  8.61767432,  6805373.68492985,  58509817) (20170306000000,  8.63764575,  8.71753149,  8.62766003,  8.70754578,  6392804.43836176,  55578478) (20170307000000,  8.69756006,  8.73750293,  8.6676029 ,  8.72751721,  5556537.05052898,  48382969) (20170308000000,  8.73750293,  8.74748865,  8.68757434,  8.71753149,  4355102.11698022,  37923041) (20170309000000,  8.69756006,  8.72751721,  8.59770288,  8.61767432,  5981340.7708223 ,  51599784) (20170310000000,  8.61767432,  8.63764575,  8.53778858,  8.55776001,  6038970.07911943,  51752620) (20170313000000,  8.55776001,  8.72751721,  8.40797425,  8.65761719,  9210128.24381347,  78601518) (20170314000000,  8.63764575,  8.6676029 ,  8.56774573,  8.58771716,  4090885.75345462,  35212145) (20170315000000,  8.58771716,  8.58771716,  8.51781714,  8.53778858,  4328680.38048463,  36997497) (20170316000000,  8.53778858,  8.67758862,  8.51781714,  8.59770288,  9091424.70705756,  77883884) (20170317000000,  8.59770288,  8.6076886 ,  8.49784571,  8.50783142, 10315795.3982988 ,  88081944) (20170320000000,  8.48785999,  8.52780286,  8.4379314 ,  8.46788855,  7008164.31796855,  59357651) (20170321000000,  8.46788855,  8.49784571,  8.41795996,  8.44791712,  7952221.65537074,  67155710) (20170322000000,  8.44791712,  8.44791712,  8.37801709,  8.42794568,  8121721.74162643,  68250177) (20170323000000,  8.41795996,  8.47787427,  8.39798853,  8.44791712,  6655715.93823311,  56137433) (20170324000000,  8.44791712,  8.65761719,  8.38800281,  8.59770288, 15520342.72416037, 132850738) (20170327000000,  8.58771716,  8.65761719,  8.54777429,  8.57773145,  9601819.67191355,  82473875) (20170328000000,  8.58771716,  8.61767432,  8.51781714,  8.53778858,  5664349.03156453,  48444177) (20170329000000,  8.51781714,  8.57773145,  8.46788855,  8.46788855,  7344999.39821556,  62450548) (20170330000000,  8.48785999,  8.89727441,  8.38800281,  8.62766003, 26109328.3027198 , 226109447) (20170331000000,  8.56774573,  8.76746008,  8.50783142,  8.72751721, 18455026.14693284, 160281584) (20170405000000,  8.73750293,  8.98714587,  8.72751721,  8.87730298, 20886737.20608133, 185595239) (20170406000000,  8.88728869,  9.31667455,  8.76746008,  9.1768745 , 38595762.88771927, 351016663) (20170407000000,  9.08700305,  9.14691735,  8.98714587,  9.06703161, 23384421.49061615, 211900464) (20170410000000,  9.05704589,  9.05704589,  8.86731726,  8.87730298, 17402356.68570175, 155659436) (20170411000000,  8.8373601 ,  9.03707446,  8.82737439,  9.03707446, 17354372.15242694, 155164139) (20170412000000,  8.98714587,  8.99713159,  8.84734582,  8.86731726, 12981297.36621967, 115516269) (20170413000000,  8.86731726,  8.92723156,  8.78743152,  8.79741723,  9902920.71549545,  87715909) (20170414000000,  8.78743152,  8.97716015,  8.78743152,  8.80740295, 11006310.61752509,  97590901) (20170417000000,  8.81738867,  8.93721728,  8.76746008,  8.86731726, 10083817.0783324 ,  89290647) (20170418000000,  8.82737439,  8.93721728,  8.75747436,  8.7774458 ,  8541184.83423892,  75510834) (20170419000000,  8.73750293,  8.76746008,  8.39798853,  8.68757434, 10818226.98957182,  93450586) (20170420000000,  8.70754578,  8.75747436,  8.59770288,  8.68757434,  8204650.18307578,  71262164) (20170421000000,  8.68757434,  8.75747436,  8.6676029 ,  8.73750293,  5084494.86251911,  44266493) (20170424000000,  8.72751721,  8.72751721,  8.39798853,  8.62766003,  7099350.55350305,  60878980) (20170425000000,  8.6076886 ,  8.67758862,  8.49784571,  8.6076886 ,  4924874.88740143,  42405404) (20170426000000,  8.6076886 ,  8.62766003,  8.39798853,  8.41795996,  9048379.22792148,  76720646) (20170427000000,  8.36803138,  8.36803138,  8.13835987,  8.20825989, 12298961.82951673, 101195519) (20170428000000,  8.20825989,  8.22823133,  8.04848841,  8.06845985, 12336983.13294032,  99955453) (20170502000000,  8.07844556,  8.11838844,  7.94863124,  7.96860267,  9698637.95134022,  77728698) (20170503000000,  7.97858839,  8.02851698,  7.88871693,  7.90868836,  7774722.14404202,  61870310) (20170504000000,  7.91867408,  7.91867408,  7.79884547,  7.86874549,  8188860.63185815,  64353551) (20170505000000,  7.88871693,  7.88871693,  7.49927394,  7.52923109, 18003343.03650618, 137175932) (20170508000000,  7.50925966,  7.50925966,  7.29955959,  7.29955959,  8838874.00485841,  65192141) (20170509000000,  7.1897167 ,  7.3594739 ,  7.17973098,  7.31953102,  5757168.59988497,  42084836) (20170510000000,  7.36945961,  7.48928822,  7.31953102,  7.34948818,  7748082.09574549,  57417439) (20170511000000,  7.30954531,  7.40940248,  7.03993093,  7.40940248,  9218217.79761166,  66447694) (20170512000000,  7.38943105,  7.39941677,  7.28957387,  7.33950246,  4023964.17359356,  29482035) (20170515000000,  7.34948818,  7.42937392,  7.27958815,  7.34948818,  5472385.8627083 ,  40212080) (20170516000000,  7.34948818,  7.37944533,  7.20968813,  7.36945961,  5808791.32936538,  42366657) (20170517000000,  7.3594739 ,  7.44934535,  7.29955959,  7.38943105,  6390683.40902968,  47235490) (20170518000000,  7.40940248,  7.40940248,  7.30954531,  7.34948818,  5436219.20816243,  40013378) (20170519000000,  7.3594739 ,  7.36945961,  7.249631  ,  7.34948818,  5170383.5318748 ,  37815285) (20170522000000,  7.30954531,  7.32951674,  7.22965957,  7.23964528,  3372416.60940503,  24536669) (20170523000000,  7.23964528,  7.27958815,  7.02994521,  7.06988808,  6914544.60685475,  49159195) (20170524000000,  7.02994521,  7.02994521,  6.84021658,  6.97003091,  5812667.86597751,  40369928) (20170525000000,  6.94007375,  7.22965957,  6.90013088,  7.16974526, 12103344.44157043,  86005229) (20170526000000,  7.13978811,  7.22965957,  7.06988808,  7.13978811,  7634492.86342722,  54590877) (20170531000000,  7.19970241,  7.25961672,  7.05990237,  7.0798738 ,  9716018.77527206,  69550628) (20170601000000,  7.0798738 ,  7.08985952,  6.92010232,  6.99998806,  6256397.617124  ,  43733142) (20170602000000,  7.02      ,  7.12      ,  6.92      ,  7.11      ,  4990163.        ,  35093587) (20170605000000,  7.08      ,  7.15      ,  7.06      ,  7.12      ,  4751960.        ,  33816131) (20170606000000,  7.12      ,  7.15      ,  7.09      ,  7.14      ,  3815206.        ,  27193276) (20170607000000,  7.14      ,  7.29      ,  7.12      ,  7.26      ,  8161184.        ,  58980614) (20170608000000,  7.28      ,  7.29      ,  7.21      ,  7.25      ,  5219035.        ,  37811340) (20170609000000,  7.27      ,  7.31      ,  7.22      ,  7.3       ,  8405393.        ,  61142968) (20170612000000,  7.29      ,  7.35      ,  7.23      ,  7.26      ,  4680335.        ,  34106959) (20170613000000,  7.23      ,  7.31      ,  7.22      ,  7.3       ,  4603179.        ,  33492864) (20170614000000,  7.33      ,  7.38      ,  7.3       ,  7.34      ,  4364141.        ,  32043928) (20170615000000,  7.36      ,  7.37      ,  7.31      ,  7.35      ,  3231081.        ,  23718932) (20170616000000,  7.35      ,  7.47      ,  7.32      ,  7.35      ,  4764094.        ,  35294460) (20170619000000,  7.35      ,  7.44      ,  7.35      ,  7.41      ,  5084628.        ,  37628064) (20170620000000,  7.45      ,  7.47      ,  7.36      ,  7.39      ,  4101019.        ,  30291002) (20170621000000,  7.41      ,  7.44      ,  7.34      ,  7.37      ,  4194541.        ,  30888486) (20170622000000,  7.37      ,  7.38      ,  7.24      ,  7.24      ,  3908003.        ,  28578997) (20170623000000,  7.23      ,  7.28      ,  7.09      ,  7.19      ,  5442818.        ,  39138906) (20170626000000,  7.18      ,  7.46      ,  7.16      ,  7.39      ,  9264470.        ,  68421418) (20170627000000,  7.42      ,  7.42      ,  7.31      ,  7.33      ,  6151328.        ,  45185552) (20170628000000,  7.33      ,  7.35      ,  7.24      ,  7.27      ,  3864428.        ,  28129734) (20170629000000,  7.26      ,  7.29      ,  7.24      ,  7.25      ,  3860970.        ,  28049182) (20170630000000,  7.24      ,  7.29      ,  7.17      ,  7.28      ,  4267960.        ,  30831432) (20170703000000,  7.27      ,  7.29      ,  7.22      ,  7.28      ,  3412848.        ,  24760281) (20170704000000,  7.28      ,  7.37      ,  7.22      ,  7.36      ,  6384022.        ,  46663377) (20170705000000,  7.35      ,  7.38      ,  7.29      ,  7.34      ,  5045283.        ,  36980465) (20170706000000,  7.35      ,  7.44      ,  7.31      ,  7.43      ,  9192932.        ,  67774736) (20170707000000,  7.43      ,  7.49      ,  7.36      ,  7.44      ,  5464860.        ,  40492955) (20170710000000,  7.44      ,  7.51      ,  7.44      ,  7.49      ,  7715466.        ,  57700773) (20170711000000,  7.48      ,  7.5       ,  7.43      ,  7.47      ,  4845718.        ,  36146871) (20170712000000,  7.46      ,  7.48      ,  7.32      ,  7.35      ,  4134085.        ,  30526665) (20170713000000,  7.35      ,  7.41      ,  7.3       ,  7.36      ,  3349879.        ,  24617761) (20170714000000,  7.36      ,  7.4       ,  7.31      ,  7.33      ,  2862200.        ,  21045912) (20170717000000,  7.33      ,  7.33      ,  7.        ,  7.21      ,  7189883.        ,  51554766) (20170718000000,  7.2       ,  7.25      ,  7.08      ,  7.25      ,  3710437.        ,  26530559) (20170719000000,  7.35      ,  7.5       ,  7.29      ,  7.46      ,  8568428.        ,  63357299) (20170720000000,  7.44      ,  7.56      ,  7.39      ,  7.55      ,  9845823.        ,  73629635) (20170721000000,  7.55      ,  7.77      ,  7.5       ,  7.69      , 12546462.        ,  96077655) (20170724000000,  7.69      ,  7.77      ,  7.58      ,  7.7       ,  7690976.        ,  59061743) (20170725000000,  7.68      ,  7.7       ,  7.53      ,  7.53      ,  5770348.        ,  43798010) (20170726000000,  7.55      ,  7.65      ,  7.53      ,  7.59      ,  4700800.        ,  35665225) (20170727000000,  7.59      ,  7.59      ,  7.5       ,  7.56      ,  4892481.        ,  36896498) (20170728000000,  7.56      ,  7.58      ,  7.5       ,  7.52      ,  3829977.        ,  28835947) (20170731000000,  7.52      ,  7.55      ,  7.46      ,  7.54      ,  5040361.        ,  37836058) (20170801000000,  7.54      ,  7.6       ,  7.44      ,  7.59      ,  7174782.        ,  53867142) (20170802000000,  7.56      ,  7.66      ,  7.5       ,  7.52      ,  6225686.        ,  47168984) (20170803000000,  7.51      ,  7.58      ,  7.46      ,  7.5       ,  5545883.        ,  41661709) (20170804000000,  7.5       ,  7.54      ,  7.43      ,  7.5       ,  6113329.        ,  45777105) (20170807000000,  7.5       ,  7.53      ,  7.46      ,  7.51      ,  3135000.        ,  23539032) (20170808000000,  7.51      ,  7.73      ,  7.47      ,  7.72      , 13357502.        , 102419771) (20170809000000,  7.69      ,  7.74      ,  7.64      ,  7.7       ,  6653136.        ,  51226535) (20170810000000,  7.7       ,  7.72      ,  7.54      ,  7.64      ,  6093869.        ,  46426780) (20170811000000,  7.6       ,  7.82      ,  7.55      ,  7.75      , 12703872.        ,  98121194) (20170814000000,  7.71      ,  8.34      ,  7.71      ,  8.15      , 19298376.        , 156484392) (20170815000000,  8.        ,  8.18      ,  7.95      ,  7.96      , 14096960.        , 113345602) (20170816000000,  7.98      ,  8.12      ,  7.95      ,  8.09      , 15876598.        , 127829333) (20170817000000,  8.07      ,  8.08      ,  7.96      ,  8.03      , 10297292.        ,  82496050) (20170818000000,  8.02      ,  8.05      ,  7.91      ,  8.        ,  9212209.        ,  73772431) (20170821000000,  8.06      ,  8.25      ,  8.06      ,  8.19      , 12043719.        ,  98491899) (20170822000000,  8.23      ,  8.28      ,  8.08      ,  8.11      ,  8647234.        ,  70681069) (20170823000000,  8.05      ,  8.1       ,  8.01      ,  8.05      ,  4351350.        ,  35021809) (20170824000000,  8.05      ,  8.26      ,  8.04      ,  8.1       ,  8359328.        ,  68099351) (20170825000000,  8.13      ,  8.16      ,  8.03      ,  8.11      ,  6133653.        ,  49593541) (20170828000000,  8.15      ,  8.17      ,  8.08      ,  8.15      ,  7739655.        ,  62967083) (20170829000000,  8.15      ,  8.15      ,  7.96      ,  7.99      , 10035992.        ,  80554137) (20170830000000,  7.98      ,  8.04      ,  7.96      ,  8.02      ,  7648051.        ,  61206776) (20170831000000,  8.04      ,  8.06      ,  7.94      ,  8.06      , 13797579.        , 110465801) (20170901000000,  8.02      ,  8.04      ,  7.95      ,  7.98      ,  8330217.        ,  66415342) (20170904000000,  7.94      ,  8.07      ,  7.93      ,  8.05      ,  7128892.        ,  57105072) (20170905000000,  8.05      ,  8.27      ,  8.04      ,  8.26      , 17609029.        , 144045143) (20170906000000,  8.25      ,  8.26      ,  8.13      ,  8.16      , 11152286.        ,  91473761) (20170907000000,  8.15      ,  8.24      ,  8.15      ,  8.23      ,  6651891.        ,  54617583) (20170908000000,  8.23      ,  8.49      ,  8.18      ,  8.36      , 14312180.        , 119590382) (20170911000000,  8.4       ,  8.48      ,  8.31      ,  8.43      ,  9313015.        ,  78320054) (20170912000000,  8.42      ,  8.5       ,  8.32      ,  8.39      ,  7037566.        ,  58963925) (20170913000000,  8.39      ,  8.41      ,  8.26      ,  8.32      ,  6634489.        ,  55124369) (20170914000000,  8.32      ,  8.48      ,  8.29      ,  8.46      , 10334816.        ,  86868777) (20170915000000,  8.46      ,  8.96      ,  8.4       ,  8.95      , 28372265.        , 247814086) (20170919000000,  9.25      ,  9.32      ,  8.87      ,  9.26      , 42098605.        , 385356034) (20170920000000,  9.26      ,  9.35      ,  9.05      ,  9.2       , 20640244.        , 189829936) (20170921000000,  9.12      ,  9.2       ,  8.96      ,  9.14      , 10430888.        ,  94413156) (20170922000000,  9.09      ,  9.12      ,  8.87      ,  9.1       , 11843103.        , 106562270) (20170925000000,  9.09      ,  9.13      ,  8.81      ,  8.92      ,  5739870.        ,  51395666) (20170926000000,  8.89      ,  8.94      ,  8.65      ,  8.74      ,  7835449.        ,  68606139) (20170927000000,  8.82      ,  8.82      ,  8.58      ,  8.62      ,  8320835.        ,  71696408) (20170928000000,  8.69      ,  8.75      ,  8.61      ,  8.62      ,  8149020.        ,  70763832) (20170929000000,  8.61      ,  9.05      ,  8.61      ,  9.03      , 13559389.        , 120467972) (20171009000000,  9.02      ,  9.15      ,  9.        ,  9.11      ,  8763142.        ,  79543463) (20171010000000,  9.15      ,  9.2       ,  9.05      ,  9.17      ,  9209742.        ,  84179647) (20171011000000,  9.19      ,  9.19      ,  8.99      ,  9.05      ,  7804675.        ,  70609515) (20171012000000,  9.04      ,  9.16      ,  8.81      ,  9.12      ,  8746241.        ,  78722767) (20171013000000,  9.03      ,  9.11      ,  8.95      ,  9.        ,  5693538.        ,  51353663) (20171016000000,  8.99      ,  9.25      ,  8.99      ,  9.2       , 14432160.        , 132277445) (20171017000000,  9.23      ,  9.28      ,  9.1       ,  9.19      ,  6460784.        ,  59410750) (20171018000000,  9.22      ,  9.26      ,  9.15      ,  9.22      ,  6831939.        ,  62906327) (20171019000000,  9.21      ,  9.21      ,  9.        ,  9.05      ,  7936000.        ,  71921245) (20171020000000,  9.04      ,  9.04      ,  8.77      ,  8.94      ,  6907136.        ,  61262064) (20171023000000,  8.94      ,  8.97      ,  8.81      ,  8.87      ,  3633951.        ,  32217372) (20171024000000,  8.87      ,  8.94      ,  8.81      ,  8.88      ,  3721828.        ,  32983643) (20171025000000,  8.88      ,  8.94      ,  8.85      ,  8.92      ,  3763000.        ,  33448604) (20171026000000,  8.94      ,  9.73      ,  8.92      ,  9.46      , 28832180.        , 267762876) (20171027000000,  9.35      ,  9.35      ,  9.09      ,  9.23      , 19495317.        , 179440239) (20171030000000,  9.22      ,  9.36      ,  9.        ,  9.29      , 20020724.        , 185300289) (20171031000000,  9.3       ,  9.34      ,  9.09      ,  9.18      , 10520237.        ,  96473850) (20171101000000,  9.15      ,  9.22      ,  9.09      ,  9.16      ,  8114400.        ,  74070073) (20171102000000,  9.17      ,  9.22      ,  9.1       ,  9.17      ,  9880120.        ,  90572056) (20171103000000,  9.17      ,  9.27      ,  8.8       ,  9.24      , 21139107.        , 191394934) (20171106000000,  9.2       ,  9.26      ,  9.09      ,  9.19      ,  8624826.        ,  79217826) (20171107000000,  9.2       ,  9.24      ,  9.1       ,  9.11      ,  5606000.        ,  51233104) (20171108000000,  9.11      ,  9.15      ,  8.91      ,  8.96      ,  8659392.        ,  77849749) (20171109000000,  8.96      ,  8.96      ,  8.85      ,  8.91      ,  5210858.        ,  46295499) (20171110000000,  8.91      ,  9.02      ,  8.85      ,  9.01      ,  5669875.        ,  50667272) (20171113000000,  8.97      ,  9.01      ,  8.89      ,  8.91      ,  4818132.        ,  42962332) (20171114000000,  8.95      ,  9.        ,  8.8       ,  8.93      ,  6999793.        ,  62074683) (20171115000000,  8.9       ,  9.08      ,  8.71      ,  9.07      ,  8546731.        ,  75858345) (20171116000000,  9.01      ,  9.01      ,  8.9       ,  8.97      ,  5125000.        ,  45857153) (20171117000000,  8.95      ,  8.95      ,  8.78      ,  8.84      ,  5877689.        ,  51883134) (20171120000000,  8.78      ,  8.9       ,  8.5       ,  8.9       ,  3956561.        ,  34702943) (20171121000000,  8.88      ,  8.98      ,  8.81      ,  8.85      ,  4680400.        ,  41684522) (20171122000000,  8.91      ,  8.93      ,  8.83      ,  8.88      ,  3793188.        ,  33678108) (20171123000000,  8.88      ,  9.09      ,  8.76      ,  8.84      ,  7089900.        ,  63346908) (20171124000000,  8.79      ,  8.94      ,  8.37      ,  8.57      ,  9862601.        ,  84991714) (20171127000000,  8.43      ,  8.55      ,  8.18      ,  8.28      ,  7338803.        ,  60815740) (20171128000000,  8.23      ,  8.39      ,  8.18      ,  8.34      ,  3877411.        ,  32145879) (20171129000000,  8.3       ,  8.35      ,  8.18      ,  8.35      ,  4093325.        ,  33811532) (20171130000000,  8.37      ,  8.56      ,  8.37      ,  8.48      ,  9089626.        ,  77028369) (20171201000000,  8.48      ,  8.74      ,  8.41      ,  8.59      ,  5689054.        ,  49033674) (20171204000000,  8.59      ,  8.66      ,  8.46      ,  8.53      ,  4328685.        ,  36961635) (20171205000000,  8.53      ,  8.68      ,  8.4       ,  8.54      ,  6643470.        ,  56688534) (20171206000000,  8.54      ,  8.62      ,  8.42      ,  8.5       ,  5657770.        ,  48093645) (20171207000000,  8.43      ,  8.5       ,  8.2       ,  8.3       ,  4630851.        ,  38546728) (20171208000000,  8.3       ,  8.46      ,  8.22      ,  8.45      ,  6667300.        ,  55598523) (20171211000000,  8.76      ,  8.88      ,  8.45      ,  8.6       , 12698100.        , 109878511) (20171212000000,  8.6       ,  8.6       ,  8.35      ,  8.38      ,  5499900.        ,  46376142) (20171213000000,  8.38      ,  8.47      ,  8.28      ,  8.3       ,  7121327.        ,  59281788) (20171214000000,  8.33      ,  8.42      ,  8.25      ,  8.31      ,  3899314.        ,  32447327) (20171215000000,  8.25      ,  8.36      ,  8.23      ,  8.34      ,  3331769.        ,  27696438) (20171218000000,  8.38      ,  8.47      ,  8.35      ,  8.42      ,  4195704.        ,  35247275) (20171219000000,  8.39      ,  8.53      ,  8.34      ,  8.51      ,  6547254.        ,  55215307) (20171220000000,  8.48      ,  8.61      ,  8.4       ,  8.52      ,  8573113.        ,  73030852) (20171221000000,  8.45      ,  8.54      ,  8.39      ,  8.47      ,  4279515.        ,  36267214) (20171222000000,  8.47      ,  8.59      ,  8.46      ,  8.51      ,  4608134.        ,  39275824) (20171225000000,  8.51      ,  8.56      ,  8.4       ,  8.52      ,  6291150.        ,  53470192) (20171226000000,  8.52      ,  8.78      ,  8.35      ,  8.75      , 11081030.        ,  94908517) (20171227000000,  8.7       ,  8.79      ,  8.59      ,  8.72      , 11019800.        ,  95569045) (20171228000000,  8.54      ,  8.75      ,  8.5       ,  8.66      ,  9102790.        ,  78280365) (20171229000000,  8.74      ,  8.86      ,  8.59      ,  8.75      , 12229888.        , 107108396) (20180102000000,  8.75      ,  8.82      ,  8.7       ,  8.77      ,  8289999.        ,  72523239) (20180103000000,  8.77      ,  8.82      ,  8.68      ,  8.75      ,  7000622.        ,  61197410) (20180104000000,  8.72      ,  8.76      ,  8.58      ,  8.68      ,  7626333.        ,  66028563) (20180105000000,  8.68      ,  8.7       ,  8.5       ,  8.55      ,  8327573.        ,  71267988) (20180108000000,  8.54      ,  8.56      ,  8.4       ,  8.47      ,  4981915.        ,  42212579) (20180109000000,  8.47      ,  8.52      ,  8.39      ,  8.49      ,  6879805.        ,  58150131) (20180110000000,  8.49      ,  8.54      ,  8.34      ,  8.49      , 10972245.        ,  92729396) (20180111000000,  8.41      ,  8.58      ,  8.35      ,  8.56      ,  8537190.        ,  72275530) (20180112000000,  8.58      ,  8.72      ,  8.56      ,  8.67      ,  9200212.        ,  79646251) (20180115000000,  8.71      ,  8.73      ,  8.02      ,  8.45      ,  7889857.        ,  66728321) (20180116000000,  8.39      ,  8.53      ,  8.32      ,  8.48      ,  7720467.        ,  65229971) (20180117000000,  8.49      ,  8.49      ,  8.33      ,  8.43      ,  6241600.        ,  52361781) (20180118000000,  8.38      ,  8.41      ,  8.32      ,  8.37      ,  4241151.        ,  35409218) (20180119000000,  8.37      ,  8.39      ,  8.07      ,  8.24      ,  9448293.        ,  77473860) (20180122000000,  8.2       ,  8.24      ,  8.08      ,  8.21      ,  5304512.        ,  43248347) (20180123000000,  8.21      ,  8.32      ,  8.13      ,  8.32      ,  7988854.        ,  65768014) (20180124000000,  8.32      ,  8.35      ,  8.22      ,  8.32      ,  5852814.        ,  48598690) (20180125000000,  8.32      ,  8.34      ,  8.21      ,  8.26      ,  5703427.        ,  46987257) (20180126000000,  8.25      ,  8.35      ,  8.17      ,  8.31      ,  9189331.        ,  75909165) (20180129000000,  8.29      ,  8.38      ,  8.19      ,  8.27      ,  5930904.        ,  48999436) (20180130000000,  8.25      ,  8.29      ,  8.21      ,  8.26      ,  3794362.        ,  31295072) (20180131000000,  8.24      ,  8.3       ,  8.18      ,  8.28      ,  5850046.        ,  48232826) (20180201000000,  8.27      ,  8.27      ,  8.08      ,  8.18      ,  5286200.        ,  43205495) (20180202000000,  8.18      ,  8.18      ,  7.36      ,  8.01      , 15696691.        , 119423233) (20180205000000,  7.75      ,  7.87      ,  7.4       ,  7.84      ,  5947795.        ,  45843289) (20180206000000,  7.61      ,  7.69      ,  7.48      ,  7.61      ,  7236259.        ,  54739363) (20180207000000,  7.6       ,  7.8       ,  7.53      ,  7.63      ,  5953441.        ,  45464839) (20180208000000,  7.64      ,  7.76      ,  7.61      ,  7.66      ,  3770166.        ,  28885964) (20180209000000,  7.63      ,  7.76      ,  7.48      ,  7.56      ,  7928953.        ,  60025790) (20180212000000,  7.56      ,  7.66      ,  7.56      ,  7.63      ,  3709643.        ,  28185779) (20180213000000,  7.72      ,  7.72      ,  7.62      ,  7.7       ,  2244830.        ,  17244091) (20180214000000,  7.69      ,  7.99      ,  7.68      ,  7.96      ,  3892826.        ,  30600623) (20180222000000,  7.96      ,  8.17      ,  7.96      ,  8.02      ,  4252263.        ,  34268312) (20180223000000,  8.02      ,  8.08      ,  7.84      ,  7.87      ,  4284867.        ,  33848753) (20180226000000,  7.9       ,  7.97      ,  7.85      ,  7.91      ,  5901961.        ,  46546582) (20180227000000,  7.88      ,  7.88      ,  7.57      ,  7.78      , 12181322.        ,  93735497) (20180228000000,  7.6       ,  7.63      ,  7.46      ,  7.56      , 15116952.        , 113606402) (20180301000000,  7.54      ,  7.58      ,  7.49      ,  7.52      , 11334878.        ,  85297795) (20180302000000,  7.52      ,  7.63      ,  7.46      ,  7.61      , 13275752.        ,  99662395) (20180305000000,  7.6       ,  7.62      ,  7.51      ,  7.55      , 10038664.        ,  75907877) (20180306000000,  7.56      ,  7.65      ,  7.51      ,  7.57      ,  6791025.        ,  51509669) (20180307000000,  7.56      ,  7.56      ,  7.47      ,  7.5       ,  5237400.        ,  39415211) (20180308000000,  7.49      ,  7.51      ,  7.45      ,  7.47      ,  5981200.        ,  44740735) (20180309000000,  7.47      ,  7.48      ,  7.38      ,  7.45      ,  9290300.        ,  68954516) (20180312000000,  7.46      ,  7.5       ,  7.43      ,  7.5       ,  8817796.        ,  65842534) (20180313000000,  7.5       ,  7.52      ,  7.38      ,  7.4       , 16065098.        , 119370865) (20180314000000,  7.4       ,  7.42      ,  7.32      ,  7.38      , 13715564.        , 101147400) (20180315000000,  7.38      ,  7.38      ,  7.23      ,  7.27      ,  7859726.        ,  57156458) (20180316000000,  7.24      ,  7.33      ,  7.2       ,  7.29      , 16140246.        , 116881642) (20180319000000,  7.28      ,  7.29      ,  7.06      ,  7.11      , 13328252.        ,  95107017) (20180320000000,  7.08      ,  7.11      ,  7.        ,  7.05      , 15091400.        , 106431629) (20180321000000,  7.05      ,  7.14      ,  7.02      ,  7.07      , 19655643.        , 138899389) (20180322000000,  7.08      ,  7.14      ,  6.99      ,  7.05      , 12016100.        ,  84826080) (20180323000000,  7.        ,  7.08      ,  6.86      ,  7.05      , 18620682.        , 129825596) (20180326000000,  7.04      ,  7.04      ,  6.89      ,  7.02      , 17717253.        , 123599011) (20180327000000,  7.01      ,  7.05      ,  6.98      ,  7.01      , 12617566.        ,  88435813) (20180328000000,  7.01      ,  7.01      ,  6.85      ,  6.87      , 15676786.        , 108159398) (20180329000000,  6.86      ,  6.9       ,  6.73      ,  6.84      , 12089957.        ,  82010284) (20180330000000,  6.84      ,  6.87      ,  6.78      ,  6.84      ,  6966392.        ,  47531984) (20180402000000,  6.83      ,  6.92      ,  6.8       ,  6.85      ,  9738448.        ,  66701699) (20180403000000,  6.83      ,  6.83      ,  6.68      ,  6.69      ,  8936474.        ,  60319525) (20180404000000,  6.69      ,  6.74      ,  6.67      ,  6.71      , 10185511.        ,  68298543) (20180409000000,  6.71      ,  6.73      ,  6.6       ,  6.64      ,  6382105.        ,  42558254) (20180410000000,  6.64      ,  6.69      ,  6.56      ,  6.58      , 12482608.        ,  82488213) (20180411000000,  6.59      ,  6.63      ,  6.5       ,  6.53      , 15559941.        , 101612261) (20180412000000,  6.52      ,  6.52      ,  6.36      ,  6.37      , 13032170.        ,  83576538) (20180413000000,  6.35      ,  6.39      ,  6.27      ,  6.3       , 14425700.        ,  91119774) (20180416000000,  6.29      ,  6.29      ,  6.01      ,  6.01      , 13396700.        ,  81782313) (20180502000000,  5.8       ,  6.19      ,  5.72      ,  5.78      , 16494386.        ,  96381781) (20180503000000,  5.8       ,  5.88      ,  5.67      ,  5.71      , 21703092.        , 125025514) (20180504000000,  5.71      ,  5.72      ,  5.56      ,  5.62      , 20381062.        , 114592445) (20180507000000,  5.63      ,  5.71      ,  5.58      ,  5.68      , 25253074.        , 143013177) (20180508000000,  5.71      ,  5.83      ,  5.7       ,  5.78      , 21818417.        , 125814384) (20180509000000,  5.81      ,  5.84      ,  5.74      ,  5.74      , 17215945.        ,  99668516) (20180510000000,  5.77      ,  5.78      ,  5.68      ,  5.74      , 11727133.        ,  67132958) (20180511000000,  5.75      ,  5.75      ,  5.65      ,  5.66      , 18302532.        , 104360197)]";
	
	public static void save(String data)
	{
		SysDao sd = new SysDao();
		String code = data.substring(0,data.indexOf("["));

		String sql = "insert into history (order_book_id,datetime,open,high,low,close,volume,total_turnover)values"
				+ "('"+code+"',?,?,?,?,?,?,?);";
		
		try {
			sd.UpdateStart(sql);
			while(data.contains(")"))
			{
				String cols = data.substring(data.indexOf("(")+1,data.indexOf(")"));
				data = data.substring(data.indexOf(")")+1);
				String[] ds = cols.split(",");
				for(int i = 0;i < ds.length;i++)
				{
					if(ds[i].contains(".") && ds[i].length()-ds[i].indexOf(".")>3 )
					{
						ds[i] = ds[i].substring(0, ds[i].indexOf(".")+3);
						
					}
				}
				

//				String sql = "insert into history (datetime,order_book_id,open,high,low,close,volume,total_turnover)values"
//						+ "('"+ds[0]+"','"+code+"',"+ds[1]+","+ds[2]+","+ds[3]+","+ds[4]+","+ds[5]+","+ds[6]+");";
//				sd.Update(sql);
//				'datetime','open','high','low','close','volume','total_turnover' 
				
				sd.UpdateIngForHistory(ds);
			}
			sd.UpdateEnd();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	

}
