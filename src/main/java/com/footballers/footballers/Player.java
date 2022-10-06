package com.footballers.footballers;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Player {
    @Id
    @GeneratedValue
    public int id;
    public String name;
    public String nationality;
    public int scoreOutOfTen;
    public boolean isReplacement;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "teamId", nullable = false)
    public Team team;

    public Player () {
    }
}
    
