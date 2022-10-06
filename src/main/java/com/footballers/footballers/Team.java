package com.footballers.footballers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Team {
    @Id
    @GeneratedValue
    public int id;
    public String name;

    public Team () {
    }
}
