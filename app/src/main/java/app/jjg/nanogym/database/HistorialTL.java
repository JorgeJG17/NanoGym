package app.jjg.nanogym.database;

public class HistorialTL {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdejercicio() {
        return idejercicio;
    }

    public void setIdejercicio(String idejercicio) {
        this.idejercicio = idejercicio;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getRepes() {
        return repes;
    }

    public void setRepes(String repes) {
        this.repes = repes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String id;
    private String idejercicio;
    private String peso;
    private String repes;
    private String date;
}
