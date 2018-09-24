package org.arxing.simpleprocessor_compile.helper;

import com.annimon.stream.Stream;

import org.stringtemplate.v4.ST;

class Utils {

    public static String renderCode(String template, ParamBox params){
        ST st = new ST(template);
        Stream.of(params.map).forEach(entry -> st.add(entry.getKey(), entry.getValue()));
        return st.render();
    }
}
