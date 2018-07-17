/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalemsj.testapp.xml.wrappers;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 *
 * @author Karagaev S.A.
 */
public class SecondEntry implements Serializable {

    private Integer field;

    public Integer getField() {
        return field;
    }

    @XmlAttribute(name = "field")
    public void setField(Integer field) {
        this.field = field;
    }

}
