package io.github.chachen.platform.dict;

import io.github.chachen.platform.web.result.ApiResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {
    private final DictService service;

    public DictController(DictService s) {
        service = s;
    }

    @GetMapping("/{type}")
    public ApiResult<List<DictItem>> get(@PathVariable String type) {
        return ApiResult.success(service.get(type));
    }

    @PutMapping("/{type}")
    public ApiResult<Void> put(@PathVariable String type, @Valid @RequestBody List<DictItem> items) {
        service.put(type, items);
        return ApiResult.success();
    }

    @DeleteMapping("/cache")
    public ApiResult<Void> refresh(@RequestParam(required = false) String type) {
        service.refresh(type);
        return ApiResult.success();
    }
}
