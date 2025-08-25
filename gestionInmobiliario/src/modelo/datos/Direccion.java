package modelo.datos;

/**
 * Clase que nos permite poder conocer la localidad de un objeto específico.
 * Está clase espera que el sistema entrante de direcciones sea el utilizado
 * en CHILE, por eso está dividido en regiones. :)
 */
public class Direccion {
    private String region;
    private String comuna;
    private String calle;
    private String numero;
    
    public Direccion(String region, String comuna, String calle, String numero) {
        this.region = region;
        this.comuna = comuna;
        this.calle = calle;
        this.numero = numero;
    }
    
    public String getRegion() { return region; }
    public String getComuna() { return comuna; }
    public String getCalle() { return calle; }
    public String getNumero() { return numero; }
    
    public String getSector() {
        return region + ", " + comuna + ", " + calle;
    }
    
    public String getDireccionCompleta() {
        return calle + " " + numero + ", " + comuna + ", " + region;
    }
    
    @Override
    public String toString() {
        return getDireccionCompleta();
    }
}