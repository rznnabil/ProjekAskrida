package com.askrida.web.service.conf;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.askrida.web.service.common.DTOMap;


public class JdbcTemplate extends org.springframework.jdbc.core.JdbcTemplate {
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	public JdbcTemplate(DriverManagerDataSource ds) {
		super(ds);
	}
	
	/// ini yang di pake
//	public JdbcTemplate(@Qualifier("db1") DataSource ds) {
//		super(ds);
//	}

	@Override
	public Object query(String sql, Object[] args, int[] argTypes,
			ResultSetExtractor rse) throws DataAccessException {
		log.info(sql);
		return super.query(sql, args, argTypes, rse);
	}

	@Override
	public void query(String sql, Object[] args, int[] argTypes,
			RowCallbackHandler rch) throws DataAccessException {
		log.info(sql);
		super.query(sql, args, argTypes, rch);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String sql, Object[] args, int[] argTypes,
			RowMapper rowMapper) throws DataAccessException {
		log.info(sql);
		return super.query(sql, args, argTypes, rowMapper);
	}

	@Override
	public Object query(String sql, PreparedStatementSetter pss,
			ResultSetExtractor rse) throws DataAccessException {
		log.info(sql);
		return super.query(sql, pss, rse);
	}

	@Override
	public void query(String sql, PreparedStatementSetter pss,
			RowCallbackHandler rch) throws DataAccessException {
		log.info(sql);
		super.query(sql, pss, rch);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List query(String sql, PreparedStatementSetter pss,
			RowMapper rowMapper) throws DataAccessException {
		log.info(sql);
		return super.query(sql, pss, rowMapper);
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List query(String sql, RowMapper rowMapper)
			throws DataAccessException {
		log.info(sql);
		return super.query(sql, rowMapper);
	}

	@Override
	public int update(String sql, Object[] args, int[] argTypes)
			throws DataAccessException {
		Integer retVal = null;
		try {
			log.info(sql);
			retVal = super.update(sql, args, argTypes);
			commit();
			return retVal;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retVal;
	}

	@Override
	public int update(String sql, Object[] args) throws DataAccessException {
		Integer retVal = null;
		try {
			log.info(sql);
			retVal = super.update(sql, args);
			commit();
			return retVal;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retVal;
	}

	@Override
	public int update(String sql) throws DataAccessException {
		Integer retVal = null;
		try {
			log.info(sql);
			retVal = super.update(sql);
			commit();
			return retVal;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	public int update(String sql, Object[] args, int[] argTypes, Boolean isCommit)
			throws DataAccessException, SQLException {
		log.info(sql);
		Integer retVal = super.update(sql, args, argTypes);
		if(isCommit)
			commit();
		return retVal;
	}

	public int update(String sql, Object[] args, Boolean isCommit) throws DataAccessException, SQLException {
		log.info(sql);
		Integer retVal = super.update(sql, args);
		if(isCommit)
			commit();
		return retVal;
	}

	public int update(String sql, Boolean isCommit) throws DataAccessException, SQLException {
		log.info(sql);
		Integer retVal = super.update(sql);
		if(isCommit)
			commit();
		return retVal;
	}
	
	public void insertData(DTOMap dtoMap, String tblName, Boolean isCommit) throws DataAccessException, SQLException {
		List<Object> param=new ArrayList<Object>();
		String sql="Insert Into "+tblName +" (";
		for (String key: dtoMap.map.keySet()) {
			sql+=key+",";
			param.add(dtoMap.get(key));
		}
		sql=sql.substring(0, sql.length()-1) + ") Values(";
		for (int i = 0; i < dtoMap.map.keySet().size(); i++) {
			sql+="?,";
		}
		sql=sql.substring(0, sql.length()-1) + ")";
		System.out.println("sgl++++++"+sql);
		Object[] paramO=param.toArray(new Object[param.size()]);
		
		update(sql,paramO,isCommit);
	}
	
	public void updateData(DTOMap dtoMap, String tblName, List<String> whereCol, Boolean isCommit) throws DataAccessException, SQLException {
		List<Object> param=new ArrayList<Object>();
		String sql="Update "+tblName+" Set ";
		for (String key : dtoMap.map.keySet()) {
			if (!whereCol.contains(key)){
				sql+=key+"=?,";
				param.add(dtoMap.get(key));
			}
		}
		
		sql=sql.substring(0, sql.length()-1)+" Where 1=1 ";
		for (int i = 0; i < whereCol.size(); i++) {
			sql+=" and "+whereCol.get(i)+"=? ";
			param.add(dtoMap.get(whereCol.get(i)));
		}
		
//		for (String wc : whereCol) {
//			sql+=wc+"=? and ";
//			param.add(dtoMap.get(wc));
//		}
		sql=sql.substring(0, sql.length());
		Object[] paramO=param.toArray(new Object[param.size()]);
		update(sql,paramO,isCommit);
	}
	public void commit() throws SQLException{
		super.getDataSource().getConnection().commit();
		
	}
	
	public void rollback(){
		try {
			super.getDataSource().getConnection().rollback();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @author Andri Rahmansyah
	 * Mendapatkan satu nilai dari satu query
	 * @param colName:Nama Colom
	 * @param tblName:Nama Tabel
	 * @param condition:Kondisi Where (cth: id=?)
	 * @param param:Parameter
	 */
	public Object getSingleValue(String colName, String tblName, String condition, Object[] param){
		try{
			return queryForObject("Select "+colName+" From "+tblName+" Where 1=1 and "+condition, param, Object.class);
		} catch (EmptyResultDataAccessException e){
			return null;
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Object> getValue(String colName, String tblName, String condition, Object[] param, RowMapper dto){
		try{
			return query("Select "+colName+" From "+tblName+" Where 1=1 and "+condition, param, dto);
		} catch (EmptyResultDataAccessException e){
			return null;
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Object[] getSingleValues(String[] colNames, String tblName, String condition, Object[] param){
		try{
			String sql="Select ";
			for (String colName : colNames) {
				sql+=colName+",";
			}
			sql=sql.substring(0, sql.length()-1);
			log.info(sql);
			return (Object[]) queryForObject(sql+" From "+tblName+" Where 1=1 and "+condition, param, new RowMapperObject(colNames.length));
		} catch (EmptyResultDataAccessException e){
			return null;
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Mendapatkan id dan nama untuk combobox
	 * @author Andri Rahmansyah
	 * @param colId
	 * @param colName
	 * @param tblName
	 * @return String[] {colId - colName}
	 */
	@SuppressWarnings("unchecked")
	public String[] getIdNameFromTable(String colId, String colName, String tblName){
		try{
			String sql="Select "+colId+","+colName+" From "+tblName;
			List<String> list=query(sql, new RowMapper(){
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					return rs.getString(1) + " - " + rs.getString(2);
				}
			});
			log.info(sql);
			return list.toArray(new String[list.size()]);
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Mendapatkan id dan nama untuk combobox
	 * @author Andri Rahmansyah
	 * @param colId
	 * @param colName
	 * @param tblName
	 * @return String[] {colId - colName}
	 */
	public String[] getIdNameFromTable(String tblName){
		try{
			Properties tableMaster=new Properties();
			tableMaster.load(JdbcTemplate.class.getResourceAsStream("/com/master-table.properties"));
			String[] cols=tableMaster.getProperty(tblName).split(",");
			
			return getIdNameFromTable(cols[0], cols[1], tblName);
		} catch (Exception e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public Date getServerTime(){
		return new Date();
	}
	

}
