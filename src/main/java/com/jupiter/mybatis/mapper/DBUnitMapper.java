/**
 * 
 */
package com.jupiter.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.jupiter.mybatis.po.EtlJobIno;

/**
 * @Description:
 * @author: Jupiter
 * @date @time
 */
@Mapper /*3.4.0支持*/
public interface DBUnitMapper {

	public List<String> checkJobnameByInput(@Param("txt") String txt);
	public int addJobIno(EtlJobIno etljobino);
    public int deleteJobInoByJobname(String jobname);
    
    //检查版本是否可用
    public String isVersionUseable(double version);

  //获取最新版本
    public double getLastVersion();
    
    public int getLoginStatus(String userName,String password) ;
}
