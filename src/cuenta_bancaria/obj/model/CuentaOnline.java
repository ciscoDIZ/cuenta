/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cuenta_bancaria.obj.model;

/**
 *
 * @author tote
 */
public abstract class CuentaOnline<T,V> {
    private static class Login{
        String nombre;
        String contra;
        static Login l;
        private Login(String n, String c){
            nombre = n;
            contra = c;
        }
        static Login login(String nombre, String contra){
            if(l == null){
                l = new Login(nombre, contra); 
            }
            return l;
        }
        static Login logout(){
            if (l != null){
                l = null;
            }
            return l;
        }
    }
    protected static enum Estado{
        ACTIVA,
        INACTIVA
    }
    protected T user;
    protected V account;
    protected Login login;
    protected Estado estado;
    
    public CuentaOnline(T user) {
        this.user = user;
        login = null;
        estado = Estado.INACTIVA;
    }
    
    public abstract void activarCuentaOnline(String nombre, String contra);
    
    /**
     *
     * @return
     */
    public T getUser() {
        return user;
    }

    public void setU(T u) {
        this.user = u;
    }

    public abstract void login(String nombre, String contra);
    public abstract void logout();
}
