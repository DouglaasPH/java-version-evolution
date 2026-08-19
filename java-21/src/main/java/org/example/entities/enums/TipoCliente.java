package org.example.entities.enums;

public enum TipoCliente {
    NORMAL(0),
    VIP(0),
    ATACADO(20);

    private final int percentualDesconto;

    TipoCliente(int percentualDesconto) { this.percentualDesconto = percentualDesconto; }

    public  int getPercentualDesconto() { return  percentualDesconto; }
}
