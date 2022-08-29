package se.hernebring.chengyu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.hernebring.chengyu.engine.ChengYuEngine;

import java.util.Map;

@Service
public class ChengYuService {

    ChengYuEngine chengYuEngine = new ChengYuEngine();

    public String find(String text) {
        StringBuilder sb = new StringBuilder("Count,ChengYu\n");
        var map = chengYuEngine.createMap(text);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            sb.append(entry.getValue());
            sb.append(',');
            sb.append(entry.getKey());
        }
        return sb.toString();
    }
}
