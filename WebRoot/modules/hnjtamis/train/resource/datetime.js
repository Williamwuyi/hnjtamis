/**
 * 取当前系统时间
 * 
 * @return
 */
function curDateTime() {
	var d = new Date();
	var year = d.getYear();
	var month = d.getMonth() + 1;
	var date = d.getDate();
	var day = d.getDay();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	var seconds = d.getSeconds();
	var ms = d.getMilliseconds();
	var curDateTime = year;
	if (month > 9)
		curDateTime = curDateTime + "-" + month;
	else
		curDateTime = curDateTime + "-0" + month;
	if (date > 9)
		curDateTime = curDateTime + "-" + date;
	else
		curDateTime = curDateTime + "-0" + date;
	if (hours > 9)
		curDateTime = curDateTime + " " + hours;
	else
		curDateTime = curDateTime + " 0" + hours;
	if (minutes > 9)
		curDateTime = curDateTime + ":" + minutes;
	else
		curDateTime = curDateTime + ":0" + minutes;
	if (seconds > 9)
		curDateTime = curDateTime + ":" + seconds;
	else
		curDateTime = curDateTime + ":0" + seconds;
	return curDateTime;
}

/**
 * 日期格式化
 * 
 * @param format
 * @return
 */
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	}

	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	}

	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}

/**
 * 两个时间之间相差的分钟数
 * 
 * @param date1
 *            日期型
 * @param date2
 *            日期型
 * @return
 */
function secondsBetween(date1, date2) {
	var differ = Math.abs(date2.getTime() - date1.getTime()); // 时间差的毫秒数

	var minutes = Math.ceil(differ / 1000);
	return minutes;
}

/**
 * 两个时间之间相差的分钟数
 * 
 * @param strDate1
 *            字符串型
 * @param strDate2
 *            字符串型
 * @return
 */
function secondsBetweenStr(strDate1, strDate2) {
	var date1 = new Date(strDate1.replace(/-/g, "/"));
	var date2 = new Date(strDate2.replace(/-/g, "/"));

	return minutesBetween(date1, date2);
}