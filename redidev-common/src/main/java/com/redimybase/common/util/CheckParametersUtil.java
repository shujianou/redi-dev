package com.redimybase.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 参数校验
 * 例子:
 * CheckParametersUtil.getInstance()
 *          .put(loginUserId, "loginUserId")
 *          .put(branId, "branId")
 *          .put(shelfNo, "shelfNo")
 *          .put(newShelfNo, "newShelfNo")
 *          .checkParameter();
 * @ClassName: CheckParametersUtil
 */
public class CheckParametersUtil {

  Map<String, Object> map = new HashMap<>();

  /**
   * 添加需要校验的参数
   * @param object 实参
   * @param parameterName 参数名称
   * @return CheckParametersUtil
   * 
   */
  public CheckParametersUtil put(Object object, String parameterName) {
    map.put(parameterName, object);
    return this;
  }
  /**
   * 获取CheckParametersUtil实例
   * @return CheckParametersUtil
   * 
   */
  public static CheckParametersUtil getInstance(){
    return new CheckParametersUtil();
  }

  /**
   * 校验
   * @return DataMessage
   * 
   * @throws Exception
   */
  public void checkParameter() throws Exception {
    for(Entry<String, Object> entry : map.entrySet()) {
      if(isEmpty(entry.getValue())){
        throw new Exception("参数【" + entry.getKey() + "】不能为空" );
      }
    }
  }

  public String toString(Object object) {
    return object == null ? "" : object.toString();
  }

  public boolean isEmpty(Collection collection) {
    return collection == null || collection.isEmpty();
  }

  public boolean isEmpty(Map map) {
    return map == null || map.isEmpty();
  }

  public boolean isEmpty(String string) {
    return toString(string).isEmpty();
  }

  public boolean isEmptyTrim(String string) {
    return toString(string).trim().isEmpty();
  }

  public boolean isEmpty(Object object) {
    return toString(object).isEmpty();
  }

  public boolean isEmptyTrim(Object object) {
    return toString(object).trim().isEmpty();
  }

  public <T> boolean isEmpty(T[] array) {
    return array == null || array.length == 0;
  }

}
