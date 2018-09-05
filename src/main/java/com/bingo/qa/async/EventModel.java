package com.bingo.qa.async;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author bingo
 */
public class EventModel {
    /**
     * eg.
     * type: comment
     * actorId: 谁评论了（触发者）
     * entityType & entityId：评论的是哪个实体
     * entityOwnerId：该实体的Owner
     */
    private EventType type;
    private int actorId;
    private int entityType;
    private int entityId;
    private int entityOwnerId;

    /**
     * 一些扩展字段
     */
    private Map<String, String> exts = new HashMap<>();

    public EventModel() {

    }
    public EventModel(EventType type) {
        this.type = type;
    }

    /**
     * 新增set/getExt()方法，方便字段的设置与读取
     * @param key
     * @param value
     */
    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public String getExt(String key) {
        return exts.get(key);
    }


    public EventType getType() {
        return type;
    }

    /**
     * 在set()方法中返回EventModel，方便链式调用
     * @param type
     * @return
     */
    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

}
