package io.github.chachen.platform.dict;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import java.util.*;
public class RedisDictService extends DictService {
    private final StringRedisTemplate redis; private final ObjectMapper mapper;
    public RedisDictService(StringRedisTemplate redis,ObjectMapper mapper){this.redis=redis;this.mapper=mapper;}
    private String key(String type){return "xs:dict:"+type;}
    @Override public List<DictItem> get(String type){try{String value=redis.opsForValue().get(key(type));return value==null?List.of():mapper.readValue(value,new TypeReference<List<DictItem>>(){}).stream().filter(DictItem::enabled).sorted(Comparator.comparing(DictItem::sort)).toList();}catch(Exception e){throw new IllegalStateException("字典缓存读取失败",e);}}
    @Override public void put(String type,List<DictItem> items){try{redis.opsForValue().set(key(type),mapper.writeValueAsString(items));}catch(Exception e){throw new IllegalStateException("字典缓存写入失败",e);}}
    @Override public void refresh(String type){if(type==null)throw new IllegalArgumentException("Redis 字典缓存刷新必须指定 type");redis.delete(key(type));}
}
