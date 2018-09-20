package org.arxing.simpleprocessor_compile.helper;

import java.util.HashMap;
import java.util.Map;

public class ParamBox {
    Map<String, Object> map = new HashMap<>();

    private ParamBox() {

    }

    public static ParamBox build() {
        return new ParamBox();
    }

    public ParamBox add(String name, Object val) {
        map.put(name, val);
        return this;
    }
}
