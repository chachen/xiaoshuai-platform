package io.github.chachen.platform.log;
import io.github.chachen.platform.core.context.CurrentUser;
import io.github.chachen.platform.core.context.TraceContext;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint; import org.aspectj.lang.annotation.*; import org.springframework.web.context.request.*;
@Aspect public class OperationLogAspect {
    private final OperationLogWriter writer;
    public OperationLogAspect(OperationLogWriter writer){this.writer=writer;}
    @Around("@annotation(operationLog)") public Object around(ProceedingJoinPoint jp,OperationLog operationLog)throws Throwable{long begin=System.currentTimeMillis();Throwable error=null;try{return jp.proceed();}catch(Throwable t){error=t;throw t;}finally{var attrs=RequestContextHolder.getRequestAttributes();HttpServletRequest request=attrs instanceof ServletRequestAttributes s?s.getRequest():null;var user=CurrentUser.get();writer.write(new OperationLogEntry(operationLog.value(),request==null?"":request.getRequestURI(),request==null?"":request.getMethod(),user==null?null:user.id(),user==null?"":user.username(),request==null?"":request.getRemoteAddr(),TraceContext.get(),System.currentTimeMillis()-begin,error==null,error==null?null:error.getMessage(),""));}}
}
