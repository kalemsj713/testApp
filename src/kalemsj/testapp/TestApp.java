package kalemsj.testapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import kalemsj.testapp.xml.wrappers.FirstEntry;
import kalemsj.testapp.dao.JDBCService;
import kalemsj.testapp.xml.service.XmlService;

/**
 *
 * @author Karagaev S.A.
 */
public class TestApp {

    private String dbUrl;
    private String dbLogin;
    private String dbPassword;
    private Integer nField;

    //optional
    private Path first_xml;
    private Path second_xml;

    private static Logger log = Logger.getLogger(TestApp.class.getName());

    public Path getFirst_xml() {
        return first_xml;
    }

    public void setFirst_xml(Path first_xml) {

        if (first_xml == null) {
            first_xml = Paths.get("./1.xml");
        }
        try {
            Files.deleteIfExists(first_xml);
        } catch (IOException ex) {
            log.log(Level.SEVERE, null, ex);
        }

        this.first_xml = first_xml;
    }

    public Path getSecond_xml() {
        return second_xml;
    }

    public void setSecond_xml(Path second_xml) {
        if (second_xml == null) {
            second_xml = Paths.get("./2.xml");
        }

        this.second_xml = second_xml;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        try {
            if (dbUrl == null) {
                throw new NullPointerException();
            }
            if (dbUrl.isEmpty()) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException ex) {
            log.log(Level.SEVERE, "Parameter login must be not null.", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, "Login size must be > 0.", ex);
            throw ex;
        }

        this.dbUrl = dbUrl;
    }

    public String getDbLogin() {

        return dbLogin;
    }

    public void setDbLogin(String dbLogin) {
        try {
            if (dbLogin == null) {
                throw new NullPointerException();

            }
            if (dbLogin.isEmpty()) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException ex) {
            log.log(Level.SEVERE, "Parameter login must be not null.", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, "Login size must be > 0.", ex);
            throw ex;
        }
        this.dbLogin = dbLogin;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        try {
            if (dbPassword == null) {
                throw new NullPointerException();

            }
            if (dbPassword.isEmpty()) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException ex) {
            log.log(Level.SEVERE, "Parameter password must be not null.", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, "Password size must be > 0.", ex);
            throw ex;
        }

        this.dbPassword = dbPassword;
    }

    public Integer getnField() {
        return nField;
    }

    public void setnField(Integer nField) {
        try {
            if (nField == null) {
                throw new NullPointerException();

            }
            if (nField < 1) {
                throw new IllegalArgumentException();
            }

        } catch (NullPointerException ex) {
            log.log(Level.SEVERE, "Parameter N must be not null.", ex);
            throw ex;
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, "Parameter N must be > 0.", ex);
            throw ex;
        }

        this.nField = nField;
    }

    public Long run() {
        try {
            if (this.dbLogin == null || this.dbPassword == null || this.dbUrl == null || this.nField == null) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException ex) {
            log.log(Level.SEVERE, "All parameters must be filled", ex);
            throw ex;
        }
        if (this.first_xml == null) {
            this.setFirst_xml(null);
        }
        if (this.second_xml == null) {
            this.setSecond_xml(null);
        }
        JDBCService.deleteRecordsFromTestTable(this.dbUrl, this.dbLogin, this.dbPassword);
        JDBCService.batchInsertRecordsIntoTable(this.dbUrl, this.dbLogin, this.dbPassword, this.nField);
        List<FirstEntry> fieldList = JDBCService.selectFieldsFromTestTable(this.dbUrl, this.dbLogin, this.dbPassword);
        XmlService.createFirstXml(fieldList, this.first_xml);
        XmlService.transformToSecondXml(this.first_xml, this.second_xml);
        Long summ = XmlService.unmarshallSecondXml(this.second_xml).getEntriesSumm();
        return summ;
    }
}
