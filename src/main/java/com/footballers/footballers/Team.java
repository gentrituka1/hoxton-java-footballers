package com.footballers.footballers;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Team {
    @Id
    @GeneratedValue
    public int id;
    public String name;
    public List<Player> players;

    public Team () {
    }
}
