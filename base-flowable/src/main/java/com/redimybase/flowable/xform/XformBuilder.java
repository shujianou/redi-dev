package com.redimybase.flowable.xform;

import com.alibaba.fastjson.JSONObject;
import com.redimybase.common.util.SecurityUtils;
import com.redimybase.flowable.xform.keyvalue.Prop;
import com.redimybase.flowable.xform.keyvalue.Record;
import com.redimybase.manager.security.entity.UserEntity;
import com.redimybase.manager.security.service.UserService;
import com.redimybase.security.utils.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class XformBuilder {
    private Xform xform;

    public XformBuilder setContent(String content) {
        xform = new Xform();
        xform.setContent(content);
        log.debug("content : {}", content);

        try {
            this.handleStructure();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }

        return this;
    }

    public XformBuilder setRecord(Record record) {
        if (record == null) {
            log.info("record is null");

            return this;
        }

        for (Prop prop : record.getProps().values()) {
            String name = prop.getCode();
            String value = prop.getValue();
            XformField xformField = xform.findXformField(name);

            if (xformField == null) {
                continue;
            }

            String type = xformField.getType();

            if ("fileupload".equals(type)) {
                //TODO 如果是上传文件则保存到文件服务器
            } else if ("userpicker".equals(type)) {
                xformField.setValue(value);

                StringBuilder buff = new StringBuilder();

                for (String userId : value.split(",")) {
                    if (StringUtils.isBlank(userId)) {
                        continue;
                    }

                    UserEntity userEntity = SecurityUtil.getCurrentUser();

                    if (null==userEntity) {
                        continue;
                    }

                    buff.append(userEntity.getUserName()).append(",");
                }

                if (buff.length() > 0) {
                    buff.deleteCharAt(buff.length() - 1);
                }

                xformField.setLabel(buff.toString());
            } else {
                xformField.setValue(value);
            }
        }

        return this;
    }

    public Xform build() {
        return xform;
    }

    public void handleStructure() throws Exception {
        if (xform.getContent() == null) {
            log.info("cannot find xform content");

            return;
        }

        Map map = JSONObject.parseObject(xform.getContent(), Map.class);
        log.debug("map : {}", map);

        if (map == null) {
            log.info("cannot find map");

            return;
        }

        List<Map> sections = (List<Map>) map.get("sections");
        log.debug("sections : {}", sections);

        Map<String, String> formTypeMap = new HashMap<String, String>();

        for (Map section : sections) {
            if (!"grid".equals(section.get("type"))) {
                continue;
            }

            List<Map> fields = (List<Map>) section.get("fields");

            for (Map field : fields) {
                this.handleField(field);
            }
        }
    }

    public void handleField(Map map) {
        XformField xformField = new XformField();
        xformField.setName((String) map.get("name"));
        xformField.setType((String) map.get("type"));
        xform.addXformField(xformField);
    }

}
