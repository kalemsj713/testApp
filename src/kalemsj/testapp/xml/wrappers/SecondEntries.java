/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalemsj.testapp.xml.wrappers;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Karagaev S.A.
 */
@XmlRootElement(name = "entries")
public class SecondEntries implements Serializable {

    private ArrayList< SecondEntry> entries;

    public ArrayList<SecondEntry> getEntries() {
        return entries;
    }

    @XmlElement(name = "entry")
    public void setEntries(ArrayList<SecondEntry> entries) {
        this.entries = entries;
    }

    public Long getEntriesSumm() {
        long summ = 0;
        for (SecondEntry entrie : this.entries) {
            summ = summ + entrie.getField();
        }
        return summ;
    }
}
