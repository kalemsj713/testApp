/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kalemsj.testapp.xml.wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Karagaev S.A.
 */
@XmlRootElement(name = "entries")
public class FirstEntries implements Serializable {

    private ArrayList< FirstEntry> entries;

    public FirstEntries(ArrayList<FirstEntry> entriesList) {
        this.entries = entriesList;
    }

    public FirstEntries() {
    }

    public ArrayList<FirstEntry> getEntries() {
        return entries;
    }

    @XmlElement(name = "entry")
    public void setEntries(ArrayList<FirstEntry> entries) {
        this.entries = entries;
    }

}
