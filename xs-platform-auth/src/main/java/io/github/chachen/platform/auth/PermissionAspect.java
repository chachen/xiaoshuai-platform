package io.github.chachen.platform.auth;
import io.github.chachen.platform.web.exception.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint; import org.aspectj.lang.annotation.*; import org.springframework.security.core.context.SecurityContextHolder;
@Aspect public class PermissionAspect {
    @Around("@annotation(permission)") public Object check(ProceedingJoinPoint point,RequirePermission permission)throws Throwable{var auth=SecurityContextHolder.getContext().getAuthentication();if(auth==null||auth.getAuthorities().stream().noneMatch(a->a.getAuthority().equals(permission.value()))){throw new BusinessException("COMMON_FORBIDDEN","无权访问");}return point.proceed();}
}
