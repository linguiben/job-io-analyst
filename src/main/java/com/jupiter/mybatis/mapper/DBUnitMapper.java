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
@Mapper /*3.4.0֧��*/
public interface DBUnitMapper {

	public List<String> checkJobnameByInput(@Param("txt") String txt);
	public int addJobIno(EtlJobIno etljobino);
    public int deleteJobInoByJobname(String jobname);
    
    //���汾�Ƿ����
    public String isVersionUseable(double version);

  //��ȡ���°汾
    public double getLastVersion();
    
    public int getLoginStatus(String userName,String password) ;
}
