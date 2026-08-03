package io.github.chachen.platform.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.github.chachen.platform.core.auth.Account;
import io.github.chachen.platform.core.auth.AccountProvider;
import io.github.chachen.platform.system.entity.SysUser;
import io.github.chachen.platform.system.mapper.SysUserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class UserService implements AccountProvider {
    private final SysUserMapper mapper;
    private final PasswordEncoder encoder;

    public UserService(SysUserMapper mapper, PasswordEncoder encoder) {
        this.mapper = mapper;
        this.encoder = encoder;
    }

    @Override
    public Optional<Account> findByUsername(String username) {
        SysUser u = mapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username).last("limit 1"));
        if (u == null) return Optional.empty();
        Set<String> permissions = new HashSet<>(mapper.findPermissionCodes(u.getId()));
        permissions.add("ROLE_USER");
        return Optional.of(new Account(u.getId(), u.getUsername(), u.getPassword(), Integer.valueOf(1).equals(u.getStatus()), Integer.valueOf(1).equals(u.getLocked()), permissions));
    }

    @Transactional
    public SysUser create(String username, String password, String nickname) {
        if (mapper.selectCount(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username)) > 0)
            throw new IllegalArgumentException("用户名已存在");
        SysUser u = new SysUser();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setNickname(nickname);
        u.setStatus(1);
        u.setLocked(0);
        mapper.insert(u);
        return u;
    }

    public List<UserView> list() {
        return mapper.selectList(null).stream().map(UserView::from).toList();
    }

    public void setStatus(Long id, boolean enabled) {
        SysUser u = mapper.selectById(id);
        if (u == null) throw new NoSuchElementException("用户不存在");
        u.setStatus(enabled ? 1 : 0);
        mapper.updateById(u);
    }

    public record UserView(Long id, String username, String nickname, Integer status, Integer locked) {
        static UserView from(SysUser u) {
            return new UserView(u.getId(), u.getUsername(), u.getNickname(), u.getStatus(), u.getLocked());
        }
    }
}
