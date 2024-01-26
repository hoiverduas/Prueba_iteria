package com.iteria.Prueba.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AFILIADO", uniqueConstraints = @UniqueConstraint(columnNames = {"TDC_ID", "AFI_DOCUMENTO"}))
@Data

public class Afiliado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AFI_ID")
    private Long afiId;

    @Column(name = "AFI_NOMBRE", length = 20)
    private String afiNombre;

    @ManyToOne
    @JoinColumn(name = "TDC_ID")
    private TipoDocumento tipoDocumento;

    @Column(name = "AFI_DOCUMENTO", length = 20)
    private String afiDocumento;

    @Column(name = "AFI_APELLIDOS", length = 30)
    private String afiApellidos;

    @Column(name = "AFI_DIRECCION", length = 30)
    private String afiDireccion;

    @Column(name = "AFI_TELEFONO", length = 20)
    private String afiTelefono;

    @Column(name = "AFI_MAIL", length = 30)
    private String afiMail;

    @OneToMany(mappedBy = "afiliado", cascade = CascadeType.ALL)
    private List<Contrato> contratos = new ArrayList<>();

    @Column(name = "AFI_ESTADO")
    @Enumerated(EnumType.ORDINAL)
    private Estado afiEstado;

    public Afiliado(Long afiId, String afiNombre, TipoDocumento tipoDocumento, String afiDocumento, String afiApellidos, String afiDireccion, String afiTelefono, String afiMail, List<Contrato> contratos, Estado afiEstado) {
        this.afiId = afiId;
        this.afiNombre = afiNombre;
        this.tipoDocumento = tipoDocumento;
        this.afiDocumento = afiDocumento;
        this.afiApellidos = afiApellidos;
        this.afiDireccion = afiDireccion;
        this.afiTelefono = afiTelefono;
        this.afiMail = afiMail;
        this.contratos = contratos;
        this.afiEstado = afiEstado;
    }

    public Afiliado() {
    }
}
