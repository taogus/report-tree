package com.ureport.ureportkeep.controller.designer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ureport.ureportkeep.console.common.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: summer
 * @Date: 2022/3/5 18:13
 * @Description:
 **/
@RestController
@RequestMapping("/api/test")
public class TestDesignerController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/test_tree")
    public List testTreeJson() {
        try {
            return objectMapper.readValue("[{\"text\":\"Parent 1\",\"nodes\":[{\"text\":\"Child 1\",\"nodes\":[{\"text\":\"Grandchild 1\"},{\"text\":\"Grandchild 2\"}]},{\"text\":\"Child 2\"}]},{\"text\":\"Parent 2\"},{\"text\":\"Parent 3\"},{\"text\":\"Parent 4\"},{\"text\":\"Parent 5\"}]", List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

}
