package com.trilogyed.retail.feign;

import com.trilogyed.retail.model.LevelUp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "level-service")
public interface LevelUpClient {
    // get levelUp
    @GetMapping(value = "/levelups/{id}")
    LevelUp getLevelup(@PathVariable("id") int id);

    // get levelup object by level up id
    @GetMapping(value = "/levelups/customer/{id}")
    LevelUp getLevelUpByCustomerId(@PathVariable("id") int id);
}
