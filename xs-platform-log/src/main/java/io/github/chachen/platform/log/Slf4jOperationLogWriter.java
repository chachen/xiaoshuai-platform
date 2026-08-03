package io.github.chachen.platform.log;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
public class Slf4jOperationLogWriter implements OperationLogWriter { private static final Logger log=LoggerFactory.getLogger(Slf4jOperationLogWriter.class); @Override public void write(OperationLogEntry e){log.info("operation={} path={} method={} user={} traceId={} durationMs={} success={}",e.title(),e.path(),e.httpMethod(),e.username(),e.traceId(),e.durationMs(),e.success());} }
