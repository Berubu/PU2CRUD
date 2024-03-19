package isa.pu2crud;

public class DatosModelo {
    private String ClientName;
    private int Spinner;
    private String Total;
    private String Notas;

    public DatosModelo(String campo1, int campo2, String campo3, String campo4) {
        this.ClientName = campo1;
        this.Spinner = campo2;
        this.Total = campo3;
        this.Notas = campo4;
    }

    // Getters y setters
    public String getClientName() {
        return ClientName;
    }
    public void setClientName(String campo1) {
        this.ClientName = campo1;
    }
    public int getSpinner() {
        return Spinner;
    }
    public void setSpinner(int campo2) {
        this.Spinner = campo2;
    }
    public String getTotal() {
        return Total;
    }
    public void setTotal(String campo3) {
        this.Total = campo3;
    }

    public String getNotas() {
        return Notas;
    }

    public void setNotas(String campo4) {
        this.Notas = campo4;
    }
}
