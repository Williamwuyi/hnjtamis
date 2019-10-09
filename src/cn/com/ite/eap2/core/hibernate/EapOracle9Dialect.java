package cn.com.ite.eap2.core.hibernate;

import java.sql.*;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.OracleTypesHelper;
import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.NvlFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.exception.spi.TemplatedViolatedConstraintNameExtracter;
import org.hibernate.exception.spi.ViolatedConstraintNameExtracter;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.JdbcExceptionHelper;
import org.hibernate.type.StandardBasicTypes;
import org.jboss.logging.Logger;

/**
 * <p>Title cn.com.ite.eap2.core.hibernate.EapOracle9Dialect</p>
 * <p>Description Oracle方言类</p>
 * <p>Company ITE </p>
 * <p>Copyright Copyright(c)2014</p>
 * @author 宋文科
 * @create time: May 20, 2014 11:12:54 AM
 * @version 2.0
 * 
 * @modified records:
 */
public class EapOracle9Dialect extends Dialect{
	 public EapOracle9Dialect()
	    {
	        LOG.deprecatedOracle9Dialect();
	        registerColumnType(-7, "number(1,0)");
	        registerColumnType(-5, "number(19,0)");
	        registerColumnType(5, "number(5,0)");
	        registerColumnType(-6, "number(3,0)");
	        registerColumnType(4, "number(10,0)");
	        registerColumnType(1, "char(1 char)");
	        registerColumnType(12, 4000L, "varchar2($l char)");
	        registerColumnType(12, "long");
	        registerColumnType(6, "float");
	        registerColumnType(8, "double precision");
	        registerColumnType(91, "date");
	        registerColumnType(92, "date");
	        registerColumnType(93, "timestamp");
	        registerColumnType(-3, 2000L, "raw($l)");
	        registerColumnType(-3, "long raw");
	        registerColumnType(2, "number($p,$s)");
	        registerColumnType(3, "number($p,$s)");
	        registerColumnType(2004, "blob");
	        registerColumnType(2005, "clob");
	        getDefaultProperties().setProperty("hibernate.jdbc.use_get_generated_keys", "false");
	        getDefaultProperties().setProperty("hibernate.jdbc.use_streams_for_binary", "true");
	        getDefaultProperties().setProperty("hibernate.jdbc.batch_size", "15");
	        registerFunction("abs", new StandardSQLFunction("abs"));
	        registerFunction("sign", new StandardSQLFunction("sign", StandardBasicTypes.INTEGER));
	        registerFunction("acos", new StandardSQLFunction("acos", StandardBasicTypes.DOUBLE));
	        registerFunction("asin", new StandardSQLFunction("asin", StandardBasicTypes.DOUBLE));
	        registerFunction("atan", new StandardSQLFunction("atan", StandardBasicTypes.DOUBLE));
	        registerFunction("cos", new StandardSQLFunction("cos", StandardBasicTypes.DOUBLE));
	        registerFunction("cosh", new StandardSQLFunction("cosh", StandardBasicTypes.DOUBLE));
	        registerFunction("exp", new StandardSQLFunction("exp", StandardBasicTypes.DOUBLE));
	        registerFunction("ln", new StandardSQLFunction("ln", StandardBasicTypes.DOUBLE));
	        registerFunction("sin", new StandardSQLFunction("sin", StandardBasicTypes.DOUBLE));
	        registerFunction("sinh", new StandardSQLFunction("sinh", StandardBasicTypes.DOUBLE));
	        registerFunction("stddev", new StandardSQLFunction("stddev", StandardBasicTypes.DOUBLE));
	        registerFunction("sqrt", new StandardSQLFunction("sqrt", StandardBasicTypes.DOUBLE));
	        registerFunction("tan", new StandardSQLFunction("tan", StandardBasicTypes.DOUBLE));
	        registerFunction("tanh", new StandardSQLFunction("tanh", StandardBasicTypes.DOUBLE));
	        registerFunction("variance", new StandardSQLFunction("variance", StandardBasicTypes.DOUBLE));
	        registerFunction("round", new StandardSQLFunction("round"));
	        registerFunction("trunc", new StandardSQLFunction("trunc"));
	        registerFunction("ceil", new StandardSQLFunction("ceil"));
	        registerFunction("floor", new StandardSQLFunction("floor"));
	        registerFunction("chr", new StandardSQLFunction("chr", StandardBasicTypes.CHARACTER));
	        registerFunction("initcap", new StandardSQLFunction("initcap"));
	        registerFunction("lower", new StandardSQLFunction("lower"));
	        registerFunction("ltrim", new StandardSQLFunction("ltrim"));
	        registerFunction("rtrim", new StandardSQLFunction("rtrim"));
	        registerFunction("soundex", new StandardSQLFunction("soundex"));
	        registerFunction("upper", new StandardSQLFunction("upper"));
	        registerFunction("ascii", new StandardSQLFunction("ascii", StandardBasicTypes.INTEGER));
	        registerFunction("to_char", new StandardSQLFunction("to_char", StandardBasicTypes.STRING));
	        registerFunction("to_date", new StandardSQLFunction("to_date", StandardBasicTypes.TIMESTAMP));
	        registerFunction("current_date", new NoArgSQLFunction("current_date", StandardBasicTypes.DATE, false));
	        registerFunction("current_time", new NoArgSQLFunction("current_timestamp", StandardBasicTypes.TIME, false));
	        registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", StandardBasicTypes.TIMESTAMP, false));
	        registerFunction("last_day", new StandardSQLFunction("last_day", StandardBasicTypes.DATE));
	        registerFunction("sysdate", new NoArgSQLFunction("sysdate", StandardBasicTypes.DATE, false));
	        registerFunction("systimestamp", new NoArgSQLFunction("systimestamp", StandardBasicTypes.TIMESTAMP, false));
	        registerFunction("uid", new NoArgSQLFunction("uid", StandardBasicTypes.INTEGER, false));
	        registerFunction("user", new NoArgSQLFunction("user", StandardBasicTypes.STRING, false));
	        registerFunction("rowid", new NoArgSQLFunction("rowid", StandardBasicTypes.LONG, false));
	        registerFunction("rownum", new NoArgSQLFunction("rownum", StandardBasicTypes.LONG, false));
	        registerFunction("concat", new VarArgsSQLFunction(StandardBasicTypes.STRING, "", "||", ""));
	        registerFunction("instr", new StandardSQLFunction("instr", StandardBasicTypes.INTEGER));
	        registerFunction("instrb", new StandardSQLFunction("instrb", StandardBasicTypes.INTEGER));
	        registerFunction("lpad", new StandardSQLFunction("lpad", StandardBasicTypes.STRING));
	        registerFunction("replace", new StandardSQLFunction("replace", StandardBasicTypes.STRING));
	        registerFunction("rpad", new StandardSQLFunction("rpad", StandardBasicTypes.STRING));
	        registerFunction("substr", new StandardSQLFunction("substr", StandardBasicTypes.STRING));
	        registerFunction("substrb", new StandardSQLFunction("substrb", StandardBasicTypes.STRING));
	        registerFunction("translate", new StandardSQLFunction("translate", StandardBasicTypes.STRING));
	        registerFunction("substring", new StandardSQLFunction("substr", StandardBasicTypes.STRING));
	        registerFunction("locate", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "instr(?2,?1)"));
	        registerFunction("bit_length", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "vsize(?1)*8"));
	        registerFunction("coalesce", new NvlFunction());
	        registerFunction("atan2", new StandardSQLFunction("atan2", StandardBasicTypes.FLOAT));
	        registerFunction("log", new StandardSQLFunction("log", StandardBasicTypes.INTEGER));
	        registerFunction("mod", new StandardSQLFunction("mod", StandardBasicTypes.INTEGER));
	        registerFunction("nvl", new StandardSQLFunction("nvl"));
	        registerFunction("nvl2", new StandardSQLFunction("nvl2"));
	        registerFunction("power", new StandardSQLFunction("power", StandardBasicTypes.FLOAT));
	        registerFunction("add_months", new StandardSQLFunction("add_months", StandardBasicTypes.DATE));
	        registerFunction("months_between", new StandardSQLFunction("months_between", StandardBasicTypes.FLOAT));
	        registerFunction("next_day", new StandardSQLFunction("next_day", StandardBasicTypes.DATE));
	        registerFunction("str", new StandardSQLFunction("to_char", StandardBasicTypes.STRING));
	        registerFunction("nlssort", new SQLFunctionTemplate(StandardBasicTypes.STRING, "nlssort(?1,'NLS_SORT=SCHINESE_PINYIN_M')"));  

	    }

	    public String getAddColumnString()
	    {
	        return "add";
	    }

	    public String getSequenceNextValString(String sequenceName)
	    {
	        return (new StringBuilder()).append("select ").append(getSelectSequenceNextValString(sequenceName)).append(" from dual").toString();
	    }

	    public String getSelectSequenceNextValString(String sequenceName)
	    {
	        return (new StringBuilder()).append(sequenceName).append(".nextval").toString();
	    }

	    public String getCreateSequenceString(String sequenceName)
	    {
	        return (new StringBuilder()).append("create sequence ").append(sequenceName).toString();
	    }

	    public String getDropSequenceString(String sequenceName)
	    {
	        return (new StringBuilder()).append("drop sequence ").append(sequenceName).toString();
	    }

	    public String getCascadeConstraintsString()
	    {
	        return " cascade constraints";
	    }

	    public boolean dropConstraints()
	    {
	        return false;
	    }

	    public String getForUpdateNowaitString()
	    {
	        return " for update nowait";
	    }

	    public boolean supportsSequences()
	    {
	        return true;
	    }

	    public boolean supportsPooledSequences()
	    {
	        return true;
	    }

	    public boolean supportsLimit()
	    {
	        return true;
	    }

	    public String getLimitString(String sql, boolean hasOffset)
	    {
	        sql = sql.trim();
	        boolean isForUpdate = false;
	        if(sql.toLowerCase().endsWith(" for update"))
	        {
	            sql = sql.substring(0, sql.length() - 11);
	            isForUpdate = true;
	        }
	        StringBuilder pagingSelect = new StringBuilder(sql.length() + 100);
	        if(hasOffset)
	            pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
	        else
	            pagingSelect.append("select * from ( ");
	        pagingSelect.append(sql);
	        if(hasOffset)
	            pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
	        else
	            pagingSelect.append(" ) where rownum <= ?");
	        if(isForUpdate)
	            pagingSelect.append(" for update");
	        return pagingSelect.toString();
	    }

	    public String getForUpdateString(String aliases)
	    {
	        return (new StringBuilder()).append(getForUpdateString()).append(" of ").append(aliases).toString();
	    }

	    public String getForUpdateNowaitString(String aliases)
	    {
	        return (new StringBuilder()).append(getForUpdateString()).append(" of ").append(aliases).append(" nowait").toString();
	    }

	    public boolean bindLimitParametersInReverseOrder()
	    {
	        return true;
	    }

	    public boolean useMaxForLimit()
	    {
	        return true;
	    }

	    public boolean forUpdateOfColumns()
	    {
	        return true;
	    }

	    public String getQuerySequencesString()
	    {
	        return "select sequence_name from user_sequences";
	    }

	    public String getSelectGUIDString()
	    {
	        return "select rawtohex(sys_guid()) from dual";
	    }

	    public ViolatedConstraintNameExtracter getViolatedConstraintNameExtracter()
	    {
	        return EXTRACTER;
	    }

	    public int registerResultSetOutParameter(CallableStatement statement, int col)
	        throws SQLException
	    {
	        statement.registerOutParameter(col, OracleTypesHelper.INSTANCE.getOracleCursorTypeSqlType());
	        return ++col;
	    }

	    public ResultSet getResultSet(CallableStatement ps)
	        throws SQLException
	    {
	        ps.execute();
	        return (ResultSet)ps.getObject(1);
	    }

	    public boolean supportsUnionAll()
	    {
	        return true;
	    }

	    public boolean supportsCommentOn()
	    {
	        return true;
	    }

	    public boolean supportsTemporaryTables()
	    {
	        return true;
	    }

	    public String generateTemporaryTableName(String baseTableName)
	    {
	        String name = super.generateTemporaryTableName(baseTableName);
	        return name.length() <= 30 ? name : name.substring(1, 30);
	    }

	    public String getCreateTemporaryTableString()
	    {
	        return "create global temporary table";
	    }

	    public String getCreateTemporaryTablePostfix()
	    {
	        return "on commit delete rows";
	    }

	    public boolean dropTemporaryTableAfterUse()
	    {
	        return false;
	    }

	    public boolean supportsCurrentTimestampSelection()
	    {
	        return true;
	    }

	    public String getCurrentTimestampSelectString()
	    {
	        return "select systimestamp from dual";
	    }

	    public boolean isCurrentTimestampSelectStringCallable()
	    {
	        return false;
	    }

	    public boolean supportsEmptyInList()
	    {
	        return false;
	    }

	    public boolean supportsExistsInSelect()
	    {
	        return false;
	    }

	    public int getInExpressionCountLimit()
	    {
	        return 1000;
	    }

	    public String getNotExpression(String expression)
	    {
	        return (new StringBuilder()).append("not (").append(expression).append(")").toString();
	    }

	    @SuppressWarnings("unused")
		private static final int PARAM_LIST_SIZE_LIMIT = 1000;
	    private static final CoreMessageLogger LOG = (CoreMessageLogger)Logger.getMessageLogger(
	    		org.hibernate.internal.CoreMessageLogger.class, cn.com.ite.eap2.core.hibernate.EapOracle9Dialect.class.getName());
	    private static final ViolatedConstraintNameExtracter EXTRACTER = new TemplatedViolatedConstraintNameExtracter() {

	        public String extractConstraintName(SQLException sqle)
	        {
	            int errorCode = JdbcExceptionHelper.extractErrorCode(sqle);
	            if(errorCode == 1 || errorCode == 2291 || errorCode == 2292)
	                return extractUsingTemplate("constraint (", ") violated", sqle.getMessage());
	            if(errorCode == 1400)
	                return null;
	            else
	                return null;
	        }

	    };
}
