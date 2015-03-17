package com.innofi.component.codeauxiliarytool.coder.util;

import java.io.*;
import java.util.Map;
import java.util.Properties;

import com.bstek.dorado.core.Context;
import com.innofi.framework.utils.resource.ResourceUtil;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class WriteUtil {
    public static void writeFile(String template, String out, Map map) {
        OutputStreamWriter writer = null;
        InputStreamReader reader = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(out, false),"UTF-8");

            Context context = Context.getCurrent();
            com.bstek.dorado.core.io.Resource resource = context.getResource("com/innofi/component/codeauxiliarytool/template/" + template);
            reader = new InputStreamReader(resource.getInputStream(), "UTF-8");

            Properties properties = new Properties();
            properties.put("input.encoding", "UTF-8");
            properties.put("output.encoding", "UTF-8");

            Velocity.init(properties);
            VelocityContext velocityContext = new VelocityContext(map);
            Velocity.evaluate(velocityContext, writer, "", reader);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}