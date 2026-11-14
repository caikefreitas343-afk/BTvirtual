package com.biblioteca.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "turma")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turma")
    private Integer id;

    @Column(name = "codigo_turma", nullable = false, length = 20)
    private String codigoTurma;

    @Column(nullable = false, length = 20)
    private String serie;

    @Column(nullable = false, length = 10)
    private String turno;

    @Column(name = "ano_letivo", nullable = false)
    private Short anoLetivo;

    public Turma() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getCodigoTurma() { return codigoTurma; }
    public void setCodigoTurma(String codigoTurma) { this.codigoTurma = codigoTurma; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }

    public Short getAnoLetivo() { return anoLetivo; }
    public void setAnoLetivo(Short anoLetivo) { this.anoLetivo = anoLetivo; }
}
