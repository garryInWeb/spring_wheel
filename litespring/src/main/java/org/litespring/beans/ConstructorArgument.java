package org.litespring.beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhengtengfei on 2018/7/9.
 */
public class ConstructorArgument {
    private final List<ValueHolder> valueHolderList = new LinkedList<ValueHolder>();

    public ConstructorArgument() {
    }

    public void addArgumentValue(ValueHolder valueHolder){
        this.valueHolderList.add(valueHolder);
    }

    public List<ValueHolder> getValueHolderList() {
        return Collections.unmodifiableList(valueHolderList);
    }

    public int getArgumentCount(){
        return this.valueHolderList.size();
    }

    public boolean isEmpty(){
        return this.valueHolderList.isEmpty();
    }

    public void clear(){
        this.valueHolderList.clear();
    }

    public static class ValueHolder {
        private Object value;

        private String type;

        private String name;

        public ValueHolder(Object value) {
            this.value = value;
        }

        public ValueHolder(Object value, String type) {
            this.value = value;
            this.type = type;
        }

        public ValueHolder(Object value, String type, String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
