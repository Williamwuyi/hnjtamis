/**解决中文排序问题,适应前排序**/
Ext.apply(Ext.data.Store.prototype,{
	createComparator:function(sorters) {
		return function(r1, r2) {
			var s = sorters[0], f = s.property;
			var v1 = r1.data[f], v2 = r2.data[f];
			var result = 0;
			if (typeof(v1) == "string") {
				result = v1.localeCompare(v2);
				if (s.direction == 'DESC') {
					result *= -1;
				}
			} else {
				result = sorters[0].sort(r1, r2);
			}
			var length = sorters.length;
			for (var i = 1; i < length; i++) {
				s = sorters[i];
				f = s.property;
				v1 = r1.data[f];
				v2 = r2.data[f];
	            if (typeof(v1) == "string") {
	                result = result || v1.localeCompare(v2);
					if (s.direction == 'DESC') {
						result *= -1;
					}
	            } else {
					result = result || s.sort.call(this, r1, r2);
				}
			}
			return result;
		};
    }
});
/**在oracle 9i之前,对中文的排序，是默认按2进制编码来进行排序的. 9i时增加了几种新的选择:
1.按中文拼音进行排序:SCHINESE_PINYIN_M
2.按中文部首进行排序:SCHINESE_RADICAL_M
3.按中文笔画进行排序:SCHINESE_STROKE_M

而oracle 9i是对中文的排序是默认按拼音排序(并不是指NLS_SORT = SCHINESE_PINYIN_M,而是说SQL中不指定NLS_SORT时对中文列排序时默认按拼音)的，跟之前的2进制编码排序有所不同.具体用法如下:
1.直接写在sql中,例如: 1.SELECT * FROM TEAM ORDER BY NLSSORT(排序字段名,'NLS_SORT = SCHINESE_PINYIN_M');
2.SELECT * FROM TEAM ORDER BY NLSSORT(排序字段名,'NLS_SORT = SCHINESE_STROKE_M');
3.SELECT * FROM TEAM ORDER BY NLSSORT(排序字段名,'NLS_SORT = SCHINESE_RADICAL_M');

2.配置在初始化参数NLS_SORT中,这可以在数据库创建时指定,也可以通过alter session来修改.如果是前者，则在所有session中生效.
* 例如: 1.使用select * from NLS_SESSION_PARAMETERS;语句可以看到NLS_SORT的值.
       2.更改配置文件:alter system set nls_sort='SCHINESE_PINYIN_M' scope=spfile;
       3.更改session:alter SESSION set NLS_SORT = SCHINESE_PINYIN_M;

 这里要额外注意一下性能问题,按oracle官方文档的解释,oracle在对中文列建立索引时,是按照2进制编码进行排序的,所以如果NLS_SORT被设置为BINARY时，排序则可以利用索引.如果不是2进制排序，而是使用上面介绍的3种针对中文的特殊排序，则oracle无法使用索引，会进行全表扫描.这点一定要注意,多用plsql工具比较一下执行效率.解决方法是,在此列上建立linguistic index.例如:CREATE INDEX nls_index ON my_table (NLSSORT(name, 'NLS_SORT = SCHINESE_PINYIN_M'));
 
**/