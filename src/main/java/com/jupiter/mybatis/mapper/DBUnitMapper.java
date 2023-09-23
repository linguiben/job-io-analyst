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

    List<String> checkJobnameByInput(@Param("txt") String txt);

    int addJobIno(EtlJobIno etljobino);

    int deleteJobInoByJobname(String jobname);

    //检查版本是否可用
    String isVersionUseable(double version);

    //获取最新版本
    double getLastVersion();

    int getLoginStatus(String userName, String password);
}
