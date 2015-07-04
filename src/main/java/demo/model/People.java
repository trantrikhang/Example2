package demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
/**
 * Created by Khang on 02/07/2015.
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class People {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "table")
    @GenericGenerator(name = "table",strategy = "enhanced-table",parameters = {@org.hibernate.annotations.Parameter(name="table_name",value = "sequence_table")})
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="password")
    private String password;

    protected People(){
    }

    public People(String name, String password){
        this.name = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
