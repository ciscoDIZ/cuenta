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
public abstract class Cuenta {
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
    
    protected Usuario user;
    protected Login login;

    public Cuenta(Usuario user) {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public Usuario getU() {
        return user;
    }

    public void setU(Usuario u) {
        this.user = u;
    }

    public void login(String nombre, String contra){
        login = Login.login(nombre, contra);
    }
    public void logout(){
        login = Login.logout();
    }
}
