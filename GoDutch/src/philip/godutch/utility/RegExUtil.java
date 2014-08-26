package philip.godutch.utility;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegExUtil {
	private static Pattern PATTERN_EMAIL = Pattern
			.compile("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");// 邮件地址
	// 固定电话
	private static Pattern PATTERN_TEL = Pattern
			.compile("^([0-9]{3,4}-)?[0-9]{7,8}$");
	// 移动电话
	private static Pattern PATTERN_MOBILE = Pattern
			.compile("^(\\+86)?0?1[3|5]\\d{9}$");
	// 金额
	private static Pattern PATTERN_MONEY = Pattern
			.compile("[\\d]+|[\\d]+[.]{1}[1-9]{1,2}|[\\d]+[.]{1}[0-9]{1}[1-9]{1}"); 
	// 汉字英文数字
	private static Pattern PATTERN_CHINISE_ENGGLISH_NUM = Pattern
			.compile("[a-zA-Z0-9\\u4e00-\\u9fa5]+"); 
	// 字母
	private static Pattern PATTERN_ALPHA = Pattern
			.compile("^[A-Za-z]+$");
	// 数字
	private static Pattern PATTERN_DIGITAL = Pattern
			.compile("^\\d+$");
	// 中文
	private static Pattern PATTERN_CHINESE = Pattern
			.compile("^[\\u4E00-\\u9FA5]+$");
	// 15位身份证格式
	private static Pattern PATTERN_IDCARD_15 = Pattern
			.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
	// 18位身份证格式
	private static Pattern PATTERN_IDCARD_18 = Pattern
			.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[\\d|x|X]$");
	// IP格式
	private static Pattern PATTERN_IP = Pattern
			.compile("^((00\\d|1?\\d?\\d|(2([0-4]\\d|5[0-5])))\\.){3}(00\\d|1?\\d?\\d|(2([0-4]\\d|5[0-5])))$");
	// 时间格式
	private static Pattern PATTERN_TIME = Pattern
			.compile("((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])(\\:([0-5]?[0-9]))");
	// 重复字符格式
	private static Pattern PATTERN_REPEAT = Pattern
			.compile(".*(.).*\\1.*");

	public RegExUtil() {
	}

	/**
	 * 校验email格式
	 * 
	 * @param email
	 *            email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null)
			return false;
		else
			return PATTERN_EMAIL.matcher(email).matches();
	}

	public static boolean isTelephone(String telephone) {
		if (telephone == null)
			return false;
		else
			return PATTERN_TEL.matcher(telephone).matches();
	}

	public static boolean isMobile(String mobile) {
		if (mobile == null)
			return false;
		else
			return PATTERN_MOBILE.matcher(mobile).matches();
	}
	
	public static boolean isMoney(String money) {
		if (money == null)
			return false;
		else
			return PATTERN_MONEY.matcher(money).matches();
	}
	
	public static boolean isChineseEnglishNum(String chiniseEnglichNum) {
		if (chiniseEnglichNum == null)
			return false;
		else
			return PATTERN_CHINISE_ENGGLISH_NUM.matcher(chiniseEnglichNum).matches();
	}

	public static boolean isAlpha(String alpha) {
		if (alpha == null)
			return false;
		else
			return PATTERN_ALPHA.matcher(alpha).matches();
	}

	public static boolean isDigital(String digital) {
		if (digital == null)
			return false;
		else
			return PATTERN_DIGITAL.matcher(digital).matches();
	}

	public static boolean isChinese(String chinese) {
		if (chinese == null)
			return false;
		else
			return PATTERN_CHINESE.matcher(chinese).matches();
	}

	public static boolean isDateTime(String dateTime) {
		return isDateTime(dateTime, "-");
	}

	/**
	 * 校验日期时间 月 日 时间 可以不写前面的0 ,例如 2008-9-2 3:9:1
	 * 
	 * @param dateTime
	 * @param partition
	 *            分隔符 这里只支持 /、/、-、空格 四类分隔符，如果是长度大于1，则取第一个字符作为分隔符
	 * @return 如何符合格式，返回true
	 */
	public static boolean isDateTime(String dateTime, String partition) {  
        if (dateTime == null || partition == null || "".equals(partition))  
            return false;  
        String s = "";  
  
        char split = partition.charAt(0);  
        if (split != '/' && split != '/' && split != '-' && split != ' ')  
            throw new IllegalArgumentException((new StringBuilder(  
                    "partition can not start with '")).append(partition)  
                   .append("'!").toString());  
        s = (new StringBuilder(String.valueOf(s))).append(split).toString();  
 
        StringBuilder part = new StringBuilder(  
               "^((//d{2}(([02468][048])|([13579][26]))");  
        part.append(s);  
        part.append("((((0?[13578]");  
        part.append(")|(1[02]))");  
        part.append(s);  
        part.append("((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[4");  
        part.append("69])|(11))");  
        part.append(s);  
        part.append("((0?[1-9])|([1-2][0-9])|(30)))|(0?2[//-/////");  
        part.append("s]?((0?[1-9])|([1-2][0-9])))))|(//d{2}(([02468][1235679])|([1");  
        part.append("3579][01345789]))");  
        part.append(s);  
        part.append("((((0?[13578])|(1[02]))");  
        part.append(s);  
        part.append("((");  
        part.append("0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");  
        part.append(s);  
        part.append("((");  
        part.append("0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");  
        part.append(s);  
        part.append("((");  
        part.append("0?[1-9])|([1-2][0-9])|(30)))|(0?2");  
        part.append(s);  
        part.append("((0?[1-9])|(1[0-9])|(2[0-8]))))))");  
        part.append("(//s(((0?[0-9])|([1-2][0-3]))//:([0-5]?[0-9])//:([0-5]?[0-9])))?");  
       return Pattern.matches(part.toString(), dateTime);  
   }
	public static boolean isDate(String date) {
		return isDate(date, "-");
	}

	/**
	 * 校验日期 月 日 可以不写前面的0 ,例如 2008-9-2 3:9:1
	 * 
	 * @param date
	 * @param partition
	 *            分隔符 这里只支持 /、/、-、空格 四类分隔符，如果是长度大于1，则取第一个字符作为分隔符
	 * @return 如何符合格式，返回true
	 */
	public static boolean isDate(String date, String partition) {  
        if (date == null || partition == null || "".equals(partition))  
            return false;  
       String s = "";  
  
        char split = partition.charAt(0);  
       if (split != '/' && split != '/' && split != '-' && split != ' ')  
          throw new IllegalArgumentException((new StringBuilder(  
                    "partition can not start with '")).append(partition)  
                    .append("'!").toString());  
        s = (new StringBuilder(String.valueOf(s))).append(split).toString();  
  
        StringBuilder part = new StringBuilder(  
                "^((//d{2}(([02468][048])|([13579][26]))");  
       part.append(s);  
        part.append("((((0?[13578]");  
        part.append(")|(1[02]))");  
        part.append(s);  
        part.append("((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[4");  
        part.append("69])|(11))");  
        part.append(s);  
        part.append("((0?[1-9])|([1-2][0-9])|(30)))|(0?2[//-/////");  
        part.append("s]?((0?[1-9])|([1-2][0-9])))))|(//d{2}(([02468][1235679])|([1");  
        part.append("3579][01345789]))");  
        part.append(s);  
        part.append("((((0?[13578])|(1[02]))");  
        part.append(s);  
        part.append("((");  
        part.append("0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");  
        part.append(s);  
        part.append("((");  
        part.append("0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");  
        part.append(s);  
        part.append("((");  
        part.append("0?[1-9])|([1-2][0-9])|(30)))|(0?2");  
        part.append(s);  
        part.append("((0?[1-9])|(1[0-9])|(2[0-8]))))))$");  
        return Pattern.matches(part.toString(), date);  
    }
	/**
	 * 校验时间 时间可以不写前面的0 如 9:3:1
	 * 
	 * @param time
	 *            时间
	 * @return
	 */
	public static boolean isTime(String time) {
		if (time == null)
			return false;
		else
			return PATTERN_TIME.matcher(time).matches();
	}

	/**
	 * 严格校验身份证的格式，支持15位和18位校验
	 * 
	 * @param card
	 * @return
	 */
	public static boolean isIdCard(String card) {
		if (card == null)
			return false;
		int length = card.length();
		if (length == 15) {// 15位号码身份证
			if (!PATTERN_IDCARD_15.matcher(card).matches())
				return false;
			if (!addressCode.containsKey(card.substring(0, 2)))
				return false;
			String birthday = (new StringBuilder("19"))
					.append(card.substring(6, 8)).append("-")
					.append(card.substring(8, 10)).append("-")
					.append(card.substring(10, 12)).toString();
			if (!isDate(birthday))
				return false;
		} else if (length == 18) {// 18位号码身份证
			if (!PATTERN_IDCARD_18.matcher(card).matches())// 格式不匹配
				return false;
			if (!addressCode.containsKey(card.substring(0, 2)))// 不符合地区码
				return false;
			String birthday = (new StringBuilder(card.substring(6, 10)))
					.append("-").append(card.substring(10, 12)).append("-")
					.append(card.substring(12, 14)).toString();
			if (!isDate(birthday))
				return false;
			int sum = 0;
			for (int i = 0; i < length - 1; i++)
				sum += (card.charAt(i) - 48) * idCoefficient[i];

			char mod = idMod[sum % 11];
			if (mod != Character.toLowerCase(card.charAt(17)))
				return false;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 校验IP格式
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isIP(String ip) {
		if (ip == null)
			return false;
		else
			return PATTERN_IP.matcher(ip).matches();
	}

	/**
	 * 校验是否有重复字符
	 * 
	 * @param repeat
	 * @return
	 */
	public static boolean hasRepeat(String repeat) {
		if (repeat == null)
			return false;
		else
			return PATTERN_REPEAT.matcher(repeat).matches();
	}

	public static Map<String, String> addressCode;
	public static int idCoefficient[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9,
			10, 5, 8, 4, 2 };
	public static char idMod[] = { '1', '0', 'x', '9', '8', '7', '6', '5', '4',
			'3', '2' };

	static {
		addressCode = new HashMap<String, String>(35);
		addressCode.put("11", "/u5317/u4EAC");
		addressCode.put("12", "/u5929/u6D25");
		addressCode.put("13", "/u6CB3/u5317");
		addressCode.put("14", "/u5C71/u897F");
		addressCode.put("15", "/u5185/u8499/u53E4");
		addressCode.put("21", "/u8FBD/u5B81");
		addressCode.put("22", "/u5409/u6797");
		addressCode.put("23", "/u9ED1/u9F99/u6C5F");
		addressCode.put("31", "/u4E0A/u6D77");
		addressCode.put("32", "/u6C5F/u82CF");
		addressCode.put("33", "/u6D59/u6C5F");
		addressCode.put("34", "/u5B89/u5FBD");
		addressCode.put("35", "/u798F/u5EFA");
		addressCode.put("36", "/u6C5F/u897F");
		addressCode.put("37", "/u5C71/u4E1C");
		addressCode.put("41", "/u6CB3/u5357");
		addressCode.put("42", "/u6E56/u5317");
		addressCode.put("43", "/u6E56/u5357");
		addressCode.put("44", "/u5E7F/u4E1C");
		addressCode.put("45", "/u5E7F/u897F");
		addressCode.put("46", "/u6D77/u5357");
		addressCode.put("50", "/u91CD/u5E86");
		addressCode.put("51", "/u56DB/u5DDD");
		addressCode.put("52", "/u8D35/u5DDE");
		addressCode.put("53", "/u4E91/u5357");
		addressCode.put("54", "/u897F/u85CF");
		addressCode.put("61", "/u9655/u897F");
		addressCode.put("62", "/u7518/u8083");
		addressCode.put("63", "/u9752/u6D77");
		addressCode.put("64", "/u5B81/u590F");
		addressCode.put("65", "/u65B0/u7586");
		addressCode.put("71", "/u53F0/u6E7E");
		addressCode.put("81", "/u9999/u6E2F");
		addressCode.put("82", "/u6FB3/u95E8");
		addressCode.put("91", "/u56FD/u5916");
	}
}
