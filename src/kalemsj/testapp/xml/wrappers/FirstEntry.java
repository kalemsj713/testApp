/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalemsj.testapp.xml.wrappers;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Karagaev S.A.
 */
public class FirstEntry implements Serializable {

    private Integer field;

    public Integer getField() {
        return field;
    }

    @XmlElement(name = "field")
    public void setField(Integer field) {
        this.field = field;
    }

    public FirstEntry(int field) {
        this.field = field;
    }
}
