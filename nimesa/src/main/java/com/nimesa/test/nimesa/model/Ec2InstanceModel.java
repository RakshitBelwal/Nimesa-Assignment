package com.nimesa.test.nimesa.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@Data
@Value
public class Ec2InstanceModel {
    private String instanceId;
    private String instanceType;
    private String state;

    public Ec2InstanceModel(String instanceId, String instanceType, String state) {
        this.instanceId = instanceId;
        this.instanceType = instanceType;
        this.state = state;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public String getState() {
        return state;
    }
}
