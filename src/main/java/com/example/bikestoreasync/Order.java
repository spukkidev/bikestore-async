package com.example.bikestoreasync;

import java.io.Serializable;

public class Order implements Serializable {
    private String pedidoId;
    private String paymentStatus;

    public String getPedidoId() { return pedidoId; }
    public void setPedidoId(String pedidoId) { this.pedidoId = pedidoId; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
}