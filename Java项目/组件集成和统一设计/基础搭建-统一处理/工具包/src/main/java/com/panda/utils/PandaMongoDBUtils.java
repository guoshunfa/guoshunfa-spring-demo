package com.panda.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName MongoDBUtils
 * @Description MongoDB 数据库操作工具类，不满足场景的可以根据需求自行调整。
 * @Author guoshunfa
 * @Date 2021/11/23 上午11:47
 * @Version 1.0
 **/
@Repository
public class PandaMongoDBUtils {

    @Autowired
    private MongoTemplate mongoTemplate;

    // 逻辑删除字段
    private final String XFLAG_FIELD_NAME = "xflag";

    public <T> T save(T t) {
        return mongoTemplate.save(t);
    }

    public void update(Class clazz, CriteriaDefinition criteriaDefinition, Update update) {
        if (clazz == null || criteriaDefinition == null || update == null) {
            throw new NullPointerException("clazz或criteriaDefinition或update 不能为 null。");
        }

        mongoTemplate.updateMulti(Query.query(criteriaDefinition), update, clazz);
    }

    public <T> List<T> searchAll(Class<T> clazz) {
        return searchList(clazz, null);
    }

    public <T> List<T> searchList(Class<T> clazz, CriteriaDefinition criteriaDefinition) {
        if (clazz == null) {
            throw new NullPointerException("clazz 不能为 null。");
        }

        if (criteriaDefinition == null) {
            return mongoTemplate.findAll(clazz);
        }

        return mongoTemplate.find(Query.query(criteriaDefinition), clazz);
    }

    public <T> List<T> searchByIds(Class<T> clazz, String... id) {
        if (clazz == null) {
            throw new NullPointerException("clazz 不能为 null。");
        }

        return mongoTemplate.find(Query.query(Criteria.where("id").in(id)), clazz);
    }

    public void deleteByIds(Class clazz, String... id) {
        Update update = new Update();
        update.addToSet(XFLAG_FIELD_NAME, true);

        update(clazz, Criteria.where("id").in(id), update);
    }

    public void aggregate(Class inputType, Class outputType) {
        mongoTemplate.aggregate(Aggregation.newAggregation(Aggregation.group("")), inputType, outputType);
    }

}
