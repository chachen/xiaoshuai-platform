package io.github.chachen.platform.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.chachen.platform.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    @Select("select distinct p.code from xs_sys_permission p join xs_sys_role_permission rp on rp.permission_id=p.id join xs_sys_user_role ur on ur.role_id=rp.role_id where ur.user_id=#{userId} and p.status=1")
    List<String> findPermissionCodes(Long userId);
}
