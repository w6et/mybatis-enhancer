/**
 *    Copyright 2009-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.utils;

import java.util.Set;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class MySqlHelper extends SqlHelper {

    public static String warpBySqlElement(String xmlString, String id) {
        return "<sql id=\"" + id + "\">" + xmlString + "</sql>";
    }

    public static String sqlGroupBy() {
        String sqlRestraint = "<sql id=\"sql_group\"><if test=\"groupBy != null\">group by ${groupBy}</if></sql>";
        return sqlRestraint;
    }
    
    public static String sqlRestraint() {
        String sqlRestraint = "<sql id=\"sql_restraint\"><if test=\"groupBy != null\">group by ${groupBy}</if><if test=\"orderBy != null\">order by ${orderBy}</if><choose><when test=\"limit != null and limit gt 0 \">limit<choose><when test=\"offset != null and offset gt 0 \">#{offset},#{limit}</when><otherwise>0,#{limit}</otherwise></choose></when><otherwise>limit 0,100</otherwise></choose></sql>";
        return sqlRestraint;
    }

    /**
     * where所有列的条件，会判断是否!=null
     *
     * @param entityClass
     * @return
     */
    public static String baseResultMap(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<resultMap id=\"BaseResultMap\" type=\"" + entityClass.getName() + "\">");
        // 获取全部列
        Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
        // 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
        for (EntityColumn column : columnList) {
            initResultMapForColumn(column, sql);
        }
        sql.append("</resultMap>");
        return sql.toString();
    }

    private static void initResultMapForColumn(EntityColumn column, StringBuilder sql) {
        if (column.isId()) {
            sql.append("<id column=\"" + column.getColumn() + "\" property=\"" + column.getProperty() + "\"/>");
        } else {
            sql.append("<result column=\"" + column.getColumn() + "\" property=\"" + column.getProperty() + "\"/>");
        }
    }

}
